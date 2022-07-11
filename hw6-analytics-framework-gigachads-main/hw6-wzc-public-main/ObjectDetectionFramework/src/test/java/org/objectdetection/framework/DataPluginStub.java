package org.objectdetection.framework;

import org.json.simple.JSONObject;

public class DataPluginStub implements DataPlugin {
	private String imageName = "uninitialized";

	@Override
	public String getPluginName() {
		return "Data Stub Plugin";
	}

	@Override
	public void initialize(JSONObject args) {
		imageName = "initialized";
	}

	@Override
	public ImageSource inputImages() {
		ImageSource source = new ImageSource();
		source.addImageByURL("https://www.fake.com/" + imageName + "1.jpg");
		source.addImageByURL("https://www.fake.com/" + imageName + "2.jpg");
		return source;
	}
}
