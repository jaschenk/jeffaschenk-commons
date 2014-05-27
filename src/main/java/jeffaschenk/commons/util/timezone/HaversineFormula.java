package jeffaschenk.commons.util.timezone;

/**
 * Provide Simple Static Utility to perform the Haversine Calculation
 * between two points on a Sphere.
 * <p/>
 * In our case it is the Earth which is the Sphere and our points
 * are two Longitude and Latitudes.
 *
 * @author jeffaschenk@gmail.com
 *         Date: Apr 1, 2011
 */
public class HaversineFormula {
    /**
     * Non-accessible Constructor for Utility Class.
     */
    private HaversineFormula() {
        // Make Constructor not Accessible for Static Utility Class.
    }

    /**
     * Earth Radius in Kilometers and conversion Multipliers.
     */
    private static final double earthRadiusInKilometers = 6372.8;
    private static final double kmToStatuteMileMultiplier = 0.621371;
    private static final double kmToNauticalMileMultiplier = 0.539957;
    /**
     * Earth Radiu in Miles.
     */
    private static final double earthRadiusInMiles = 3958.75;

    /**
     * Return the Distance in Miles between the two points on Earth
     * <p/>
     * To return a measurement other than Miles, you will need to modify the
     * EarthRadius in Miles Constant or supply a different constant.
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return double containing distance between the two points on the Earth.
     */
    public static double distanceInMilesFrom(double lat1, double lng1, double lat2, double lng2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadiusInMiles * c;

        return dist;
    }


    /**
     * Main to Provide Command Line Interface to Utility.
     *
     * @param args
     */
    public static void main(String[] args) {

        double distance = HaversineFormula.distanceInMilesFrom(0, 0, 0, 0);
        System.out.println("Distance Result: " + distance + " miles.");

        System.out.println("Radius of Earth: " + HaversineFormula.earthRadiusInKilometers + " km, " +
                HaversineFormula.kmToStatuteMileMultiplier * HaversineFormula.earthRadiusInKilometers +
                " Statute Miles, " +
                HaversineFormula.kmToNauticalMileMultiplier * HaversineFormula.earthRadiusInKilometers +
                " Nautical Miles.");

    }


}
