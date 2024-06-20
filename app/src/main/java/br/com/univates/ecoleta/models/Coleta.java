package br.com.univates.ecoleta.models;

public class Coleta {
    public String id;
    public String tipoLocalizacao;
    public String tipoColeta;
    public double longitude;
    public double latitude;
    public String cep;
    public String estado;
    public String cidade;
    public String logradouro;
    public String bairro;
    public String complemento;
    public String ibge;

    // Constructor (empty for Firestore)
    public Coleta() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoLocalizacao() {
        return tipoLocalizacao;
    }

    public void setTipoLocalizacao(String tipoLocalizacao) {
        this.tipoLocalizacao = tipoLocalizacao;
    }

    public String getTipoColeta() {
        return tipoColeta;
    }

    public void setTipoColeta(String tipoColeta) {
        this.tipoColeta = tipoColeta;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }
}