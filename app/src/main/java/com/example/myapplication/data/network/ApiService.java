package com.example.myapplication.data.network;


import androidx.room.Insert;

import com.example.myapplication.data.dto.LineaDTO;
import com.example.myapplication.data.dto.LoginDto;
import com.example.myapplication.data.dto.PedidoDTO;
import com.example.myapplication.data.dto.PedidoRequestDTO;
import com.example.myapplication.data.dto.ProductoDTO;
import com.example.myapplication.data.dto.TiendaDTO;
import com.example.myapplication.data.dto.UsuarioDTO;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.entities.Tienda;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiService {
    @GET("tiendas")
    Call<TiendaDTO> obtenerTiendas();

//    @POST("tiendas")
//    Call<String> crearTienda(@Body Tienda tienda);


    @POST("usuarios/login/movil")
    Call<UsuarioDTO> login(@Body LoginDto loginDto);

    @POST("pedidos")
    Call<PedidoDTO> obtenerPedidos(@Body PedidoRequestDTO pedidoRequestDTO);

    @GET("productos")
    Call<ProductoDTO> obtenerProductos();

    @GET("lineaProductos")
    Call <LineaDTO> obtenerLineas();
}
