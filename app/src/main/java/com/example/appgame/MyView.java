package com.example.appgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.Random;

public class MyView extends View implements Runnable {

    private SoundManager soundManager;
    private ArrayList<Brick> lists;
    private  int x1=300, y1=500, dx1=10, dy1=10;
    float width = 500;
    float height = 100;
    float x2= (getWidth() - width)/2.0f;
    float y2= (getHeight() - height)/2.0f;


    boolean stop = false;

    WindowManager windowManager;

    //Bitmap ball= BitmapFactory.decodeResource(getResources(), R.drawable.ball);
    //Bitmap ballResize = Bitmap.createScaledBitmap(ball, 100,100,false);

    //Bitmap bg= BitmapFactory.decodeResource(getResources(), R.drawable.background);

    //Bitmap bar= BitmapFactory.decodeResource(getResources(), R.drawable.bar);
    //Bitmap barResize = Bitmap.createScaledBitmap(bar, 500,100,false);


    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        soundManager = SoundManager.getInstance();
        soundManager.init(context);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenX= size.x;
        int screenY= size.y;
        int brickwidth= screenX/9;
        int brickheight= screenY/20;


        lists= new ArrayList<Brick>();
        for( int i= 0; i<15; i++){
            Brick brick1= new Brick(110*i, 0, 105,70);
            Brick brick2= new Brick(110*i, 72, 105,70);
            Brick brick3= new Brick(110*i, 144, 105,70);
            Brick brick4= new Brick(110*i, 216, 105,70);
            Brick brick5= new Brick(110*i, 290, 105,70);

            lists.add(brick1);
            lists.add(brick2);
            lists.add(brick3);
            lists.add(brick4);
            lists.add(brick5);

        }
    }

    @Override



    protected void onDraw(Canvas canvas){

        super.onDraw(canvas);

        int x= this.getWidth();
        int y= this.getHeight();
        int radius=50;

        Paint paint= new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);




        canvas.drawPaint(paint);
        //canvas.drawBitmap(bg, 0, 0, null);
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(x1, y1, radius, paint);
        //canvas.drawBitmap(ballResize, x1,y1, null);
        //canvas.drawBitmap(barResize, x2,y2, null);
        y2= this.getHeight()-280;
        canvas.drawRect(x2, y2, x2 + width, y2 + height, paint);

        if (x1<0 || x1>x-50){
            dx1 = -dx1;
        }
        if (y1<0|| y1>y-50){
            dy1 = -dy1;
        }

        if (y1 > y - 300){
            if (x1 > x2 - 100 && x1 < x2 + 500){
                dy1 = -dy1;
                soundManager.playSound(R.raw.music);
            }
        }

        for(Brick element : lists){
            paint.setColor(Color.parseColor("#CD5C5C"));
            element.drawBrick(canvas, paint);
            if(element.getVisibility()) {

                if(y1 > element.getY() && y1 < (element.getY() + element.getHeight())){
                    if (x1 > element.getX() && x1 < (element.getX() + element.getWidth())){
                        element.setInVisible();
                        dy1 = -dy1;
                        dx1 = -dx1;
                    }
                }
                //kiểm tra ball va chạm với gạch
                // viên nào bể thì set visible = false

            }
        }



        update();
        invalidate();

    }

    void update(){
        x1+= dx1;
        y1+= dy1;

    }

    @Override
    public void run() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean handled = false;

        int xTouch;
        int yTouch;
        int actionIndex = event.getActionIndex();


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);



                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);


                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    x2= xTouch;

                }


                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:

                handled = true;
                break;

            default:
                // do nothing
                break;


        }

        return super.onTouchEvent(event) || handled;
    }


}
