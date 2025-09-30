package com.example.myapplication.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.dao.DetallePedidoDao;
import com.example.myapplication.data.dao.LineaProductoDao;
import com.example.myapplication.data.dao.PedidoDao;
import com.example.myapplication.data.dao.ProductoDao;
import com.example.myapplication.data.dao.TiendaDao;
import com.example.myapplication.data.entities.DetallePedido;
import com.example.myapplication.data.entities.LineaProducto;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;


@Database(entities = {Tienda.class, Pedido.class, Producto.class, LineaProducto.class, DetallePedido.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public abstract TiendaDao tiendaDao();
    public abstract PedidoDao pedidoDao();
    public abstract ProductoDao productoDao();

    public abstract LineaProductoDao lineaDao();

    public abstract DetallePedidoDao detallePedidoDao();
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
