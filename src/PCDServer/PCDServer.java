package PCDServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class PCDServer {

	public static void main(String[] args) throws IOException {
		int port;
		int minPort = 0;
		int maxPort = 65536;
		Scanner scanner = new Scanner(System.in);
		
        System.out.println("Introduce un valor entre " + minPort + " y " + maxPort + ": ");
        
        while (true) {
            if (scanner.hasNextInt()) {
            	port = scanner.nextInt();

                if (port >= minPort && port <= maxPort) {
                    break;
                } else {
                    System.out.println("Puerto fuera del rango. Inténtalo de nuevo: ");
                }
            } else {
                System.out.println("Entrada inválida. Introduce un número: ");
                scanner.next();
            }
        }
		
        scanner.close();
        
        // Stablish conneciton
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket;


        System.out.println("Servidor levantado en: " + InetAddress.getLocalHost() + ":" + port);

        while(true) {
            clientSocket = serverSocket.accept();

            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            output.writeUTF("Escribe tu nombre");
            String nombreCliente = input.readUTF();

            ServerThread thread = new ServerThread(input, output, nombreCliente);
            thread.start();

            System.out.println("Creada la conexión con el cliente: " + nombreCliente);
        }
	}
}