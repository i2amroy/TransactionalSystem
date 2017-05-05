package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReadWriteLock;

import Messages.Message;


/**
 * Created by ChristianBuskirk on 5/4/17.
 */
public class TransactionThread extends Thread {
    private Socket socket;
    private DataManager datasource;
    private ObjectInputStream instream;
    private ObjectOutputStream outstream;

    public TransactionThread(Socket socket, DataManager accounts) throws IOException {
        this.socket = socket;
        datasource = accounts;
        instream = new ObjectInputStream(socket.getInputStream());
        outstream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            Message goal = (Message) instream.readObject();
            // If we are doing a transfer
            if (goal.action.equals("TRANSFER")) {
                if (goal.source != goal.target) {
                    // first get the locks we need
                    ReadWriteLock sourcelock = datasource.get_lock(goal.source);
                    ReadWriteLock targetlock = datasource.get_lock(goal.target);

                    // Set these up for use in our return message
                    int source = 0;
                    int target = 0;

                    // Actually lock our locks here
                    sourcelock.writeLock().lock();
                    targetlock.writeLock().lock();
                    try {
                        // Get the balance of each amount and store it
                        source = datasource.get_account_balance(goal.source);
                        target = datasource.get_account_balance(goal.target);
                        source -= goal.value;
                        target += goal.value;
                        // Then set to the new balances appropriately
                        datasource.set_account_balance(goal.source, source);
                        datasource.set_account_balance(goal.target, target);
                    } finally {
                        // Lastly we always unlock here regardless of if something goes wrong above
                        sourcelock.writeLock().unlock();
                        targetlock.writeLock().unlock();
                    }

                    // At this point we've supposedly completed our transaction, setup and send our return message
                    Message ret = new Message("TRANSFERCOMPLETE");
                    ret.source = source;
                    ret.target = target;
                    outstream.writeObject(ret);
                } else {
                    // If they are the same we don't actually need to do anything, but we do need to return the balance
                    ReadWriteLock sourcelock = datasource.get_lock(goal.source);
                    int source = 0;
                    sourcelock.readLock().lock();
                    try {
                        source = datasource.get_account_balance(goal.source);
                    } finally {
                        sourcelock.readLock().unlock();
                    }

                    // At this point we've supposedly completed our transaction, setup and send our return message
                    Message ret = new Message("TRANSFERCOMPLETE");
                    ret.source = source;
                    ret.target = source;
                    outstream.writeObject(ret);
                }
                System.out.println("Transaction complete. Source: " + goal.source + " Target: " + goal.target);
            }
        } catch (IOException e) {
            System.err.println("Error in Transaction Thread: Unable to get I/O connection.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error in Transaction Thread: Class not found.");
            e.printStackTrace();
        }

        // Lastly we close our streams and socket
        try {
            instream.close();
            outstream.close();
            socket.close();
        } catch (IOException e) {
            // If something went wrong then things closed anyways, we're still done
        }
    }
}
