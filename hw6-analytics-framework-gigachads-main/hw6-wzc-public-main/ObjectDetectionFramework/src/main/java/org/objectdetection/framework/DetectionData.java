package org.objectdetection.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetectionData {
	private List<Map<String, Float>> detectionResult;

	/**
	 * Initialize the detection data.
	 * @param detectionResult A {@link Map} object that contains the detection results from the web api.
	 */
	public DetectionData(List<Map<String, Float>> detectionResult) {
		this.detectionResult = detectionResult;
	}

	/**
	 * Get the number of the images extracted by the data plugin.
	 * @return The number of the images.
	 */
	public int getImageNumber() {
		return detectionResult.size();
	}

	/**
	 * Get the detection result of a specified image.
	 * @param index The index of the image.
	 * @return A {@link Map} object that contains the detection result of the image.
	 * The key of the map is the name of the object in the image, the value is the probability of the detection.
	 */
	public Map<String, Float> getDetectionResult(int index) {
		return detectionResult.get(index);
	}

	/**
	 * Search for a specified object in the detection result.
	 * @param objectName the name of the object.
	 * @return A {@link List} object which contains the indexes of the images that have the object in it.
	 */
	public List<Integer> searchObject(String objectName) {
		List<Integer> indexes = new ArrayList<>();
		for (int i = 0; i < detectionResult.size(); i++) {
			if (detectionResult.get(i).containsKey(objectName)) {
				indexes.add(i);
			}
		}
		return indexes;
	}

	/**
	 * Get the most frequent object in the image set.
	 * @return The name of the most frequent object in the image set.
	 */
	public String getMostFrequentObject() {
		Map<String, Integer> overview = overviewObjects();
		String object = "";
		int maximumCount = 0;
		for (String objectName : overview.keySet()) {
			if (overview.get(objectName) > maximumCount) {
				maximumCount = overview.get(objectName);
				object = objectName;
			}
		}
		return object;
	}

	/**
	 * Get an overview of all objects detected in the image set.
	 * The overview shows the name of the objects detected in the image set and their frequency.
	 * @return A {@link Map} object that contains the overview information of the images.
	 * The key is the name of the object and the value is the frequency of that object appears in the image set.
	 */
	public Map<String, Integer> overviewObjects() {
		HashMap<String, Integer> overviewData = new HashMap<>();
		for (int i = 0; i < detectionResult.size(); i++) {
			for (String objectName : detectionResult.get(i).keySet()) {
				if (overviewData.containsKey(objectName)) {
					overviewData.replace(objectName, overviewData.get(objectName) + 1);
				} else {
					overviewData.put(objectName, 1);
				}
			}
		}
		return overviewData;
	}
}
