package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "linea_productos")
public class LineaProducto {
    @PrimaryKey
    public int id_linea_productos;

    public String linea_productos;
    public String descripcion;

    public int getId_linea_productos() {
        return id_linea_productos;
    }

    public void setId_linea_productos(int id_linea_productos) {
        this.id_linea_productos = id_linea_productos;
    }

    public String getLinea_productos() {
        return linea_productos;
    }

    public void setLinea_productos(String linea_productos) {
        this.linea_productos = linea_productos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}