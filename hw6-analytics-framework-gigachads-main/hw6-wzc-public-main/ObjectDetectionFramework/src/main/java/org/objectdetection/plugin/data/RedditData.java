package org.objectdetection.plugin.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.objectdetection.framework.DataPlugin;
import org.objectdetection.framework.ImageSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class RedditData implements DataPlugin {
    @Override
    public String getPluginName() {
        return "Reddit Plugin";
    }

    @Override
    public void initialize(JSONObject args) {

    }

    @Override
    public ImageSource inputImages() {
        RedditPlugin rp = new RedditPlugin();
        try {
            ImageSource is = rp.getImages();
            return is;
        }
        catch(Exception e) {
            return null;
        }
    }
}


class RedditPlugin {

    private static final String PLUGIN_NAME = "Reddit";
    private static String accountUsername = "Sadass_coffee_addict";
    private static final String REDDIT_APP_ID = "spweZ17iuUsan_LYYBdBTA";
    private static final String REDDIT_APP_SECRET = "cw_9e2BlC40d5Ef3e3EO_Mm9v5UedA";

    public String getName() {
        return PLUGIN_NAME;
    }

    public String getAccountName() {
        return accountUsername;
    }

    public void setAccountName(String userName) {
        accountUsername = userName;
    }

    /**
     * Get Reddit posts from the provided username's account and extract all the images
     * @return image, number of upvote, and time posted as an ImageData object
     */
    public ImageSource getImages() throws IOException {
        ImageSource is = new ImageSource();
        HttpClient httpclient = HttpClientBuilder.create().setUserAgent("Mozilla/5.0 Firefox/26.0").build();
        String accessToken = getRefreshToken("N8zGWPRFqHHcgPc1iRW4MduGJXxrng", "https://reddit.com");
        System.out.println(accessToken);
        try {
            String uri = "https://oauth.reddit.com/user/" + accountUsername + "/submitted?limit=10";
            HttpGet httpget = new HttpGet(uri);
            httpget.setHeader("Authorization", "bearer "+ accessToken +"");

            System.out.println("executing request" + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder content = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    content.append(line);
                }
                Map jsonMap = (Map) JSONValue.parse(content.toString());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement je = JsonParser.parseString(content.toString());
                String prettyJsonString = gson.toJson(je);
                JSONObject json = new JSONObject(jsonMap);
                JSONArray posts = (JSONArray) ((JSONObject)json.get("data")).get("children");
                for (Object post : posts) {
                    JSONObject jsonPost = (JSONObject) ((JSONObject)(post)).get("data");
                    if (jsonPost.containsKey("url")) {
                        String url = jsonPost.get("url").toString();
                        if (url.contains("jpg") || url.contains("jpeg") || url.contains("png")) {
                            System.out.println(url);
                            is.addImageByURL(url);
                        }
                    }
                }
            }
            EntityUtils.consume(entity);
            return is;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }


    /**
     * Get the access token given unique code and redirect url
     * @param code temporary oauth code
     * @param redirectUrl url to redirect to
     * @return access token
     */
    public static String getAccessToken(String code, String redirectUrl) throws ClientProtocolException, IOException {

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(REDDIT_APP_ID, REDDIT_APP_SECRET);
        provider.setCredentials( new AuthScope("ssl.reddit.com", 443), credentials);
        HttpClient httpclient = HttpClientBuilder.create().setUserAgent("Mozilla/5.0 Firefox/26.0").setDefaultCredentialsProvider(provider).build();
        try {

            HttpPost httppost = new HttpPost("https://ssl.reddit.com/api/v1/access_token");
            List <NameValuePair> nvps = new ArrayList <NameValuePair>(3);
            nvps.add(new BasicNameValuePair("code", code));
            nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
            nvps.add(new BasicNameValuePair("redirect_uri", "https://www.duckdns.org/login"));
            httppost.setEntity(new UrlEncodedFormEntity(nvps));
            httppost.setHeader("Accept","any;");

            System.out.println("executing request " + httppost.getRequestLine());

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            //System.out.println(response.getStatusLine());
            if (entity != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder content = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    content.append(line);
                }
                System.out.println(content.toString());
                Map json = (Map) JSONValue.parse(content.toString());
                if (json.containsKey("access_token")) {
                    return (String) (json.get("access_token"));
                }
            }
            EntityUtils.consume(entity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return null;
    }


    /**
     * Get the access token given unique code and redirect url
     * @param code temporary oauth code
     * @param redirectUrl url to redirect to
     * @return access token
     */
    public String getRefreshToken(String code, String redirectUrl) throws ClientProtocolException, IOException {

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(REDDIT_APP_ID, REDDIT_APP_SECRET);
        provider.setCredentials( new AuthScope("ssl.reddit.com", 443), credentials);
        HttpClient httpclient = HttpClientBuilder.create().setUserAgent("Mozilla/5.0 Firefox/26.0").setDefaultCredentialsProvider(provider).build();
        try {
            HttpPost httppost = new HttpPost("https://ssl.reddit.com/api/v1/access_token");
            List <NameValuePair> nvps = new ArrayList <NameValuePair>(3);
            String refreshToken = "66004688-UpOlVstwJ05gKHmbWcz6FySh57miEQ";
            nvps.add(new BasicNameValuePair("refresh_token", refreshToken));
            nvps.add(new BasicNameValuePair("grant_type", "refresh_token"));
            //nvps.add(new BasicNameValuePair("code", code));
            //nvps.add(new BasicNameValuePair("redirect_uri", "https://www.reddit.com"));
            httppost.setEntity(new UrlEncodedFormEntity(nvps));
            httppost.setHeader("Accept","any;");

            System.out.println("executing request " + httppost.getRequestLine());

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            //System.out.println(response.getStatusLine());
            if (entity != null) {
                System.out.println("here");
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder content = new StringBuilder();
                String line;
                while (null != (line = br.readLine())) {
                    content.append(line);
                }
                System.out.println(content.toString());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement je = JsonParser.parseString(content.toString());
                String prettyJsonString = gson.toJson(je);
                Map json = (Map) JSONValue.parse(content.toString());

                if (json.containsKey("access_token")) {
                    return (String) (json.get("access_token"));
                }
            }
            EntityUtils.consume(entity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return null;
    }
}

/**
 * Runner class is used to create unique access token
 */
class Runner {
    public static void main(String[] args) throws Exception {
        RedditPlugin rp = new RedditPlugin();
        rp.setAccountName("Sadass_coffee_addict");
        System.out.println("Images are : " + rp.getImages());
    }
}
