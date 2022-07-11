For milestone C related work, scroll to the bottom of the README and find section labelled "Implementing Team wzc's Object Detection Framework".

# Social Media Color Detection & Analysis Framework

# How to Start
## Frontend
```
cd backend
mvn exec:exec
```

## Backend
```
cd frontend
npm install
npm run compile
npm run start
```
-----
# How to Use
1. Once you are at [localhost:3000](localhost:3000) you will be presented with a screen that asks you to input a 
specific username and select a data plugin.
2. Input username following these rules:
   1. Specific Instagram username (_e.g. ang.nathan_)
   2. Specific Twitter handle user-id (_e.g. 762799939395158016_)
   3. Specific Reddit username (_e.g. Sadass_coffee_addict_)
3. Click the corresponding data plugin.
4. You will then be presented with 3 buttons:
   1. Color Gradient: Output an image of color gradients using images that were extracted from the provided username's social media handle.
   2. Color vs. Time: Output a chart of Color vs. Time showing when certain colors were posted at which hour of the day.
   3. Color vs. Likes: Output a chart of Color vs. Likes showing how many likes certain colors from posts/images receive.
5. Select one.
6. Your visualization will appear!

-----

# Data Processing
Our framework uses a form of machine learning through color detection. This color detection algorithm is special as 
it uses the median-cut quantization algorithm to analyze the color palette of an image. an algorithm to sort data of an 
arbitrary number of dimensions into series of sets by recursively cutting each set of data at the median point along the 
longest dimension _(Source: Wikipedia)_.

-----

# API Documentation
### How to extend framework with addition plugins
- To add a data plugin, you will need to create a DataPlugin instance and give it a specific name, such as Pinterest.
- Then, our framework requires an implementation of getImage() as the method of retrieving images for a username is very different
for different social media platforms.
  - Some will require OAuth2, some will not.
  - We have both examples (Reddit and Twitter use OAuth, Instagram does not.)
- Then you can call registerPlugin() to register the data plugin with the framework.
- Visualization plugins are similar in that you must implement the visualize() function.
- You can also call registerDisplayPlugin() to register the display plugin with the framework.
## Framework API
```
BufferedImage getImage(String imageUrl);
```
- String imageUrl: the display image url
- return: the actual image that the image url links to in BufferedImage format
```
int[][] getColorPalette(BufferedImage image);
```
- BufferedImage image: the image that you receive when calling getImage
- return: an array of arrays in the form of [[r,g,b], [r,g,b]] going from the most dominant color in the image
to the least dominant

## Plugin API
```
String getName();
```
- return: get the name of the plugin
```
void setAccountName(String userName);
```
- set the username to analyze with framework
```
List<ImageData> getImages() throws IOException;
```
- return: get the images from the username's social media handle
```
void onRegister(Framework framework);
```
- register with framework
```
List<String> visualize(List<ImageData> processedImages) throws IOException;
```
- processedImages: the images that were passed from the data-plugin to the framework
- return: a list of filenames that represent the visualization image that is going to be rendered in the GUI

-----

# Implementing Team wzc's Object Detection Framework
Our 3-man team implemented 3 data plugins and 2 visualization plugins. The three
data plugins are Instagram Plugin, Reddit Plugin, and Twitter Plugin. These three plugins
are the same data plugins we used for our milestone B and, conveniently enough, our data plugins also
returned image urls. Aside from what is written below, everything else from setting up to getting the framework to run
is the same as what is documented in team wzc's readme file.
## Reddit plugin:
The Reddit plugin uses in the most recent images from a specific user
(Randomly chosen to be "Sadas*_coffee_addict") on Reddit for the framework.
Nothing is needed to start the reddit plugin.
## Twitter plugin:
Twitter Plugin gets image urls from tweets by the Twitter account specified by the ID.  
The ID is configured in configure.json and is the numeric ID for the account.
## Instagram plugin:
The Instagram plugin returns image urls from an Instagram user's most recent posts. The username that is being
passed into the data plugin is written in config.json.
## Word cloud visualization plugin:
The word cloud visualization plugin creates a local file named wordcloud.png that is then rendered
as html. One thing to note here is that the file will be created in the ObjectDetectionFramework folder.
As you may already know, a word cloud will show certain words with a larger font if they have a higher frequency.
I used kumo's word cloud 3rd party library that I found online. All you have to do to ensure it works is to mvn install
in the ObjectDetectionFramework folder.
## Bar chart visualization plugin:
The bar chart visualization plugin will get the k most recent objects and graph the object name
on the x-axis and its frequency on the y-axis. I used the ECharts library (same library that was used for team wzc's display plugins).
The variable k is defined in config.json and is initialized as this.num in the BarChartVisualization class.