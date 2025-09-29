package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.DetallesActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.entities.Pedido;

import java.util.List;

public class PedidoAdapter extends BaseAdapter {
    Context context;
    public List<Pedido> listaPedidos;

    public PedidoAdapter(Context context, List<Pedido> listaPedidos) {
        this.context = context;
        this.listaPedidos = listaPedidos;
    }

    @Override
    public int getCount() {
        return listaPedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        }
        TextView txtIdPedido = convertView.findViewById(R.id.txtIdPedido);
        TextView txtFechaPedido = convertView.findViewById(R.id.txtFechaPedido);
        TextView txtTiendaPedido = convertView.findViewById(R.id.txtTiendaPedido);
        TextView txtUsuarioPedido = convertView.findViewById(R.id.txtUsuarioPedido);
        TextView txtTotalPedido = convertView.findViewById(R.id.txtTotalPedido);

        Pedido pedidoItem = listaPedidos.get(position);

        txtIdPedido.setText("Pedido #"+String.valueOf(pedidoItem.getId_pedido()));
        txtFechaPedido.setText("Fecha: " +String.valueOf(pedidoItem.getFecha()));
        txtTiendaPedido.setText("Tienda: " +String.valueOf(pedidoItem.getNombre_tienda()));
        txtUsuarioPedido.setText("Usuario: " +String.valueOf(pedidoItem.getNombre_usuario()));
        txtTotalPedido.setText("Total: Q" +String.valueOf(pedidoItem.getTotal()));

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetallesActivity.class);
            intent.putExtra("ID_PEDIDO", pedidoItem.getId_pedido());

            context.startActivity(intent);
        });


        return convertView;
    }
}
