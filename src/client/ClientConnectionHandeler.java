package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientConnectionHandeler extends Thread{

	//private	final String serverIP = "193.136.166.150";
	private	final String serverIP = "localhost";

	private	final int serverPort = 4444;
	private Socket localSock = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private InputConnectionHandler inc = null;
	private OutConnectionHandler outc = null;
	private boolean running = false;
	//private ArrayList<Object> objects = new ArrayList<Object>();
	private boolean isConnectedToServer = false;

	public void send(Object oo){

		while(this.running){

			if(outc != null && outc.isRunning()){
				outc.send(oo);
				break;
			}
			else{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//	public ArrayList<Object> receve(){
	//		synchronized (objects) {
	//			ArrayList<Object> objectscopy = (ArrayList<Object>) objects.clone();
	//			objects.clear();
	//			return objectscopy;
	//		}
	//	}
	//
	//	public ArrayList<Object> getObjectList(){
	//		return objects;
	//	}
	//
	//	public boolean recevedObjects() {
	//
	//		synchronized (objects){
	//			if(objects != null && objects.size() > 0){
	//				return true;
	//			}
	//			else{
	//				return false;
	//			}
	//		}
	//	}


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

	public boolean isConnected(){
		return this.isConnectedToServer;
	}

	@Override
	public void run() {

		this.running = true;

		// Contacting the Server , Retry if error
		while(true){
			try{
				this.localSock = new Socket(this.serverIP, this.serverPort);
				break;
			}catch(Exception e){
				System.out.println("Cannot Reach the Server " +this.serverIP + ":"+this.serverPort+"   Sleeping for 5s");
				System.out.println(e.toString());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {}
			}
		}
		System.out.println("Thread with Client"+ localSock.getRemoteSocketAddress().toString() + " started.");

		try {
			out = new  ObjectOutputStream(localSock.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		try {
			localSock.getOutputStream().flush();	//Needed to deBlock the inputStream
			in = new  ObjectInputStream(localSock.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}



		inc = new InputConnectionHandler(this.in , this);

		inc.start();
		System.out.println("Input Channel Created");



		outc = new OutConnectionHandler(this.out);
		outc.start();
		System.out.println("Output Channel Created");

		while(this.running){

			try {
				this.isConnectedToServer = true;
				Thread.sleep(250); // Time for the Channels to Connect
				Thread.yield();	
			} catch (InterruptedException e) { /*Never Happens*/ }


			if(!this.outc.isRunning()){
				System.out.println("Out Channel Down, Killing CONNNECTION_HANDELER");
				this.running = false;
				this.isConnectedToServer = false;
				return;
			}

			if(!this.inc.isRunning()){
				System.out.println("In Channel Down, Killing CONNNECTION_HANDELER");
				this.running = false;
				this.isConnectedToServer = false;
				return;
			}
		}

	}

	public interface OnObjectReceived { public void informArrival(Object dto); }

}

class InputConnectionHandler extends Thread{

	private ObjectInputStream in = null;
	private boolean running = false;
	private ClientConnectionHandeler connectionHandler = null;

	public InputConnectionHandler(ObjectInputStream in, ClientConnectionHandeler connHandler) {
		this.in = in;
		this.connectionHandler = connHandler;
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

					//Handle Object Receve
					System.out.println("Object Receved");

				}else{
					System.out.println("Null Value Receved");
				}
			}

			catch(EOFException e){
				System.out.println("Channel was closed");
				this.running = false;
				return;
			}

			catch(SocketException e){
				System.out.println("Channel is closed");
				this.running = false;
				return;
			}

			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
}


class OutConnectionHandler extends Thread{

	private ObjectOutputStream out;
	private boolean running = false;

	public boolean isRunning(){
		return this.running;
	}

	public void send(Object oo){
		try {
			if(this.running){
				out.writeObject(oo);
				out.flush();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		this.running = true;

		while (this.running) {

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//Thread.yield();
		}
	}

	public OutConnectionHandler(ObjectOutputStream out) {
		this.out = out;
	}
}