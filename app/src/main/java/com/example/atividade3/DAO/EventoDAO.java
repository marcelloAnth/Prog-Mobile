package com.example.atividade3.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.atividade3.Entities.Evento;

import java.util.List;

@Dao
public interface EventoDAO {
    @Insert
    void inserir(Evento evento);

    @Query("SELECT * FROM eventos WHERE idEvento = :id")
    Evento buscarEvento(int id);

    @Query("SELECT foto FROM eventos WHERE idEvento = :id")
    byte[] buscarFotoEvento(int id);

    @Query("SELECT idEvento, nome, local, data, descricao, numIngressos FROM eventos WHERE idEvento = :id")
    Evento buscarEventoSemFoto(int id);

    @Query("SELECT idEvento, nome, local, data, descricao, numIngressos FROM eventos")
    List<Evento> buscarEventosSemFoto();

    @Query("UPDATE eventos SET numIngressos = numIngressos - :quantidade WHERE idEvento = :idEvento AND numIngressos >= :quantidade")
    int decrementarIngressos(int idEvento, int quantidade);

    @Delete
    void excluir(Evento eventoSelecionado);
}
