package edu.cmu.cs.cs214.hw6.plugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.framework.core.Framework;
import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class InstagramPlugin implements DataPlugin {

    private static final String PLUGIN_NAME = "Instagram";
    private static String accountUsername = "";
    private Framework framework;

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getAccountName() {
        return accountUsername;
    }

    @Override
    public void setAccountName(String userName) {
        accountUsername = userName;
    }

    @Override
    public void setCredentials() {

    }

    /**
     *
     * @param uri uri to send GET request to
     * @return the request response as a string
     */
    public String getResponse(String uri) throws IOException {
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
        System.out.println(content);
        return content.toString();
    }

    /**
     * Get 5 or less instagram posts from the provided username's account and extract all the images
     * @return image, number of likes, and time posted as an ImageData object
     */
    @Override
    public List<ImageData> getImages() {
        String uri = String.format("https://www.instagram.com/%s/reels/?__a=1", accountUsername);
        System.out.println(uri);
        String responseBody = null;
        try {
            responseBody = getResponse(uri);
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        List<ImageData> imagesData = new ArrayList<>();
        List<Integer> numLikes = new ArrayList<>();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(responseBody);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert json != null;
        JSONObject graphqlJson = (JSONObject) json.get("graphql");
        JSONObject userJson = (JSONObject) graphqlJson.get("user");
        JSONObject edge_owner_to_timeline_mediaJson = (JSONObject) userJson.get("edge_owner_to_timeline_media");
        JSONArray edgesJson = (JSONArray) edge_owner_to_timeline_mediaJson.get("edges");
//        int minIterations = Math.min(x, edgesJson.size());
        int minIterations = Math.min(5, edgesJson.size());
        for (int i = 0; i < minIterations; i++) {
            Object o = edgesJson.get(i);
            JSONObject obj = (JSONObject) o;
            JSONObject nodeJson = (JSONObject) obj.get("node");
//                String contentId = nodeJson.get("id").toString();
            String datePosted = nodeJson.get("taken_at_timestamp").toString();
            int epochTime = Double.valueOf(datePosted).intValue();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(epochTime*1000L);
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            int hour = cal.get(Calendar.HOUR_OF_DAY);
//                JSONObject commentsJson = (JSONObject) nodeJson.get("edge_media_to_comment");
//                String commentsCount = commentsJson.get("count").toString();
            JSONObject likedJson = (JSONObject) nodeJson.get("edge_liked_by");
            String likesCount = likedJson.get("count").toString();
            int likes = Integer.parseInt(likesCount);
            String imageUrl = nodeJson.get("display_url").toString();
            ImageData imageData = new ImageData(imageUrl, likes, hour);
            imagesData.add(imageData);
            }
        return imagesData;
    }

    @Override
    public void onRegister(Framework f) {
        framework = f;
    }

    @Override
    public void onPluginClosed() {

    }
}
