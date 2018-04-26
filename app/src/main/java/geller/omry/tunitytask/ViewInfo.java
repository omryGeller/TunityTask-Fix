package geller.omry.tunitytask;

import android.graphics.Point;

/**
 * Created by omry on 4/26/2018.
 */

public class ViewInfo {
   private int height;
   private int width;

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
     * Returns a String that represents the central pixel of the View properties that was calculated first with calculateViewCenterPixel function.
     * @return The String representing the central pixel, or null if problem occured with calculation.
     */
    public String getCenterPixelAsString(){
        if(height > 0 && width > 0)
            return height+"x"+width;
        else
            return null;
    }
}
