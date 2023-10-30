package PCDServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
    
    private final Socket clientSocket;

    // Constantes de control
    private static final int STX = 2;
    private static final int ETX = 3;

    public ServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                String message = "";
                String receivedHash = "";
                if (in.read() != STX) break;  // Leemos el byte STX

                int ch;
                while ((ch = in.read()) != ETX) {  // Leemos hasta ETX
                    message += (char) ch;
                }

                if (in.read() != STX) break;  // Leemos el siguiente STX

                while ((ch = in.read()) != ETX) {  // Leemos el hash hasta ETX
                    receivedHash += (char) ch;
                }

                System.out.println("Mensaje recibido: " + message);

                String computedHash = String.valueOf(message.hashCode());
                
                Thread.sleep(2000);  // Espera 2 segundos

                if (computedHash.equals(receivedHash)) {
                    out.write(0x05);  // ACK
                } else {
                    out.write(0x15);  // NAK
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}