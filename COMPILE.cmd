
:: Compile the Server Java Project
javac src\Server\TCPServer.java -d bin -classpath bin
javac src\Server.java -d bin -classpath bin

:: Compile the Client Java Project 
javac src\Client\TCPClient.java -d bin -classpath bin
javac src\Client.java -d bin -classpath bin
