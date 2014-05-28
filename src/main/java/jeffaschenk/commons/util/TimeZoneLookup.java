package jeffaschenk.commons.util;


import jeffaschenk.commons.standards.TimeZone;

import java.util.HashMap;
import java.util.Map;

/**
 * TimeZoneLookup
 * Provides Static Helper Methods to provide easy TimeZone Lookup
 * by using various inputs.
 *
 * @author jeffaschenk@gmail.com
 */
public class TimeZoneLookup {

    private TimeZoneLookup() {
    }


    private static Map<Long, TimeZone> UTC_OFFSETS = new HashMap<Long, TimeZone>();

    private static Map<Long, TimeZone> UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS = new HashMap<Long, TimeZone>();

    /**
     * Initialize our UTC Offset Map
     */
    static {
        UTC_OFFSETS.put(0L, TimeZone.Antarctica_Vostok);
        UTC_OFFSETS.put(+1L, TimeZone.Europe_Zurich);
        UTC_OFFSETS.put(+2L, TimeZone.Europe_Kiev);
        UTC_OFFSETS.put(+3L, TimeZone.Europe_Moscow);
        UTC_OFFSETS.put(+4L, TimeZone.Asia_Dubai);
        UTC_OFFSETS.put(+5L, TimeZone.Asia_Oral);
        UTC_OFFSETS.put(+6L, TimeZone.Antarctica_Mawson);
        UTC_OFFSETS.put(+7L, TimeZone.Antarctica_Davis);
        UTC_OFFSETS.put(+8L, TimeZone.Asia_Shanghai);
        UTC_OFFSETS.put(+9L, TimeZone.Asia_Tokyo);
        UTC_OFFSETS.put(+10L, TimeZone.Australia_Brisbane);
        UTC_OFFSETS.put(+11L, TimeZone.Pacific_Ponape);
        UTC_OFFSETS.put(+12L, TimeZone.Pacific_Fiji);
        UTC_OFFSETS.put(+13L, TimeZone.Pacific_Tongatapu);
        UTC_OFFSETS.put(+14L, TimeZone.Pacific_Kiritimati);
        UTC_OFFSETS.put(-12L, TimeZone.UTC_12);
        UTC_OFFSETS.put(-11L, TimeZone.Pacific_Midway);
        UTC_OFFSETS.put(-10L, TimeZone.Pacific_Honolulu);
        UTC_OFFSETS.put(-9L, TimeZone.America_Anchorage);
        UTC_OFFSETS.put(-8L, TimeZone.America_Los_Angeles);
        UTC_OFFSETS.put(-7L, TimeZone.America_Denver);
        UTC_OFFSETS.put(-6L, TimeZone.America_Chicago);
        UTC_OFFSETS.put(-5L, TimeZone.America_New_York);
        UTC_OFFSETS.put(-4L, TimeZone.America_Goose_Bay);
        UTC_OFFSETS.put(-3L, TimeZone.America_Argentina_Buenos_Aires);
        UTC_OFFSETS.put(-2L, TimeZone.America_Noronha);
        UTC_OFFSETS.put(-1L, TimeZone.Atlantic_Azores);
    }

    ;

    /**
     * Initialize our UTC Offset Map
     */
    static {
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(0L, TimeZone.Antarctica_Vostok);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+1L, TimeZone.Atlantic_Canary);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+2L, TimeZone.Europe_Zurich);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+3L, TimeZone.Europe_Kiev);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+4L, TimeZone.Europe_Moscow);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+5L, TimeZone.Asia_Baku);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+6L, TimeZone.Asia_Yekaterinburg);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+7L, TimeZone.Asia_Novokuznetsk);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+8L, TimeZone.Asia_Krasnoyarsk);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+9L, TimeZone.Asia_Irkutsk);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+10L, TimeZone.Asia_Yakutsk);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+11L, TimeZone.Australia_Melbourne);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+12L, TimeZone.Asia_Magadan);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(+13L, TimeZone.Pacific_Auckland);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-9L, TimeZone.America_Adak);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-8L, TimeZone.America_Anchorage);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-7L, TimeZone.America_Los_Angeles);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-6L, TimeZone.America_Denver);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-5L, TimeZone.America_Chicago);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-4L, TimeZone.America_New_York);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-3L, TimeZone.America_Goose_Bay);
        UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.put(-2L, TimeZone.America_Argentina_Buenos_Aires);
    }

    ;

    /**
     * Get Timezone Based upon UTC Offset.
     *
     * @param utc_offset
     * @return TimeZone
     */
    public static TimeZone getTimeZoneByUTC(long utc_offset) {
        // Determine which Table to use?
        if ((TimeZone.isDayLightSavingsTimeEnabled()) &&
                (UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.get(utc_offset) != null)) {
            return UTC_DAYLIGHT_SAVINGS_TIME_OFFSETS.get(utc_offset);
        }
        return UTC_OFFSETS.get(utc_offset);
    }

}
