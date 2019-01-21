package com.andoresu.heremaptest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.BindView;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {


    private String TAG = "TESTTAG_" + BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestActivityPermissions();
        }

        super.onCreate(savedInstanceState);
    }



    public void requestActivityPermissions(){
        boolean permissions = checkPermissions(this);
        if (!permissions) {
            myRequestPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(checkPermissions(this)){

        }else{
            Toast.makeText(this, R.string.error_no_permission, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public static boolean checkPermissions(Context context){
        boolean res = true;
        for(String per : Constants.permissions){
            if (ContextCompat.checkSelfPermission(context, per) != PackageManager.PERMISSION_GRANTED){
                res = false;
                break;
            }
        }
        return res;
    }

    public static void myRequestPermissions(Activity activity){
        ActivityCompat.requestPermissions(activity, Constants.permissions, Constants.REQUEST_PERMISSIONS);
    }


}
