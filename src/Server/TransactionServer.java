package Server;

/**
 * Created by ChristianBuskirk on 5/4/17.
 */
public class TransactionServer {
    private DataManager data;
    // The Port used for the server
    private static final int PORT = 23657;

    public TransactionServer() {
        this.data = new DataManager();
    }

    private void run_server() {
        new TransactionAcceptThread(data, PORT).start();
        try {
            // Wait 15 seconds
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // Don't need to do anything here
        }
        // Print out the whole database (In reality we would want to acquire all locks here but in this case
        // we want to print out everything even if the system deadlocks itself so we just do this unsafely).
        for (int i = 0; i < 10; i++) {
            System.out.println("Balance " + i + ": " + data.get_account_balance(i));
        }
    }

    public static void main(String[] args) {
        System.out.println("Initializing Transaction Server");
        new TransactionServer().run_server();
    }
}
