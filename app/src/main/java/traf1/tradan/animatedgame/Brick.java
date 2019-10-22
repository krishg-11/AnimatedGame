package traf1.tradan.animatedgame;

import android.graphics.RectF;

public class Brick extends RectF {
    public int hits;
    public Brick(){
        super();
        hits = 2;
    }
    public Brick(float left, float top, float right, float bottom, int counts){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.hits = counts;
    }
    public void gotHit(){
        hits--;

    }

}
