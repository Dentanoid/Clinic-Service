package org.example;

import java.util.Arrays;

public class Utils {
    // Haversine Formula, Credits to https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    // Returns the distance in kilometers between two global coordinates
    public static double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);

        double a = 
          Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
          Math.sin(dLon/2) * Math.sin(dLon/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        double d = R * c; // Distance in km
        return d;
      }
      
    private static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    public static double[] convertStringToDoubleArray(String[] input) {
        return Arrays.stream(input)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
