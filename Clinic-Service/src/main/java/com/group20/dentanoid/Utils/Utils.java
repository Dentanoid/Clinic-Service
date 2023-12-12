package com.group20.dentanoid.Utils;

import java.util.Arrays;

public class Utils {
    /*
      * Haversine Formula, Credits to https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    
      * Returns the distance in kilometers between two global coordinates,
        accounting for the spherical shape of the Earth by using its radius
    */
    public static double haversineFormula(double[] positionA, double[] positionB) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(positionB[0] - positionA[0]);
        double dLon = deg2rad(positionB[1] - positionA[1]);

        double a = 
          Math.sin(dLat/2) * Math.sin(dLat/2) +
          Math.cos(deg2rad(positionA[0])) * Math.cos(deg2rad(positionB[0])) * 
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
