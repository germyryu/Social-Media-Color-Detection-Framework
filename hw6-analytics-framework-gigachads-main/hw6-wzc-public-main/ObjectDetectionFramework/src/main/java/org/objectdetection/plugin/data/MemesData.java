package org.objectdetection.plugin.data;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.objectdetection.framework.DataPlugin;
import org.objectdetection.framework.ImageSource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class MemesData implements DataPlugin {
    private int num;

    /**
     * Get the name of the plugin.
     * @return The name of the plugin.
     */
    public String getPluginName() {
        return "Memes Plugin";
    }

    /**
     * Get the number of images from the json file.
     * @param args A json object that contains the arguments of the plugin.
     */
    public void initialize(JSONObject args) {
        num = (int)((long) args.get("num"));
    }

    /**
     * Get URLs from a json file and add images to the dataset by URLs.
     * @return A dataset of the images.
     */
    public ImageSource inputImages() {
        String json;
        int num;
        ImageSource source = new ImageSource();

        try {
            // Get the URLs in the json file
            json = IOUtils.toString(new URI("https://api.imgflip.com/get_memes"), Charset.forName("UTF-8"));
            JSONParser parser = new JSONParser();
            JSONObject meme = (JSONObject) parser.parse(json);
            JSONObject data = (JSONObject) meme.get("data");
            JSONArray memes = (JSONArray) data.get("memes");

            // The number of images
            if(this.num > memes.size()){
                num = meme.size();
            } else {
                num = this.num;
            }

            // Add to the dataset
            for(int i = 0; i < num; i++){
                String pic = (String)((JSONObject) memes.get(i)).get("url");
                pic = pic.replaceAll("\\\\","");
                source.addImageByURL(pic);
            }
        } catch (IOException | URISyntaxException | ParseException e) {
            e.printStackTrace();
        }

        return source;
    }
}
