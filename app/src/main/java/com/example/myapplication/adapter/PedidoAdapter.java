package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        txtIdPedido.setText(String.valueOf(pedidoItem.getId_pedido()));
        txtFechaPedido.setText(String.valueOf(pedidoItem.getFecha()));
        txtTiendaPedido.setText(String.valueOf(pedidoItem.getId_tienda()));
        txtUsuarioPedido.setText(String.valueOf(pedidoItem.getId_usuario()));
        txtTotalPedido.setText(String.valueOf(pedidoItem.getTotal()));

        return convertView;
    }
}
