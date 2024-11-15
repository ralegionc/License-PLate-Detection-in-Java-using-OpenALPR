import com.openalpr.jni.Alpr;
import com.openalpr.jni.AlprPlateResult;
import com.openalpr.jni.AlprResults;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;

import java.io.File;

public class LicensePlateDetector {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Load OpenCV library
    }

    public static void main(String[] args) {
        String country = "us";  // Set this to your country code (e.g., "eu", "br")
        String configFile = "D:\\OpenCv\\openalpr-2.3.0-win-64bit\\openalpr_64\\openalpr.conf";  // Path to OpenALPR config file
        String runtimeDataDir = "D:\\OpenCv\\openalpr-2.3.0\\openalpr-2.3.0\\runtime_data\\";  // Path to OpenALPR runtime data
        String folderPath = "C:\\Users\\legio\\OneDrive\\Pictures\\02152024";  // Folder containing images

        try {
            // Initialize OpenALPR
            Alpr alpr = new Alpr(country, configFile, runtimeDataDir);
            alpr.setTopN(5);  // Set number of plate candidates to return
            alpr.setDefaultRegion("md");  // Set default region (optional)

            File folder = new File(folderPath);
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String filePath = file.getAbsolutePath();
                        System.out.println("Processing file: " + filePath);  // Debugging - Print file path

                        // Load image
                        Mat image = Imgcodecs.imread(filePath);
                        if (image.empty()) {
                            System.out.println("Error: Unable to load image: " + filePath);  // Debugging - Image failed to load
                            continue;
                        }

                        // Recognize the license plate in the image
                        AlprResults results = alpr.recognize(filePath);

                        // Check if plates were detected
                        if (results.getPlates().size() > 0) {
                            System.out.println("Number of plates detected in image: " + results.getPlates().size());

                            // Iterate over detected plates
                            for (AlprPlateResult plate : results.getPlates()) {
                                System.out.println("Plate detected: " + plate.getBestPlate().getCharacters());
                            }
                        } else {
                            System.out.println("No plates detected in image: " + filePath);
                        }
                    } else {
                        System.out.println("Skipped non-file: " + file.getName());  // Debugging - Skipped non-file
                    }
                }
            } else {
                System.out.println("No files found in the specified folder.");
            }

            // Release resources
            alpr.unload();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



