package telran.no_threads_garage;

import telran.utills.Car;
import telran.utills.Garage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class GarageNoThreads implements Garage {
    public static final String carsMsg = "Total recovered cars: %d, total rejected cars %d, total time: %d hours";
    public static final String timeMsg = "Total working time: %d hours, total idle time: %d hours";

    private final Queue<Car> queue;
    private final Worker[] workers;
    private long simulationTime;
    private int rejectedCarCount;
    private int recoveredCarCount;
    private int totalWorkingTime;
    private int totalIdleTime;

    public GarageNoThreads(int capacity, int nWorkers, int simulationTime, ChronoUnit simulationUnit) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.workers = new Worker[nWorkers];
        this.simulationTime = Duration.of(simulationTime, simulationUnit).toMinutes();
        this.simulationTime = this.simulationTime;

        for (int i = 0; i < nWorkers; i++) {
            workers[i] = new Worker();
        }
    }

    public void simulate() {
        for (int i = 0; i < simulationTime; i++) {
            getNewCar();
            work();
        }
        while (!queue.isEmpty() || Arrays.stream(workers).anyMatch(Worker::isRecovering)) {
            simulationTime++;
            work();
        }
        printInfo();
    }

    private void printInfo() {
        recoveredCarCount = Arrays.stream(workers).mapToInt(Worker::getTotalCarsRecovered).sum();
        totalWorkingTime = Arrays.stream(workers).mapToInt(Worker::getWorkingTime).sum();
        totalIdleTime = Arrays.stream(workers).mapToInt(Worker::getIdleTime).sum();

        System.out.println(String.format(carsMsg, recoveredCarCount, rejectedCarCount, simulationTime / 60));
        System.out.println(String.format(timeMsg, totalWorkingTime / 60, totalIdleTime / 60));
    }

    private void work() {
        Arrays.stream(workers).forEach(worker -> {
            if (!worker.isRecovering()) {
                Car car = queue.poll();
                if (car != null) {
                    worker.setCar(car);
                }
            }
            worker.repair();
        });
    }

    private void getNewCar() {
        if (rd.nextDouble() <= CAR_SPAWN_PROBABILITY && !queue.offer(new Car())) {
            rejectedCarCount++;
        }
    }
}
