package edu.cmu.cs.cs214.hw6.plugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.framework.core.Framework;
import edu.cmu.cs.cs214.hw6.framework.core.ImageData;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataPlugin for Twitter
 */
public class TwitterPlugin implements DataPlugin {

    private static final String PLUGIN_NAME = "Twitter";
    private static String accountUsername = "";
    private static final String BEARER_TOKEN =
            "AAAAAAAAAAAAAAAAAAAAAG5qbgEAAAAANP8E3V9XmzyG4gaDqgP" +
                    "AkPMzcWY%3Dnn8LeyfMP4zFSPo4rEyJSoe1z8yR1Jn7xzuz0o3aMsygzoMH9V";

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
     * @return List of image data pertaining to the account
     */
    @Override
    public List<ImageData> getImages() {
        final String bearerToken = BEARER_TOKEN;
        ArrayList<ImageData> imagesData = new ArrayList<>();
        try {
            String response = getTweets(accountUsername, bearerToken);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            JSONObject data = (JSONObject) json.get("includes");
            List medias = (List) data.get("media");

            for (Object media : medias) {
                JSONObject jsonMedia = (JSONObject) media;
                String type = (String) jsonMedia.get("type");
                if (type.equals("photo")) {
                    String mediaKey = (String) jsonMedia.get("media_key");
                    for (Object tweetData : (List) json.get("data")) {
                        JSONObject jsonTweetData = (JSONObject) tweetData;
                        JSONObject attachments = (JSONObject) jsonTweetData.get("attachments");
                        List mediaKeys = (List) attachments.get("media_keys");
                        for (Object key : mediaKeys) {
                            String attachment_key = (String) key;
                            if (mediaKey.equals(attachment_key)) {
                                Long likes = (Long) ((JSONObject) jsonTweetData.get("public_metrics")).get("like_count");
                                String created = (String) jsonTweetData.get("created_at");
                                String[] split = created.split("T", 2);
                                int hour = Integer.parseInt(split[1].substring(0, 2));
                                ImageData id = new ImageData((String) jsonMedia.get("url"), likes.intValue(), hour);
                                imagesData.add(id);
                            }
                        }
                    }
                }
            }
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println("Exception!");
        }
        return imagesData;
    }

    /**
     *
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

    @Override
    public void onRegister(Framework f) {
        framework = f;
    }

    public void onPluginClosed() {
    }
}
