package com.example.myapplication.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.dao.PedidoDao;
import com.example.myapplication.data.dao.TiendaDao;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;

@Database(entities = {Tienda.class, Pedido.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public abstract TiendaDao tiendaDao();
    public abstract PedidoDao pedidoDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "yummiesv2.db"
                    )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
