package com.example.atividade3.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.atividade3.DAO.EventoDAO;
import com.example.atividade3.DAO.UsuarioDAO;
import com.example.atividade3.Entities.Evento;
import com.example.atividade3.Entities.Usuario;


@Database(entities = {Evento.class, Usuario.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract UsuarioDAO usuarioDAO();
    public abstract EventoDAO eventoDAO();
    private static AppDatabase db;


    public static AppDatabase getDatabase(Context context){
        if(db==null){
            db= Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "PassAPP").
                    allowMainThreadQueries().build();
        }
        return db;
    }

}
