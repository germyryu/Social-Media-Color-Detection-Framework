package org.objectdetection.framework;

import java.util.ArrayList;
import java.util.List;

public class ImageSource {
	private List<String> types;
	private List<String> locations;

	/**
	 * The constructor of ImageSource.
	 */
	public ImageSource() {
		types = new ArrayList<>();
		locations = new ArrayList<>();
	}

	/**
	 * Get the number of images.
	 * @return The number of images.
	 */
	public int size() {
		return types.size();
	}

	/**
	 * Get the type of the way to add the image.
	 * @param index The index of the image.
	 * @return The type of the way to add the image.
	 */
	public String getType(int index) {
		return types.get(index);
	}

	/**
	 * Get the location of the image.
	 * @param index The index of the image.
	 * @return The location of the image.
	 */
	public String getLocation(int index) {
		return locations.get(index);
	}

	/**
	 * Add an image by url.
	 * @param url The url of the image.
	 */
	public void addImageByURL(String url) {
		types.add("url");
		locations.add(url);
	}

	/**
	 * Add an image by path.
	 * @param path The path of the image.
	 */
	public void addImageByPath(String path) {
		types.add("path");
		locations.add(path);
	}
}
