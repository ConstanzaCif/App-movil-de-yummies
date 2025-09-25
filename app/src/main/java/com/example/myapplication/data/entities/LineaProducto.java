package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "linea_productos")
public class LineaProducto {
    @PrimaryKey
    public int id_linea_productos;

    public String linea_productos;
    public String descripcion;
}