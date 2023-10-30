package PCDServer;

import java.io.*;
import java.net.*;

public class PCDServer {

	public static void main(String[] args) throws IOException {
	    int port = Integer.valueOf(args[0]);
		
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor levantado en: " + InetAddress.getLocalHost() + ":" + port);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                new ServerThread(clientSocket).start();

                System.out.println("Creada la conexión con el cliente");
            }
        } catch (Exception e) {
            System.out.println("--- Error, introduce número de puerto entre 1 y 65535 ---\r");
        }
        
        // Stablish conneciton
        

        
	}
}