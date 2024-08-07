package br.com.univates.ecoleta.db.entity;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import br.com.univates.ecoleta.db.type.TipoLocalizacaoType;

public class Usuario implements Serializable {
    public String id;
    public String nome;
    public String email;
    public TipoLocalizacaoType tipoLocalizacao;
    public double longitude;
    public double latitude;
    public String cep;
    public String estado;
    public String cidade;
    public String logradouro;
    public String bairro;
    public String complemento;
    public String ibge;
    public String numero;
    public String urlFoto;
    public String queColetar;
    public Calendar horarioColeta;
    public String descricao;

    // Constructor (empty for Firestore)
    public Usuario() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoLocalizacaoType getTipoLocalizacao() {
        return tipoLocalizacao;
    }

    public void setTipoLocalizacao(TipoLocalizacaoType tipoLocalizacao) {
        this.tipoLocalizacao = tipoLocalizacao;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getQueColetar() {
        return queColetar;
    }

    public void setQueColetar(String queColetar) {
        this.queColetar = queColetar;
    }

    public Calendar getHorarioColeta() {
        return horarioColeta;
    }

    public void setHorarioColeta(Calendar horarioColeta) {
        this.horarioColeta = horarioColeta;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario convertFromFirebaseUser(FirebaseUser firebaseUser) {
        Usuario user = new Usuario();
        user.setId(firebaseUser.getUid());
        user.setNome(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        user.setUrlFoto(firebaseUser.getPhotoUrl().toString());
        return user;
    }
}