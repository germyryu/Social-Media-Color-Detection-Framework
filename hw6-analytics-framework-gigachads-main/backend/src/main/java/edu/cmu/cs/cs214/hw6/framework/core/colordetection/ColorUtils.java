package edu.cmu.cs.cs214.hw6.framework.core.colordetection;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 *
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * From https://stackoverflow.com/questions/4126029/convert-rgb-values-to-color-name
 *
 */
public class ColorUtils {

    /**
     * Initialize the color list that we have.
     */
    private ArrayList<ColorName> initColorList() {
        ArrayList<ColorName> colorList = new ArrayList<ColorName>();
        colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));
        colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));
        colorList.add(new ColorName("Brown", 0xA5, 0x2A, 0x2A));
        colorList.add(new ColorName("Gold", 0xFF, 0xD7, 0x00));
        colorList.add(new ColorName("Gray", 0x80, 0x80, 0x80));
        colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));
        colorList.add(new ColorName("Orange", 0xFF, 0xA5, 0x00));
        colorList.add(new ColorName("Pink", 0xFF, 0xC0, 0xCB));
        colorList.add(new ColorName("Purple", 0x80, 0x00, 0x80));
        colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));
        colorList.add(new ColorName("Silver", 0xC0, 0xC0, 0xC0));
        colorList.add(new ColorName("White", 0xFF, 0xFF, 0xFF));
        colorList.add(new ColorName("Yellow", 0xFF, 0xFF, 0x00));
        return colorList;
    }

    /**
     * Get the closest color name from our list
     *
     * @param r
     * @param g
     * @param b
     * @return the name of the color based off of (r,g,b)
     */
    public String getColorNameFromRgb(int r, int g, int b) {
        ArrayList<ColorName> colorList = initColorList();
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colorList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }

        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return "No matched color name.";
        }
    }

    /**
     * Convert hexColor to rgb, then call getColorNameFromRgb(r, g, b)
     *
     * @param hexColor
     * @return the name of the color based off of a hex value
     */
    public String getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }

    public int colorToHex(Color c) {
        return Integer.decode("0x"
                + Integer.toHexString(c.getRGB()).substring(2));
    }

    public String getColorNameFromColor(Color color) {
        return getColorNameFromRgb(color.getRed(), color.getGreen(),
                color.getBlue());
    }

    /**
     * SubClass of ColorUtils. In order to lookup color name
     *
     */
    public static class ColorName {
        private final int r;
        private final int g;
        private final int b;
        private String name;

        public ColorName(String name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public int computeMSE(int pixR, int pixG, int pixB) {
            return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
                    * (pixB - b)) / 3);
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }

        public String getName() {
            return name;
        }
    }
}