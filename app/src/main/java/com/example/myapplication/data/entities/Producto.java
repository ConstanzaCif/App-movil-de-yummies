package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class Producto {
    @PrimaryKey
    public int id_producto;

    public String nombre_producto;
    public String descripcion;
    public String imagen_url;
    public double precio;
    public double gramaje;
    public int id_linea;
}