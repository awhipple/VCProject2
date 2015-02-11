package interpolation;

import org.opencv.core.Core;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.core.Mat;

class InterpolateDemo {
  public void run() {
    VideoCapture vc = loadVideo("/resources/videos/smw.avi");
    Mat frame = new Mat();
    for(int i = 0; i < 165; i++) vc.read(frame);
    showFrame(frame);
    vc.release();
  }
  
  private void showFrame(Mat frame) {
      Highgui.imwrite("result.png", frame);
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
