package telran.utills;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Worker extends Thread {
    private final GarageImp garage;
    private boolean working;
    private long workingTime;
    private long idleTime;

    public static Worker createAndStart(GarageImp garage) {
        Worker worker = new Worker(garage);
        worker.start();
        return worker;
    }

    public Worker(GarageImp garage) {
        this.garage = garage;
        this.working = true;
    }

    @Override
    public void run() {
        while (working) {
            Car car;
            try {
                Instant start = Instant.now();
                car = garage.getQueue().take();
                idleTime += ChronoUnit.MILLIS.between(start, Instant.now());
                recoverCar(car);
            } catch (InterruptedException e) {
                do {
                    car = garage.getQueue().poll();
                    if (car != null) {
                        try {
                            recoverCar(car);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } while (car != null);
                working = false;
            }
        }
    }

    private void recoverCar(Car car) throws InterruptedException {
        sleep(car.recoveringTime);
        workingTime += car.recoveringTime;
        garage.getRecoveredCarCount().incrementAndGet();
    }

    public long getWorkingTime() {
        return workingTime;
    }

    public long getIdleTime() {
        return idleTime;
    }
}