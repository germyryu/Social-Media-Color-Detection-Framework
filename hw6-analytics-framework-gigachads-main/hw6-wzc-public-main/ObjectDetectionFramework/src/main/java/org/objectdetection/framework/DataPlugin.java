package org.objectdetection.framework;

import org.json.simple.JSONObject;

/**
 * Data plugin to generate dataset for the framework.
 */
public interface DataPlugin {
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
	 * Input images to the plugin to generate dataset for the framework.
	 * @return A dataset of images.
	 */
	ImageSource inputImages();
}
