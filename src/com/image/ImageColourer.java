package com.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.image.colourer.ImageDisplay;
import com.image.colourer.ImageReader;
import com.image.graph.ImagePixels;

public class ImageColourer {

	public static void main(final String[] args) throws InterruptedException {
	  if (args.length != 1) {
	    System.out.println("Requires a directory to use.");
	    return;
	  }
	  final ImageDisplay display = ImageDisplay.create();
	  File picturesDir = new File(args[0]);
	  List<File> files = Arrays.asList(picturesDir.listFiles());
	  Collections.shuffle(files);
    for (File file : files) {
	    display.newImage();
	    final BufferedImage original = ImageReader.read(file);
	    if (original == null) {
	      continue;
	    }
	    final BufferedImage copy = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
	    int backgroundColour = defaultBackground(original, copy);
	    System.out.println("Creating graph...");
	    final ImagePixels pixels = ImagePixels.create(original, backgroundColour);
	    System.out.println("Graph created.");
	    Point nextPoint;
      while (pixels.hasNext()) {
        nextPoint = pixels.next();
        Thread.sleep(10);
        for (int xDiff = 0; xDiff < ImagePixels.PIXEL_SIZE; ++xDiff) {
          copyColour(original, copy, nextPoint.x, nextPoint.y, nextPoint.x + xDiff, nextPoint.y + xDiff);
          copyColour(original, copy, nextPoint.x, nextPoint.y, nextPoint.x + xDiff + 1, nextPoint.y + xDiff);
          copyColour(original, copy, nextPoint.x, nextPoint.y, nextPoint.x + xDiff, nextPoint.y + ImagePixels.PIXEL_SIZE - xDiff - 1);
          copyColour(original, copy, nextPoint.x, nextPoint.y, nextPoint.x + xDiff + 1, nextPoint.y + ImagePixels.PIXEL_SIZE - xDiff - 1);
        }
        display.show(copy);
      }
    }
	}

  private static int defaultBackground(final BufferedImage original, final BufferedImage copy) {
    int commonColour = findMostCommonColour(original);
    System.out.println("Setting background.");
    for (int x = 0; x < copy.getWidth(); ++x) {
      for (int y = 0; y < copy.getHeight(); ++y) {
        copy.setRGB(x, y, commonColour);
      }
    }
    return commonColour;
  }

  private static int findMostCommonColour(final BufferedImage image) {
    System.out.println("Finding most common colour in image.");
    Map<Integer, Integer> colourToCount = new LinkedHashMap<Integer, Integer>();
    int commonColour = Integer.MIN_VALUE;
    int commonCount = -1;
    for (int x = 0; x < image.getWidth(); ++x) {
      for (int y = 0; y < image.getHeight(); ++y) {
        int colour = image.getRGB(x, y);
        Integer count = colourToCount.get(colour);
        if (count == null) {
          count = 0;
        }
        ++count;
        if (commonCount < count) {
          commonCount = count;
          commonColour = colour;
        }
        colourToCount.put(colour, count);
      }
    }
    return commonColour;
  }

  private static void copyColour(final BufferedImage original, final BufferedImage copy, final int origX, final int origY, final int newX, final int newY) {
	  if (origX < original.getWidth() && origY < original.getHeight() && newX < copy.getWidth() && newY < copy.getHeight()) {
	    copy.setRGB(newX, newY, original.getRGB(origX, origY));
	  }
	}

}
