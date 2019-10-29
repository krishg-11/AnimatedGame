package traf1.tradan.animatedgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawView extends View {
    Paint paint=new Paint();
    static int circPosY=800, dY=0;//set initial y position and vertical speed
    static int circPosX=400, dX=0;
    static int rectPosX=600, rectSizeX=200;
    static int rectPosY=0, rectSizeY=25;
    static int r = 40;
    static int score = 0;
    static int ballColor = Color.rgb(255,0,0);
    static ArrayList<Brick> bricks = new ArrayList<Brick>();
    static int brickWidth;
    static int brickHeight;
    RectF paddle;
    RectF ball;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        brickWidth = getWidth()/5;
        brickHeight = getHeight()/10;

        paddle = new RectF(rectPosX-rectSizeX, rectPosY-rectSizeY, rectPosX+rectSizeX, rectPosY+rectSizeY);
        ball = new RectF(circPosX-r, circPosY-r, circPosX+r, circPosY+r);

        for(int r=0;r<4;r++){
            for(int c=0; c<5; c++){
                bricks.add(new Brick(brickWidth*(float)c+5, brickHeight*(float)r+5, brickWidth*(float)(c+1)-5, brickHeight*(float)(r+1)-5, 1));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#444444"));//set paint to gray
        canvas.drawRect(getLeft(),0,getRight(),getBottom(),paint);//paint background gray

        paint.setColor(Color.RED);//set paint to red

        //Draw Paddle
        rectPosY = getHeight();
        paddle.set(rectPosX-rectSizeX, rectPosY-rectSizeY, rectPosX+rectSizeX, rectPosY+rectSizeY);
        canvas.drawRoundRect(paddle, 15, 15, paint);

        //Ball drawing and bouncing stuff
        paint.setColor(ballColor);
        ball.set(circPosX-r, circPosY-r, circPosX+r, circPosY+r);

        if(paddle.intersect(ball) && dY!=0){
            paddleBounce();
        }

        canvas.drawRoundRect(ball, r, r, paint);
        paint.setColor(Color.WHITE);
        circPosY+=dY;//increment y position
        circPosX+=dX;
        if(circPosY+r > getHeight()){
            if(circPosX>rectPosX-rectSizeX && circPosX<rectPosX+rectSizeX && dY!=0){
                paddleBounce();
            }
            else {
                dY = 0;
                dX = 0;

                canvas.drawText("GAME OVER", getWidth() / 2, getHeight() / 2, paint);
                //GAME OVER
            }
        }
        if(circPosY-r<0){
            dY = -dY;
            circPosY = r;
        }
        if(circPosX+r >= getWidth()){
            dX = -dX;
            circPosX = getWidth()-r;
        }
        if(circPosX-r<0){
            dX = -dX;
            circPosX = r;
        }

        //Draw bricks
        paint.setColor(Color.LTGRAY);
        for(int i=0; i<bricks.size(); i++){
            Brick brick = bricks.get(i);
            canvas.drawRect(brick, paint);
            if(brick.intersect(ball)){
                float wy = (brickWidth + r) * ((brick.top + brick.bottom)/2 - circPosY);
                float hx = (brickHeight + r) * ((brick.left + brick.right)/2 - circPosX);

                if (wy > hx) {
                    if (wy > -hx){
                        /* top */
                        dY = -dY;
//                        circPosY = (int)brick.top - r;
//                        ball.set(circPosX-r, circPosY-r, circPosX+r, circPosY+r);
                        System.out.println("Top");
                    }
                    else{
                        /* right */
                        dX = -dX;
//                        circPosX = (int)brick.right + r;
//                        ball.set(circPosX-r, circPosY-r, circPosX+r, circPosY+r);
                        System.out.println("Right");
                    }
                }
                else {
                    if (wy > -hx) {
                        /* left */
                        dX = -dX;
//                        circPosX = (int)brick.left - r;
//                        ball.set(circPosX-r, circPosY-r, circPosX+r, circPosY+r);
                        System.out.println("Left");
                    } else {
                        dY = -dY;
//                        circPosY = (int)brick.bottom + r;
//                        ball.set(circPosX-r, circPosY-r, circPosX+r, circPosY+r);
                        System.out.println("Bottom");
                        /* bottom */
                    }
                }
                brick.gotHit();
                if(brick.hits <= 0){
                    bricks.remove(i);
                    i--;
                }
            }
        }

        if(bricks.size()==0){
            dX = 0;
            dY = 0;

            canvas.drawText("YOU WON", getWidth() / 2, getHeight() / 2, paint);
        }

        //Print score
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(80);
        canvas.drawText("Score: " + score, getWidth()/2, 100, paint);
        invalidate();  //redraws screen, invokes onDraw()
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        rectPosX = (int) event.getX();
        rectPosY = getHeight();

        invalidate();
        return true;
    }

    public static void startOver() {
        circPosY=800; dY=15; //set initial y position and vertical speed
        circPosX=400; dX=15;
        rectPosX=600; rectSizeX=200;
        r = 40;
        score = 0;

        bricks.clear();
        for(int r=0;r<4;r++){
            for(int c=0; c<5; c++){
                bricks.add(new Brick(brickWidth*(float)c+5, brickHeight*(float)r+5, brickWidth*(float)(c+1)-5, brickHeight*(float)(r+1)-5, 1));
            }
        }
    }

    public static void paddleBounce(){
        dY = -dY;
        dX = (circPosX - rectPosX)/8;
        score += 1;
        ballColor = Color.rgb((float)Math.random()*128+128,(float)Math.random()*128+128,(float)Math.random()*128+128);
    }
}
