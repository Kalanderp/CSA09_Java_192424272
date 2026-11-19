public class DeadlockSafeTransfer {
    static class Account {
        final int id;
        int balance;
        Account(int id, int balance) { this.id = id; this.balance = balance; }
    }

    static void transfer(Account a, Account b, int amount) {
        Account first = a.id < b.id ? a : b;
        Account second = a.id < b.id ? b : a;
        synchronized (first) {
            synchronized (second) {
                if (a.balance >= amount) {
                    a.balance -= amount;
                    b.balance += amount;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1, 1000), b = new Account(2, 1000);
        Thread t1 = new Thread(() -> { for (int i=0; i<1000; i++) transfer(a,b,1); });
        Thread t2 = new Thread(() -> { for (int i=0; i<1000; i++) transfer(b,a,1); });
        t1.start(); t2.start(); t1.join(); t2.join();
        System.out.println("Final total: " + (a.balance + b.balance));
    }
}
