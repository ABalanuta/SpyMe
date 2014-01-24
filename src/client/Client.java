package client;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import dto.IdDto;
import dto.ImageDto;

public class Client {

	public static void main(String[] args) throws UnknownHostException{

		System.out.println("Hello Humans I am A Client");

		DisplayWatcher dw = new DisplayWatcher();
		BufferedImage image = null;
		String computer = InetAddress.getLocalHost().getHostName();
		ClientConnectionHandeler ch = new ClientConnectionHandeler();
		ch.start();
		
		ch.send(new IdDto());
		
		
		
//		while(true){
//		
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		image = dw.captureScreen();
//		ch.send(new ImageDto(computer, image));
//		
//		}
	}	


}


