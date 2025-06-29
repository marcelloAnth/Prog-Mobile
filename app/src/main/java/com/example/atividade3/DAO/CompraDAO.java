package com.example.atividade3.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.atividade3.Entities.Compra;

import java.util.List;

@Dao
public interface CompraDAO {
    @Insert
    void inserir(Compra compra);

    @Query("SELECT * FROM compras WHERE idUsuario = :idUsuario")
    List<Compra> getComprasPorUsuario(int idUsuario);

    @Query("SELECT * FROM compras WHERE idEvento = :idEvento")
    List<Compra> getComprasPorEvento(int idEvento);
}