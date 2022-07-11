package edu.cmu.cs.cs214.hw6.framework.core;

import java.io.IOException;
import java.util.List;

public interface DataPlugin {
    /**
     * Gets the name of the data plug-in (where the data is coming from)
     * i.e. Instagram:
     * @return the name of the data plugin
     */
    String getName();

    String getAccountName();

    void setAccountName(String userName);

    /**
     * Sets the credentials to get the data i.e. username/password
     */
    void setCredentials();

    /**
     * Gets the images using this specific data plugin.
     * @return A list of image urls where length of list is minimum of x and total number of images
     */
    List<ImageData> getImages() throws IOException;

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the object detection has begun (if necessary).
     *
     * @param framework The {@link Framework} instance with which the plug-in
     *                  was registered.
     */
    void onRegister(Framework framework);

    void onPluginClosed();
}
