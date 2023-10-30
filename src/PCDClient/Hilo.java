package cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class Hilo extends Thread {
	// declaramos el socket, el archivo a enviar y el error en el envío del hash
    private final Socket socket;
    private final int errorHash;
    private final String fileName;

    // constructor que recibe los parámetros
    public Hilo(Socket socket, String fileName, int errorHash) {
        this.socket = socket;
        this.fileName = fileName;
        this.errorHash = errorHash;
    }

    @Override
    public void run() {
        try {
        	// socket para flujo de datos i/o entre cliente/servidor 
        	DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            // creamos objeto fichero
            File file = new File(fileName);
            
            if (!file.exists() || !file.isFile()) {
                System.out.println("Error, el archivo especificado no existe.");
                socket.close();
                return;
            }

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // enviamos datos con hash
                    enviarDatosConHash(output, line);
                    byte response = recibirRespuesta(input);

                    while (response != ACK) {
                        if (response == NAK) {
                            System.out.println("NAK recibido");
                            System.out.println();
                            // reenviamos datos si se produce NAK
                            enviarDatosConHash(output, line);
                        }
                        response = recibirRespuesta(input);
                    }

                    System.out.println("ACK recibido");
                    System.out.println();
                }

                output.write(EOT);
                System.out.println("Fichero enviado correctamente");
                
            // declaramos posibles errores
            } catch (FileNotFoundException e) {
                System.out.println("Error al abrir el archivo: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el servidor: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    // declaramos constantes de control 
    private static final byte ACK = 6;
    private static final byte NAK = 21;
    private static final byte EOT = 4;

    // enviamos datos con hash
    private void enviarDatosConHash(DataOutputStream output, String data) throws IOException {
        System.out.println(data);
        output.write(STX);
        output.writeBytes(data);
        output.write(ETX);
        output.write(STX);

        // introducimos error hash
        if (Math.random() * 100 < errorHash) {
            output.writeBytes("00000");
            System.out.println("Envío de hash incorrecto");
        } else {
            output.writeBytes(String.valueOf(data.hashCode()));
        }

        System.out.println(data.hashCode());
        output.write(ETX);
    }

    private byte recibirRespuesta(DataInputStream input) throws IOException {
        return input.readByte();
    }

    // declaramos constantes de control 
    private static final int STX = 2;
    private static final int ETX = 3;
}

