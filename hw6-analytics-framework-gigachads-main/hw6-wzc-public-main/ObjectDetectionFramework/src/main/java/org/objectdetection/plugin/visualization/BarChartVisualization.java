package org.objectdetection.plugin.visualization;

import org.icepear.echarts.Bar;
import org.icepear.echarts.components.grid.Grid;
import org.icepear.echarts.origin.coord.cartesian.AxisOption;
import org.icepear.echarts.origin.coord.cartesian.CartesianAxisOption;
import org.icepear.echarts.origin.coord.cartesian.ValueAxisOption;
import org.icepear.echarts.render.Engine;
import org.json.simple.JSONObject;
import org.objectdetection.framework.DetectionData;
import org.objectdetection.framework.VisualizationPlugin;

import java.util.Arrays;
import java.util.Map;

public class BarChartVisualization implements VisualizationPlugin {
    private int num;
    @Override
    public String getPluginName() {
        return "Bar Chart Plugin";
    }

    @Override
    public void initialize(JSONObject args) {
        num = (int)((long) args.get("num"));
    }

    @Override
    public String visualizeData(DetectionData data) {
        return renderBarChart(data.overviewObjects());
    }

    private String renderBarChart(Map<String, Integer> chartData) {
        int toRender = Math.min(this.num, chartData.size());
        String[] xValues = new String[toRender];
        Number[] yValues = new Number[toRender];
        int i = 0;
        for (Map.Entry<String, Integer> entry : chartData.entrySet()) {
            if (i >= toRender) {
                break;
            }
            xValues[i] = entry.getKey();
            yValues[i] = entry.getValue();
            i++;
        }
        Bar bar = new Bar()
                .addXAxis(xValues)
                .addYAxis()
                .addSeries(yValues);
        Engine engine = new Engine();
        return engine.renderHtml(bar, "700px", "1800px");
    }
}
