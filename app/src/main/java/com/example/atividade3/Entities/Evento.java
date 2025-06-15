package com.example.atividade3.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity()
public class Evento {
    @PrimaryKey(autoGenerate = true)
    private int idEvento;
    private String nome;
    private String local;
    private Date data;
    private String descricao;
    private int numIngressos;
    private byte[] foto;

    public Evento(int idEvento, String nome, String local, Date data, String descricao, int numIngressos) {
        this.idEvento = idEvento;
        this.nome = nome;
        this.local = local;
        this.data = data;
        this.descricao = descricao;
        this.numIngressos = numIngressos;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "nome='" + nome + '\'' +
                ", local='" + local + '\'' +
                ", data=" + data +
                ", descricao='" + descricao + '\'' +
                ", numIngressos=" + numIngressos +
                '}';
    }
}
