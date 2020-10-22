package com.NHAS.Infantime.util.helpers;

public class AQIWarningModel {


    /**
     *
     * @param aqi
     * @return
     */
    public static String getServerity(int aqi) {
        if (aqi >= 200) {
            return "Hazardous";
        } else if (aqi >= 150) {
            return "Very poor";
        } else if (aqi >= 100) {
            return "Poor";
        } else if (aqi >= 67) {
            return "Fair";
        } else if (aqi >= 34) {
            return "Good";
        } else if (aqi >= 0) {
            return "Very good";
        } else {
            return "Fail to load AQI.";
        }
    }

    /**
     *
     * @param aqi
     * @return
     */
    public static String getSuggestion(int aqi) {
        if (aqi >= 200) {
            return "Reschedule outdoor activities with infants (AQI 200+)";
        } else if (aqi >= 150) {
            return "Reschedule outdoor activities with infants (AQI 150-199)";
        } else if (aqi >= 100) {
            return "Not recommend outdoor activities with infants (AQI 100-149)";
        } else if (aqi >= 67) {
            return "Reduce or reschedule outdoor activities with infants (AQI 67-99)";
        } else if (aqi >= 34) {
            return "Good air quality (34-66) to go out with infants";
        } else if (aqi >= 0) {
            return "Perfect air quality (0-33) to go out with infants";
        } else {
            return "Fail to load AQI";
        }
    }
}
