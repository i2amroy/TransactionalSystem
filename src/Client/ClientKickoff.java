package Client;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ChristianBuskirk on 5/5/17.
 */
public class ClientKickoff {
    // The port used for the connection, NOTE: must match the server's port
    private static final int PORT = 23657;
    private static final String IP = "192.168.0.3";
    private static final int threadcount = 50;
    private static final int max_transfer = 10;
    private static final int max_account_num = 10;

    public static void main(String[] args) {
        for (int i = 0; i < threadcount; i++) {
            int source = ThreadLocalRandom.current().nextInt(max_account_num);
            int target = ThreadLocalRandom.current().nextInt(max_account_num);
            int value = ThreadLocalRandom.current().nextInt(max_transfer);
            System.out.println("Creating Transaction " + i + " Source: " + source + " Target: " + target +
                    " Value: " + value);
            new ClientThread(PORT, IP, i, source, target, value).start();
        }
    }
}
