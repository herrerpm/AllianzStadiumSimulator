package Utils;

import java.awt.*;

/**
 * This class informs the Threads where are all the zones available for them
 * to go around the stadium, depending on which role and status they have.
 */
public class ZoneCoordinates {
    public static final Rectangle ENTRANCE_ZONE = new Rectangle(20, 20, 280, 230);
    public static final Rectangle GENERAL_ZONE = new Rectangle(320,120,480,180);
    public static final Rectangle FOOD_ZONE = new Rectangle(570,20,230,80);
    public static final Rectangle BATHROOM_ZONE = new Rectangle(320,20,230,80);
    public static final Rectangle STANDS_ZONE = new Rectangle(20,270,780,305);
    public static final Rectangle TICKETS_ZONE = new Rectangle(20, 170, 130, 130);
    public static final Rectangle REGISTER_ZONE = new Rectangle(170, 170, 130, 130);
    public static final Rectangle FIELD_ZONE = new Rectangle(120,320,580,230);
    public static final Rectangle BENCH_ZONE = new Rectangle(170,570,480,5);

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