package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myapplication.data.clases.ProductoConLinea;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;

import java.util.List;

@Dao
public interface ProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarProducto(Producto producto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarProductos(List<Producto> productos);

    @Query("SELECT * FROM productos")
    LiveData<List<Producto>> obtenerProductos();
    @Query("SELECT * FROM productos WHERE id_producto = :id")
    LiveData<Producto> obtenerProductoPorId(int id);

    @Transaction
    @Query("SELECT * FROM productos WHERE id_producto = :id")
    LiveData<ProductoConLinea> obtenerProductoConLinea(int id);
}
