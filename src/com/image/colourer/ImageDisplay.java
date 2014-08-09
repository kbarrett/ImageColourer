package com.image.colourer;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ImageDisplay {

	private final JLabel _jLabel = new JLabel();
	private final JFrame _editorFrame = new JFrame("Image Colourer");
  private boolean _firstImage;

	public static ImageDisplay create() {
		return new ImageDisplay().createDisplay();
	}

	private ImageDisplay() {
	  _firstImage = true;
	}

	private ImageDisplay createDisplay() {
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				_editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				_editorFrame.getContentPane().add(_jLabel, BorderLayout.CENTER);
				_editorFrame.pack();
				_editorFrame.setVisible(true);
			}
		});
		return this;
	}

	public void show(final Image image) {
		ImageIcon imageIcon = new ImageIcon(image);
		_jLabel.setIcon(imageIcon);
		if (_firstImage) {
  		_editorFrame.pack();
  		_firstImage = false;
		}
	}

  public void newImage() {
    _firstImage = true;
  }

}
