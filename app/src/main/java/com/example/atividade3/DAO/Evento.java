package com.example.atividade3.DAO;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity()
public class Evento {
    @PrimaryKey(autoGenerate = true)
    int idEvento;
    String nome;
    String local;
    Date data;
    String descricao;
    int numIngressos;

    public Evento(int idEvento, String nome, String local, Date data, String descricao, int numIngressos) {
        this.idEvento = idEvento;
        this.nome = nome;
        this.local = local;
        this.data = data;
        this.descricao = descricao;
        this.numIngressos = numIngressos;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
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
