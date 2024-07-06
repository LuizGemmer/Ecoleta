package br.com.univates.ecoleta.db.rest;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import br.com.univates.ecoleta.db.entity.dto.GeoLocationDto;
import br.com.univates.ecoleta.db.entity.dto.GeoLocationResponseDto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NominatimExecutor {
    // Singleton instance
    private static NominatimExecutor instance;
    private final OkHttpClient httpClient;

    // Private constructor to prevent instantiation
    private NominatimExecutor() {
        httpClient = new OkHttpClient();
    }

    // Method to get the singleton instance
    public static NominatimExecutor getInstance() {
        if (instance == null) {
            instance = new NominatimExecutor();
        }
        return instance;
    }

    // Method to search location based on GeoLocationDto
    public List<GeoLocationResponseDto> searchLocation(@NonNull GeoLocationDto geoLocationDto) {
        String urlApi = "https://nominatim.openstreetmap.org";
        String query = geoLocationDto.getStreetAddress() + ", " + geoLocationDto.getCity() + ", " + geoLocationDto.getState();
        String format = "jsonv2";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(urlApi + "/search.php")).newBuilder();
        urlBuilder.addQueryParameter("q", query);
        urlBuilder.addQueryParameter("format", format);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        final List<GeoLocationResponseDto>[] resultList = new List[]{null};
        final CountDownLatch latch = new CountDownLatch(1);

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseBody = response.body().string();
                    resultList[0] = parseJsonResponse(responseBody);
                }
                latch.countDown();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                latch.countDown();
            }
        });

        try {
            latch.await(); // Wait for the callback to be called
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultList[0];
    }

    private List<GeoLocationResponseDto> parseJsonResponse(String responseBody) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GeoLocationResponseDto>>() {}.getType();
        return gson.fromJson(responseBody, listType);
    }
}
