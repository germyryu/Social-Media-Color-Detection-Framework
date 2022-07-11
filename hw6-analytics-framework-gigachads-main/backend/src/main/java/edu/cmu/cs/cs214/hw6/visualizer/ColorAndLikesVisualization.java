package edu.cmu.cs.cs214.hw6.visualizer;

import edu.cmu.cs.cs214.hw6.framework.core.Framework;
import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
import edu.cmu.cs.cs214.hw6.framework.core.VisualizationPlugin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.ChartUtilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ColorAndLikesVisualization implements VisualizationPlugin {

    @Override
    public String getName() {
        return "ColorAndLikes";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public int[][] getRGBValues(BufferedImage img) {
        return new int[0][];
    }

    /**
     * Create the visualization of gradient images given a list of images
     * @param processedImages List of objects that have been processed by framework
     * @return list of filenames of the bar charts created
     */
    @Override
    public List<String> visualize(List<ImageData> processedImages) throws IOException {
        List<String> result = new ArrayList<>();
        Map<String, Double> averages = generateAverages(processedImages);
        BarChart_AWT chart = new BarChart_AWT(averages);
        String fullFileName = "./../frontend/src/visualization_images/colorandlikes.png";
        chart.makeBarGraph(averages, fullFileName);
        System.out.println("Created file");
        System.out.println("Wrote chart as png");
        String fileName = "colorandlikes.png";
        result.add(fileName);
        return result;
    }

    @Override
    public void onRegister(Framework framework) {

    }

    @Override
    public void onPluginClosed() {

    }

    /**
     * Get the average number of likes for each color seen
     * @param processedImages the images used for analysis
     * @return a map of color to number of likes
     */
    private Map<String, Double> generateAverages(List<ImageData> processedImages) {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Integer> count = new HashMap<>();
        for (ImageData id : processedImages) {
            int likes = id.getLikes();
            List<String> colors = id.getColors();

            for (String color : colors) {
                if (averages.containsKey(color)) {
                    averages.put(color, averages.get(color) + likes);
                    count.put(color, count.get(color) + 1);
                } else {
                    averages.put(color, (double) likes);
                    count.put(color, 1);
                }
            }
        }

        for (Map.Entry<String,Double> entry : averages.entrySet()) {
            String color = entry.getKey();
            Double avg = entry.getValue();
            averages.put(color, avg / count.get(color));
        }

        return averages;
    }
}

class Runner {
    public static void main(String[] args) throws Exception {
        ColorAndLikesVisualization vis = new ColorAndLikesVisualization();
        vis.visualize(null);
    }
}

/**
 * New modifier created to include more parameters
 */
class BarChart_AWT extends ApplicationFrame {
    private JFreeChart barChart;
    public BarChart_AWT(Map<String, Double> averages) throws IOException {
        super( "App" );
    }

    public JFreeChart getBarChart() { return this.barChart; }

    /**
     * Create the bar chart
     * @param averages color to number of likes map
     * @param fileName filename to give the bar chart png file
     */
    public void makeBarGraph(Map<String, Double> averages, String fileName) throws IOException {
        this.barChart = ChartFactory.createBarChart(
                "Color and Likes",
                "Color",
                "Likes",
                createDataset(averages),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );
        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */
        File BarChart = new File( fileName );
        ChartUtilities.saveChartAsJPEG( BarChart , barChart , width , height );
        setContentPane( chartPanel );
    }

    /**
     * Helper function to help populate the data in bar chart
     * @param averages map of color to number of likes
     * @return data for each category returned for bar chart to render
     */
    private CategoryDataset createDataset(Map<String, Double> averages) {
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset( );

        for (Map.Entry<String,Double> entry : averages.entrySet()) {
            String color = entry.getKey();
            Double avg = entry.getValue();
            dataset.addValue( avg, color, "Likes" );
        }
        return dataset;
    }
}
