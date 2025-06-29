package com.example.atividade3.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "compras",
        foreignKeys = {
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "idUsuario",
                        childColumns = "idUsuario",
                        onDelete = ForeignKey.CASCADE),

                @ForeignKey(entity = Evento.class,
                        parentColumns = "idEvento",
                        childColumns = "idEvento",
                        onDelete = ForeignKey.CASCADE)
        })
public class Compra {
    @PrimaryKey(autoGenerate = true)
    private int idCompra;

    private int idUsuario;
    private int idEvento;
    private int quantidade;
    private long dataCompra;

    public Compra(int idUsuario, int idEvento, int quantidade) {
        this.idUsuario = idUsuario;
        this.idEvento = idEvento;
        this.quantidade = quantidade;
        this.dataCompra = System.currentTimeMillis();
    }

    public int getIdCompra() { return idCompra; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdEvento() { return idEvento; }
    public int getQuantidade() { return quantidade; }
    public long getDataCompra() { return dataCompra; }

    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }
    public void setDataCompra(long dataCompra) { this.dataCompra = dataCompra; }
}