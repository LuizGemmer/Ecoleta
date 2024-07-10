package br.com.univates.ecoleta.layout.agendamento;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;
import br.com.univates.ecoleta.db.service.ColetaService;
import br.com.univates.ecoleta.utils.EcoletaUtils;

public class AgendamentoEditModal extends DialogFragment {

    private Coleta coleta;
    private ColetaService coletaService;

    public interface OnColetaUpdatedListener {
        void onColetaUpdated(Coleta coleta);
    }

    public AgendamentoEditModal(Coleta coleta, ColetaService coletaService) {
        this.coleta = coleta;
        this.coletaService = coletaService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agendamento_item_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editTextDate = view.findViewById(R.id.editTextDate);
        EditText editTextEndereco = view.findViewById(R.id.editTextEndereco);
        Button btnSave = view.findViewById(R.id.btnSave);

        editTextDate.setText(coleta.getHorarioAtendimento().isEmpty() ? EcoletaUtils.getCurrentDate() : coleta.getHorarioAtendimento());
        editTextEndereco.setText(coleta.getLogradouro().isEmpty() || coleta.getNumero().isEmpty() ? "" : String.format("%s,%s", coleta.getLogradouro(), coleta.getNumero()));

        btnSave.setOnClickListener(v -> {
            String newDate = editTextDate.getText().toString();
            String newLogradouro = editTextEndereco.getText().toString();
            if(!newDate.isEmpty() ){
                coleta.setHorarioAtendimento(newDate);
            }
            if (newLogradouro.contains(",")) {
                coleta.setLogradouro(newLogradouro.split(",")[0]);
                coleta.setNumero(newLogradouro.split(",")[1]);
            }
            saveChangesAndCloseModal();
        });
    }

    private void saveChangesAndCloseModal() {
        coletaService.update(coleta);
        if (getTargetFragment() instanceof OnColetaUpdatedListener) {
            ((OnColetaUpdatedListener) getTargetFragment()).onColetaUpdated(coleta);
        }
        dismiss();
    }
}
