package br.com.univates.ecoleta.layout;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import br.com.univates.ecoleta.MainActivityPrincipal;
import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;

public class NewPontoColetaFinal extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView capturedImage;
    private ImageView deleteIcon;
    private LinearLayout imagePickerLayout;
    private Coleta pontoColeta;
    private Bitmap imageBitmap;

    public NewPontoColetaFinal() {}

    public static NewPontoColetaFinal newInstance() {
        return new NewPontoColetaFinal();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pontoColeta = (Coleta) getArguments().getSerializable("pontoColeta");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_ponto_coleta_final, container, false);

        imagePickerLayout = view.findViewById(R.id.imagePickerLayout);
        capturedImage = view.findViewById(R.id.capturedImage);
        deleteIcon = view.findViewById(R.id.deleteIcon);

        imagePickerLayout.setOnClickListener(v -> checkCameraPermission());
        deleteIcon.setOnClickListener(v -> removeCapturedImage());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Verifica se há argumentos e atualiza a UI conforme necessário
        if (getArguments() != null) {
            pontoColeta = (Coleta) getArguments().getSerializable("pontoColeta");
            imageBitmap = getArguments().getParcelable("capturedImage");

            // Atualize sua UI com pontoColeta e imageBitmap aqui
            if (imageBitmap != null) {
                capturedImage.setImageBitmap(imageBitmap);
                capturedImage.setVisibility(View.VISIBLE);
                deleteIcon.setVisibility(View.VISIBLE);
                imagePickerLayout.setVisibility(View.GONE);
            }
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(getContext(), "Permissão para usar a câmera é necessária para tirar fotos.", Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    private void removeCapturedImage() {
        capturedImage.setImageBitmap(null);
        capturedImage.setVisibility(View.GONE);
        deleteIcon.setVisibility(View.GONE);
        imagePickerLayout.setVisibility(View.VISIBLE);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Permissão para usar a câmera é necessária para tirar fotos.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                        Bundle args = new Bundle();
                        args.putSerializable("pontoColeta", pontoColeta);
                        args.putParcelable("capturedImage", imageBitmap);
                        setArguments(args);

                        NewPontoColetaFinal ponto = new NewPontoColetaFinal();
                        ponto.setArguments(args);

                        MainActivityPrincipal activity = (MainActivityPrincipal) requireActivity();
                        activity.navigateToPhotoDisplay(ponto);
                    }
                }
            }
    );
}
