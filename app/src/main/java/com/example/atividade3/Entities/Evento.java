package com.example.atividade3.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "eventos")
public class Evento {
    @PrimaryKey(autoGenerate = true)
    private int idEvento;
    private String nome;
    private String local;
    private Date data;
    private String descricao;
    private int numIngressos;
    private byte[] foto;

    public Evento(int idEvento, String nome, String local, Date data, String descricao, int numIngressos, byte[] foto) {
        this.idEvento = idEvento;
        this.nome = nome;
        this.local = local;
        this.data = data;
        this.descricao = descricao;
        this.numIngressos = numIngressos;
        this.foto = foto;
    }


    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNumIngressos() {
        return numIngressos;
    }

    public void setNumIngressos(int numIngressos) {
        this.numIngressos = numIngressos;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
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
