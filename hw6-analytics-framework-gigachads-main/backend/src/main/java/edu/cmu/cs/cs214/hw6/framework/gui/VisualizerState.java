package edu.cmu.cs.cs214.hw6.framework.gui;

import edu.cmu.cs.cs214.hw6.framework.core.FrameworkImpl;

import java.util.Arrays;
import java.util.List;

public class VisualizerState {
    /*
    private final Plugin[] dataPlugins, visualizerPlugins;
    private VisualizerState(Plugin[] dataPlugins, Plugin[] visualizationPlugins) {
        this.dataPlugins = dataPlugins;
        this.visualizerPlugins = visualizationPlugins;
    }

    private static Plugin[] getDataPlugins(FrameworkImpl framework) {
        List<String> dPlugins = framework.getRegisteredDataPluginName();
        Plugin[] plugins = new Plugin[dPlugins.size()];
        for (int i = 0; i < dPlugins.size(); i++) {
            String link = "/dataplugin?i="+ i;
            plugins[i] = new Plugin(dPlugins.get(i), link);
        }
        return plugins;
    }

    private static Plugin[] getVisualizationPlugins(FrameworkImpl framework) {
        List<String> vPlugins = framework.getRegisteredVisualizationPluginName();
        Plugin[] plugins = new Plugin[vPlugins.size()];
        for (int i = 0; i < vPlugins.size(); i++) {
            String link = "/visplugin?i="+ i;
            plugins[i] = new Plugin(vPlugins.get(i), link);
        }
        return plugins;
    }

    public Plugin[] getDataPlugins() {
        return dataPlugins;
    }

    public Plugin[] getVisualizerPlugins() {
        return visualizerPlugins;
    }

    @Override
    public String toString() {
        return ("{ \"dataPlugins\": " + Arrays.toString(dataPlugins) + "," +
                " \"visPlugins\": " + Arrays.toString(visualizerPlugins)).replace("null", "");
    }
    */
}
