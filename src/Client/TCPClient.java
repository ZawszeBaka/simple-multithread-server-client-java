package Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class TCPClient {

	private String serverName;
	private int port ;
	
	public TCPClient(String serverName , int port) {
		this.serverName = serverName; 
		this.port = port ;
	}
	
	public void start() {

		try {
			
			Socket serverSocket = new Socket(serverName, port);
			ServerConnectionThread serverConnection = new ServerConnectionThread(serverSocket);
			serverConnection.start();

			Scanner scan = new Scanner(System.in);

			while(true) {
				
				print("Send to " + serverName + ":" + Integer.toString(port) + "$ " );
				String mess = scan.nextLine();
				print("You just entered " + mess);

				if (mess != null){
					PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
					out.println(mess);
				} else if (mess.equalsIgnoreCase("quit") || mess.equalsIgnoreCase("end")){
					serverSocket.close();
				}
				
			}
			
			
		} catch(Exception e) {
			print("Error: ");
			print(e.getMessage());
		}
	}

	class ServerConnectionThread extends Thread{

		private Socket myServerSocket ;
		private boolean isConnect = true; 
		public String mess = "";

		// init
		public ServerConnectionThread(){
			super();
		}

		public ServerConnectionThread(Socket s){
			this.myServerSocket = s;
		}

		public void run(){
			
			BufferedReader in = null ; 
			PrintWriter out = null ;
			// OutputStream out = null;

			try{

				// Buffer Reader for reading from server throughout connection socket
				in = new BufferedReader(
						new InputStreamReader(myServerSocket.getInputStream())
				);

				while(isConnect){

					// Receive message from server 
					try{
						String receivedMes = in.readLine();
						print("Server replied: " + receivedMes);

						if(receivedMes.equalsIgnoreCase("Server has already stopped")){
							isConnect = false; // disconnect to server
						}
					} catch(Exception e){
						// print("...waiting for replying from server");
					}
					
				}

			} catch(Exception e){
				e.printStackTrace();
			} finally {
				try{
					in.close();
					out.close();
					myServerSocket.close();
					print("Close connection !! ");
					System.exit(-1);
				} catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}

	}

	public void print(String x) {
		System.out.println(x);
	}
	
}
