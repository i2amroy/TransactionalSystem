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
        new TransactionAcceptThread(data, PORT).run();
    }

    public static void main(String[] args) {
        System.out.println("Initializing Transaction Server");
        new TransactionServer().run_server();
    }
}
