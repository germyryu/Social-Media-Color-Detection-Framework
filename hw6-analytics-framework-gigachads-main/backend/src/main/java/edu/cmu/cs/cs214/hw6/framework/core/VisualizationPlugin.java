package edu.cmu.cs.cs214.hw6.framework.core;

import java.awt.*;

import java.awt.image.BufferedImage;

import java.io.IOException;

import java.util.List;

public interface VisualizationPlugin {
    /**
     * Returns the name of the visualizer
     * @return a general name of the visualization display plugin
     */
    String getName();

    /**
     * Gets the description of the visualizer
     * @return the description of the display plugin
     */
    String getDescription();

    /**
     * Get the color palette (list of lists of rgb values)
     * @param img the image to analyze
     * @return an array of arrays where each array is in the form [red, blue, green]
     */
    int[][] getRGBValues(BufferedImage img);

    /**
     *
     * @param processedImages List of objects that have been processed by framework
     * @return A list of filenames representing the visualizer output image files
     */
    List<String> visualize(List<ImageData> processedImages) throws IOException;

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the object detection has begun (if necessary).
     *
     * @param framework The {@link Framework} instance with which the plug-in
     *                  was registered.
     */
    void onRegister(Framework framework);

    void onPluginClosed();
}
