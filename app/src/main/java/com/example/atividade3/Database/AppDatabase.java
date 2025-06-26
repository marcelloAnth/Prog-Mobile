package com.example.atividade3.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.atividade3.DAO.UsuarioDAO;
import com.example.atividade3.DAO.EventoDAO;
import com.example.atividade3.Entities.Usuario;
import com.example.atividade3.Entities.Evento;
import com.example.atividade3.R;

import org.mindrot.jbcrypt.BCrypt;

@Database(entities = {Evento.class, Usuario.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDAO usuarioDAO();
    public abstract EventoDAO eventoDAO();
    private static AppDatabase db;

    public static AppDatabase getDatabase(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "PassAPP")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase dbSqlite) {
                            super.onCreate(dbSqlite);
                            new Thread(() -> {
                                UsuarioDAO usuarioDAO = getDatabase(context).usuarioDAO();
                                EventoDAO eventoDAO = getDatabase(context).eventoDAO();

                                String hashedAdminPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
                                Usuario admin = new Usuario(0, "admin", hashedAdminPassword, null);
                                usuarioDAO.inserir(admin);

                                String hashedUserPassword = BCrypt.hashpw("user123", BCrypt.gensalt());
                                Usuario user = new Usuario(0, "user", hashedUserPassword, null);
                                usuarioDAO.inserir(user);

                                byte[] imagem = com.example.atividade3.Utils.ImageUtils.drawableToByteArray(context, R.drawable.grafo_facom);

                                Evento evento = new Evento(0, "Chá na Facom", "FACOM - UFMS",
                                        new java.util.Date(), "Evento tradicional da FACOM", 50, imagem);

                                eventoDAO.inserir(evento);
                            }).start();
                        }
                    })
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
    }
}
