package com.example.atividade3.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.atividade3.Entities.Usuario;

@Dao
public interface UsuarioDAO{
    @Insert
    void inserir(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE login = :login LIMIT 1")
    Usuario buscarUsuarioPorLogin(String login);

    @Query("SELECT * FROM usuarios WHERE idUsuario = :id")
    Usuario buscarUsuario(int id);
}
