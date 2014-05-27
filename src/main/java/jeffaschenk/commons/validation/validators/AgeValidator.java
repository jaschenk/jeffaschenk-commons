package jeffaschenk.commons.validation.validators;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Provide Age Validation based upon a Supplied Birth Date
 *
 * @author jeffaschenk@gmail.com
 *
 */

/**
 * A class to provide Age Validation
 */
public class AgeValidator {

    /**
     * Constants
     * Allowable age for a person can Register.
     */
    public static final int ALLOWED_YEARS_OF_AGE = 18;
    public static final int MAX_ALLOWED_YEARS_OF_AGE = 100;

    /**
     * Is Age Valid?
     *
     * @param birthDate
     * @return boolean True if valid Birth Date is within a Valid Range, otherwise false.
     */
    public static boolean isAgeValid(Date birthDate) {
        Calendar age = Calendar.getInstance();
        age.setTime(birthDate);
        if (age.after(getYoungestBirthDateAllowedToday())) {
            return false;
        }
        return true;
    }

    /**
     * get the Youngest BirthDate a Person may Have today to Register.
     *
     * @return Calendar
     */
    public static Calendar getYoungestBirthDateAllowedToday() {
        Calendar YOUNGEST_BIRTH_DATE_ALLOWED_TODAY = Calendar.getInstance();
        YOUNGEST_BIRTH_DATE_ALLOWED_TODAY.add(Calendar.YEAR, (ALLOWED_YEARS_OF_AGE * -1));
        return YOUNGEST_BIRTH_DATE_ALLOWED_TODAY;
    }

    /**
     * Get Number of Days Before Person Is Allowed To Register
     *
     * @param birthDate
     * @return long - Number of Days which need to pass before one can Register.
     */
    public static long getNumberOfDaysBeforePersonIsAllowedToRegister(Date birthDate) {
        Calendar age = Calendar.getInstance();
        age.setTime(birthDate);
        if (age.after(getYoungestBirthDateAllowedToday())) {
            long remainingTime = (birthDate.getTime() - getYoungestBirthDateAllowedToday().getTimeInMillis());
            // Narrow Down to Whole Days.
            long remainingWholeDays = 0;
            if (remainingTime >= (24 * (60 * (60 * 1000)))) {
                remainingWholeDays = (remainingTime / (24 * (60 * (60 * 1000))));
                remainingTime = remainingTime - (remainingWholeDays * ((24 * (60 * (60 * 1000)))));
                if (remainingTime > 0) {
                    remainingWholeDays++;
                }
            } else {
                remainingWholeDays++;
            }
            return remainingWholeDays;
        }
        return 0;
    }

    /**
     * Get Available Years Born
     *
     * @return Map<String,String> of Valid Years Born.
     */
    public static List<Integer> getYearsBorn() {
        List<Integer> list = new ArrayList<Integer>();
        Calendar youngest = Calendar.getInstance();
        Calendar oldest = Calendar.getInstance();
        youngest.add(Calendar.YEAR, (ALLOWED_YEARS_OF_AGE * -1));
        oldest.add(Calendar.YEAR, (MAX_ALLOWED_YEARS_OF_AGE * -1));
        while (youngest.after(oldest)) {
            list.add(youngest.get(Calendar.YEAR));
            youngest.add(Calendar.YEAR, -1);
        }
        return list;
    }


}
