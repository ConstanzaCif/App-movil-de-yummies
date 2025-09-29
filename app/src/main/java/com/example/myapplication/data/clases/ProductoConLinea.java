package com.example.myapplication.data.clases;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.myapplication.data.entities.LineaProducto;
import com.example.myapplication.data.entities.Producto;

public class ProductoConLinea {
    @Embedded
    public Producto producto;

    @Relation(
            parentColumn = "id_linea",
            entityColumn = "id_linea_productos"
    )
    public LineaProducto lineaProducto;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LineaProducto getLineaProducto() {
        return lineaProducto;
    }

    public void setLineaProducto(LineaProducto lineaProducto) {
        this.lineaProducto = lineaProducto;
    }
}
