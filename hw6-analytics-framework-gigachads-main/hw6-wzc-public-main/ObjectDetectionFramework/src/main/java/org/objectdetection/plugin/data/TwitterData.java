package org.objectdetection.plugin.data;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.objectdetection.framework.DataPlugin;
import org.objectdetection.framework.ImageSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TwitterData implements DataPlugin {
    private String username;
    private static final String BEARER_TOKEN =
            "AAAAAAAAAAAAAAAAAAAAAG5qbgEAAAAANP8E3V9XmzyG4gaDqgP" +
                    "AkPMzcWY%3Dnn8LeyfMP4zFSPo4rEyJSoe1z8yR1Jn7xzuz0o3aMsygzoMH9V";

    @Override
    public String getPluginName() {
        return "Twitter Plugin";
    }

    @Override
    public void initialize(JSONObject args) {
        this.username = (String) args.get("id");
    }

    @Override
    public ImageSource inputImages() {
        ImageSource images = new ImageSource();
        List<String> urls = getImages();
        for (String url : urls) {
            images.addImageByURL(url);
        }
        return images;
    }

    /**
     * @return List of image urls pertaining to the account
     */
    public List<String> getImages() {
        final String bearerToken = BEARER_TOKEN;
        ArrayList<String> imagesData = new ArrayList<>();
        try {
            String response = getTweets(this.username, bearerToken);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            JSONObject data = (JSONObject) json.get("includes");
            List medias = (List) data.get("media");

            for (Object media : medias) {
                JSONObject jsonMedia = (JSONObject) media;
                String type = (String) jsonMedia.get("type");
                if (type.equals("photo")) {
                    imagesData.add((String) jsonMedia.get("url"));
                }
            }
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println("Exception!");
        }
        return imagesData;
    }

    /**
     * @param userId id of the desired Twitter account
     * @return JSON formatted string containing tweets and related metadata
     * @throws IOException
     * @throws URISyntaxException
     */
    private static String getTweets(String userId, String bearerToken) throws IOException, URISyntaxException {
        String tweetResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder(String.format("https://api.twitter.com/2/users/%s/tweets", userId));
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at,public_metrics"));
        queryParameters.add(new BasicNameValuePair("media.fields", "url"));
        queryParameters.add(new BasicNameValuePair("expansions", "attachments.media_keys"));
        uriBuilder.addParameters(queryParameters);
        System.out.println(uriBuilder);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            tweetResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return tweetResponse;
    }
}
