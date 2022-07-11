package edu.cmu.cs.cs214.hw6.framework.core;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageData {
    private String url;
    private int likes;
    private int hour;
    private List<String> colors;
    private int[][] colorPalette;

    public ImageData(String url) {
        this.url = url;
    }

    public ImageData(String url, int likes, int hour) {
        this.url = url;
        this.likes = likes;
        this.hour = hour;
    }

    public ImageData(int hour, List<String> colors) {
        this.hour = hour;
        this.colors = colors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "url: " + this.url + " likes: " + this.likes + " hour: " + this.hour;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public int[][] getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(int[][] colorPalette) {
        this.colorPalette = colorPalette;
    }


}
