import Client.TCPClient;

/*
 * ### Client Side ###  
 * 
 * 
 * 	How to run : 
 * 		cd to /bin/
 * 		cmd: java Client <hostname> <port> 
 * 
 * 		If you miss any argument, it will set to default value: 
 * 			hostname : localhost
 * 			port: 5000 
 * 
 */

public class Client {

	public static void main(String[] args) {
				
		String serverName = "" ;
		int serverPort = 5000;
		String mess = ""; 
			
		try {
			serverName = args[0];
		} catch(ArrayIndexOutOfBoundsException e) {
			
		} catch (Exception e) {
			System.out.println("Something went wrong !");
			System.exit(0);
		}
		
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch(ArrayIndexOutOfBoundsException e) {
			
		} catch(NumberFormatException e) {
			System.out.println("Port " + args[1] + " is invalid!");
			
		} catch (Exception e) {
			System.out.println("Something went wrong !");
			System.exit(0);
		}
		
		System.out.println("Client Socket is running ...");
		TCPClient client = new TCPClient(serverName, serverPort);
		client.start();
		
	}

}
