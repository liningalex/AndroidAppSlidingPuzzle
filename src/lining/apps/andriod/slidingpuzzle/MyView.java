package lining.apps.andriod.slidingpuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MyView extends GLSurfaceView {
    private MyBlock mBlocks[] = null;
	private MyMap mMap = null;
	private float mBlockH = 0;
	private float mBlockV = 0;
	private float mScale;
	private Handler mHandler = new Handler();
	private int mSpeed = 1000;
	private boolean mAnswer = false;
	private int mIdx = 0;
	private String mSteps[] = null;
	private boolean mAuto = false;
	private boolean mUseGL = true;
	public Button mResetButton = null;
	public Button mAnswerButton = null;
	public Button mFastButton = null;
	public Button mSlowButton = null;
	public Button mStepButton = null;
	
	public MyView(Context context) {
        super(context);
        initMyView();  
    	setRenderer(new BlockRenderer(context, mBlocks, (int)mBlockH, (int)mBlockV));
    	setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
    
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView();
    	setRenderer(new BlockRenderer(context, mBlocks, (int)mBlockH, (int)mBlockV));
    	// setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void reset() {
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	mAnswer = false;
    	mSpeed = 1000;
    	mFastButton.setVisibility(View.INVISIBLE);
    	mSlowButton.setVisibility(View.INVISIBLE);
    	mStepButton.setVisibility(View.INVISIBLE);
    	mAnswerButton.setVisibility(View.VISIBLE);
    	initMyView();
        update();
    }
    
    private Runnable mUpdateTimeTask = new Runnable() {
    	public void run() {
            if ((3 * mIdx) >= mSteps.length)
                return;
            int id = Integer.valueOf(mSteps[3*mIdx]);
            mMap.toMove(mBlocks[id], 0, 0);
            if (mMap.canMove(mBlocks[id], Integer.valueOf(mSteps[3*mIdx+1]) * mScale, Integer.valueOf(mSteps[3*mIdx+2]) * mScale)) {
            	mMap.place(mBlocks[id]);
                update();
            	mIdx++;
            	if (mAuto)
    	            mHandler.postDelayed(mUpdateTimeTask, mSpeed);
            }  	    
    	}
    };
    
    public void goal() {
    	reset();
    	mIdx = 0;
    	mAnswer = true;
    	mAuto = true;
    	mSpeed = 0;
    	mHandler.postDelayed(mUpdateTimeTask, mSpeed);
    }
    	
    public void answer() {
    	reset();
    	mIdx = 0;
    	mAnswer = true;
    	mAuto = true;
       	mFastButton.setVisibility(View.VISIBLE);
    	mSlowButton.setVisibility(View.VISIBLE);
    	mStepButton.setVisibility(View.VISIBLE);
    	mAnswerButton.setVisibility(View.INVISIBLE);
    	mHandler.postDelayed(mUpdateTimeTask, mSpeed);
    }
    
    public void fast() {
    	mSpeed /= 2;
    }
    
    public void slow() {
    	mSpeed *= 2;
    }
    
    public void step() {
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	mAuto = false;
    	mHandler.postDelayed(mUpdateTimeTask, 0);
    }
    
    final private void initMyView() {	
        if (mSteps == null)
            mSteps = getResources().getString(R.string.answer).split(",");
        
        String[] items = getResources().getStringArray(R.array.blocks);
        if (mBlocks == null)
            mBlocks = new MyBlock[items.length];
       
        for (int i = 0; i < items.length; i++) {
        	if (mBlocks[i] == null) {
        	    mBlocks[i] = new MyBlock(items[i].split(","));
 
        	    if ((mBlocks[i].left + mBlocks[i].width) > mBlockH)
        		    mBlockH = mBlocks[i].left + mBlocks[i].width;
        	    if ((mBlocks[i].top + mBlocks[i].height) > mBlockV)
        		    mBlockV = mBlocks[i].top + mBlocks[i].height;  
        	}
           	else
        		mBlocks[i].assign(items[i].split(","));
        }
        
        if (mMap == null)
            mMap = new MyMap((int)mBlockH, (int)mBlockV);
        else
            mMap.clearAll();
        
        for (int i = 0; i < mBlocks.length; i++) {
        	mMap.assign(mBlocks[i], i);
        }
    }
    
   
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int width = MeasureSpec.getSize(widthMeasureSpec);
    	int height = MeasureSpec.getSize(heightMeasureSpec);
    	  	
        mScale = width / mBlockH;
        if ((height / mBlockV) < mScale)
        	mScale = height / mBlockV;
        mMap.setScale(mScale);
        setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas); 
        for (int i = 0; i < mBlocks.length; i++) {
        	mBlocks[i].redraw(canvas, mScale);
        }
    }
    

    @Override
    public boolean onTouchEvent (MotionEvent event) {   	
    	if (mAnswer)
    		return true;
    	
    	int action = event.getAction();
    	if (action == MotionEvent.ACTION_DOWN) { 
    	   	float x = event.getX();
        	float y = event.getY();
    		mIdx = mMap.blockId(x, y);
		    
    		if (mIdx >= 0) {
    			mMap.toMove(mBlocks[mIdx], x, y);
    			update();
       		}

    	}
    	else if (action == MotionEvent.ACTION_UP && mIdx >= 0) {
    		mMap.place(mBlocks[mIdx]);
                update();
    		mIdx = -1;
    	}
    	else if (action == MotionEvent.ACTION_MOVE) {
    	   	float x = event.getX();
        	float y = event.getY();
    		if (mIdx < 0) {
    		    mIdx = mMap.blockId(x, y);
    		    
    		    if (mIdx >= 0) {
    			    mMap.toMove(mBlocks[mIdx], x, y);
                    update();
    		    }
    		}
    		if (mIdx >= 0 && mMap.canMove(mBlocks[mIdx], x, y)) {
    			update();
    		}
    	}
    	return true;   
    }
 
    private void update() {
    	if (!mUseGL)
   	        invalidate();
    	/*
    	else 
	        requestRender ();
	    */
    
    }
}
