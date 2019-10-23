package com.example.vrfa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.service.media.CameraPrewarmService;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Administrator on 2017-08-14.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{

    SurfaceHolder holder;   //서피스홀더
    Camera cam=null;        //카메라

    public CameraPreview (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public CameraPreview (Context context) {
        super(context);
        init(context);
    }
    public void init(Context context){

        holder=getHolder();
        holder.addCallback(this);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {//카메라 각도를 포트레이트로(90도)
        cam.startPreview();                 //프리뷰 시작

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        cam=Camera.open();                  //카메라 객체를 오픈(퍼미션 되어있어야 됨)
        try{
            cam.setPreviewDisplay(holder);  //프리뷰를 홀더로
        }catch(Exception e){
            e.printStackTrace();
        }
        cam.setPreviewCallback(this);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cam.stopPreview();
        cam.release();                      //카메라 죽이기
        cam=null;
        cam.setPreviewCallback(null);

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Parameters params = camera.getParameters();
        int w = params.getPreviewSize().width;
        int h = params.getPreviewSize().height;
        int format = params.getPreviewFormat();
        YuvImage image = new YuvImage(data, format, w, h, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Rect area = new Rect(0, 0, w, h);
        image.compressToJpeg(area, 100, out);
        Bitmap bm = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,w, h, matrix, true);
        MainActivity.shareBitmap=rotatedBitmap;
    }




}