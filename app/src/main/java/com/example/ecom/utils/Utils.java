package com.example.ecom.utils;

import java.text.DecimalFormat;

public class Utils {
    public static String formatDouble(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }
}
