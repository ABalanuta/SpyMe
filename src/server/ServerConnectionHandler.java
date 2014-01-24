package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import utils.ClientHandeler;
import dto.IdDto;

public class ServerConnectionHandler extends Thread{

	private Socket localSock = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private InputConnectionHandler inc = null;
	private OutConnectionHandler outc = null;
	public DtoExtractor dtoExtractor = null;
	private boolean running = false;

	public ServerConnectionHandler(Socket sock, DtoExtractor dtoExtractor) {
		this.localSock = sock;
		this.dtoExtractor = dtoExtractor;
	}

	public void send(Object oo){
		// Out Channel mabe not started yet
		if((this.running && !outc.isRunning())){
			int x = 30;

			while(this.running && !outc.isRunning() && x > 0){
				try {
					Thread.sleep(100);
					System.out.print("*");
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			x--;
		}

		synchronized (this) {
			outc.send(oo);
		}
	}



	public void close(){
		try { 
			out.close();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}


	public boolean isRunning(){
		return this.running;
	}

	@Override
	public void run() {
		this.running = true;
		System.out.println("Thread with Client"+ localSock.getRemoteSocketAddress().toString() + " started.");

		try { out = new ObjectOutputStream(localSock.getOutputStream()); } catch (IOException e1) { e1.printStackTrace(); }

		try {
			localSock.getOutputStream().flush();	//Needed to deBlock the inputStream
			in = new  ObjectInputStream(localSock.getInputStream());
		} catch (IOException e1) { e1.printStackTrace(); }

		inc = new InputConnectionHandler(in, this, dtoExtractor);
		inc.start();
		System.out.println("Input Channel Created");

		outc = new OutConnectionHandler(out);
		outc.start();
		System.out.println("Output Channel Created");

		while(this.running){

			try {
				Thread.sleep(250); // Time for the Channels to Connect
				Thread.yield();	
			} catch (InterruptedException e) { /*Never Happens*/ }

			if(!this.outc.isRunning()){
				System.out.println("Out Channel Down, Killing CONNNECTION_HANDELER");
				this.running = false;
				break;
			}

			if(!this.inc.isRunning()){
				System.out.println("In Channel Down, Killing CONNNECTION_HANDELER");
				this.running = false;
				break;
			}
		}

	}



	class InputConnectionHandler extends Thread{

		private ObjectInputStream in = null;
		public DtoExtractor dtoExtractor = null;
		private ServerConnectionHandler serverConnectionHandler;
		private boolean running = false;

		public InputConnectionHandler(ObjectInputStream in, ServerConnectionHandler serverConnectionHandler, DtoExtractor dtoExtractor) {
			this.in = in;
			this.dtoExtractor = dtoExtractor;
			this.serverConnectionHandler = serverConnectionHandler;
		}

		public boolean isRunning(){
			return this.running;
		}

		@Override
		public void run() {
			this.running = true;

			while (this.running) {
				try {
					Object oo = in.readObject();

					if(oo != null){ 

						// Process Object
						System.out.println("Object Receved");
						
						if(oo instanceof IdDto){
							dtoExtractor.Extract(new ClientHandeler(this.serverConnectionHandler, (IdDto)oo));
						}else{
							dtoExtractor.Extract(oo);
						}
						
						
						

					}else{ System.out.println("Null Value Receved"); }

				} catch(EOFException e){
					System.out.println("Channel was closed");
					this.running = false;
					return;
				} catch(SocketException e){
					System.out.println("Channel is closed");
					this.running = false;
					return;
				} catch (IOException e) { e.printStackTrace();
				} catch (ClassNotFoundException e) { e.printStackTrace(); }
			}
		}
	}


	class OutConnectionHandler extends Thread{

		private ObjectOutputStream out;
		private boolean running = false;

		public OutConnectionHandler(ObjectOutputStream out) { this.out = out; }

		public boolean isRunning(){ return this.running; }

		public void send(Object oo){
			try {
				if(this.running){
					out.writeObject(oo);
					out.flush();
				}
			} catch (IOException e) { e.printStackTrace(); }

		}

		@Override
		public void run() {
			this.running = true;

			while (this.running) {
				try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
	}
}

