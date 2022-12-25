import java.util.concurrent.*;

public class MyApp {
    public static void main(String[] args) {
// 1).
        ThreadSafeList<String> threadSafeList = new ThreadSafeList<>();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                threadSafeList.add("thread1 - " + i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                threadSafeList.add("thread2 - " + i);
            }
        });

        try {
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(threadSafeList.getSize());
        System.out.println("--------------------------\n");

// 2).
        PetrolStation petrolStation = new PetrolStation(4000);
        System.out.printf("+++ At a gas station %s gallons of fuel\n", petrolStation.getAmount());
        ExecutorService service = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 6; i++) {
            int fillingVolume = (int) (Math.random()*1000);
            service.execute(() ->{
                petrolStation.doRefuel(fillingVolume);
            });
        }

        service.shutdown();
    }

}
