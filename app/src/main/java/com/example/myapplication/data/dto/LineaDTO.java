package com.example.myapplication.data.dto;

import com.example.myapplication.data.entities.LineaProducto;
import com.example.myapplication.data.entities.Producto;

import java.util.List;

public class LineaDTO {
    public boolean success;
    public String message;
    public List<LineaProducto> data;
}
