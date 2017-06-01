package lining.apps.andriod.slidingpuzzle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MyBlock {

	public float width;
	public float height;
	public float top;
	public float left;
	public float touchX;
	public float touchY;
	public float tLeft;
	public float tTop;
	public boolean inMove = false;
	public Bitmap bmp;
	public int tex;
    private Paint mBlockPaint = new Paint();
    private Paint mBlockMove = new Paint();

    private int one = 0x10000;
	private int two = 0x20000;

    private IntBuffer mColorBuffer;
    private IntBuffer mTextureBuffer;
    private ByteBuffer mIndexBuffer;
    
    public MyBlock(String[] b) {
        mBlockPaint.setARGB(255, 128, 0, 128);
        mBlockMove.setARGB(255, 128, 0, 0);
	    assign(b);
	    
	    byte indices[] = {0, 1, 2, 3};
	    int colors[] = {
	               0,    0,    one,  one,
	                one,    0,    0,  one,
	                one,  one,    one,  one,
	                0,  one,    0,  one,
	    		/*
	            one/2,  0, one/2,  one,
	            one/2,  0, one/2,  one,
	            one/2,  0, one/2,  one,
	            one/2,  0, one/2,  one,
	            */
	    };
	    int texture[] = {0, one, one, one, 0, 0, one, 0};
	    
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);       
        ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asIntBuffer();
        mTextureBuffer.put(texture);
        mTextureBuffer.position(0);       
        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        
	}
	public void assign(String[] b) {
	    width = Integer.valueOf(b[0]);
	    height = Integer.valueOf(b[1]);
	    top = Integer.valueOf(b[2]);
	    left = Integer.valueOf(b[3]);
	    inMove = false;
	}
	
	public void draw(GL10 gl, float x, float y) {
        x = two / x;
        y = two / y;
		
		int vertice1[] = {(int)(left * x), (int) (two - (top + height) * y), 0,
				          (int)((left + width) * x), (int) (two - (top + height) * y), 0,
				          (int)(left * x), (int) (two - top * y), 0,
				          (int)((left + width) * x), (int) (two - top * y), 0};
				          
		int vertice2[] = {(int)(tLeft * x), (int) (two - (tTop + height) * y), 0,
		                  (int)((tLeft + width) * x), (int) (two - (tTop + height) * y), 0,
		                  (int)(tLeft * x), (int) (two - tTop * y), 0,
		                  (int)((tLeft + width) * x), (int) (two - tTop * y), 0};
		          
		int vertices[] = inMove ? vertice2 : vertice1;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        IntBuffer vertexBuffer = vbb.asIntBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
             
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        if (inMove) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        }
        else
        	gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_FRONT);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
        gl.glTexCoordPointer (2, GL10.GL_FIXED, 0, mTextureBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);

	}
	
	public void redraw(Canvas canvas, float scale) {
   	    if (inMove) {
    	    canvas.drawRect(tLeft * scale, tTop * scale, 
		        (tLeft + width) * scale - 1, (tTop + height) * scale - 1, mBlockMove);
	    }
	    else {
	        canvas.drawRect(left * scale, top * scale, 
			        (left + width) * scale - 1, (top + height) * scale - 1, mBlockPaint);

	    }
	}

}

