PImage[] images;
PGraphics[] pGraphics;
String[] imageFiles;

void setup() {
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

void draw() {
  try {
    noLoop();
    for (int i = 0; i < pGraphics.length; i++) {
      PGraphics pg = pGraphics[i];
      int nCols = 10;
      int nRows = 10;
      pg.beginDraw();
      pg.image(images[i], 0, 0, pg.width, pg.height);
      for (int y = 0; y < nRows; y++)
        for (int x = 0; x < nCols; x++)
          copyPix(pg, nCols, nRows, pg.width, pg.height, 0, 3);
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
  exit();
}

String[] getImageFiles() {
  File dataDirectory = new File(sketchPath() + "/data");
  File[] imageFiles = dataDirectory.listFiles();
  int imageFilesLength = imageFiles.length;
  ArrayList<String> fileNames = new ArrayList<String>();
  
  for (int i = 0; i < imageFilesLength; i++)
    if (imageFiles[i].getName().endsWith(".png") || imageFiles[i].getName().endsWith(".jpg"))
      fileNames.add(imageFiles[i].getAbsolutePath());
      
  return fileNames.toArray(new String[0]);
}

void copyPix(PGraphics pg, int nCols, int nRows, int w, int h, int cd, int md) {
  int colWidth = w / nCols;
  int rowHeight = h / nRows;
  
  for (int y = 0; y < nRows; y++) {
    for (int x = 0; x < nCols; x++) {
      if (random(1) < 0.001 && cd < md) {
        copyPix(pg, nCols, nRows, colWidth, rowHeight, cd + 1, md);
      } else {
        int sx = int(random(pg.width));
        int sy = int(random(pg.height));
        int dx = sx + int(random(pg.width * -0.003, pg.width * 0.003));
        int dy = sy + int(random(pg.height * -0.003, pg.height * 0.003));
        pg.copy(sx, sy, colWidth, rowHeight, dx, dy, colWidth, rowHeight);
      }
    }
  }
}
