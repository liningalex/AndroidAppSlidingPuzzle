/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lining.apps.andriod.slidingpuzzle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;

/**
 * Render a pair of tumbling cubes.
 */

class BlockRenderer implements GLSurfaceView.Renderer {
	private MyBlock mBlocks[];
	private int mBlockH;
	private int mBlockV;
    public BlockRenderer(Context context, MyBlock[] mb, int h, int v) {
    	mBlocks = mb;
    	mBlockH = h;
    	mBlockV = v;
        mBlocks[0].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block0);
    	mBlocks[1].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block1);
    	mBlocks[2].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block2);
    	mBlocks[3].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block3);
    	mBlocks[4].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block4);
    	mBlocks[5].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block5);
    	mBlocks[6].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block6);
    	mBlocks[7].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block7);
    	mBlocks[8].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block8);
    	mBlocks[9].bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.block9);   	 
    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(-1f, -1f, 0f);
     
        for (int i = 0; i < mBlocks.length; i++) {
        	mBlocks[i].draw(gl, mBlockH, mBlockV);
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        int scale = width / mBlockH;
        if ((height / mBlockV) < scale)
        	scale = height / mBlockV;
         gl.glViewport(0, 0, scale * mBlockH, scale * mBlockV);
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
 
         gl.glDisable(GL10.GL_DITHER);
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);
         gl.glClearColor(0,0,0,0);
         gl.glEnable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);
         gl.glEnable(GL10.GL_TEXTURE_2D);

         for (MyBlock b : mBlocks)
             b.tex = loadTexture (gl, b.bmp);
    }

    
    private int loadTexture (GL10 gl, Bitmap bmp) 
    {
  		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight()*bmp.getWidth()*4);
  		bb.order(ByteOrder.BIG_ENDIAN);
  		IntBuffer ib = bb.asIntBuffer();
  		
  		for (int y=0;y<bmp.getHeight();y++)
  			for (int x=0;x<bmp.getWidth();x++) {
  				    int c = bmp.getPixel(x,y);
  					ib.put(c << 8);
  			}
  		ib.position(0);
  		bb.position(0);
  		
  		int[] tmp_tex = new int[1];
  		
  		gl.glGenTextures(1, tmp_tex, 0);
  		int tex = tmp_tex[0];
  		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
  		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(), bmp.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
  		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
  		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
  	
  		return tex;
    }
}
