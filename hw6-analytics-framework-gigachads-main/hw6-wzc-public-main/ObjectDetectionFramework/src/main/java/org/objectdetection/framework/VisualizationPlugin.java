package org.objectdetection.framework;

import org.json.simple.JSONObject;

/**
 * Visualization plugin to visualize the detection
 * result of images for the framework.
 */
public interface VisualizationPlugin {
	/**
	 * Get the name of the plugin.
	 * @return The name of the plugin.
	 */
	String getPluginName();

	/**
	 * Get arguments of the plugin from a json file.
	 * @param args A json object that contains the arguments of the plugin.
	 */
	void initialize(JSONObject args);

	/**
	 * Visualize the detection data to a chart.
	 * @param data The detection result of given images.
	 * @return An HTML string represents the generated visualization webpage.
	 */
	String visualizeData(DetectionData data);
}
