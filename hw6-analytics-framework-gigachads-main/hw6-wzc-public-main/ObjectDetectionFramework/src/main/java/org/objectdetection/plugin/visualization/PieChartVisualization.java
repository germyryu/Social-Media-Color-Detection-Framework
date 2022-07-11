package org.objectdetection.plugin.visualization;

import org.json.simple.JSONObject;
import org.objectdetection.framework.DetectionData;
import org.objectdetection.framework.VisualizationPlugin;
import org.icepear.echarts.Pie;
import org.icepear.echarts.charts.pie.*;
import org.icepear.echarts.render.Engine;

import java.util.Map;

public class PieChartVisualization implements VisualizationPlugin {

	/**
	 * Get the name of the plugin.
	 * @return The name of the plugin.
	 */
	public String getPluginName() {
		return "Pie Chart Plugin";
	}

	/**
	 * Get arguments from the json file.
	 * @param args A json object that contains the arguments of the plugin.
	 */
	public void initialize(JSONObject args) {
	}

	/**
	 * Visualize the overview data of detection data to a pie chart
	 * @param data The detection result of given images.
	 * @return An HTML string represents the generated pie chart.
	 */
	public String visualizeData(DetectionData data) {
		return renderPieChart(data.overviewObjects());
	}

	private String renderPieChart(Map<String, Integer> chartData) {
		PieDataItem[] pieDataItem = new PieDataItem[chartData.size()];
		int i = 0;

		// Get data from the overview of detection data
		for (String key : chartData.keySet()) {
			pieDataItem[i] = new PieDataItem().setValue(chartData.get(key)).setName(key);
			i++;
		}

		// Generate pie chart
		Pie pie = new Pie();
		pie.setTooltip("item");
		pie.setLegend();
		pie.addSeries(new PieSeries().setRadius(new String[]{"40%", "70%"})
				.setItemStyle(new PieItemStyle()
						.setBorderRadius(10)
						.setBorderColor("#fff")
						.setBorderWidth(2))
				.setLabel(new PieLabel().setShow(false).setPosition("center"))
				.setEmphasis(new PieEmphasis().setLabel(new PieLabel()
						.setShow(true).setFontSize(20).setFontWeight("bold")))
				.setData(pieDataItem));
		Engine engine = new Engine();
		return engine.renderHtml(pie);
	}
}
