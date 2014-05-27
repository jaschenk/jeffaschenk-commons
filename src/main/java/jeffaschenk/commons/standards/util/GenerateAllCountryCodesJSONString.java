package jeffaschenk.commons.standards.util;

import jeffaschenk.commons.standards.CountryCode;

/**
 * GenerateAllCountryCodesToJSONString
 * Static Helper utility fir use in development to produce a
 * JSON String.
 *
 * @author jeffaschenk@gmail.com
 *
 */
public class GenerateAllCountryCodesJSONString {

        public static void main(String[] args) {

            System.out.println(CountryCode.getJSONString(false));

            System.out.println(CountryCode.getJSONString(true));

        }



}
