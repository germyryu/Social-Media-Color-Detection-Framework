package org.objectdetection.plugin.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.objectdetection.framework.DataPlugin;
import org.objectdetection.framework.ImageSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InstagramData implements DataPlugin {
    private int num;
    private String username;

    @Override
    public String getPluginName() {
        return "Instagram Plugin";
    }

    @Override
    public void initialize(JSONObject args) {
        this.username = (String) args.get("id");
        this.num = (int)((long) args.get("num"));
    }

    private String getResponse(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
//        System.out.println(content);
        return content.toString();
    }

    @Override
    public ImageSource inputImages() {
        ImageSource source = new ImageSource();

        String uri = String.format("https://www.instagram.com/%s/reels/?__a=1", this.username);
//        System.out.println(uri);
        String responseBody = null;
        try {
            responseBody = getResponse(uri);
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(responseBody);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert json != null;
        JSONObject graphqlJson = (JSONObject) json.get("graphql");
        JSONObject userJson = (JSONObject) graphqlJson.get("user");
        JSONObject edgeOwnerTimelineMedia = (JSONObject) userJson.get("edge_owner_to_timeline_media");
        JSONArray edgesJson = (JSONArray) edgeOwnerTimelineMedia.get("edges");
        int minIterations = Math.min(this.num, edgesJson.size());
        for (int i = 0; i < minIterations; i++) {
            Object o = edgesJson.get(i);
            JSONObject obj = (JSONObject) o;
            JSONObject nodeJson = (JSONObject) obj.get("node");
            String imageUrl = nodeJson.get("display_url").toString();
            source.addImageByURL(imageUrl);
        }
        return source;
    }
}
