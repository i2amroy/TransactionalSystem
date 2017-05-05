package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

import Messages.Message;

/**
 * Created by ChristianBuskirk on 5/5/17.
 */
public class ClientThread extends Thread {
    private int port;
    private String IP_address;
    private int client_ID;
    private int source_account;
    private int target_account;
    private int transfer_value;

    public ClientThread(int port, String IP_address, int client_ID, int source_account, int target_account,
                        int transfer_value) {
        this.IP_address = IP_address;
        this.port = port;
        this.client_ID = client_ID;
        this.source_account = source_account;
        this.target_account = target_account;
        this.transfer_value = transfer_value;
    }

    @Override
    public void run() {
        // Make threads wait a random amount at the start so we can easily see that interleaving works
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(500));
        } catch (InterruptedException e) {
            // Do nothing
        }
        try {
            Socket server_sock = new Socket(InetAddress.getByName(IP_address), port);
            ObjectOutputStream outstream = new ObjectOutputStream(server_sock.getOutputStream());
            ObjectInputStream instream = new ObjectInputStream(server_sock.getInputStream());
            Message outmess = new Message("TRANSFER");
            outmess.target = target_account;
            outmess.source = source_account;
            outmess.value = transfer_value;
            outstream.writeObject(outmess);
            Message retmess = (Message) instream.readObject();
            if (retmess.action.equals("TRANSFERCOMPLETE")) {
                System.out.println("Client " + client_ID + " completed successfully. Account " + source_account + ": " +
                        retmess.source + " Account " + target_account + ": " + retmess.target);
            } else {
                System.err.println("Client " + client_ID + "failed to complete. Invalid return message.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error in client" + client_ID + ": Unknown Class.");
        } catch (UnknownHostException e) {
            System.err.println("Error in client" + client_ID + ": Invalid IP address.");
        } catch (IOException e) {
            System.err.println("Error in client" + client_ID + ": Unable to get I/O connection for " + IP_address + ".");
        }
    }
}
