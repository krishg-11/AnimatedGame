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

public class DrawView extends View {
    Paint paint=new Paint();
    static int circPosY=400, dY=0;//set initial y position and vertical speed
    static int circPosX=400, dX=0;
    static int rectPosX=600, rectSizeX=200;
    static int rectPosY=0, rectSizeY=25;
    static int r = 40;
    static int score = 0;
    int ballColor = Color.rgb(255,0,0);
    RectF paddle;
    RectF ball;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#444444"));//set paint to gray
        canvas.drawRect(getLeft(),0,getRight(),getBottom(),paint);//paint background gray

        paint.setColor(Color.RED);//set paint to red

        //Draw Paddle
        rectPosY = getHeight();
        paddle = new RectF(rectPosX-rectSizeX, rectPosY-rectSizeY, rectPosX+rectSizeX, rectPosY+rectSizeY);
        canvas.drawRect(paddle, paint);

        //draw ball
        paint.setColor(ballColor);
        ball = new RectF(circPosX-r, circPosY-r, circPosX+r, circPosY+r);

        if(paddle.intersect(ball) && dY!=0){
            dY = -dY;
            score += 1;
            ballColor = Color.rgb((float)Math.random()*128+128,(float)Math.random()*128+128,(float)Math.random()*128+128);
        }

        canvas.drawRoundRect(ball, r, r, paint);
        paint.setColor(Color.WHITE);
        circPosY+=dY;//increment y position
        circPosX+=dX;
        if(circPosY+r > getHeight()){
            if(circPosX>rectPosX-rectSizeX && circPosX<rectPosX+rectSizeX && dY!=0){
                dY = -dY;
                score += 1;
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
        }
        if(circPosX+r >= getWidth() || circPosX-r<0){
            dX = -dX;
        }

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
        circPosY=400; dY=25; //set initial y position and vertical speed
        circPosX=400; dX=20;
        rectPosX=600; rectSizeX=200;
        r = 40;
        score = 0;
    }
}
