package org.objectdetection.plugin.visualization;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.Word;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;
import org.json.simple.JSONObject;
import org.objectdetection.framework.DetectionData;
import org.objectdetection.framework.VisualizationPlugin;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordCloudVisualization implements VisualizationPlugin {
    @Override
    public String getPluginName() {
        return "Word Cloud Plugin";
    }

    @Override
    public void initialize(JSONObject args) {
    }

    @Override
    public String visualizeData(DetectionData data) {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        List<WordFrequency> wordFrequencies = new ArrayList<>();
        Map<String, Integer> chartData = data.overviewObjects();
        for (Map.Entry<String, Integer> entry : chartData.entrySet()) {
            wordFrequencies.add(new WordFrequency(entry.getKey(), entry.getValue()));
        }
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1),
                new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1),
                new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("wordcloud.png");
         String htmlString = "<img src='wordcloud.png" + "'>";
         System.out.println(htmlString);
        // must return HTML string
        return htmlString;
    }
}
