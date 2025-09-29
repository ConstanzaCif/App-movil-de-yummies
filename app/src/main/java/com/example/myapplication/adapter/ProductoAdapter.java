package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.entities.Producto;

import java.util.List;

public class ProductoAdapter extends BaseAdapter {
    Context context;
    public List<Producto> listaProductos;

    public ProductoAdapter(Context context,List<Producto> listaProductos){
        this.context = context;
        this.listaProductos = listaProductos;
    }


    @Override
    public int getCount() {
        return listaProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        ImageView imgProducto = convertView.findViewById(R.id.imgProducto2);
        TextView txtNombre = convertView.findViewById(R.id.txtNombreProducto2);
        TextView txtCodigo = convertView.findViewById(R.id.txtCodigoProducto);
        TextView txtPrecio = convertView.findViewById(R.id.txtPrecioProducto);

        Producto productoItem = listaProductos.get(position);

        txtNombre.setText(productoItem.getNombre_producto());
        txtCodigo.setText("Código: " + productoItem.getId_producto());
        txtPrecio.setText("Precio: Q" + productoItem.getPrecio());

        String imageUrl = productoItem.getImagen_url();
        Log.d("Imagen", "Imagen en la bd "+imageUrl);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (!imageUrl.startsWith("http")) {
                imageUrl = "http://10.0.2.2:3001" + imageUrl;
                Log.d("Imagen", "Imagen en la bd "+imageUrl);
            }
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_products)
                    .error(R.drawable.ic_error)
                    .into(imgProducto);
        } else {
            imgProducto.setImageResource(R.drawable.ic_products);
        }


        return convertView;
    }

}
