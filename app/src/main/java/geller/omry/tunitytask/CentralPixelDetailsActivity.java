package geller.omry.tunitytask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
/**
 * Created by Omry Geller
 * Date 26/4/18
 */
public class CentralPixelDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_central_pixel_details);

        Button backBtn=(Button)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView pixelDetails=(TextView)findViewById(R.id.pixel_details);

        Intent intent=getIntent();
        if(intent != null && intent.hasExtra("CentralPixel")){
            String details=intent.getStringExtra("CentralPixel");
            pixelDetails.setText(details);
        }
        else {
            pixelDetails.setText("No data go back and calculate again");
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }
}
