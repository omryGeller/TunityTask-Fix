package geller.omry.tunitytask;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by omry on 4/26/2018.
 */

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private final int CAMERA_REQUEST_CODE=100;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera.PictureCallback rawCallback;
    private Camera.ShutterCallback shutterCallback;
    private boolean isFirstCalcClick=true;
    private ViewInfo viewInfo;
    private final String[] permissions=new String[]{Manifest.permission.CAMERA};
    private Button calculate;
    private boolean surfaceCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewInfo=new ViewInfo(); // object responsible for pixel calculations.

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, CAMERA_REQUEST_CODE);
            }
        }

        calculate=(Button)findViewById(R.id.calc_pixel);
        calculate.setClickable(false);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(isFirstCalcClick) {

                    viewInfo.calculateViewCenterPixel(surfaceView.getWidth(),surfaceView.getHeight());
                    isFirstCalcClick=false;
                    calculate.setText("SHOW CENTER PX DETAILS");
                    camera.takePicture(shutterCallback, rawCallback,rawCallback, rawCallback);
                }
                else {
                    Intent intent=new Intent(MainActivity.this,CentralPixelDetailsActivity.class);
                    String centralPixel=viewInfo.getCenterPixelRGBAsString();
                    intent.putExtra("CentralPixel",centralPixel);
                    isFirstCalcClick=true;
                    startActivity(intent);
                    finish();

                }
            }
        });
        surfaceView = (SurfaceView)findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                if(data == null)
                    return;
                Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length);
                Point point=viewInfo.getCenterPixel();
                if(bmp == null) {
                    return;
                }
                bmp=Bitmap.createScaledBitmap(bmp, surfaceView.getWidth(), surfaceView.getHeight(), false);
                int pixel=bmp.getPixel(point.x,point.y);
                viewInfo.calculateRGBOfPixel(pixel);
            }
        };
        shutterCallback=new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };
    }
    @Override
    public void onRequestPermissionsResult( int requestCode
                                            , @NonNull String[] permissions
                                            , @NonNull int[] grantResults)
    {
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults != null && grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (surfaceCreated)
                        startCamera();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    /**
     * This method starts the camera and configures it to be vertical and auto focused.
     */
    private void startCamera()
    {
        try{
            camera = Camera.open();
        }catch(RuntimeException e){

            return;
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.setDisplayOrientation(90);
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            params.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(params);
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                }
            });
            camera.enableShutterSound(false);
        } catch (Exception e) {
            return;
        }
    }
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        surfaceCreated=true;
        calculate.setClickable(true);
        surfaceHolder.setFixedSize(getWindow().getWindowManager()
                .getDefaultDisplay().getWidth(), getWindow().getWindowManager()
                .getDefaultDisplay().getHeight());
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED)
            startCamera();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceCreated=false;
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
