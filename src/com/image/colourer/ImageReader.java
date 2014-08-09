package com.image.colourer;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReader {

	final static int MAX_WIDTH = 600;
	final static int MAX_HEIGHT = 600;

	public static BufferedImage read(final String fileName) throws IOException {
	  return read(new File(fileName));
	}

	public static BufferedImage read(final File file) {
		BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
		if (image != null) {
		  image = scale(image);
		}
    return image;
	}

	public static BufferedImage scale(final BufferedImage original) {
	  final double ratio = getRatio(original);
	  int newWidth = (int)(original.getWidth() * ratio);
	  int newHeight = (int)(original.getHeight() * ratio);
	  BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
    Graphics2D g = resized.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
    g.dispose();
    return resized;
	}

	private static double getRatio(final BufferedImage origImage) {
		if (origImage.getWidth() > origImage.getHeight()) {
			int newWidth = Math.min(MAX_WIDTH, origImage.getWidth());
			return (float)newWidth / origImage.getWidth();
		}
		else {
			int newHeight = Math.min(MAX_HEIGHT, origImage.getHeight());
			return (float)newHeight / origImage.getHeight();
		}
	}

}

