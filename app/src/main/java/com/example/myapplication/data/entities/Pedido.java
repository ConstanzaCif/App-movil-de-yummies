package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidos")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    public int id_local;  // id interno SQLite
    public Integer id_pedido; // id real del servidor, puede ser null si está pendiente

    public String fecha;
    public int id_tienda;
    public String ubicacion;
    public int id_usuario;
    public double total;
    public boolean pendiente;
}