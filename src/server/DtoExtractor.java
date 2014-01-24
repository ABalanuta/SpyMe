package server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import client.Client;
import utils.ClientHandeler;
import dto.IdDto;
import dto.ImageDto;

public class DtoExtractor {

	private ArrayList<ClientHandeler> clients = null;

	public DtoExtractor(ArrayList<ClientHandeler> clients){
		this.clients = clients;
	}

	public void Extract(Object oo) throws IOException {


		//ID
		if(oo instanceof ClientHandeler){
			ClientHandeler client = (ClientHandeler)oo;
			System.out.println(client.id + " Just Connected");
			this.clients.add(client);
		}


		//IMAGEM
		else if(oo instanceof ImageDto){

			ImageDto image = (ImageDto)oo;


			//Criar Directorio
			File theDir = new File(image._sender);

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				System.out.println("creating directory: " + image._sender);
				boolean result = theDir.mkdir();  
				if(result) {    
					System.out.println("DIR created");  
				}
			}

			File f = new File(image._sender + "/" + image.time + ".png");
			BufferedImage img = image._image.getImage();
			ImageIO.write(img, "PNG", f);
		}

	}

}
