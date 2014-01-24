package client;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class DisplayWatcher {


	public BufferedImage captureScreen()
	{
		File oDir = new File("");
		String sDirectory = "";
		String sPathSeparator = "";
		
		
		if (!sDirectory.endsWith(File.separator))
		{
			sPathSeparator = File.separator;
		}
		
		
		try{
			sDirectory = oDir.getCanonicalPath();
		}
		catch (IOException ex){
			System.out.println(ex.getMessage());
		}

		final String sCurrentDateTime = getCurrentDataTime();
		final String sFilename = sDirectory + sPathSeparator + "_" + sCurrentDateTime + ".png";


		System.out.println("---");
		try
		{
			// Determine current screen size
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension oScreenSize = toolkit.getScreenSize();
			Rectangle oScreen = new Rectangle(oScreenSize);

			// Create screen shot
			Robot robot = new Robot();
			BufferedImage oImage = robot.createScreenCapture(oScreen);

			// Save captured image to PNG file
			//ImageIO.write(oImage, "png", new File(sFilename));
			// Display info of image saved.
			//String sMsg = String.format("Screenshot(%s x %s pixels) is saved to %s.", oImage.getWidth(), oImage.getHeight(), sFilename);
			//System.out.println(sMsg);
			return oImage;
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			System.exit(0); // Shutdown the application completely. Need this because of the infinite loop.
		}
		return null;
	}

	public final String getCurrentDataTime()
	{
		Date oCurrentDate = new Date();
		SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSSS");
		return oSimpleDateFormat.format(oCurrentDate);
	}
}
