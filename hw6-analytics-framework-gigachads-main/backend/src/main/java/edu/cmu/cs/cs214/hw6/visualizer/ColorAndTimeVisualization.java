package edu.cmu.cs.cs214.hw6.visualizer;

import edu.cmu.cs.cs214.hw6.framework.core.Framework;
import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
import edu.cmu.cs.cs214.hw6.framework.core.VisualizationPlugin;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Visualizer for frequency of colors vs time
 */
public class ColorAndTimeVisualization implements VisualizationPlugin {
    private Framework framework;

    @Override
    public String getName() {
        return "ColorAndTime";
    }

    @Override
    public String getDescription() {
        return "This display plugin creates a graph of color vs. time showing when certain colors are posted more/less frequently at what time of the day.";
    }

    @Override
    public int[][] getRGBValues(BufferedImage img) {
        return framework.getColorPalette(img);
    }

    /**
     * @param processedImages List of objects that have been processed by framework
     * @return List of strings of filenames for the visualizations
     * @throws IOException
     */
    @Override
    public List<String> visualize(List<ImageData> processedImages) throws IOException {
        List<String> result = new ArrayList<>();
        LineChart_AWT chart = new LineChart_AWT("Visualizer", "Time vs. Frequency based on Color", processedImages);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        OutputStream out = new FileOutputStream("./../frontend/src/visualization_images/colorandtime.png");
        ChartUtilities.writeChartAsPNG(out, chart.getChart(), 600, 400);
        result.add("colorandtime.png");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onRegister(Framework f) {
        this.framework = f;
    }

    @Override
    public void onPluginClosed() {

    }
}

/**
 * Line Chart Object
 */
class LineChart_AWT extends ApplicationFrame {
    private final JFreeChart chart;

    /**
     * @param applicationTitle Title of app
     * @param chartTitle       Title of the chart
     * @param images           List of {@link ImageData} to be visualized
     */
    public LineChart_AWT(String applicationTitle, String chartTitle, List<ImageData> images) {
        super(applicationTitle);
        this.chart = ChartFactory.createLineChart(
                chartTitle,
                "Hour", "Number of images with Color",
                createDataset(images),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(this.chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    /**
     * @param images List of {@link ImageData} to be visualized
     * @return processed dataset of frequency of each color at each hour of the day
     */
    private CategoryDataset createDataset(List<ImageData> images) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        //Maps color to hour to count
        Map<String, Map<Integer, Integer>> colorMap = new HashMap<>();
        for (ImageData image : images) {
            List<String> colors = image.getColors();
            for (String color : colors) {
                for (Integer i = 0; i < 24; i++) {
                    if (colorMap.containsKey(color)) {
                        colorMap.get(color).put(i, 0);
                        continue;
                    }
                    Map<Integer, Integer> newMap = new HashMap();
                    colorMap.put(color, newMap);
                    newMap.put(i, 0);
                }
            }
        }

        for (ImageData image : images) {
            List<String> colors = image.getColors();
            for (String color : colors) {
                Integer oldCount = colorMap.get(color).get(image.getHour());
                colorMap.get(color).put(image.getHour(), oldCount + 1);
            }
        }

        for (String color : colorMap.keySet()) {
            for (Integer i = 0; i < 24; i++) {
                dataset.addValue(colorMap.get(color).get(i), color, i);
            }
        }
        return dataset;

    }
}
