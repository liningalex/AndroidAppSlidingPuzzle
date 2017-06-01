package lining.apps.andriod.slidingpuzzle;

import android.util.FloatMath;

public class MyMap {
	private int mAvail[] = null;
	private int mValue;
	private int mWidth;
	private int mHeight;
	private float mScale;
	private float mMove = 5;
	public MyMap(int x, int y) {
		mAvail = new int[x * y];
		mWidth = x;
		mHeight = y;
		clearAll();
	}
	
	   
    public void setScale(float s) {
    	mScale = s;
    }
    
	public int blockId(float x1, float y1) {
		int x = (int)(x1/ mScale);
		int y = (int)(y1 / mScale);
		if (x < mWidth && y < mHeight) 
	        return mAvail[(int) (x + y * mWidth)]; 
		else 
			return -1;
	}
	public void clearAll() {
        for (int i = 0; i < mAvail.length; i++)
        	mAvail[i] = -1;
	}

	public void toMove(MyBlock b, float x, float y) {
	    mValue = mAvail[(int) (b.left + b.top * mWidth)];
	    assign(b, -1);
   		b.tLeft = b.left;
		b.tTop = b.top;
        b.inMove = true;
        b.touchX = x;
	    b.touchY = y;
	}
	
	public void place(MyBlock b) {
	   	float x = StrictMath.abs(b.tLeft - (int)b.tLeft);
    	float y = StrictMath.abs(b.tTop - (int)b.tTop);
    	
    	if (x >= 0.5)
    	    b.left = (int) FloatMath.ceil(b.tLeft);
    	else
    		b.left = (int) FloatMath.floor(b.tLeft);
    	
    	if (y >= 0.5)
    		b.top = (int) FloatMath.ceil(b.tTop);
    	else
    	    b.top = (int) FloatMath.floor(b.tTop);
    	
    	b.tLeft = b.left;
    	b.tTop = b.top;
    	
		assign(b, mValue);
   		b.inMove = false;
	}
	
	public void assign(MyBlock b, int v) {
        for (int i = 0; i < b.width; i++) {
            for (int j = 0; j < b.height; j++) {
               mAvail[(int) (b.left + i + (b.top + j) * mWidth)] = v;
            }
        }
	}
    boolean free(MyBlock b, int left, int top) { 
    	if (left < 0 ||(int)(left + b.width) > mWidth ||
    		 top < 0 ||(int)(top + b.height) > mHeight)
    		return false;
    	
        for (int i = 0; i < b.width; i++) {
            for (int j = 0; j < b.height; j++) {         	
                if (mAvail[(int) (left + i + (top + j) * mWidth)] >= 0)
                	return false;              
            }
        }   	
    	return true;
    }
    
    public boolean canMove(MyBlock b, float x, float y) {  
    	x = x - b.touchX;
    	y = y - b.touchY;
    	
    	if (b.top != b.tTop)
    		return canMoveY(b, y);
    	
    	if (b.left != b.tLeft)
    		return canMoveX(b,x);
    	
       	if (StrictMath.abs(x) > StrictMath.abs(y)) {
    		if (canMoveX(b, x))
    			return true;
    		else if (canMoveY(b, y))
    			return true;  		
    	}
    	else {
    		if (canMoveY(b, y))
    			return true;
    		else if (canMoveX(b, x))
    			return true;
    	}
    	return false;
    }
    
    
    private boolean canMoveX(MyBlock b, float x) { 
    	if (StrictMath.abs(x) < mMove)
    		return false;
    	int x1 = (int)FloatMath.floor(x / mScale + b.left);
    	int x2 = (int)FloatMath.ceil(x / mScale + b.left);
        
    	if (free(b, x1, (int) b.top) && free(b, x2, (int) b.top)) {
    		b.tLeft = b.left + x / mScale;
    		return true;
    	}
    	return false;   	
    }
    
    private boolean canMoveY(MyBlock b, float y) {
    	if (StrictMath.abs(y) < mMove)
    		return false;
       	int y1 = (int) FloatMath.floor(y / mScale + b.top);
    	int y2 = (int) FloatMath.ceil(y / mScale + b.top);    	
        
    	if (free(b, (int) b.left, y1) && free(b, (int) b.left, y2)) {
    		b.tTop = b.top + y / mScale;    		
    		return true;
    	}
    	return false;
    }
}
