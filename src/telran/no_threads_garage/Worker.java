package telran.no_threads_garage;

import telran.utills.Car;

public class Worker {
    private Car car;
    private int recoveryCounter;
    private int idleTime;
    private int workingTime;
    private boolean isRecovering;
    private int totalCarsRecovered;

    public void setCar(Car car) {
        this.car = car;
        recoveryCounter = 0;
        isRecovering = true;
    }

    public int getTotalCarsRecovered() {
        return totalCarsRecovered;
    }

    public void repair() {
        if (car != null && recoveryCounter++ == car.recoveringTime) {
            car = null;
            isRecovering = false;
            workingTime += recoveryCounter;
            recoveryCounter = 0;
            totalCarsRecovered++;
        } else if (car == null) {
            idleTime++;
        }
    }

    public boolean isRecovering() {
        return isRecovering;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public int getWorkingTime() {
        return workingTime;
    }
}
