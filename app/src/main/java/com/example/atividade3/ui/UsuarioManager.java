package com.example.atividade3.ui;

import android.content.Context;

import com.example.atividade3.DAO.UsuarioDAO;
import com.example.atividade3.Database.AppDatabase;
import com.example.atividade3.Entities.Usuario;
import com.example.atividade3.Security.SecurityUtils;

public class UsuarioManager {

    public final UsuarioDAO usuarioDAO;

    public UsuarioManager(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.usuarioDAO = db.usuarioDAO();
    }

    public void cadastrarUsuario(String login, String senha, byte[] foto) {
        new Thread(() -> {
            try {
                String senhaCriptografada = SecurityUtils.hashPassword(senha);
                Usuario usuario = new Usuario(0, login, senhaCriptografada, foto);
                usuarioDAO.inserir(usuario);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void atualizarUsuario(int id, String novaSenha, byte[] novaFoto) {
        new Thread(() -> {
            Usuario usuario = usuarioDAO.buscarUsuario(id);
            if (usuario != null) {
                if (novaSenha != null && !novaSenha.isEmpty()) {
                    usuario.setHashedPassword(SecurityUtils.hashPassword(novaSenha));
                }
                usuarioDAO.update(usuario);
            }
        }).start();
    }

    public void buscarUsuario(int id, UsuarioCallback callback) {
        new Thread(() -> {
            Usuario usuario = usuarioDAO.buscarUsuario(id);
            callback.onUsuarioCarregado(usuario);
        }).start();
    }

    public interface UsuarioCallback {
        void onUsuarioCarregado(Usuario usuario);
    }
}
