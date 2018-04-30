package geller.omry.tunitytask;


import android.graphics.Color;
import android.graphics.Point;


/**
 * Created by omry on 4/26/2018.
 */

public class ViewInfo {
   private int height;
   private int width;
   private int R,G,B;


    public ViewInfo() {
        this.height = 0;
        this.width = 0;
    }

    /**
     * This method calculates the central pixel relativly to the view properties it gets as parameters.
     * @param width The relevant View width to be calculated, positive value only.
     * @param height The relevant View height to be calculated, positive value only.
     */
    public void calculateViewCenterPixel(int width,int height){
        if(width <= 0 || height <= 0){
            return;
        }
        this.height=height/2;
        this.width=width/2;
    }
    /**
     * Returns a Point object that represents the central pixel of the View properties that was calculated first with calculateViewCenterPixel function.
     * @return The Point Object representing the central pixel, or null if problem occured with calculation.
     */
    public Point getCenterPixel(){
        if(height > 0 && width > 0)
            return new Point(width,height);
        else
            return null;
    }

    /**
     * Returns a String that represents the central pixel RGB of the View properties that was calculated first with calculateViewCenterPixel function.
     * @return The String representing the central pixel RGB, or null if problem occured with calculation.
     */
    public String getCenterPixelRGBAsString(){
        if(height > 0 && width > 0)
            return R+","+G+","+B;
        else
            return "0,0,0";
    }

    /**
     * This method extarct the RGB values of a specific pixel
     * @param pixel pixel value of a Bitmap.
     */
    public void calculateRGBOfPixel(int pixel){

        R = Color.red(pixel);
        G = Color.green(pixel);
        B = Color.blue(pixel);
    }
}
