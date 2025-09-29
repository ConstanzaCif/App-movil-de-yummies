package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.myapplication.data.clases.ProductoConLinea;
import com.example.myapplication.data.entities.LineaProducto;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;

import java.util.List;

@Dao
public interface LineaProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarLineaProducto(LineaProducto lineaProducto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarLineas(List<LineaProducto> lineaProductos);

    @Query("SELECT * FROM linea_productos")
    LiveData<List<LineaProducto>> obtenerLineas();
    @Query("SELECT * FROM linea_productos WHERE id_linea_productos = :id")
    LiveData<LineaProducto> obtenerLineaPorId(int id);

}
