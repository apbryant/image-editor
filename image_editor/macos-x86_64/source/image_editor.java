/* autogenerated by Processing revision 1291 on 2023-03-26 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import javax.swing.*;
import java.awt.event.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class image_editor extends PApplet {




PImage[] images;
PGraphics[] pGraphics;
String[] imageFiles;
JComboBox<String> jComboBox;
String[] optionsToChoose = {"Painting effect", "Grid effect"};

public void setup() {
  JFrame jFrame = new JFrame("Image editor");

  JLabel jLabel = new JLabel();
  jLabel.setText("Pick an editing option");
  jLabel.setBounds(100, 50, 400, 100);
  jFrame.add(jLabel);

  JButton jButton = new JButton("Process images");
  jButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      prepareImages();
      println("Begin processing images");
      processImages();
      println("Images processed");
    }
  }
  );
  jButton.setBounds(100, 300, 400, 100);
  jFrame.add(jButton);
  
  jComboBox = new JComboBox<>(optionsToChoose);
  jComboBox.setBounds(100, 100, 400, 50);
  jFrame.add(jComboBox);
  
  jFrame.setSize(900, 900);
  jFrame.setLayout(null);
  jFrame.setVisible(true);
}

public void draw() {
}

public void prepareImages() {
  try {
    String[] imageFiles = getImageFiles();
    pGraphics = new PGraphics[imageFiles.length];
    images = new PImage[imageFiles.length];

    for (int i = 0; i < imageFiles.length; i++) {
      PImage img = loadImage(imageFiles[i]);
      int imageWidth = img.width;
      int imageHeight = img.height;
      pGraphics[i] = createGraphics(imageWidth, imageHeight);
      images[i] = img;
    }
  }
  catch (Exception e) {
    String[] msg = new String[2];
    msg[0] = e.getStackTrace().toString();
    msg[1] = e.getMessage();
    saveStrings("setupException.txt", msg);
  }
}

public void processImages() {
  try {
    noLoop();
    for (int i = 0; i < pGraphics.length; i++) {
      PGraphics pg = pGraphics[i];
      int nCols = 10;
      int nRows = 10;
      pg.beginDraw();
      pg.image(images[i], 0, 0, pg.width, pg.height);
      
      for (int y = 0; y < nRows; y++) {
        for (int x = 0; x < nCols; x++) {
          int index = jComboBox.getSelectedIndex();
          switch (index) {
           case 0:
             doPaintingEffect(pg, nCols, nRows, pg.width, pg.height, 0, 3);
             break;
           case 1:
             doGridEffect(pg, nCols, nRows, pg.width, pg.height, 0, 3);
             break;
           default:
             doPaintingEffect(pg, nCols, nRows, pg.width, pg.height, 0, 3);
             break;
          }
        } 
      }
        
      pg.endDraw();
      pg.save(java.util.UUID.randomUUID() + ".png");
    }
  }
  catch (Exception e) {
    String[] msg = new String[2];
    msg[0] = e.getStackTrace().toString();
    msg[1] = e.getMessage();
    saveStrings("drawException.txt", msg);
  }
}


public String[] getImageFiles() {
  File dataDirectory = new File(sketchPath() + "/data");
  File[] imageFiles = dataDirectory.listFiles();
  int imageFilesLength = imageFiles.length;
  ArrayList<String> fileNames = new ArrayList<String>();

  for (int i = 0; i < imageFilesLength; i++)
    if (imageFiles[i].getName().endsWith(".png") || imageFiles[i].getName().endsWith(".jpg"))
      fileNames.add(imageFiles[i].getAbsolutePath());

  return fileNames.toArray(new String[0]);
}

public void doGridEffect(PGraphics pg, int nCols, int nRows, int w, int h, int cd, int md) {
  int colWidth = w / nCols;
  int rowHeight = h / nRows;

  for (int y = 0; y < nRows; y++) {
    for (int x = 0; x < nCols; x++) {
      if (random(1) < 0.001f && cd < md) {
        doGridEffect(pg, nCols, nRows, colWidth, rowHeight, cd + 1, md);
      } else {
        int sx = x * colWidth;
        int sy = y * rowHeight;
        int dx = sx + PApplet.parseInt(map(noise(sx, x, cd), 0, 1, width * -0.1f, width * 0.1f));
        int dy = sy + PApplet.parseInt(map(noise(sy, y, cd), 0, 1, width * -0.1f, width * 0.1f));
        pg.copy(sx, sy, colWidth, rowHeight, dx, dy, colWidth, rowHeight);
      }
    }
  }
}

public void doPaintingEffect(PGraphics pg, int nCols, int nRows, int w, int h, int cd, int md) {
  int colWidth = w / nCols;
  int rowHeight = h / nRows;

  for (int y = 0; y < nRows; y++) {
    for (int x = 0; x < nCols; x++) {
      if (random(1) < 0.001f && cd < md) {
        doPaintingEffect(pg, nCols, nRows, colWidth, rowHeight, cd + 1, md);
      } else {
        int sx = PApplet.parseInt(random(pg.width));
        int sy = PApplet.parseInt(random(pg.height));
        int dx = sx + PApplet.parseInt(random(pg.width * -0.003f, pg.width * 0.003f));
        int dy = sy + PApplet.parseInt(random(pg.height * -0.003f, pg.height * 0.003f));
        pg.copy(sx, sy, colWidth, rowHeight, dx, dy, colWidth, rowHeight);
      }
    }
  }
}


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "image_editor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
