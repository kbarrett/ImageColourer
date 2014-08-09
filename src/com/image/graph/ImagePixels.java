package com.image.graph;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.ClosestFirstIterator;

public class ImagePixels implements Iterator<Pixel> {

  public static final int PIXEL_SIZE = 5;
  private static final int MAX_WEIGHT = 40;
  private static final int COLOUR_CHANGE_COST = 30;
  private final SimpleWeightedGraph<Pixel, DefaultWeightedEdge> _graph;
  private final ClosestFirstIterator<Pixel, DefaultWeightedEdge> _closestFirstIterator;

  public ImagePixels(final SimpleWeightedGraph<Pixel, DefaultWeightedEdge> graph) {
    _graph = graph;
    _closestFirstIterator = new ClosestFirstIterator<>(_graph);
  }

  public static ImagePixels create(final BufferedImage image, final int backgroundColour) {
    SimpleWeightedGraph<Pixel, DefaultWeightedEdge> graph = new SimpleWeightedGraph<Pixel, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    System.out.println("Creating vertices...");
    List<Pixel> vertices = new ArrayList<>();
    for (int y = 0; y < image.getHeight(); y += PIXEL_SIZE) {
      for (int x = 0; x < image.getWidth(); x += PIXEL_SIZE) {
        int colour = image.getRGB(x, y);
        if (colour != backgroundColour) {
          Pixel pixel = new Pixel(x, y);
          graph.addVertex(pixel);
          vertices.add(pixel);
        }
      }
    }
    System.out.println("Finished creating vertices. Adding edges...");
    for (int i = 0; i < vertices.size(); ++i) {
      Pixel pixel1 = vertices.get(i);
      int colour1 = image.getRGB(pixel1.x, pixel1.y);
      for (int j = i + 1; j < vertices.size(); ++j) {
        Pixel pixel2 = vertices.get(j);
        int colourDiff = (int) (Math.abs(colour1 - image.getRGB(pixel2.x, pixel2.y)) / 10.0);
        int weight = (int) Math.min(colourDiff, Math.max(COLOUR_CHANGE_COST, pixel1.distanceSq(pixel2)));
        if (weight < MAX_WEIGHT) {
          DefaultWeightedEdge edge = graph.addEdge(pixel1, pixel2);
          graph.setEdgeWeight(edge, weight);
        }
      }
    }
    System.out.println("Finished adding edges.");
    return new ImagePixels(graph);
  }

  @Override
  public boolean hasNext() {
    return _closestFirstIterator.hasNext();
  }

  @Override
  public Pixel next() {
    return _closestFirstIterator.next();
  }

}
