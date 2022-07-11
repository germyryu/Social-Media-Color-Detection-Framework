package org.objectdetection.framework;

import org.json.simple.JSONObject;

public class VisualizationPluginStub implements VisualizationPlugin {
	private String html = "uninitialized";
	@Override
	public String getPluginName() {
		return "Visualization Stub Plugin";
	}

	@Override
	public void initialize(JSONObject args) {
		html = "test";
	}

	@Override
	public String visualizeData(DetectionData data) {
		return html;
	}
}
