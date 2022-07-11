package org.objectdetection;

import fi.iki.elonen.NanoHTTPD;
import org.objectdetection.framework.DataPlugin;
import org.objectdetection.framework.Framework;
import org.objectdetection.framework.VisualizationPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class App extends NanoHTTPD {

	private final Object lock = new Object();
	private List<DataPlugin> dataPlugins;
	private List<VisualizationPlugin> visualizationPlugins;
	private DataPlugin dataPlugin;
	private VisualizationPlugin visualizationPlugin;
	private String state;

	/**
	 * Start the server.
	 * @throws IOException
	 */
	public App() throws IOException {
		super(8080);

		dataPlugins = loadDataPlugin();
		visualizationPlugins = loadVisualizationPlugin();
		state = "chooseDataPlugin";

		// Start the server.
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("The server has been started successfully.\n");
	}

	public static void main(String[] args) {
		try {
			new App();
		} catch (IOException e) {
			System.err.println("An Error occurred when starting the server: " + e + "\n");
		}
	}

	/**
	 * Get information from the frontend, process in the backend
	 * and send response to frontend.
	 * @param session A session.
	 * @return A response to the frontend.
	 */
	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		Map<String, String> params = session.getParms();

		switch (uri) {
			case "/click":
				if (state.equals("chooseDataPlugin")) {
					synchronized (lock) {
						dataPlugin = dataPlugins.get(Integer.parseInt(params.get("index")));
						state = "chooseVisualizationPlugin";
					}
				} else if (state.equals("chooseVisualizationPlugin")) {
					synchronized (lock) {
						visualizationPlugin = visualizationPlugins.get(Integer.parseInt(params.get("index")));
						state = "busy";
					}
					boolean error = false;
					Framework framework = new Framework(dataPlugin, visualizationPlugin);
					try {
						framework.runFramework();
					} catch (RuntimeException e) {
						System.out.println(e);
						error = true;
					}
					synchronized (lock) {
						if (error) {
							state = "error";
						} else {
							state = "finish";
						}
					}
				}
				break;
			case "/new":
				synchronized (lock) {
					state = "chooseDataPlugin";
				}
				break;
			case "/fetch":
				break;
			default:
				// Unknown request.
				System.out.println("Unknown request:" + uri);

		}
		return newFixedLengthResponse(generateInfo());
	}

	private List<DataPlugin> loadDataPlugin(){
		ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
		List<DataPlugin> result = new ArrayList<>();
		for (DataPlugin plugin : plugins) {
			result.add(plugin);
		}
		return result;
	}

	private List<VisualizationPlugin>  loadVisualizationPlugin() {
		ServiceLoader<VisualizationPlugin> plugins = ServiceLoader.load(VisualizationPlugin.class);
		List<VisualizationPlugin> result = new ArrayList<>();
		for (VisualizationPlugin plugin : plugins) {
			result.add(plugin);
		}
		return result;
	}

	private String generateInfo() {
		StringBuilder pluginNames = new StringBuilder("[");
		synchronized (lock) {
			if (state.equals("chooseDataPlugin")) {
				for (DataPlugin plugin : dataPlugins) {
					if (!pluginNames.toString().equals("[")) {
						pluginNames.append(",");
					}
					pluginNames.append("\"").append(plugin.getPluginName()).append("\"");
				}
				pluginNames.append("]");
			} else if (state.equals("chooseVisualizationPlugin")) {
				for (VisualizationPlugin plugin : visualizationPlugins) {
					if (!pluginNames.toString().equals("[")) {
						pluginNames.append(",");
					}
					pluginNames.append("\"").append(plugin.getPluginName()).append("\"");
				}
				pluginNames.append("]");
			} else {
				pluginNames = new StringBuilder("[]");
			}
		}

		return "{\"pluginNames\": " + pluginNames + "," +
				" \"state\": " + "\"" + state + "\"" + "}";
	}
}