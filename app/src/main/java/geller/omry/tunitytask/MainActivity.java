package geller.omry.tunitytask;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.cameraview.CameraView;

/**
 * Created by omry on 4/26/2018.
 */

public class MainActivity extends AppCompatActivity{
    private final int CAMERA_REQUEST_CODE=100;
    private CameraView cameraView;
    private boolean isFirstCalcClick=true;
    private ViewInfo viewInfo;
    private final String[] permissions=new String[]{Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewInfo=new ViewInfo(); // object responsible for pixel calculations.
        boolean canUseCamera=false;

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, CAMERA_REQUEST_CODE);
            }
            else
                canUseCamera=true;
        }
        else
            canUseCamera=true;

        cameraView=(CameraView)findViewById(R.id.camera);

        if(canUseCamera && !cameraView.isCameraOpened())
            cameraView.start();

        final Button calculate=(Button)findViewById(R.id.calc_pixel);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFirstCalcClick) {
                    viewInfo.calculateViewCenterPixel(cameraView.getWidth(),cameraView.getHeight());
                    isFirstCalcClick=false;
                    calculate.setText("SHOW CENTER PX DETAILS");
                }
                else {
                    Intent intent=new Intent(MainActivity.this,CentralPixelDetailsActivity.class);
                    String centralPixel=viewInfo.getCenterPixelAsString();
                    intent.putExtra("CentralPixel",centralPixel);
                    isFirstCalcClick=true;
                    startActivity(intent);
                    finish();

                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult( int requestCode
                                            , @NonNull String[] permissions
                                            , @NonNull int[] grantResults)
    {
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults != null && grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(!cameraView.isCameraOpened())
                        cameraView.start();
                }
                else {
                    if (shouldShowRequestPermissionRationale(permissions[0]))
                            requestPermissions(permissions, CAMERA_REQUEST_CODE);
                    else
                        showPermissionAlert("Camera permission");
                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED){
                if(!cameraView.isCameraOpened())
                    cameraView.start();
            }
        }

    }

    @Override
    protected void onPause() {
        if(cameraView.isCameraOpened())
            cameraView.stop();
        super.onPause();
    }

    /**
     * <p>
     *     This method shows an AlertDialog to inform the user that the permission is vital.
     *     if the user clicks YES it will redirect him to the application settings screen to grant the app the permission it needs.
     *     if the user clicks NO the app will exit.
     * </p>
     */
    private void showPermissionAlert(String permission){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Mandatory Permission");
        builder.setMessage(permission+" is mandatory in order for the app to work\nwould you like to grant this permission?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        });
        builder.create().show();
    }
}
