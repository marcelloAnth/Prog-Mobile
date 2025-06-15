package com.example.atividade3.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
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
