package com.example.sw03_app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.core.app.ActivityCompat;

public class UserLocationManager {

    private static final int PERMISSION_REQUEST_CODE = 1001;

    private final LocationManager locationManager;
    private final LocationListener locationListener;

    public UserLocationManager(Context context, LocationListener listener) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = listener;
    }


    private boolean hasLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /*
        권한을 체크한뒤
        이미 권한 체크 로직이 있음
     */
    @SuppressWarnings({"MissingPermission"})
    public Location getLastKnownLocation(Context context) {
        if (hasLocationPermission(context)) {
            requestLocationUpdates();
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        //todo 이경우 어떡할지(위치 권한을 설정하지 않았거나 푼 경우 서버에 그냥 해제요청보낸다?)
        //그렇다고 그대로 냅두면 이 앱 왜씀?
        return null;
    }

    @SuppressWarnings({"MissingPermission"})
    private void requestLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }


    public void stopLocationUpdates() {

        locationManager.removeUpdates(locationListener);
    }


}
