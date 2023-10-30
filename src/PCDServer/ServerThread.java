package PCDServer;

import java.io.*;

public class ServerThread extends Thread {
	private DataInputStream input;
	private DataOutputStream output;
    private String nombreCliente;

    public ServerThread(DataInputStream input, DataOutputStream output, String nombreCliente) {
		this.input = input;
        this.output = output;
        this.nombreCliente = nombreCliente;
	}

    @Override
    public void run() {

    }
}
