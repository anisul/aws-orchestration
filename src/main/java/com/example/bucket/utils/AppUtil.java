package com.example.bucket.utils;

import com.amazonaws.regions.Regions;

public class AppUtil {
    public static Regions getRegionsFromString(String regionName) {
        return Regions.fromName(regionName);
    }
}
