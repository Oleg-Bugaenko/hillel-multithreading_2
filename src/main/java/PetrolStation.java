import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PetrolStation {
    private final Semaphore smp = new Semaphore(3);
    private final Lock lock = new ReentrantLock();
    private int amount;

    public PetrolStation(int amount) {
        this.amount = amount;
    }

    public void doRefuel(int value) {
        try {
            smp.acquire();
            System.out.println(Thread.currentThread().getName() + ": fills " + value + " gallons of fuel");
            Thread.sleep((int)(Math.random()*7000)+3000);

            lock.lock();
            try {
                if (amount >= value) {
                    amount -= value;
                } else {
                    System.out.println("Fuel ran out");
                    value = amount;
                    amount = 0;
                }
                System.out.println(Thread.currentThread().getName() + " refueled " + value + " gallons of fuel");
            } finally {
                lock.unlock();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            smp.release();
        }
    }

    public int getAmount() {
        return amount;
    }
}
