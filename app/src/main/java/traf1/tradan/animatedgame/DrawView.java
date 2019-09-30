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
    int circPosY=400,dY=25;//set initial y position and vertical speed
    int circPosX=400, dX=20;
    int rectPosX=600, rectSizeX=200;
    int rectPosY=getHeight(), rectSizeY=25;
    int r = 40;
    RectF paddle;
    RectF ball;

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#CCCCCC"));//set paint to gray
        canvas.drawRect(getLeft(),0,getRight(),getBottom(),paint);//paint background gray

        paint.setColor(Color.RED);//set paint to red
        rectPosY = getHeight();
        paddle = new RectF(rectPosX-rectSizeX, rectPosY-rectSizeY, rectPosX+rectSizeX, rectPosY+rectSizeY);
        canvas.drawRect(paddle, paint);
        //draw red circle

        ball = new RectF(circPosX-r, circPosY-r, circPosX+r, circPosY+r);

        if(paddle.intersect(ball)){
            dY = -dY;
        }

        canvas.drawRoundRect(ball, r, r, paint);
        circPosY+=dY;//increment y position
        circPosX+=dX;
        if(circPosY+r >= getHeight()){
            dY = 0;
            dX = 0;
            //GAME OVER
        }
        if(circPosY-r<0){
            dY = -dY;
        }
        if(circPosX+r >= getWidth() || circPosX-r<0){
            dX = -dX;
        }

        invalidate();  //redraws screen, invokes onDraw()
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        rectPosX = (int) event.getX();
        rectPosY = getHeight();

        invalidate();
        return true;
    }
}
