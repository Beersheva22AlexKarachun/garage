package telran.utills;

import java.time.temporal.ChronoUnit;
import java.util.Random;

public interface Garage {
    static final Random rd = new Random();
    static final ChronoUnit SIMULATION_TIME_UNIT = ChronoUnit.MILLIS;
    static final ChronoUnit REAL_TIME_UNIT = ChronoUnit.MINUTES;
    static final double CAR_SPAWN_PROBABILITY = 0.15;

    static final long DEFAULT_SIMULATION_DURATION = 180;
    static final ChronoUnit DEFAULT_SIMULATION_UNIT = ChronoUnit.DAYS;

}
