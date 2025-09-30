package com.example.myapplication.data.dto;

import java.util.List;

public class PedidoPostDTO {
    public String fecha;
    public int tienda;
    public double latitud;
    public double longitud;
    public int id_usuario;
    public List<Detalle> detalle;

    public static class Detalle {
        public int id_producto;
        public int cantidad;

        public Detalle(int id_producto, int cantidad) {
            this.id_producto = id_producto;
            this.cantidad = cantidad;
        }
    }
}
