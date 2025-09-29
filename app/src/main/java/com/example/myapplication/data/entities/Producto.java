package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "productos",
        foreignKeys = @ForeignKey(
                entity = LineaProducto.class,
                parentColumns = "id_linea_productos",
                childColumns = "id_linea",
                onDelete = ForeignKey.CASCADE
        )
)
public class Producto {
    @PrimaryKey
    public int id_producto;

    public String nombre_producto;
    public String descripcion;
    public String imagen_url;
    public double precio;
    public double gramaje;
    public int id_linea;

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getGramaje() {
        return gramaje;
    }

    public void setGramaje(double gramaje) {
        this.gramaje = gramaje;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }
}