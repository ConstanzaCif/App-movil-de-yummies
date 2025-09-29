package com.example.myapplication.data.dto;

import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;

import java.util.List;

public class ProductoDTO {
    public boolean success;
    public String message;
    public List<Producto> data;
}
