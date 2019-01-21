package com.andoresu.heremaptest;

class Constants {
    public static final int REQUEST_PERMISSIONS = 1;
    public static String[] permissions = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
}
