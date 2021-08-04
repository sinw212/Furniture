package com.example.vrfa;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends Activity {
    private final static int RESULT_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super .onCreate(savedInstanceState);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Thread.sleep(500);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    requestPermissionCamera();
                }
            } else { //마시멜로우 미만
                Thread.sleep(500);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //퍼미션 권한 진행 함수
    public void requestPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            //사용자의 최초 퍼미션 허용을 확인            -true : 사용자 퍼미션 거부, -false : 사용자 동의 미 필
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(getApplicationContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RESULT_PERMISSIONS);
            }
            else {
                Toast.makeText(getApplicationContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RESULT_PERMISSIONS);
            }
        }
        else { // 퍼미션을 다 동의 했을 경우 다음으로 진행
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (requestCode == RESULT_PERMISSIONS) {
            if (grantResult.length > 0) {
                for (int aGrantResult : grantResult) {
                    if (aGrantResult == PackageManager.PERMISSION_DENIED) {
                        //권한이 하나라도 거부 될 시
                        finish();
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // 여러개의 권한 확인 후 메인으로 넘기기 위함
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                }
            }
        }
    }
}