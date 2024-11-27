package Utils;

import java.awt.*;

/**
 * This class informs the Threads where are all the zones available for them
 * to go around the stadium, depending on which role and status they have.
 */
public class ZoneCoordinates {
    public static final Rectangle ENTRANCE_ZONE = new Rectangle(0, 0, 300, 150);
    public static final Rectangle TICKETS_ZONE = new Rectangle(0, 150, 150, 150);
    public static final Rectangle REGISTER_ZONE = new Rectangle(150, 150, 150, 150);
    public static final Rectangle BATHROOM_ZONE = new Rectangle(300, 0, 250, 100);
    public static final Rectangle FOOD_ZONE = new Rectangle(550, 0, 250, 100);
    public static final Rectangle GENERAL_ZONE = new Rectangle(300, 100, 500, 200);
    public static final Rectangle STANDS_ZONE = new Rectangle(0, 300, 400, 325);
    public static final Rectangle FIELD_ZONE = new Rectangle(400, 300, 400, 150);
    public static final Rectangle BENCH_ZONE = new Rectangle(400, 450, 400, 100);

    /**
     *
     * @param zone The Limiting zone where the Thread is supposed to be
     * @param position The actual position of the Thread
     * @return Returns true if the Thread is within the zone. And returns false it is not within the zone
     */
    public static boolean isWithinZone(Rectangle zone, Point position) {
        return zone.contains(position);
    }
}