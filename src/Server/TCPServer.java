package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TCPServer {

	private int port = 5000;
	private boolean ServerOn = true;
	private int maxClients ;
	private boolean[] ids ;

	public TCPServer(int port, int maxClients) {
		this.port = port; 
		this.maxClients = maxClients;
	}

	public void showInfo(){
		int n = 0 ; 
		for(int i = 0; i < ids.length; i++){
			if(ids[i]){
				n++;
			}
		}

		print("\n\n-------CURRENT STATE-----------");
		print(n + " connection(s) are being establishing ");
		print("ids : ");

		for(int i = 0; i < ids.length; i++){
			if(ids[i]){
				print("\t" + Integer.toString(i));
			}
		}

		print("-------------------------------\n\n");
	}

	public void start() {
		String req ;
		String res ;

		try{
			
			//// SERVER CONFIGURATION
			ServerSocket myServerSocket = null;

			try{
				// Listening on port .... 
				myServerSocket = new ServerSocket(port);
			} catch(IOException ioe){
				System.out.println("Could not create server socket on port "+ port + " . Quitting. ");
				System.exit(-1);
			}
			print("Listening on port ", port);

			


			// Initialize Connection Information 
			ids = new boolean[maxClients];
			for(int i = 0; i < ids.length; i++){
				ids[i] = false;
			}
			ConnectionInfo info = new ConnectionInfo();
			info.start();
			info.isShowInfo = true; 




			// SERVER PROCESS
			while(ServerOn) {
				
				print("waiting for connection... ");
				Socket clientSocket = myServerSocket.accept();
				
				// accept() will block until a client connects to the server
				// If execution reaches this point, then it means that a client 
				// socket has been accepted
				if(getId() == -1){
					print("Server is full ! Please connect later !");
					clientSocket.close();
				} else{
					int i = getId();
					ids[i] = true;
					print("Connection " + Integer.toString(i) + " has been just established ");

					// show connection info
					showInfo();

					ClientServiceThread cliThread = new ClientServiceThread(clientSocket, i);
					cliThread.start();
					
				}
				
			}
			
			



		} catch(Exception e) {
			print("Error");
			print(e.getMessage());
		}
		
	}

	class ClientServiceThread extends Thread{

		private Socket myClientSocket;
		private int id ;
		private boolean n_ThreadRun = true  ;

		// init
		public ClientServiceThread(){
			super();
		}

		public ClientServiceThread(Socket s, int id){
			this.myClientSocket = s;
			this.id = id ;
		}


		public void run(){
			BufferedReader in = null ; 
			PrintWriter out = null ;

			print("Accepted Client number " + Integer.toString(id) +  " from " + myClientSocket.getInetAddress().getHostName());

			try{

				// Buffer Reader for reading from client throughout connection socket
				in = new BufferedReader(
						new InputStreamReader(myClientSocket.getInputStream())
				);
				
				// OutputStream for writing to client throughout connection socket
				out = new PrintWriter( 
						new OutputStreamWriter(myClientSocket.getOutputStream())
				);
				
				while(n_ThreadRun){

					String messFromClient = in.readLine();
					print("Client number " + Integer.toString(id) + " said " + messFromClient);

					if(!ServerOn){
						print("Server has already stopped");
						out.println("Server has already stopped\n");
						out.flush();
						n_ThreadRun = false;
					}

					if(messFromClient.equalsIgnoreCase("quit") || messFromClient.equalsIgnoreCase("end")){
						n_ThreadRun = false;
					} else{
						out.println("I received your message !" + messFromClient);
						out.flush();
					}
				}

			} catch(Exception e){
				e.printStackTrace();
			} finally {
				try{
					in.close();
					out.close();
					myClientSocket.close();
					ids[id] = false;
					print("Stopping client thread for client number " + id);
				} catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}

	}
	

	class ConnectionInfo extends Thread{

		public boolean isShowInfo = false;

		public void run(){

			while(ServerOn){

				if(isShowInfo){

					int n = 0 ; 
					for(int i = 0; i < ids.length; i++){
						if(ids[i]){
							n++;
						}
					}

					print("\n\n-------CURRENT STATE-----------");
					print(n + " connection(s) are being establishing ");
					print("ids : ");

					for(int i = 0; i < ids.length; i++){
						if(ids[i]){
							print("\t" + Integer.toString(i));
						}
					}

					print("-------------------------------\n\n");

					isShowInfo = false ;
				}
			}
		}
	}	



	// get free id 
	public int getId(){
		for(int i = 0 ; i < ids.length; i++){
			if(!ids[i]){
				return i;
			}
		} 

		return -1;
	}

	// support for print 
	public void print() {
		System.out.println();
	}
	
	public void print(String x) {
		System.out.println(x);
	}
	
	public void print(String x, int y) {
		System.out.print(x);
		System.out.println(y);
	}
	
}
