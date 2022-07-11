package org.objectdetection.plugin.visualization;

import org.icepear.echarts.Heatmap;
import org.icepear.echarts.charts.heatmap.HeatmapSeries;
import org.icepear.echarts.render.Engine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.objectdetection.framework.DetectionData;
import org.objectdetection.framework.VisualizationPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeatMapVisualization implements VisualizationPlugin {
    private int num;
    private JSONArray objects;

    /**
     * Get the name of the plugin.
     * @return The name of the plugin.
     */
    public String getPluginName() {
        return "Heat Map Plugin";
    }

    /**
     * Get the number of images to show in the heatmap
     * and the objects specified by the user from the
     * json file.
     * @param args A json object that contains the arguments of the plugin.
     */
    public void initialize(JSONObject args) {
        num = (int)((long) args.get("num"));
        objects = (JSONArray) args.get("objects");
    }

    /**
     * Visualize the detection data to a heatmap
     * @param data The detection result of given images.
     * @return An HTML string represents the generated heatmap.
     */
    public String visualizeData(DetectionData data) {
        return renderHeatMap(data);
    }

    private String renderHeatMap(DetectionData data) {
        Map<String, Integer> chartData = data.overviewObjects();
        int size = chartData.size();
        int numOfObject = (num < size ? num:size);
        String[] title = new String[size];
        int[] num  = new int[size];
        String[] result  = new String[numOfObject];
        int i = 0, j, index, max;
        int imageNum = data.getImageNumber();
        String[] picName  = new String[imageNum];
        List<Number[]> arrayData = new ArrayList<>();

        // If the user specifies the objects, it only generates the
        // specified objects. Otherwise, it generates the most frequent
        // objects
        if(objects.size() == 0){
            // Get the overview data of objects
            for (String key : chartData.keySet()) {
                title[i] = key;
                num[i] = chartData.get(key);
                i++;
            }

            // Find the most frequent objects
            for(i = 0; i < numOfObject; i++){
                index = 0;
                max = -1;
                for(j = 0; j < size; j++){
                    if(num[j] > max){
                        index = j;
                        max = num[j];
                    }
                }
                result[i] = title[index];
                num[index] = -1;
            }

            // Generate the data for heatmap
            for(i = 0; i < imageNum; i++) {
                Map<String, Float> map = data.getDetectionResult(i);
                for (j = 0; j < numOfObject; j++) {
                    arrayData.add(new Number[]{i, j, map.containsKey(result[j]) ? 1 : null});
                }
            }
        } else {
            result  = new String[objects.size()];
            // Objects specified by user
            for(i = 0; i < objects.size(); i++){
                result[i] = (String)objects.get(i);
            }

            // Generate the data for heatmap
            for(i = 0; i < imageNum; i++) {
                Map<String, Float> map = data.getDetectionResult(i);
                for (j = 0; j < objects.size(); j++) {
                    arrayData.add(new Number[]{i, j, map.containsKey(result[j]) ? 1 : null});
                }
            }
        }

        // Give names to pictures
        for(i = 0; i < imageNum; i++){
            picName[i] = "p" + i;
        }

        // Generate heatmap
        Heatmap heatmap = new Heatmap()
                .addXAxis(picName)
                .addYAxis(result)
                .setVisualMap(0, 1)
                .addSeries(new HeatmapSeries().setName("Image data")
                        .setData(arrayData.toArray()));
        Engine engine = new Engine();
        return engine.renderHtml(heatmap);
    }
}
