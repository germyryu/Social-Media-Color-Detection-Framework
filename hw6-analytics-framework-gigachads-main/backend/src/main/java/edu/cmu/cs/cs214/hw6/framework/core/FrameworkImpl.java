package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.framework.core.colordetection.ColorThief;
import edu.cmu.cs.cs214.hw6.framework.core.colordetection.ColorUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FrameworkImpl implements Framework{
    private static final String NO_PLUGIN_NAME = "A framework";
    private static final List<ImageData> NO_IMAGES = new ArrayList<>();
    private List<DataPlugin> registeredDataPlugins;
    private List<VisualizationPlugin> registeredVisualizationPlugins;
    private DataPlugin currentPlugin;
    private VisualizationPlugin currentDisplayPlugin;
    private List<ImageData> imagesData;

    public FrameworkImpl() {
        registeredDataPlugins = new ArrayList<DataPlugin>();
        registeredVisualizationPlugins = new ArrayList<VisualizationPlugin>();
        imagesData = new ArrayList<>();
    }

    /**
     * Registers a new {@link DataPlugin} with the framework
     * @param plugin the specified data plugin to register with the framework
     */
    public void registerPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        registeredDataPlugins.add(plugin);
    }

    /**
     * Registers a new {@link VisualizationPlugin} with the framework
     * @param plugin the specified visualization plugin to register with the framework
     */
    public void registerDisplayPlugin(VisualizationPlugin plugin) {
        plugin.onRegister(this);
        registeredVisualizationPlugins.add(plugin);
    }

    /**
     * Sets currentPlugin to the specified {@link DataPlugin}
     * @param plugin the selected data plugin
     */
    public void startDataPlugin(DataPlugin plugin) {
        if (currentPlugin != plugin) {
            if (currentPlugin != null) currentPlugin.onPluginClosed();
            currentPlugin = plugin;
        }
    }

    /**
     * Sets currentPlugin to the specified {@link VisualizationPlugin}
     * @param plugin the selected visualization plugin
     */
    public void startDisplayPlugin(VisualizationPlugin plugin) {
        if (currentDisplayPlugin != plugin) {
            if (currentDisplayPlugin != null) currentDisplayPlugin.onPluginClosed();
            currentDisplayPlugin = plugin;
        }
    }

    /**
     * Generates visualizations from the visualization plugins
     * @return list of filenames for visualization output images
     * @throws IOException
     */
    public List<String> generateOutput() throws IOException {
        if (currentDisplayPlugin == null) {
            return new ArrayList<>();
        }
        return currentDisplayPlugin.visualize(this.imagesData);
    }

    public void setUsername(String username) {
        if (currentPlugin == null) {
            return;
        }
        currentPlugin.setAccountName(username);
    }

    /**
     * Processes the color palettes for the images given from {@link DataPlugin}
     * @throws IOException
     */
    public void processColorPalettes() throws IOException {
        List<ImageData> imagesData;
        if (currentPlugin == null) {
            return;
        }
        imagesData = currentPlugin.getImages();
        for (int i=0; i<imagesData.size(); i++) {
            ImageData id = imagesData.get(i);
            int[][] colorPalette = getColorPalette(getImage(id.getUrl()));
            id.setColorPalette(colorPalette);
        }
        this.imagesData = imagesData;
    }

    /**
     * Processes the color names for the images given from {@link DataPlugin}
     */
    public void processColors() throws IOException {
        if (currentPlugin == null) {
            return;
        }
        for (int i=0; i<imagesData.size(); i++) {
            ImageData id = imagesData.get(i);
            int[][] colorPalette = id.getColorPalette();
            List<String> colors = new ArrayList<>();
            ColorUtils cu = new ColorUtils();

            for (int[] rgb : colorPalette) {
                System.out.println(rgb.length);
                String color = cu.getColorNameFromRgb(rgb[0], rgb[1], rgb[2]);
                System.out.println(color);
                colors.add(color);
            }
            id.setColors(colors);
        }
    }

    public List<String> getRegisteredDataPluginName() {
        return registeredDataPlugins.stream().map(DataPlugin::getName).collect(Collectors.toList());
    }

    public List<String> getRegisteredVisualizationPluginName() {
        return registeredVisualizationPlugins.stream().map(VisualizationPlugin::getName).collect(Collectors.toList());
    }

    @Override
    public BufferedImage getImage(String imageUrl) {
        BufferedImage image = null;
        try {
            URL url = new URL(imageUrl);
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public int[][] getColorPalette(BufferedImage image) {
        return ColorThief.getPalette(image, 10);
    }

    public String getPluginName() {
        if (currentPlugin == null) {
            return NO_PLUGIN_NAME;
        } else {
            return currentPlugin.getName();
        }
    }
}
