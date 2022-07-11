package org.objectdetection.framework;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FrameworkTest {
	private DataPlugin dataStub;
	private VisualizationPlugin visualizationStub;
	private Framework testFramework;

	@Before
	public void setup() {
		dataStub = new DataPluginStub();
		visualizationStub = new VisualizationPluginStub();
		testFramework = new Framework(dataStub, visualizationStub);
	}

	@Test
	public void testInitializeDataPlugin() {
		ImageSource source = dataStub.inputImages();
		assertEquals("https://www.fake.com/uninitialized1.jpg", source.getLocation(0));
		assertEquals("https://www.fake.com/uninitialized2.jpg", source.getLocation(1));
		testFramework.initializePlugins();
		source = dataStub.inputImages();
		assertEquals("https://www.fake.com/initialized1.jpg", source.getLocation(0));
		assertEquals("https://www.fake.com/initialized2.jpg", source.getLocation(1));
	}

	@Test
	public void testInitializeVisualizationPlugin() {
		assertEquals("uninitialized", visualizationStub.visualizeData(new DetectionData(null)));
		testFramework.initializePlugins();
		assertEquals("test", visualizationStub.visualizeData(new DetectionData(null)));
	}

	@Test
	public void testExtractImages() {
		testFramework.initializePlugins();
		assertEquals(testFramework.extractImages(), 2);
	}

	@Test
	public void testRenderVisualization() {
		testFramework.initializePlugins();
		assertEquals(testFramework.renderVisualization(false), "test");
		// Clean up space.
		try{
			File htmlFile = new File("index.html");
			htmlFile.delete();
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
