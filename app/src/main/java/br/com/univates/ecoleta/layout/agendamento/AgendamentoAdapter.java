package br.com.univates.ecoleta.layout.agendamento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import br.com.univates.ecoleta.R;
import br.com.univates.ecoleta.db.entity.Coleta;
import br.com.univates.ecoleta.db.service.ColetaService;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.ColetaViewHolder> {
    private final List<Coleta> coletaList;
    private final ColetaService coletaService;
    private OnItemClickListener onItemClickListener;
    private FragmentActivity fragmentActivity;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public static class ColetaViewHolder extends RecyclerView.ViewHolder {
        public TextView tvColetaId, tvAddress, tvDate, textViewUsuario;
        public ImageButton btnDelete, btnEdit;

        public ColetaViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvColetaId = itemView.findViewById(R.id.textViewId);
            tvAddress = itemView.findViewById(R.id.textViewAddress);
            tvDate = itemView.findViewById(R.id.textViewDate);
            textViewUsuario = itemView.findViewById(R.id.textViewUsuario);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(position);
                    }
                }
            });
        }
    }

    public AgendamentoAdapter(List<Coleta> coletas, ColetaService service, FragmentActivity activity) {
        coletaList = coletas;
        coletaService = service;
        fragmentActivity = activity;
    }

    @NonNull
    @Override
    public ColetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.agendamento_item, parent, false);
        return new ColetaViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ColetaViewHolder holder, int position) {
        Coleta currentItem = coletaList.get(position);
        holder.tvColetaId.setText(String.format("#%s", currentItem.getId().replaceAll("-", "").substring(5)));
        holder.tvAddress.setText(String.format("%s, %s", currentItem.getLogradouro(), currentItem.getNumero()));
        holder.tvDate.setText(currentItem.getHorarioAtendimento());
        holder.textViewUsuario.setText(currentItem.getUsuario().getNome());

        holder.btnEdit.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEditClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coletaList.size();
    }

    public void removeItem(int position) {
        Coleta coleta = coletaList.get(position);
        coletaService.delete(coleta.getId());
        coletaList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(int position, Coleta updatedColeta) {
        coletaList.set(position, updatedColeta);
        notifyItemChanged(position);
    }
}
