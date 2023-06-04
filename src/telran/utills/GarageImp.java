package telran.utills;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class GarageImp extends Thread implements Garage {
    private final BlockingQueue<Car> queue;
    private final Worker[] workers;
    private final AtomicInteger recoveredCarCount;
    private final long simulationTime;
    private final ChronoUnit simulationUnit;
    private int rejectedCarCount;
    private long simulationTimeMills;

    public GarageImp(int capacity, int nWorkers, long simulationTime, ChronoUnit simulationUnit) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.workers = new Worker[nWorkers];
        this.recoveredCarCount = new AtomicInteger();
        this.simulationTimeMills = Duration.of(simulationTime, simulationUnit).toMinutes();
        this.simulationTime = simulationTime;
        this.simulationUnit = simulationUnit;

        for (int i = 0; i < workers.length; i++) {
            workers[i] = Worker.createAndStart(this);
        }
    }

    public GarageImp(int capacity, int nWorkers) {
        this(capacity, nWorkers, DEFAULT_SIMULATION_DURATION, DEFAULT_SIMULATION_UNIT);
    }


    @Override
    public void run() {
        System.out.println("Garage has started");
        while (simulationTimeMills > 0) {
            try {
                getNewCar();
                sleep(1);
                simulationTimeMills--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Arrays.stream(workers).forEach(Worker::interrupt);
        Arrays.stream(workers).forEach(worker -> {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        print_info();
    }

    private void print_info() {
        long totalWorkingTime = Arrays.stream(workers).mapToLong(Worker::getWorkingTime).sum();
        long totalIdleTime = Arrays.stream(workers).mapToLong(Worker::getIdleTime).sum();

        String countersStr = String.format("%d cars have been recovered, %d cars have been rejected",
                recoveredCarCount.get(),
                rejectedCarCount);
        String modelingInfo = String.format("Simulation duration: %d %s", simulationTime, simulationUnit.toString());
        String timeStr = String.format("Total working time: %d minutes, Total idle time: %d minutes",
                totalWorkingTime,
                totalIdleTime);
        String repairTime = String.format("Average repair time: %d", (totalWorkingTime / recoveredCarCount.get()));

        System.out.println("Garage has finished");
        System.out.println(modelingInfo);
        System.out.println(countersStr);
        System.out.println(timeStr);
        System.out.println(repairTime);
    }

    public BlockingQueue<Car> getQueue() {
        return queue;
    }

    public AtomicInteger getRecoveredCarCount() {
        return recoveredCarCount;
    }

    private void getNewCar() {
        if (rd.nextDouble() <= CAR_SPAWN_PROBABILITY && !queue.offer(new Car())) {
            rejectedCarCount++;
        }
    }
}