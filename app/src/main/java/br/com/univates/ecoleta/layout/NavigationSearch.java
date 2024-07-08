package br.com.univates.ecoleta.layout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.dto.GeoLocationDto;
import br.com.univates.ecoleta.db.entity.dto.GeoLocationResponseDto;
import br.com.univates.ecoleta.db.service.GeoLocationService;

public class NavigationSearch extends Fragment implements MapListener {

    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private final GeoLocationService geoLocationService = new GeoLocationService();

    public NavigationSearch() {}

    public static NavigationSearch newInstance() {
        NavigationSearch fragment = new NavigationSearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue("br.com.univates.ecoleta");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.navigation_search, container, false);

        if (!checkLocationPermissions()) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        // Initialize the map view
        mapView = rootView.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Setup MyLocationNewOverlay
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
        mapController = mapView.getController();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);

        mapController.setCenter(new GeoPoint(-29.44442, -51.96514));
        mapController.setZoom(12.0);
        mapView.getOverlays().add(myLocationOverlay);
        mapView.addMapListener(this);

        addPointsColetas();
        addMyLocation();

        return rootView;
    }

    private void addMarker(GeoPoint point, String title, String description) {
        if (mapView == null) return;

        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle(title);
        marker.setSnippet(description);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private boolean checkLocationPermissions() {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myLocationOverlay.enableMyLocation();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }

    private void addPointsColetas() {
        // Example address
        GeoLocationDto dto = new GeoLocationDto();
        dto.setStreetAddress("Av. Cristiano Dexheimer".replaceAll(" ", "+"));
        dto.setCity("Lajeado".replaceAll(" ", "+"));
        dto.setState("RS".replaceAll(" ", "+"));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<GeoLocationResponseDto> responseList = geoLocationService.findGeoCodeAddres(dto);
                if (isAdded() && getActivity() != null) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseList != null && !responseList.isEmpty()) {
                                GeoPoint geoPoint = new GeoPoint(responseList.get(0).getLatitude(), responseList.get(0).getLongitude());
                                addMarker(geoPoint, responseList.get(0).getName(), responseList.get(0).getDisplayName());
                            }
                        }
                    });
                }
            }
        });
    }

    private void addMyLocation() {
        myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GeoPoint myLocation = myLocationOverlay.getMyLocation();
                        if (myLocation != null) {
                            mapController.setCenter(myLocation);
                            mapController.animateTo(myLocation);
                            //addMarker(myLocation, "Você está aqui!", "");
                        }
                    }
                });
            }
        });
    }
}
