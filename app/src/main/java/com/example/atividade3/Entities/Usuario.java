package com.example.atividade3.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "usuarios")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    private int idUsuario;
    private String login;
    private String hashedPassword;
    byte[] foto;

    public Usuario(int idUsuario, String login, String hashedPassword, byte[] foto) {
        this.idUsuario = idUsuario;
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", login='" + login + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", foto=" + Arrays.toString(foto) +
                '}';
    }
}
