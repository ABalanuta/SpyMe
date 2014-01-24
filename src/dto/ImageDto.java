package dto;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageDto implements Serializable{

	public String _type = "ImageDto";
	public String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	public String _sender;
	public ImageRGB _image;

	public ImageDto(String sender, BufferedImage image){
		_sender = sender;
		_image =  new ImageRGB(image);
	}



}
