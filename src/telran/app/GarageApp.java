package telran.app;

import telran.utills.GarageImp;

import java.time.temporal.ChronoUnit;

public class GarageApp {

    public static void main(String[] args) {
        int capacity = 15;
        int nWorkers = 22;
        int duration = 20;
        ChronoUnit unit = ChronoUnit.DAYS;

        GarageImp garage = new GarageImp(capacity, nWorkers, duration, unit);
        garage.start();
    }
}
