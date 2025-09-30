package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.data.dto.PedidoDTO;
import com.example.myapplication.data.entities.DetallePedido;

import java.util.List;

@Dao
public interface DetallePedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarDetalle(DetallePedido detalle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarDetalles(List<DetallePedido> detalles);

    @Query("SELECT * FROM detalle_pedidos WHERE id_pedido = :idPedido")
    LiveData<List<DetallePedido>> obtenerDetallesPorPedido(int idPedido);

    @Query("SELECT * FROM detalle_pedidos")
    LiveData<List<DetallePedido>> obtenerTodos();

    @Query("SELECT * FROM detalle_pedidos WHERE id_pedido = :idPedido")
    List<DetallePedido> obtenerDetallesPorPedidoSync(int idPedido);
}
