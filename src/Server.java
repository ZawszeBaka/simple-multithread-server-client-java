import Server.TCPServer;

/*
 * ## Server Side ## 
 * 
 * How to run:
 * 		cd to /bin/
 * 		cmd: java Server <port> <maxClients>
 * 		
 * 		If you miss any argument, it will be set to default value:
 * 			port: 5000
 *			maxClients = 5 
 *
 */

public class Server {

	public static void main(String[] args) {

		int port = 5000;
		int maxClients = 5; 
		
		try {
			port = Integer.parseInt(args[0]);
		} catch(ArrayIndexOutOfBoundsException e) {
			
		} catch(NumberFormatException e) {
			
		} catch(Exception e) {
			System.out.println("Something went wrong !");
		}
		
		try {
			maxClients = Integer.parseInt(args[1]);
		} catch(ArrayIndexOutOfBoundsException e) {
			
		} catch(NumberFormatException e) {
			
		} catch(Exception e) {
			System.out.println("Something went wrong !");
		}

		TCPServer server = new TCPServer(port, maxClients);
		server.start();
		
	}

}
