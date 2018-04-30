package geller.omry.tunitytask;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView title=(TextView)findViewById(R.id.title);

        Intent intent=getIntent();
        if(intent != null && intent.hasExtra("CentralPixel")){
            String details=intent.getStringExtra("CentralPixel");

            String[] RGBValues=details.split(",");
            int r=Integer.valueOf(RGBValues[0]);
            int g=Integer.valueOf(RGBValues[1]);
            int b=Integer.valueOf(RGBValues[2]);
            String titleText=title.getText().toString();
            title.setText(titleText+"\n"+"R="+r+","+"G="+g+","+"B="+b);

            pixelDetails.setBackgroundColor(Color.rgb(r,g,b));

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
