package interpolation;

import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.core.Mat;
import javax.media.MediaLocator;

class InterpolateDemo {
  
  private enum IType {
      FIRST_FRAME, ALPHA;
  }
  
  public Mat firstFrame;
  
  public void run() {
    
    final int START_VIDEO = 100, END_VIDEO = 200;
    final IType iType = IType.ALPHA;
          
    VideoCapture vc = loadVideo("/resources/videos/bbt.avi");
    Mat oldFrame = new Mat(), newFrame = new Mat(), tempFrame;
    Vector fileList = new Vector();
    vc.read(newFrame);
    firstFrame = newFrame.clone();
    
    for(int i = 0; i < END_VIDEO; i++) {
        
        tempFrame = oldFrame;
        oldFrame = newFrame;
        newFrame = tempFrame;
        
        vc.read(newFrame);
        if(i < START_VIDEO) continue;

        Mat iFrame = interpolateFrames(iType, oldFrame, newFrame);
        
        if(i == START_VIDEO) {
            showExamples(oldFrame, newFrame, iFrame);
        }

        String fileName;
        
        fileName = "out/images/" + (2*i) + ".jpg";
        fileList.add(fileName);
        showFrame(iFrame, fileName);
        
        fileName = "out/images/" + (2*i+1) + ".jpg";
        fileList.add(fileName);
        showFrame(newFrame, fileName);
    }
    JpegImagesToMovie imagesToMovie = new JpegImagesToMovie();
    MediaLocator oml;
    String fileName = "out/movie.mp4";
    if ((oml = imagesToMovie.createMediaLocator(fileName)) == null) {
        System.err.println("Cannot build media locator from: " + fileName);
        System.exit(0);
    }
    try {
        imagesToMovie.doIt(newFrame.width(), newFrame.height(), 48, fileList, oml);
    } catch(Exception ex) {
        System.out.println("Error saving video: " + ex.getMessage());
    }
    
    vc.release();
  }
  
  private Mat interpolateFrames(IType iType, Mat oldFrame, Mat newFrame) {
      Mat iFrame = new Mat();
      
      switch(iType) {
          case FIRST_FRAME: iFrame = firstFrame;
                            break;
          case ALPHA:       Core.addWeighted(oldFrame, 0.5, newFrame, 0.5, 1, iFrame);
                            break;
      }
      
      return iFrame;
  }
  
  private void showFrame(Mat frame, String fileName) {
      Highgui.imwrite(fileName, frame);
  }
  
  private void showExamples(Mat oldFrame, Mat newFrame, Mat iFrame) {
      showFrame(oldFrame, "out/a-old.png");
      showFrame(newFrame, "out/b-new.png");
      showFrame(iFrame, "out/c-int.png");
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
