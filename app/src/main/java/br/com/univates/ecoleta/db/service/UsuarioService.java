package br.com.univates.ecoleta.db.service;

import br.com.univates.ecoleta.db.dao.UsuarioDAO;
import br.com.univates.ecoleta.db.entity.Usuario;

public class UsuarioService {

    UsuarioDAO usuarioDao;

    public UsuarioService() {
        usuarioDao = new UsuarioDAO();
    }

    public void save(Usuario usuario) {
       usuarioDao.salvar(usuario);
    }

    public void update(Usuario usuario){
        usuarioDao.atualizar(usuario);
    }

    public void delete(String idUsuario){
        usuarioDao.remover(idUsuario);
    }

    public Usuario findById(String id) {
        if(id == null && id.isEmpty() || id.isBlank())
            return null;

        return usuarioDao.findById(id);
    }
}
