package edu.cmu.cs.cs214.hw6.visualizer;

import edu.cmu.cs.cs214.hw6.framework.core.Framework;
import edu.cmu.cs.cs214.hw6.framework.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
import edu.cmu.cs.cs214.hw6.framework.core.VisualizationPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ColorGradientPlugin implements VisualizationPlugin {
    private Framework framework;

    @Override
    public String getName() {
        return "ColorGradient";
    }

    @Override
    public String getDescription() {
        return "This display plugin creates a gradient for each image based on its top two dominant colors from the color palette.";
    }

    /**
     * Get an array of arrays of rgb values given an image
     * @param img the image to analyze
     * @return an array of arrays of rgb values
     */
    @Override
    public int[][] getRGBValues(BufferedImage img) {
        return framework.getColorPalette(img);
    }

    /**
     * Get an instance of Java's Color class using provided rgb value
     * @param rgbArray array of [r,g,b]
     * @return Color representing the rgb value
     */
    private Color getColorFromRGB(int[] rgbArray) {
        int r = rgbArray[0];
        int g = rgbArray[1];
        int b = rgbArray[2];
        return new Color(r, g, b);
    }

    /**
     * Returns an image of the gradient created using the two most dominant colors from an image
     * @param width width of gradient image
     * @param height height of gradient image
     * @param gradient1 the dominant color
     * @param gradient2 the second most dominant color
     * @return the output image showing the gradient from gradient1 to gradient2
     */
    public static BufferedImage createGradientImage(int width, int height, Color gradient1,
                                                    Color gradient2) {

        BufferedImage gradientImage = createCompatibleImage(width, height);
        GradientPaint gradient = new GradientPaint(0, 0, gradient1, 0, height, gradient2, false);
        Graphics2D g2 = (Graphics2D) gradientImage.getGraphics();
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);
        g2.dispose();

        return gradientImage;
    }

    /**
     * Creates a compatible image with respect to device configuration
     * @param width width of gradient image
     * @param height height of gradient image
     * @return image to paint gradient in
     */
    private static BufferedImage createCompatibleImage(int width, int height) {

        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);

    }

    /**
     * Saves the gradient image file to specified folder with specific filename
     * @param imageToSave the gradient image to save
     * @return filename of the saved gradient image file
     */
    private String saveImgFile(BufferedImage imageToSave) {
        String filePath = "./../frontend/src/visualization_images/gradient.png";
        File f = new File(filePath);
        try {
            ImageIO.write(imageToSave, "PNG", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "gradient.png";
    }

    /**
     * Returns an image using the image url that is provided
     * @param imageUrl url of the image
     * @return BufferedImage instance of the image
     */
    private BufferedImage getImage(String imageUrl) {
        BufferedImage image = null;
        try {
            URL url = new URL(imageUrl);
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Combine all the gradient images into one image
     * @param gradientImages gradient images
     * @return one BufferedImage containing all the gradient images
     */
    public static BufferedImage joinBufferedImages(List<BufferedImage> gradientImages) {
        //int offset = 2;
        int widthFactor = gradientImages.size() / 2; //2
        int heightFactor = gradientImages.size() - widthFactor; //3
        int width = gradientImages.get(0).getWidth() * widthFactor;
        int height = gradientImages.get(0).getHeight() * heightFactor;
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        for (int i = 0; i < gradientImages.size(); i++) {
            g2.drawImage(gradientImages.get(i), null, (i / heightFactor) * gradientImages.get(0).getWidth(),
                    (i % heightFactor) * gradientImages.get(0).getHeight());
        }
//        g2.drawImage(img1, null, 0, 0);
//        g2.drawImage(img2, null, 0, img1.getHeight());
        g2.dispose();
        return newImage;
    }

    /**
     * Create the visualization of gradient images given a list of images
     * @param processedImages List of objects that have been processed by framework
     * @return list of filenames
     */
    @Override
    public List<String> visualize(List<ImageData> processedImages) {
        List<BufferedImage> gradientImages = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < processedImages.size(); i++) {
            ImageData id = processedImages.get(i);
            BufferedImage img = getImage(id.getUrl());
            int[][] rgbValues = getRGBValues(img);
            int[] primaryColorArr = rgbValues[0];
            int[] secondaryColorArr = rgbValues[1];
            Color primaryColor = getColorFromRGB(primaryColorArr);
            Color secondaryColor = getColorFromRGB(secondaryColorArr);
            BufferedImage newImg = createGradientImage(200, 200, primaryColor, secondaryColor);
            gradientImages.add(newImg);
//            String fileName = saveImgFile(i, newImg);
//            result.add(fileName);
        }
        BufferedImage finalImage = joinBufferedImages(gradientImages);
        String fileName = saveImgFile(finalImage);
        result.add(fileName);
        return result;
    }

    @Override
    public void onRegister(Framework f) {
        framework = f;
    }

    @Override
    public void onPluginClosed() {

    }
}
