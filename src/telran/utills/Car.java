package telran.utills;

import java.util.Random;

public class Car {
    private static final int MIN_RECOVERY_TIME = 30;
    private static final int MAX_RECOVERY_TIME = 480;
    private static final Random rd = new Random();
    public final int recoveringTime;

    public Car() {
        recoveringTime = getRecoveringTime();
    }

    private static int getRecoveringTime() {
        return rd.nextInt(MIN_RECOVERY_TIME, MAX_RECOVERY_TIME + 1);
    }
}
