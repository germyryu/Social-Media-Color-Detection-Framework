package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.plugin.InstagramPlugin;
import edu.cmu.cs.cs214.hw6.plugin.RedditPlugin;
import edu.cmu.cs.cs214.hw6.plugin.TwitterPlugin;
import edu.cmu.cs.cs214.hw6.visualizer.ColorAndLikesVisualization;
import edu.cmu.cs.cs214.hw6.visualizer.ColorAndTimeVisualization;
import edu.cmu.cs.cs214.hw6.visualizer.ColorGradientPlugin;

public class Connector {
    public static DataPlugin convertNameToDataPlugin(String name) {
        if (name.equalsIgnoreCase("instagram")) {
            return new InstagramPlugin();
        } else if (name.equalsIgnoreCase("twitter")) {
            return new TwitterPlugin();
        } else if (name.equalsIgnoreCase("reddit")) {
            return new RedditPlugin();
        } else {
            throw new RuntimeException("Unknown plugin name: " + name);
        }
    }

    public static VisualizationPlugin convertNameToVisualizationPlugin(String name) {
        if (name.equalsIgnoreCase("colorgradient")) {
            return new ColorGradientPlugin();
        } else if (name.equalsIgnoreCase("colorandtime")) {
            return new ColorAndTimeVisualization();
        } else if (name.equalsIgnoreCase("colorandlikes")) {
            return new ColorAndLikesVisualization();
        } else {
            throw new RuntimeException("Unknown plugin name: " + name);
        }
    }
}
