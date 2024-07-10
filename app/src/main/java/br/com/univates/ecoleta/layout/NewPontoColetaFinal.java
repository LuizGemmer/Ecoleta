package br.com.univates.ecoleta.layout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;

public class NewPontoColetaFinal extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private PreviewView cameraPreview;
    private FrameLayout cameraPreviewLayout;
    private ImageView imagePicker;
    private LinearLayout imagePickerLayout;
    private ImageView capturedImage;
    private ImageView btnDelete;
    private Button btnCapture;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    public NewPontoColetaFinal() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_ponto_coleta_final, container, false);
        cameraExecutor = Executors.newSingleThreadExecutor();

        initViews(rootView);
        requestPermissionsApp();
        retriveArguments();

        btnCapture.setOnClickListener(v -> captureImage());
        btnDelete.setOnClickListener(v -> deleteImage());
        imagePicker.setOnClickListener(v -> toggleFullScreenPreview(true));

        return rootView;
    }

    private void initViews(View rootView) {
        cameraPreviewLayout = rootView.findViewById(R.id.cameraPreviewLayout);
        cameraPreview = rootView.findViewById(R.id.cameraPreview);
        btnCapture = rootView.findViewById(R.id.btnCapture);
        btnDelete = rootView.findViewById(R.id.btnDelete);
        imagePicker = rootView.findViewById(R.id.imagePicker);
        capturedImage = rootView.findViewById(R.id.capturedImage);
        imagePickerLayout = rootView.findViewById(R.id.imagePickerLayout);
    }

    private void requestPermissionsApp() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
    }

    private void retriveArguments() {
        Bundle args = getArguments();
        if (args != null && args.containsKey("pontoColeta")) {
            Coleta pontoColeta = (Coleta) args.getSerializable("pontoColeta");
            assert pontoColeta != null;
            System.out.println(pontoColeta.getLocalidade());
        }
    }

    private void toggleFullScreenPreview(boolean isfullScreen) {
        if (isfullScreen) {
            cameraPreviewLayout.setVisibility(View.VISIBLE);
            imagePicker.setVisibility(View.GONE);
            capturedImage.setVisibility(View.GONE);

            startCamera();
        } else {
            cameraPreviewLayout.setVisibility(View.GONE);
            imagePicker.setVisibility(View.VISIBLE);
            stopCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(getContext(), "Permissões necessárias para usar a câmera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder()
                        .build();

                imageCapture = new ImageCapture.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void stopCamera() {
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }

    private void captureImage() {
        if (imageCapture != null) {
            imageCapture.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                @Override
                public void onCaptureSuccess(@NonNull ImageProxy image) {
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            capturedImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                            capturedImage.setVisibility(View.VISIBLE);
                            btnDelete.setVisibility(View.VISIBLE);
                            cameraPreviewLayout.setVisibility(View.GONE);
                        }
                    });
                    image.close();
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Toast.makeText(getContext(), "Erro ao capturar imagem: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteImage() {
        capturedImage.setImageBitmap(null);
        imagePickerLayout.setVisibility(View.VISIBLE);
        imagePicker.setVisibility(View.VISIBLE);
        capturedImage.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
    }
}
