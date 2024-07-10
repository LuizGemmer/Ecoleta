package br.com.univates.ecoleta.layout.navigation;

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
import java.util.stream.Collectors;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;
import br.com.univates.ecoleta.db.entity.ColetaType;
import br.com.univates.ecoleta.db.entity.dto.GeoLocationDto;
import br.com.univates.ecoleta.db.entity.dto.GeoLocationResponseDto;
import br.com.univates.ecoleta.db.service.ColetaService;
import br.com.univates.ecoleta.db.service.GeoLocationService;

public class NavigationSearch extends Fragment implements MapListener {

    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;
    private GeoLocationService geoLocationService;
    private ColetaService coletaService;

    private static final int REQUEST_LOCATION_PERMISSION = 1;


    public NavigationSearch() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue("br.com.univates.ecoleta");
        geoLocationService = new GeoLocationService();
        coletaService = new ColetaService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_search, container, false);

        if (!checkLocationPermissions()) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        // Initialize the map view
        mapView = view.findViewById(R.id.mapView);
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

        return view;
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
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Coleta> listaPontosColeta = coletaService.findAll();
            if(!listaPontosColeta.isEmpty()) {
                for (Coleta coleta : listaPontosColeta.stream().filter(coleta -> coleta.getTipoColeta() == ColetaType.PONTO_COLETA).collect(Collectors.toList())) {
                    if (coleta.getLatitude() == 0 || coleta.getLongitude() == 0) {
                        renderPinByAddress(coleta);
                    } else {
                        addMarker(new GeoPoint(coleta.getLatitude(), coleta.getLongitude()), coleta.getQueColeta(), coleta.getDescricao());
                    }
                }
            }
        });

    }

    private void renderPinByAddress(Coleta coleta) {
        GeoLocationDto dto = new GeoLocationDto();
        dto.setStreetAddress(coleta.getLogradouro().replaceAll(" ", "+"));
        dto.setCity(coleta.getLocalidade().replaceAll(" ", "+"));
        dto.setState(coleta.getUf().replaceAll(" ", "+"));

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<GeoLocationResponseDto> responseList = geoLocationService.findGeoCodeAddres(dto);
            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseList != null && !responseList.isEmpty()) {
                            GeoPoint geoPoint = new GeoPoint(responseList.get(0).getLatitude(), responseList.get(0).getLongitude());
                            addMarker(geoPoint, coleta.getQueColeta(), coleta.getDescricao());
                        }
                    }
                });
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
