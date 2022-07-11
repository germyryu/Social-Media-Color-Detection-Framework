package edu.cmu.cs.cs214.hw6;

import java.io.IOException;
import java.util.*;
import edu.cmu.cs.cs214.hw6.framework.core.Connector;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.framework.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw6.framework.core.VisualizationPlugin;
import fi.iki.elonen.NanoHTTPD;

import java.util.Map;

public class App extends NanoHTTPD {

    public static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }
    private FrameworkImpl framework;

    public App() throws IOException {
        super(PORT);

        this.framework = new FrameworkImpl();
        // Start Framework

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    private String convertToJsonFormat(List<String> filenames) {
        String retString = "{ \"filenames\": [\"" +  String.join(",",filenames) + "\"]}";
        System.out.println(retString);
        return (retString);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();

        try {
            if (uri.equals("/dataplugin")) {
                String name = params.get("name");
                String username = params.get("username");
                DataPlugin dp = Connector.convertNameToDataPlugin(name);
                framework.registerPlugin(dp);
                framework.startDataPlugin(dp);
                framework.setUsername(username);
                framework.processColorPalettes();
                framework.processColors();
                System.out.println("Finished data plugin");
                return newFixedLengthResponse(("{ }"));
            } else if (uri.equals("/visplugin")) {
                String name = params.get("name");
                VisualizationPlugin dp = Connector.convertNameToVisualizationPlugin(name);
                framework.registerDisplayPlugin(dp);
                framework.startDisplayPlugin(dp);
                List<String> generatedImageLinks = framework.generateOutput();
                // Send back to frontend
                String request = convertToJsonFormat(generatedImageLinks);
                return newFixedLengthResponse(request);
            }

        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
        return null;
    }

    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getName());
            result.add(plugin);
        }
        return result;
    }
}
