package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;

import java.util.List;

@Dao
public interface TiendaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTienda(Tienda tienda);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTiendas(List<Tienda> tiendas);

    @Query("SELECT * FROM tiendas")
    LiveData<List<Tienda>> obtenerTiendas();

}
