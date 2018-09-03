package mandiri.finance.faith.Interface;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by dwi.prayogo on 11/27/2017.
 */

public class GetAllPermissionV2 {
    Context context;
    List<String> permissionList = new ArrayList<>();
    List<String> notGrantedList = new ArrayList<>();
    public GetAllPermissionV2(Context context)
    {
        this.context=context;
    }

    int PERMISSION_ALL = 101;


    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE};

    public boolean checkPermission(){
        ActivityCompat.requestPermissions(((Activity)context), PERMISSIONS, PERMISSION_ALL);
        Toast.makeText(((Activity)context),"Permission IMEI and Camera and Location HAVE BEEN GRANTED",Toast.LENGTH_SHORT).show();

//        if(!hasPermissions(context, PERMISSIONS)){
//            new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("Warning!")
//                    .setContentText("Your device need all requested permission for this apps. Please make sure you Allow all permission..")
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            ActivityCompat.requestPermissions(((Activity)context), PERMISSIONS, PERMISSION_ALL);
//                            sweetAlertDialog.dismiss();
//                        }
//                    }).show();
//
//            return false;
//        }
//        else
//        {
//            new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
//                    .setTitleText("Warning!")
//                    .setContentText("Your device have all requested permission for this apps")
//                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                        @Override
//                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                            sweetAlertDialog.dismiss();
//                        }
//                    }).show();
//            return true;
//        }

        return true;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}

