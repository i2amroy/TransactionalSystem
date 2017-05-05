package Server;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by ChristianBuskirk on 5/4/17.
 */
public class DataManager {
    private int accounts[];
    private ReadWriteLock locks[];

    public DataManager() {
        // Initialize all account values
        accounts = new int[10];
        for (int i = 0; i < 10; i++) {
            accounts[i] = 10;
        }
        // Initialize our locks
        locks = new ReadWriteLock[10];
    }

    public int get_account_balance(int account) {
        return accounts[account];
    }

    public void set_account_balance(int account, int value) {
        accounts[account] = value;
    }

    public ReadWriteLock get_lock(int account) {
        return locks[account];
    }
}
