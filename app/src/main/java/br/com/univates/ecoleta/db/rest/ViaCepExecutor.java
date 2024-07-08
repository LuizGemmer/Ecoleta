package br.com.univates.ecoleta.db.rest;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import br.com.univates.ecoleta.db.entity.dto.ViaCepResponseDto;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViaCepExecutor {

    // Singleton instance
    private static volatile ViaCepExecutor instance;
    private final OkHttpClient httpClient;

    // Private constructor to prevent instantiation
    private ViaCepExecutor() {
        httpClient = new OkHttpClient();
    }

    // Method to get the singleton instance
    public static ViaCepExecutor getInstance() {
        if (instance == null) {
            synchronized (ViaCepExecutor.class) {
                if (instance == null) {
                    instance = new ViaCepExecutor();
                }
            }
        }
        return instance;
    }

    public ViaCepResponseDto searchLocation(String cep) {
        String urlApi = "https://viacep.com.br/ws";
        String url = urlApi + "/" + cep + "/json/";
        Request request = new Request.Builder()
                .url(url)
                .build();

        final ViaCepResponseDto[] result = new ViaCepResponseDto[1];
        final CountDownLatch latch = new CountDownLatch(1);

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String responseBody = response.body().string();
                        result[0] = parseJsonResponse(responseBody);
                    }
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
            Thread.currentThread().interrupt();
        }

        return result[0];
    }

    private ViaCepResponseDto parseJsonResponse(String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, ViaCepResponseDto.class);
    }
}
