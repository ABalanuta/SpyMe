package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.cli.ServerCli;
import utils.ClientHandeler;


public class Server {

	public static ArrayList<ClientHandeler> clients = new ArrayList<ClientHandeler>();
	
	
	public static void main(String[] args){

		System.out.println("Hello Humans I am A Server");
		
		DtoExtractor de = new DtoExtractor(clients);
		ConnectionInicializer conHandler = new ConnectionInicializer(de);
		ServerCli cli = new ServerCli();
		
		
		conHandler.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		cli.showCommands();
		
	}
}



class ConnectionInicializer extends Thread{

	public static final int SERVERPORT = 4444;
	private boolean running = false;
	private DtoExtractor dtoExtractor;
	private ServerSocket serverSocket = null;

	public ConnectionInicializer(DtoExtractor de){
		this.dtoExtractor = de;
	}
	
	@Override
	public void run() {

		super.run();
		this.running = true;

		try {
			serverSocket = new ServerSocket(SERVERPORT);
			System.out.println("Server: Listening...");

			while(running){

				// Cria um socket novo por cada pedido
				Socket sock = serverSocket.accept();
				System.out.println("New Client Request");

				ServerConnectionHandler ch = new ServerConnectionHandler(sock, dtoExtractor);
				ch.start();
				System.out.println("Listening Again...");
			}
		} catch (Exception e) {
			System.out.println("S: Error");
			e.printStackTrace();
		} finally{ try { serverSocket.close(); } catch (IOException e) { e.printStackTrace(); } }

	}

}
