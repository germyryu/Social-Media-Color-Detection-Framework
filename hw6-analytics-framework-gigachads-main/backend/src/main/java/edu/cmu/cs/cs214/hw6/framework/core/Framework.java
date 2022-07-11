package edu.cmu.cs.cs214.hw6.framework.core;

import java.awt.image.BufferedImage;

/**
 * The interface by which {@link DataPlugin} instances can directly interact
 * with the framework.
 */
public interface Framework {
    BufferedImage getImage(String imageUrl);
    int[][] getColorPalette(BufferedImage image);
}
