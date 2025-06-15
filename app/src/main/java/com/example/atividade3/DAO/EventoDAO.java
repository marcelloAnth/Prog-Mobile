package com.example.atividade3.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.atividade3.Entities.Evento;

@Dao
public interface EventoDAO {
    @Insert
    void inserir(Evento evento);

    @Query("SELECT * FROM eventos WHERE idEvento = :id")
    Evento buscarEvento(int id);

}
