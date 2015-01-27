/*
 *  This file is part of C-Compact.
 *
 *  C-Compact is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  C-Compact is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with C-Compact. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright (c) 2014-2015 Fabian Hummer
 *  Copyright (c) 2014-2015 Thomas Pointhuber
 *  Copyright (c) 2014-2015 Peter Wassermair
 */
 
package at.jku.ssw.cmm.gui.file;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import sun.swing.ImageIconUIResource;

public final class LoadStatics {

	public static final JLabel loadImage(String path) {
		return loadImage(path, true);
	}

	// TODO
	public static final JLabel loadImage(String path, boolean createBorder) {
		return loadImage(path, true, -1, -1);
	}

	public static final ImageIcon loadIcon(String path, int width, int height){
		ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); //Scale it smoothly
		return new ImageIcon(newimg); //return the new image
	}
	
	public static final JLabel loadImage(String path, boolean createBorder,
			int width, int height) {
		
		BufferedImage loadBuffer = null;
		try {
			loadBuffer = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println("Error reading image:" + path);
			return new JLabel();
		}

		// Resize Window works up to 200px
		if (width != -1 && height != -1)
			loadBuffer = scaleImage(loadBuffer, width, height);
			JLabel picture = new JLabel(new ImageIcon(loadBuffer));
			if (createBorder)
	
				picture.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
			
			return picture;
	}

	// TODO Scale image only with width or only with height
	private static final BufferedImage scaleImage(BufferedImage image, int width, int height) {

		/*BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = newImage.getGraphics();

		double relX = width / image.getWidth();
		double relY = height / image.getHeight();
		
		if( relY > relX ){
			g.drawImage(image, 0, 0, (int)(image.getWidth()*relY), (int)(image.getHeight()*relY), null);
		}
		else{
			g.drawImage(image, 0, 0, (int)(image.getWidth()*relX), (int)(image.getHeight()*relX), null);
		}
		

		g.dispose();

		return newImage;*/

        BufferedImage newImage = new BufferedImage(width, height,
        		BufferedImage.TYPE_INT_ARGB);

        double relX = (double)(width) / (double)(image.getWidth());
		double relY =  (double)(height) / (double)(image.getHeight());
        
        Graphics2D grph = (Graphics2D) newImage.getGraphics();
        grph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //grph.scale(2, 2);
        
        if( relY < relX ){
        	grph.scale(relY, relY);
		}
		else{
			grph.scale(relX, relX);
		}
        

        // everything drawn with grph from now on will get scaled.

        grph.drawImage(image, 0, 0, null);
        grph.dispose();

        return newImage;
		
		/*BufferedImage resized = new BufferedImage(width, height, image.getType());
	    Graphics2D g = resized.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    
	    double relX = (double)(width) / (double)(image.getWidth());
		double relY = (double)(height) / (double)(image.getHeight());
		
		System.out.println("Scale target: " + width + ", " + height + ", " + image.getWidth() + ", " + image.getHeight());
		System.out.println("Scaling: " + relX + ", " + relY);
		
		if( relY > relX ){
			g.drawImage(image, 0, 0, (int)(image.getWidth()*relY), (int)(image.getHeight()*relY), null);
		}
		else{
			g.drawImage(image, 0, 0, (int)(image.getWidth()*relX), (int)(image.getHeight()*relX), null);
		}
	    
	    //g.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
	    g.dispose();
	    
	    return resized;*/

	}

	/**
	 * Copy Files from one Destination to another
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFileUsingStream(File source, File dest) throws IOException {
		
		//Closes the operation if source or destination is null
		if(source == null || dest == null)
			return;
		
		//is the file existing
		if(!source.exists())
			throw new IOException();
		
		//To cancel useless operations
		if(source.equals(dest))
			return;
		
		//TODO
		//Creating direktories,:
		String absolutePath = dest.getAbsolutePath();
		new File(absolutePath.substring(0, absolutePath.indexOf(dest.getName()))).mkdirs();
		
		System.out.println("Copying files from:" + source + " to " + dest); 
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	    	if(is != null)
	    		is.close();
	        
	    	if(is != null)
	    		os.close();
	    }
	}
	
	@Deprecated
	public static final JScrollPane loadHTMLdoc(String path, String pathCSS) {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		java.net.URL htmlURL = null;
		java.net.URL cssURL = null;
		try {
			htmlURL = new File(path).toURI().toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			cssURL = new File(pathCSS).toURI().toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		// Set content type of editor pane to HTML
		editorPane.setContentType("text/html");

		// Import stylesheet
		if (cssURL != null) {
			System.out.println("Reading stylesheet: " + cssURL);
			StyleSheet s = new StyleSheet();
			s.importStyleSheet(cssURL);
			HTMLEditorKit kit = (HTMLEditorKit) editorPane.getEditorKit();
			kit.setStyleSheet(s);
			javax.swing.text.Document doc = kit.createDefaultDocument();
			editorPane.setDocument(doc);
		}

		// Import Text
		if (htmlURL != null) {
			System.out.println("Loading HTML document: " + htmlURL);
			try {
				editorPane.setPage(htmlURL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + htmlURL);
			}
		} else {
			System.err.println("Couldn't find file");
		}

		// Put the editor pane in a scroll pane.
		editorPane.setMinimumSize(new Dimension(10, 10));
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setPreferredSize(new Dimension(100, 300));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));

		return editorScrollPane;
	}

	public static javax.swing.text.Document readStyleSheet(String path) {
		java.net.URL cssURL = null;
		try {
			cssURL = new File(path).toURI().toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		// Import stylesheet
		if (cssURL != null) {
			System.out.println("Reading stylesheet: " + cssURL);
			StyleSheet s = new StyleSheet();
			s.importStyleSheet(cssURL);
			HTMLEditorKit kit = new HTMLEditorKit();
			kit.setStyleSheet(s);
			javax.swing.text.Document doc = kit.createDefaultDocument();
			return doc;
		}
		return null;
	}

	public static java.net.URL getHTMLUrl(String path) {

		java.net.URL htmlURL = null;

		try {
			htmlURL = new File(path).toURI().toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		return htmlURL;
	}
}