package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tiendas")
public class Tienda {
    @PrimaryKey
    public int id_tiendas;
    public String nombre_tienda;
    public String direccion;
    public String telefono;
    public String nombre_encargado;

    public int getId_tiendas() {
        return id_tiendas;
    }

    public void setId_tiendas(int id_tiendas) {
        this.id_tiendas = id_tiendas;
    }

    public String getNombre_tienda() {
        return nombre_tienda;
    }

    public void setNombre_tienda(String nombre_tienda) {
        this.nombre_tienda = nombre_tienda;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre_encargado() {
        return nombre_encargado;
    }

    public void setNombre_encargado(String nombre_encargado) {
        this.nombre_encargado = nombre_encargado;
    }
}
