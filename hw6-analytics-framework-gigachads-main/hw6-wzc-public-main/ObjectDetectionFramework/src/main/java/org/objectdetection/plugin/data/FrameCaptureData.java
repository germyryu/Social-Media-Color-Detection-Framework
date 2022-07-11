package org.objectdetection.plugin.data;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.json.simple.JSONObject;
import org.objectdetection.framework.DataPlugin;
import org.objectdetection.framework.ImageSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FrameCaptureData implements DataPlugin {
	private int num;
	private String videoPath;
	private String picturePath;

	/**
	 * Get the name of the plugin.
	 * @return The name of the plugin.
	 */
	public String getPluginName() {
		return "Frame Capture Plugin";
	}

	/**
	 * Get video path and picture path from the json file.
	 * @param args A json object that contains the arguments of the plugin.
	 */
	public void initialize(JSONObject args) {
		num = (int)((long) args.get("num"));
		videoPath = (String) args.get("videoPath");
		picturePath = (String) args.get("picturePath");
	}

	/**
	 * Capture images from a video and add the images to the dataset by paths.
	 * @return A dataset of the images.
	 */
	public ImageSource inputImages() {
		frameCapture(videoPath, picturePath);
		ImageSource source = new ImageSource();
		for (int i = 0; i < num; i++) {
			source.addImageByPath(picturePath + i + ".jpg");
		}
		return source;
	}

	private void frameCapture(String videoPath, String picPath) {
		// Disable log messages.
		avutil.av_log_set_level(avutil.AV_LOG_QUIET);
		FFmpegFrameGrabber video = new FFmpegFrameGrabber(new File(videoPath));
		try {
			video.start();
			int frameNum = video.getLengthInFrames();
			Java2DFrameConverter converter = new Java2DFrameConverter();
			for (int i = 0; i < num; i++) {
				video.setFrameNumber(i * (frameNum / num));
				Frame frame = video.grabFrame();
				BufferedImage screenshot = converter.getBufferedImage(frame);
				ImageIO.write(screenshot, "jpg", new File(picPath + i + ".jpg"));
			}
			video.stop();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
