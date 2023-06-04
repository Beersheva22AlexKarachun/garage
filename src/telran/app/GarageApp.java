package telran.app;

import telran.no_threads_garage.GarageNoThreads;
import telran.utills.GarageImp;

import java.time.temporal.ChronoUnit;

public class GarageApp {

    public static void main(String[] args) throws InterruptedException {
        int capacity = 25;
        int nWorkers = 35;
        int duration = 20;
        ChronoUnit unit = ChronoUnit.DAYS;

        GarageImp garage = new GarageImp(capacity, nWorkers, duration, unit);
        garage.start();garage.join();
        new GarageNoThreads(capacity, nWorkers, duration, unit).simulate();
    }
}
