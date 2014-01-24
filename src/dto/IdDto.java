package dto;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IdDto implements Serializable{

	public String id = null;
	
	public IdDto() throws UnknownHostException{
		this.id = InetAddress.getLocalHost().getHostName() + ":" + System.getProperty("user.name");
	}
	
}
