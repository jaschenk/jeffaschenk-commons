package jeffaschenk.commons.standards.util;

import com.outbid.sor.commons.container.security.object.OutbidSecuritySessionUserObject;
import com.outbid.sor.model.orm.user.RegisteredUser;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Provides utilites for internationalization.
 *
 * @author (asvenss) Andreas Svensson
 */
@Component("internationalizationUtils")
public class  InternationalizationUtils {

    /**
     * @param registeredUser the user
     * @return Returns the locale of the user. Returns a US locale if the user hasn't specified one.
     */
    public Locale getLocale(RegisteredUser registeredUser) {
        if (registeredUser == null || registeredUser.getRegisteredUserProfile() == null ||
                registeredUser.getRegisteredUserProfile().getUserLocale() == null) {
            return Locale.US;
        } else {
            return LocaleUtils.toLocale(registeredUser.getRegisteredUserProfile().getUserLocale());
        }
    }

    /**
     * Returns the locale of the logged in user
     *
     * @param user the user
     * @return the user's selected locale or US if no selection has been made.
     */
    public Locale getLocale(OutbidSecuritySessionUserObject user) {
        if (user == null || user.getOutbidSecuritySessionProfileObject() == null ||
                user.getOutbidSecuritySessionProfileObject().getUserLocale() == null) {
            return Locale.US;
        } else {
            return LocaleUtils.toLocale(user.getOutbidSecuritySessionProfileObject().getUserLocale());
        }
    }

    /**
     * @param registeredUser the user
     * @return Returns the user's preferred timezone. Returns pacific time if the user hasn't specified one.
     */
    public TimeZone getTimeZone(RegisteredUser registeredUser) {
        if (registeredUser.getRegisteredUserProfile() == null ||
                registeredUser.getRegisteredUserProfile().getUserTimeZoneName() == null ||
                "Default".equals(registeredUser.getRegisteredUserProfile().getUserTimeZoneName())) {
            return com.outbid.sor.commons.standards.TimeZone.getJavaTimeZone(com.outbid.sor.commons.standards.TimeZone.DEFAULT_TIME_ZONE.TimeZoneStandardName());
        } else {
            return com.outbid.sor.commons.standards.TimeZone.getJavaTimeZone(registeredUser.getRegisteredUserProfile().getUserTimeZoneName());
        }
    }

    /**
     * @param user the profile object of the logged in user
     * @return the user's preferred timezone. If none has been specified, it returns Pacific time.
     */
    public TimeZone getTimeZone(OutbidSecuritySessionUserObject user) {

        if (user == null || user.getOutbidSecuritySessionProfileObject() == null ||
                user.getOutbidSecuritySessionProfileObject().getTimeZone() == null ||
                "Default".equals(user.getOutbidSecuritySessionProfileObject().getTimeZone())) {
            return com.outbid.sor.commons.standards.TimeZone.getJavaTimeZone(com.outbid.sor.commons.standards.TimeZone.DEFAULT_TIME_ZONE.TimeZoneStandardName());
        } else {
            return com.outbid.sor.commons.standards.TimeZone.getJavaTimeZone(user.getOutbidSecuritySessionProfileObject().getTimeZone());
        }
    }

}
