package edu.cmu.cs.cs214.hw6.test;

import edu.cmu.cs.cs214.hw6.plugin.InstagramPlugin;
import edu.cmu.cs.cs214.hw6.plugin.TwitterPlugin;
import org.junit.Test;

import java.io.IOException;

public class GetRequestTest {
    private final InstagramPlugin myPlugin = new InstagramPlugin();
    @Test
    public void test1() throws IOException {
        myPlugin.setAccountName("cristiano");
        System.out.println(myPlugin.getImages());
    }

    private final TwitterPlugin twitterPlugin = new TwitterPlugin();
    @Test
    public void test2() {
        twitterPlugin.setAccountName("762799939395158016");
        System.out.println(twitterPlugin.getImages());
    }
}
