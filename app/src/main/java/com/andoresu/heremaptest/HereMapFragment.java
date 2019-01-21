package com.andoresu.heremaptest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.CopyrightLogoPosition;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.MapSettings;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.guidance.VoiceCatalog;
import com.here.android.mpa.guidance.VoiceSkin;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapTransitLayer;
import com.here.android.mpa.mapping.MapView;
import com.here.android.mpa.routing.Maneuver;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



@SuppressLint("LogNotTimber")
public class HereMapFragment extends BaseFragment implements
        PositioningManager.OnPositionChangedListener,
        OnEngineInitListener {

    private String TAG = "TESTTAG_" + HereMapFragment.class.getSimpleName();

    @BindView(R.id.mapView)
    MapView mapView;

    private Map map;

    // positioning manager instance
    private PositioningManager positioningManager;

    // current location
    private GeoCoordinate currentCoordinate;

    private Location currentLocation;

    private boolean foregroundServiceStarted;

    public HereMapFragment() {}

    public static HereMapFragment newInstance() {
        Bundle bundle = new Bundle();
        HereMapFragment mapFragment = new HereMapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_here_map, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        getMapAsync(this);
        mapView = view.findViewById(R.id.mapView);
        return view;
    }

    private void getMapAsync(OnEngineInitListener onEngineInitListener){
        if(getActivity() == null){
            Log.e(TAG, "getMapAsync: context null");
        }else{
            boolean success = MapSettings.setIsolatedDiskCacheRootPath(
                    getActivity().getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                    "com.revapp.android.heretutorail.MapService");
            if (!success) {
                Toast.makeText(getActivity(), "Unable to set isolated disk cache path.", Toast.LENGTH_LONG).show();
            } else {
                MapEngine.getInstance().init(new ApplicationContext(getActivity()), onEngineInitListener);
            }
        }
    }

    private void onMapReady(){

        map = new Map();
        initPositionManager();
        map.getMapTransitLayer().setMode(MapTransitLayer.Mode.EVERYTHING);
        map.setTrafficInfoVisible(true);
        map.setMapScheme(Map.Scheme.NORMAL_DAY_TRANSIT);
        map.setCenter(new GeoCoordinate(10.989781, -74.803889, 0.0),
                Map.Animation.NONE);
        if(mapView == null){
            Log.e(TAG, "onMapReady: MapView null");
        }else{
            mapView.setCopyrightLogoPosition(CopyrightLogoPosition.BOTTOM_LEFT);
            mapView.setMap(map);
        }

    }

    private void initPositionManager(){
        positioningManager = PositioningManager.getInstance();
        positioningManager.addListener(new WeakReference<>(this));
        // start position updates, accepting GPS, network or indoor positions
        if (positioningManager.start(PositioningManager.LocationMethod.GPS_NETWORK)) {
            Log.i(TAG, "initPositionManager: PositioningManager.start: OK");
            map.getPositionIndicator().setVisible(true);
        } else {
            Log.e(TAG, "initPositionManager: PositioningManager.start: failed, exiting");
        }
    }

    @Override
    public void onEngineInitializationCompleted(Error error) {
        if(error == Error.NONE){
            onMapReady();
        }else {
            Log.e(TAG, "onEngineInitializationCompleted: cant load engine");
            Log.e(TAG, "onEngineInitializationCompleted: " + error);
        }
    }

    @Override
    public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
        currentCoordinate = geoPosition.getCoordinate();
        Location location = new Location("");
        location.setLatitude(currentCoordinate.getLatitude());
        location.setLongitude(currentCoordinate.getLongitude());
        handleOnLocationChange(location);
    }

    public void handleOnLocationChange(Location location){
        if(location == null){
            return;
        }
        currentLocation = location;
        centerCamera(currentLocation);
    }

    @Override
    public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView != null) {
            mapView.onPause();
        }
    }


    public void centerCamera(Location location){
        if(location == null){
            return;
        }
        map.setCenter(new GeoCoordinate(location.getLatitude(), location.getLongitude()), Map.Animation.LINEAR);
    }

}
