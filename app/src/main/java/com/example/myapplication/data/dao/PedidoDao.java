package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.data.entities.Pedido;

import java.util.List;

@Dao
public interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarPedido(Pedido pedido);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarPedidos(List<Pedido> pedidos);

    @Query("SELECT * FROM pedidos")
    LiveData<List<Pedido>> obtenerPedidos();

    @Query("SELECT * FROM pedidos WHERE pendiente = 1")
    List<Pedido> obtenerPedidosPendientesSync();


}
