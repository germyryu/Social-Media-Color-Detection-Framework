package edu.cmu.cs.cs214.hw6.test;

import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
import edu.cmu.cs.cs214.hw6.visualizer.ColorAndTimeVisualization;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class VisualizationTest {
    private final ColorAndTimeVisualization vis = new ColorAndTimeVisualization();
    private final ImageData image1 = new ImageData(0, Arrays.asList("brown", "blue"));
    private final ImageData image2 = new ImageData(2, Arrays.asList("green", "blue"));
    private final ImageData image3 = new ImageData(4, Arrays.asList("yellow", "white", "blue"));
    private final ImageData image4 = new ImageData(6, Arrays.asList("brown", "blue", "green"));
    private final ImageData image5 = new ImageData(0, Arrays.asList("brown", "blue", "green"));


    @Test
    public void test1() throws IOException {
        vis.visualize(Arrays.asList(image1, image2, image3, image4, image5));
    }
}
