package PCDClient;

import java.util.Scanner;
import java.net.Socket;

public class Client {
	protected static String ip;
	protected static int puerto;
	protected static Hilo HiloCliente;

	public static void main(String[] args) {
		// comprobamos que pasamos únicamente 2 argumentos
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("localhost")) {
				// usamos la ip local
				ip = "127.0.0.1";
			} else {
				// usamos ip proporcionada
				ip = args[0];
			}

			try {
				// puerto de conexión
				puerto = Integer.valueOf(args[1]);

				try {
					Scanner teclado = new Scanner(System.in);
					System.out.println("Introducir nombre del fichero: ");
					String fichero = teclado.nextLine();
					System.out.println("Introducir error aleatorio envío del Hash");
					int error = teclado.nextInt();
					teclado.close();

					// creamos socket para conexión al servidor
					Socket socket = new Socket(ip, puerto);
					
					// creamos instancia de la clase hilo para la conexión
					HiloCliente = new Hilo(socket, fichero, error);
					HiloCliente.start();
					
				} catch (Exception e) {
					System.out.println("Error: " + e);
				}
			} catch (Exception e) {
				System.out.println("--- Error, introduce número de puerto entre 1 y 65535 ---\r");
                System.out.println("Ejemplo de uso desde línea de comandos donde se encuentre el fichero .jar\r"); 
                System.out.println("\tjava -jar Client <IP servidor o localhost> <Numero de puerto entre 1 y 65535>");
			}
		} else {
			System.out.println("--- Error, introduce ip o localhost y número de puerto entre 1 y 65535 ---\r");
			System.out.println("Ejemplo de uso desde línea de comandos donde se encuentre el fichero .jar\r"); 
            System.out.println("\tjava -jar Client <IP servidor o localhost> <Numero de puerto entre 1 y 65535>");
		}
	}
}