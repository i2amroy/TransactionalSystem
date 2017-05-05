package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ChristianBuskirk on 5/5/17.
 */
public class TransactionAcceptThread extends Thread {
    private DataManager data;
    private int port;

    public TransactionAcceptThread(DataManager data, int port) {
        this.data = data;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serversock = new ServerSocket(port);
            System.out.println("Server up and ready for connections");
            while (true) {
                // We copy over the socket here so no need for a mutex lock
                Socket socket = serversock.accept();
                System.out.println("Accepting connection");
                new TransactionThread(socket, data).start();
            }
        } catch (IOException e) {
            System.err.println("Error in TransactionAcceptThread: Unable to get I/O connection.");
            e.printStackTrace();
        }
    }
}
