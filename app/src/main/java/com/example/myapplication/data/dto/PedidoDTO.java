package com.example.myapplication.data.dto;

import com.example.myapplication.data.entities.Pedido;

import java.util.List;

public class PedidoDTO {
    public List<Pedido> pedidos;

    public static class Pedido {
        public int id_pedido;
        public String fecha;
        public int id_tienda;
        public int id_usuario;
        public String total;
        public String latitud;   // agrega
        public String longitud;  // agrega

        public Usuario id_usuario_usuario;
        public Tienda id_tienda_tienda;
        public List<DetallePedido> detalle_pedidos;

        // Helpers
        public String getNombreUsuario() {
            return id_usuario_usuario != null ? id_usuario_usuario.nombre + " " + id_usuario_usuario.apellido : "Desconocido";
        }

        public String getNombreTienda() {
            return id_tienda_tienda != null ? id_tienda_tienda.nombre_tienda : "Desconocida";
        }
    }

    public static class Usuario {
        public int id_usuarios;
        public String nombre;
        public String apellido;
        public int id_rol;
    }

    public static class Tienda {
        public int id_tiendas;
        public String nombre_tienda;
    }

    public static class DetallePedido {
        public int id_detalle_pedidos;
        public int id_producto;
        public int id_pedido;
        public int cantidad;
        public String precio_unitario;
        public String subtotal;
        public Producto id_producto_producto;
    }

    public static class Producto {
        public int id_producto;
        public String nombre_producto;
        public String precio;
    }

}
