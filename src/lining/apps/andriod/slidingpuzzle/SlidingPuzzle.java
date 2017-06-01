package lining.apps.andriod.slidingpuzzle;

import android.app.Activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SlidingPuzzle extends Activity {
	MyView mView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	

        
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.main);
        
        mView = (MyView)findViewById(R.id.blocks);
        
        mView.mResetButton = (Button)findViewById(R.id.reset);
        mView.mResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mView.reset();
            }
        });
        mView.mAnswerButton = (Button)findViewById(R.id.goal);
        mView.mAnswerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mView.goal();
            }
        });
        mView.mAnswerButton = (Button)findViewById(R.id.learn);
        mView.mAnswerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mView.answer();
            }
        });
        mView.mFastButton = (Button)findViewById(R.id.fast);
        mView.mFastButton.setVisibility(View.INVISIBLE);
        mView.mFastButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mView.fast();
            }
        });
        mView.mSlowButton = (Button)findViewById(R.id.slow);
        mView.mSlowButton.setVisibility(View.INVISIBLE);
        mView.mSlowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mView.slow();
            }
        });
        
        mView.mStepButton = (Button)findViewById(R.id.step);
        mView.mStepButton.setVisibility(View.INVISIBLE);
        mView.mStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mView.step();
            }
        });
        
    }
    
    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        if (mView != null)
            mView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        if (mView != null)
            mView.onPause();
    }
    
}