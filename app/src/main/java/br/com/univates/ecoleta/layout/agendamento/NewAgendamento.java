package br.com.univates.ecoleta.layout.agendamento;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;
import br.com.univates.ecoleta.db.entity.ColetaType;
import br.com.univates.ecoleta.db.entity.Usuario;
import br.com.univates.ecoleta.db.entity.dto.ViaCepResponseDto;
import br.com.univates.ecoleta.db.rest.ViaCepExecutor;
import br.com.univates.ecoleta.db.service.ColetaService;
import br.com.univates.ecoleta.layout.navigation.NavigationHome;
import br.com.univates.ecoleta.utils.EcoletaUtils;

public class NewAgendamento extends Fragment {

    private EditText cepEditText, estadoEditText, cidadeEditText, bairroEditText, logradouroEditText, numeroEditText, complementoEditText;
    private ViaCepExecutor viaCepExecutor;
    private ExecutorService executorService;
    private Handler mainHandler;
    private FirebaseAuth mAuth;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private EditText editTextDate;
    private ColetaService coletaService;

    public NewAgendamento() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viaCepExecutor = ViaCepExecutor.getInstance();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).build();
        coletaService = new ColetaService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_ponto_coleta, container, false);

        init(view);

        // Ações da tela
        focusChangeCepEditText(cepEditText);
        buttonClickLocalizacaoAtual(view);
        buttonClickAvancar(view);

        return view;
    }

    private void init(View view) {
        cepEditText = view.findViewById(R.id.editTextCep);
        estadoEditText = view.findViewById(R.id.editTextEstado);
        cidadeEditText = view.findViewById(R.id.editTextCidade);
        bairroEditText = view.findViewById(R.id.editTextBairro);
        logradouroEditText = view.findViewById(R.id.editTextLogradouro);
        numeroEditText = view.findViewById(R.id.editTextNumero);
        complementoEditText = view.findViewById(R.id.editTextComplemento);
        TextView textViewHeader = view.findViewById(R.id.textViewHeader);
        editTextDate = view.findViewById(R.id.editTextDate);
        textViewHeader.setText("Nova Coleta");
        editTextDate.setVisibility(View.VISIBLE);

        editTextDate.setText(EcoletaUtils.getCurrentDate());
        Button btnAvancar = view.findViewById(R.id.btnNewPontoColetaAvancar);
        btnAvancar.setText("Finalizar");
    }

    private void buttonClickAvancar(View view) {
        view.findViewById(R.id.btnNewPontoColetaAvancar).setOnClickListener(v -> {
            //validacao do campos
            goToNextScreen(0,0);
        });
    }

    private void buttonClickLocalizacaoAtual(View view) {
        view.findViewById(R.id.btnUseCurrentLocation).setOnClickListener(v -> {
            requestCurrentLocation();
        });
    }

    private void requestCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        goToNextScreen(location.getLatitude(), location.getLongitude());
                    } else {
                        startLocationUpdates();
                    }
                });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    goToNextScreen(location.getLatitude(), location.getLongitude());
                }
            }
        }, null);
    }


    private void focusChangeCepEditText(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String cep = editText.getText().toString();
                if (!cep.isEmpty()) {
                    clearEditText();
                    fetchAndPopulateAddress(cep.replaceAll("-", ""));
                }
            }
        });
    }

    private void fetchAndPopulateAddress(final String cep) {
        executorService.execute(() -> {
            final ViaCepResponseDto response = viaCepExecutor.searchLocation(cep);
            mainHandler.post(() -> {
                if (response != null) {
                    estadoEditText.setText(response.getUf());
                    cidadeEditText.setText(response.getLocalidade());
                    bairroEditText.setText(response.getBairro());
                    logradouroEditText.setText(response.getLogradouro());
                }
            });
        });
    }

    private void goToNextScreen(double latitude, double longitude) {
        Coleta pontoColeta = new Coleta();
        pontoColeta.setCep(cepEditText.getText().toString());
        pontoColeta.setUf(estadoEditText.getText().toString());
        pontoColeta.setLocalidade(cidadeEditText.getText().toString());
        pontoColeta.setBairro(bairroEditText.getText().toString());
        pontoColeta.setLogradouro(logradouroEditText.getText().toString());
        pontoColeta.setNumero(numeroEditText.getText().toString());
        pontoColeta.setComplemento(complementoEditText.getText().toString());
        pontoColeta.setHorarioAtendimento(editTextDate.getText().toString());
        pontoColeta.setUsuario(new Usuario().convertFromFirebaseUser(Objects.requireNonNull(mAuth.getCurrentUser())));
        pontoColeta.setTipoColeta(ColetaType.COLETA);
        pontoColeta.setLatitude(latitude);
        pontoColeta.setLongitude(longitude);

        try {
            coletaService.save(pontoColeta);
            Toast.makeText(requireActivity(), "Cadastro Ponto Coleta realizado com sucesso", Toast.LENGTH_SHORT).show();
            EcoletaUtils.replaceFragment(NavigationHome.class, false, requireActivity().getSupportFragmentManager(),true);
        }catch (Exception e) {
            Toast.makeText(requireActivity(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEditText() {
        estadoEditText.setText("");
        cidadeEditText.setText("");
        bairroEditText.setText("");
        logradouroEditText.setText("");
        complementoEditText.setText("");
        numeroEditText.setText("");
        editTextDate.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
