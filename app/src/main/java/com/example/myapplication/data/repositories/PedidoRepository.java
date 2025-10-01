package com.example.myapplication.data.repositories;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.data.database.AppDatabase;
import com.example.myapplication.data.dto.LoginDto;
import com.example.myapplication.data.dto.PedidoDTO;
import com.example.myapplication.data.dto.PedidoPostDTO;
import com.example.myapplication.data.dto.PedidoRequestDTO;
import com.example.myapplication.data.entities.DetallePedido;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.network.ApiClient;
import com.example.myapplication.data.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoRepository {
    private final AppDatabase db;
    private final ApiService apiService;
    private final Context context;
    public PedidoRepository(Context context){
        this.context = context;
        db = AppDatabase.getInstance(context);
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public LiveData<List<Pedido>> sincronizarPedidos(int usuario){
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(usuario);
        apiService.obtenerPedidos(pedidoRequestDTO).enqueue(new Callback<PedidoDTO>() {
            @Override
            public void onResponse(Call<PedidoDTO> call, Response<PedidoDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PedidoDTO.Pedido> pedidosResponse = response.body().pedidos;

                    if (pedidosResponse != null && !pedidosResponse.isEmpty()) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                // Mapeamos los pedidos
                                List<Pedido> pedidos = mapearPedidos(pedidosResponse);
                                db.pedidoDao().insertarPedidos(pedidos);

                                // Ahora insertamos los detalles de cada pedido
                                List<DetallePedido> listaDetalles = new ArrayList<>();
                                for (int i = 0; i < pedidosResponse.size(); i++) {
                                    PedidoDTO.Pedido pDto = pedidosResponse.get(i);
                                    Pedido pedidoGuardado = pedidos.get(i); // mismo orden
                                    for (PedidoDTO.DetallePedido dDto : pDto.detalle_pedidos) {
                                        DetallePedido detalle = new DetallePedido();
                                        detalle.id_pedido = pedidoGuardado.id_pedido; // FK
                                        detalle.id_producto = dDto.id_producto;
                                        detalle.cantidad = dDto.cantidad;
                                        detalle.precio_unitario = Double.parseDouble(dDto.precio_unitario);
                                        detalle.subtotal = Double.parseDouble(dDto.subtotal);
                                        listaDetalles.add(detalle);
                                        Log.d("PedidoRepository", "Detalle: PedidoID=" + detalle.id_pedido +
                                                " ProductoID=" + detalle.id_producto +
                                                " Cantidad=" + detalle.cantidad +
                                                " Subtotal=" + detalle.subtotal);

                                    }
                                }
                                db.detallePedidoDao().insertarDetalles(listaDetalles);

                                Log.d("PedidoRepository", "Pedidos y detalles insertados en DB: " + pedidos.size());


                            } catch (Exception e) {
                                Log.e("PedidoRepository", "Error al insertar pedidos y detalles: " + e.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("PedidoRepository", "Respuesta no exitosa o body nulo");
                }
            }

            @Override
            public void onFailure(Call<PedidoDTO> call, Throwable throwable) {
                Log.e("PedidoRepository", "Error en API: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
        return db.pedidoDao().obtenerPedidos();
    }
    private List<Pedido> mapearPedidos(List<PedidoDTO.Pedido> pedidosDto) {
        List<Pedido> lista = new ArrayList<>();
        for (PedidoDTO.Pedido p : pedidosDto) {
            Pedido pedido = new Pedido();
            pedido.id_pedido = p.id_pedido;
            pedido.fecha = p.fecha;
            pedido.id_tienda = p.id_tienda;
            pedido.id_usuario = p.id_usuario;
            pedido.total = Double.parseDouble(p.total);
            pedido.latitud = Double.parseDouble(p.latitud);
            pedido.longitud = Double.parseDouble(p.longitud);
            pedido.nombre_tienda = p.getNombreTienda();
            pedido.nombre_usuario = p.getNombreUsuario();
            pedido.pendiente = false;
            lista.add(pedido);
        }
        return lista;
    }
    public LiveData<List<Pedido>> obtenerPedidos(){return db.pedidoDao().obtenerPedidos();}

    public void guardarPedido(PedidoPostDTO pedidoDTO) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (tieneInternet()) {
                // Aquí intentar enviar al API
                try {
                    Response<PedidoDTO> response = apiService.crearPedido(pedidoDTO).execute();
                    if (!response.isSuccessful()) {
                        insertarPedidoOffline(pedidoDTO);
                    }
                } catch (Exception e) {
                    insertarPedidoOffline(pedidoDTO);
                }
            } else {
                // No hay internet → guardar en SQLite
                insertarPedidoOffline(pedidoDTO);
            }
        });
    }



    public LiveData<List<DetallePedido>> obtenerDetallesPorPedido(int idPedido) {
        return db.detallePedidoDao().obtenerDetallesPorPedido(idPedido);
    }
    public void crearPedido(PedidoPostDTO pedidoPostDTO, Callback<PedidoDTO> callback) {
        if (tieneInternet()) {
            apiService.crearPedido(pedidoPostDTO).enqueue(callback);
        } else {
            // Guardar en SQLite como pendiente
            Executors.newSingleThreadExecutor().execute(() -> {
                Pedido pedido = new Pedido();
                pedido.fecha = pedidoPostDTO.fecha;
                pedido.id_tienda = pedidoPostDTO.tienda;
                pedido.latitud = pedidoPostDTO.latitud;
                pedido.longitud = pedidoPostDTO.longitud;
                pedido.id_usuario = pedidoPostDTO.id_usuario;
                pedido.pendiente = true;
                db.pedidoDao().insertarPedido(pedido);
            });
        }
    }

    private boolean tieneInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            } else {
                return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
            }
        }
        return false;
    }

    public interface PedidoCallback {
        void onResult(boolean success);
    }

    public void crearPedido(PedidoPostDTO pedido, PedidoCallback callback) {
        apiService.crearPedido(pedido).enqueue(new Callback<PedidoDTO>() {
            @Override
            public void onResponse(Call<PedidoDTO> call, Response<PedidoDTO> response) {
                callback.onResult(response.isSuccessful() && response.body() != null);
            }

            @Override
            public void onFailure(Call<PedidoDTO> call, Throwable t) {
                callback.onResult(false);
            }
        });
    }
    private void insertarPedidoOffline(PedidoPostDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        pedido.fecha = pedidoDTO.fecha;
        pedido.id_tienda = pedidoDTO.tienda;
        pedido.id_usuario = pedidoDTO.id_usuario;
        pedido.latitud = pedidoDTO.latitud;
        pedido.longitud = pedidoDTO.longitud;
        pedido.pendiente = true;

        long idPedido = db.pedidoDao().insertarPedido(pedido);

        List<DetallePedido> detalles = new ArrayList<>();
        for (PedidoPostDTO.Detalle d : pedidoDTO.detalle) {
            DetallePedido detalle = new DetallePedido();
            detalle.id_pedido = (int) idPedido;
            detalle.id_producto = d.id_producto;
            detalle.cantidad = d.cantidad;
            detalles.add(detalle);
        }

        db.detallePedidoDao().insertarDetalles(detalles);
        Log.d("PedidoRepository", "Pedido offline guardado con ID: " + idPedido);
    }

    public void enviarPedidosPendientes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (!tieneInternet()) return;

            List<Pedido> pendientes = db.pedidoDao().obtenerPedidosPendientesSync();
            for (Pedido p : pendientes) {
                PedidoPostDTO dto = mapearPedidoPostDTO(p);
                try {
                    Response<PedidoDTO> response = apiService.crearPedido(dto).execute();
                    if (response.isSuccessful()) {
                        // Marcar como enviado
                        p.pendiente = false;
                        db.pedidoDao().insertarPedido(p);
                        Log.d("PedidoRepository", "Pedido pendiente enviado ID: " + p.id_pedido);
                    }
                } catch (Exception e) {
                    Log.e("PedidoRepository", "Error enviando pedido pendiente ID: " + p.id_pedido);
                }
            }
        });
    }
    private PedidoPostDTO mapearPedidoPostDTO(Pedido pedido) {
        PedidoPostDTO dto = new PedidoPostDTO();
        dto.id_usuario = pedido.id_usuario;
        dto.tienda = pedido.id_tienda;
        dto.fecha = pedido.fecha;
        dto.latitud = pedido.latitud;
        dto.longitud = pedido.longitud;

        List<DetallePedido> detalles = db.detallePedidoDao().obtenerDetallesPorPedidoSync(pedido.id_pedido);
        List<PedidoPostDTO.Detalle> listaDetalles = new ArrayList<>();
        for (DetallePedido d : detalles) {
            listaDetalles.add(new PedidoPostDTO.Detalle(d.id_producto, d.cantidad));
        }
        dto.detalle = listaDetalles;

        return dto;
    }




}
