package utils;

import server.ServerConnectionHandler;
import dto.IdDto;

public class ClientHandeler {

	public ServerConnectionHandler conn;
	public String id;
	
	
	public ClientHandeler(ServerConnectionHandler serverConnectionHandler,
			IdDto oo) {
		this.conn = serverConnectionHandler;
		this.id = oo.id;
	}

}
