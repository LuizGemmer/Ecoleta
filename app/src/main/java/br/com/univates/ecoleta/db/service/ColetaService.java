package br.com.univates.ecoleta.db.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.univates.ecoleta.db.dao.ColetaDAO;
import br.com.univates.ecoleta.db.entity.Coleta;

public class ColetaService {

    ColetaDAO dao;

    public ColetaService() {
        dao = new ColetaDAO();
    }

    public void save(Coleta coleta) {
        dao.salvar(coleta);
    }

    public void update(Coleta coleta){
        dao.atualizar(coleta);
    }

    public void delete(String idColeta){
        dao.remover(idColeta);
    }

    public Coleta findById(String id) {
        if(id == null && id.isEmpty() || id.isBlank())
            return null;

        return dao.findById(id);
    }

    public List<Coleta> findAll() {
        try {
            return dao.findAll();
        } catch (ExecutionException | InterruptedException e) {
            return new ArrayList<>();
        }
    }
}
