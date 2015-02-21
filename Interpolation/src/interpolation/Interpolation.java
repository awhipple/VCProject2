package interpolation;

import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.core.Mat;
import javax.media.MediaLocator;

class InterpolateDemo {
  public void run() {
    
    final int START_VIDEO = 100, END_VIDEO = 200;
          
    VideoCapture vc = loadVideo("/resources/videos/bbt.avi");
    Mat frame = new Mat();
    Vector fileList = new Vector();
    for(int i = 0; i < END_VIDEO; i++) {
        vc.read(frame);
        if(i < START_VIDEO) continue;
        String fileName = "out/images/" + i + ".jpg";
        fileList.add(fileName);
        showFrame(frame, fileName);
    }
    JpegImagesToMovie imagesToMovie = new JpegImagesToMovie();
    MediaLocator oml;
    String fileName = "out/movie.mp4";
    if ((oml = imagesToMovie.createMediaLocator(fileName)) == null) {
        System.err.println("Cannot build media locator from: " + fileName);
        System.exit(0);
    }
    try {
        imagesToMovie.doIt(frame.width(), frame.height(), 48, fileList, oml);
    } catch(Exception ex) {
        System.out.println("Error saving video: " + ex.getMessage());
    }
    
    vc.release();
  }
  
  private void showFrame(Mat frame, String fileName) {
      Highgui.imwrite(fileName, frame);
  }
  
  private VideoCapture loadVideo(String fileName) {
    String inputVideoFileName = getClass().getResource(fileName).getPath();
    if(inputVideoFileName.startsWith("/", 0)) {
        inputVideoFileName = inputVideoFileName.replaceFirst("/", "");
    }
    VideoCapture vc = new VideoCapture();
    vc.open(inputVideoFileName);
    return vc;
  }
  
  private Mat loadImage(String fileName) {
    String inputImageFileName = getClass().getResource(fileName).getPath();
    if(inputImageFileName.startsWith("/", 0)) {
        inputImageFileName = inputImageFileName.replaceFirst("/", "");
    }
    return Highgui.imread(inputImageFileName);
  }
}

public class Interpolation {
    public static void main(String[] args) {
        System.out.println("Hello, OpenCV");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new InterpolateDemo().run();
    }
}
