package edu.cmu.cs.cs214.hw6.test;

import edu.cmu.cs.cs214.hw6.framework.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
import edu.cmu.cs.cs214.hw6.plugin.InstagramPlugin;
import edu.cmu.cs.cs214.hw6.visualizer.ColorGradientPlugin;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitTests {
    private InstagramPlugin myPlugin;
    private FrameworkImpl framework;
    private ColorGradientPlugin displayPlugin;

    @Before
    public void setUp() {
        framework = new FrameworkImpl();
        myPlugin = new InstagramPlugin();
        myPlugin.setAccountName("ang.nathan");
        displayPlugin = new ColorGradientPlugin();
        framework.registerPlugin(myPlugin);
        framework.registerDisplayPlugin(displayPlugin);
        framework.startDataPlugin(myPlugin);
        framework.startDisplayPlugin(displayPlugin);
    }

    /*
    @Test
    public void test1() throws IOException {
        System.out.println(framework.getImageUrlsForPlugin());
    }

    @Test
    public void test2() throws IOException {
        List<String> imageUrls = framework.getImageUrlsForPlugin();
        for (String s : imageUrls) {
            BufferedImage img = framework.getImage(s);
            System.out.println(Arrays.deepToString(framework.getColorPalette(img)));
        }
    }

     */

    @Test
    public void testGradientImages() {
//        try {
//            List<String> imageUrls = framework.getImageUrlsForPlugin();
//            List<ImageData> images = new ArrayList<>();
//            for (String s : imageUrls) {
//                images.add(img);
//            }
//            displayPlugin.visualize(images);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
