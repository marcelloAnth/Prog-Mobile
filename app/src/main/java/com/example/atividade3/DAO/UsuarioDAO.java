package com.example.atividade3.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.atividade3.Entities.Usuario;

@Dao
public interface UsuarioDAO{
    @Insert
    void inserir(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE idUsuario = :id")
    Usuario buscarUsuario(int id);
}
