package com.example.myapplication.data.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "detalle_pedidos",
        foreignKeys = @ForeignKey(
                entity = Pedido.class,
                parentColumns = "id_local",
                childColumns = "id_pedido_local",
                onDelete = CASCADE
        )
)
public class DetallePedido {
    @PrimaryKey(autoGenerate = true)
    public int id_detalle_local;

    public int id_producto;
    public int id_pedido_local;
    public int cantidad;
    public double precio_unitario;
    public double subtotal;
}