# Domain: 
Our domain is going to be in object detection.  The framework will take in images and run object detection on each of those images.  After that, we will link together the images with the image metadata from the website and the object lists created by the object detection algorithm.  We will also do some aggregation with the provided data, such as a sorted list of objects by frequency in our image feed. This will allow users to have a more creative take on their social media presence, providing means for introspection and fun.

Some possible data plugins could be:
- Instagram plugin that takes a user’s posted images using Instagram API
- Reddit plugin that takes in a subreddit’s feed using Reddit API
- Pinterest plugin that takes a user’s posted images using Pinterest API

Some possible visualization plugins could be:
- Word cloud containing objects of the highest frequency within the given image feed
- AI-generated art using some combination of the key words created by the object detection algorithm using Pixray API
- Graph containing correlation between objects and a chosen metadata (such as likes/upvotes)

# Generality vs. Specificity:
The scope of our framework is general in the sense that it will be able to allow for multiple data sources/plugins. With the frontend, the user will be able to choose which social media platform to run the framework on. Moreover, the data processing that our framework does is quite generic to most social media platforms as most, if not all, of them have images in the form of posts or threads.

Most social media platforms and apps also have similar metadata that is tagged along with an image whether it be part of a post or thread. The metadata that we specifically look at in our data visualization consists of reactions (likes, dislikes, etc.). However, there are a lot of extension points here for more interesting and unique analysis as the framework could be extended to use different metadata such as the date or location of the post/thread that the object was found on. This opens up a lot of reusability for the user using this framework.

The cost of changing our framework will be kept to a minimum as the framework will perform the object detection itself, hence providing benefits for reuse. Introducing or creating a new method in the framework will not require any heavy changes to all the plugins because once the framework runs the object detection on images, all the metadata will be available and methods can call upon this metadata for further analysis. Also, we plan to use a black box framework as it provides a lot more modularity and extension points in which we only have to implement a plugin interface and redirect control back to the framework. This achieves our goals of 1. Ability to load many plugins (social media platforms) without any hassle. 2. Having the framework delegate work to the specific plugins (e.g. plugin.getReactions()). 
	
Our framework will abstract away from any implementation details when it comes to object detection. Users do not need any information about how the underlying object detection works and users should only be responsible for choosing which data plugins to use as part of their data visualizations. Some other key abstractions that our framework provides is only using specific data in our data visualizations. An example, as mentioned above, is only retrieving the value for the frequency of appearances on a generic feed.

This framework also shows specificity because it specifically benefits users who are interested in understanding the visual analytics of their social media feeds. As an example, a user could use our framework to see what objects appear the most often on their feed or even try to understand why some objects have more reactions.

# Project Structure: 
Our project structure will be similar to the structure laid out in recitation 10, where the frontend is in TypeScript React and the backend (framework and plugins) is in Java.  
Frontend: The src folder will hold all of our React code. 
Backend: The framework folder will contain the framework implementation and the framework interface.  It will also include interfaces for the data plugins and visualization plugins.  A separate plugin folder will contain a folder for data plugins and a folder for visualization plugins.  In these folders will be the implementations for plugins which follow the interface specified in the framework folder.  We will load plugins similarly to how they are loaded in recitation 10, using ServiceLoader.

# Plugin Interfaces:

Descriptions are listed as comments in the API.

    /**
      *  The data plug-in interface that plug-ins use to implement the image loaders.
      */
    public interface DataPlugin {
    
        //*
          *  Gets where the data is coming from i.e Instagram: thezucc's feed
          */
        String getName();
    
        //*
          *  Sets the credentials to get the data i.e username/password 
          */
        void setCredentials();
    
        //*
          *	 Loads the minimum between total num images and x images from the given plugin
          */ 
        List<Image> getImages(int x);
    }

        //*
          *	 Returns the number of reactions for each reaction on posts that the object was found in.
          */ 
        Map<String, Integer> getReactions(Image i);
    }
    
    /**
      *  The visualization plug-in interface that plug-ins use to implement the data visualizers.
      */
    
    public interface VisualizationPlugin {
    
        //*
          *  Gets description of the visualizer
          */
        String getDescription();
    
        //*
          *  Does the visualizing given a list of processed images 
          *  (images paired with their metadata and list of keywords by object detection)
          */
        Image visualize(List<ProcessedImages> images);

            //*
          *  Lifecycle methods
          */
  	render();
	componentDidMount();
	componentDidUpdate();
	componentWillUnmount();
    }
    