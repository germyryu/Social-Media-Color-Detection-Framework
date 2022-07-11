package org.objectdetection.framework;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Framework {
	private ImageSource sourceData;
	private DetectionData detectionData;
	private DataPlugin dataPlugin;
	private VisualizationPlugin visualizationPlugin;

	/**
	 * Initialize the framework with data plugin and visualization plugin.
	 * @param dataPlugin The data plugin.
	 * @param visualizationPlugin The visualization plugin.
	 */
	public Framework(DataPlugin dataPlugin, VisualizationPlugin visualizationPlugin) {
		this.dataPlugin = dataPlugin;
		this.visualizationPlugin = visualizationPlugin;
	}

	/**
	 * Run the framework.
	 */
	public void runFramework() {
		// Load arguments from config file and initialize plugins.
		initializePlugins();
		// Run data plugin and extract images.
		extractImages();
		// Run object detection web API.
		objectDetection();
		// Run visualization plugin and render webpage for visualization.
		renderVisualization(true);
	}

	/**
	 * Use visualization plugin to render an HTML page. The page could pop up in user's browser if the option is turned on.
	 * This method should be called in the order of framework's life cycle.
	 * @param popUp Whether the page is to pop up in the user's browser.
	 * @return The string of the HTML page.
	 */
	public String renderVisualization(boolean popUp) {
		String html = visualizationPlugin.visualizeData(detectionData);
		try {
			BufferedWriter htmlFile = new BufferedWriter(new FileWriter("index.html"));
			htmlFile.write(html);
			htmlFile.close();
			if (popUp) {
				File webpage = new File("index.html");
				Desktop.getDesktop().browse(webpage.toURI());
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		return html;
	}

	/**
	 * Initialize the data plugin and the visualization plugin.
	 * This method should be called in the order of framework's life cycle.
	 */
	public void initializePlugins() {
		try {
			JSONParser parser = new JSONParser();
			JSONObject pluginJSON = (JSONObject) parser.parse(new FileReader("src/main/resources/config.json"));
			JSONObject dataJSON = (JSONObject) pluginJSON.get(dataPlugin.getPluginName());
			JSONObject visualizationJSON = (JSONObject) pluginJSON.get(visualizationPlugin.getPluginName());
			dataPlugin.initialize(dataJSON);
			visualizationPlugin.initialize(visualizationJSON);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void objectDetection() {
		List<Map<String, Float>> detectionResult = new ArrayList<>();
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
			for (int i = 0; i < sourceData.size(); i++) {
				System.out.println("image" + (i + 1) + "/" + sourceData.size());
				ByteString imgBytes = null;
				Map<String, Float> currentImageData = new HashMap<>();
				if (sourceData.getType(i).equals("url")) {
					imgBytes = addImageByURL(sourceData.getLocation(i));
				} else if (sourceData.getType(i).equals("path")) {
					imgBytes = addImageByPath(sourceData.getLocation(i));
				}

				// Builds the image annotation request
				List<AnnotateImageRequest> requests = new ArrayList<>();
				com.google.cloud.vision.v1.Image img = com.google.cloud.vision.v1.Image.newBuilder().setContent(imgBytes).build();
				Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
				AnnotateImageRequest request =
						AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
				requests.add(request);

				// Performs label detection on the image file
				BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
				List<AnnotateImageResponse> responses = response.getResponsesList();
				for (AnnotateImageResponse res : responses) {
					if (res.hasError()) {
						System.out.format("Error: %s%n", res.getError().getMessage());
						throw new RuntimeException("error!");
					}

					for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
						currentImageData.put(annotation.getDescription(), annotation.getScore());
					}
					detectionResult.add(currentImageData);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}

		detectionData = new DetectionData(detectionResult);
		System.out.println("Request from web API successfully.");
	}

	/**
	 * Extract the images using the data plugin.
	 * This method should be called in the order of framework's life cycle.
	 * @return The number of images extracted by the data plugin.
	 */
	public int extractImages() {
		sourceData = dataPlugin.inputImages();
		return sourceData.size();
	}

	private ByteString addImageByURL(String url) {
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.connect();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			IOUtils.copy(conn.getInputStream(), stream);

			return ByteString.copyFrom(stream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ByteString addImageByPath(String path) {
		ByteString imgBytes = null;
		try {
			// Reads the image file into memory
			Path filePath = Paths.get(path);
			byte[] data = Files.readAllBytes(filePath);
			imgBytes = ByteString.copyFrom(data);
		} catch (Exception e) {
			System.out.println(e);
		}
		return imgBytes;
	}
}
