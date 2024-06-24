package br.com.univates.ecoleta.db.entity;
import java.util.Date;

public class Agendamento {
    private String id;
    private String idColeta;
    private Date dataHoraColeta;

    public Agendamento() {
        // Default constructor required for Firebase
    }

    public Agendamento(String id, String idColeta, Date dataHoraColeta) {
        this.id = id;
        this.idColeta = idColeta;
        this.dataHoraColeta = dataHoraColeta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdColeta() {
        return idColeta;
    }

    public void setIdColeta(String idColeta) {
        this.idColeta = idColeta;
    }

    public Date getDataHoraColeta() {
        return dataHoraColeta;
    }

    public void setDataHoraColeta(Date dataHoraColeta) {
        this.dataHoraColeta = dataHoraColeta;
    }
}