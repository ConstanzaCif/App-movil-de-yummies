    package com.example.myapplication.data.repositories;

    import android.content.Context;
    import android.util.Log;

    import androidx.lifecycle.LiveData;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.myapplication.data.dao.TiendaDao;
    import com.example.myapplication.data.database.AppDatabase;
    import com.example.myapplication.data.dto.TiendaDTO;
    import com.example.myapplication.data.entities.Tienda;
    import com.example.myapplication.data.network.ApiClient;
    import com.example.myapplication.data.network.ApiService;

    import java.util.List;
    import java.util.concurrent.Executor;
    import java.util.concurrent.Executors;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class TiendaRepository {
        private final AppDatabase db;
        private final ApiService apiService;

        public TiendaRepository(Context context) {
            db = AppDatabase.getInstance(context);
            apiService = ApiClient.getClient().create(ApiService.class);
        }
        int tamanio;
        public void sincronizarTiendas() {
            apiService.obtenerTiendas().enqueue(new Callback<TiendaDTO>() {
                @Override
                public void onResponse(Call<TiendaDTO> call, Response<TiendaDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Tienda> tiendas = response.body().data;
                        if (tiendas != null && !tiendas.isEmpty()) {
                            Log.d("TiendaRepository", "Tiendas recibidas de API: " + tiendas.size());
                            Tienda primera = tiendas.get(0);
                            Log.d("TiendaRepository", "Tienda ejemplo: id=" + primera.getId_tiendas() + ", nombre=" + primera.getNombre_tienda());
                            Executors.newSingleThreadExecutor().execute(() -> {
                                try {
                                    db.tiendaDao().insertarTiendas(tiendas);
                                    Log.d("TiendaRepository", "Tiendas insertadas en DB: " + tiendas.size());
                                    List<Tienda> tiendasEnDB = db.tiendaDao().obtenerTiendas().getValue();
                                    Log.d("TiendaRepository", "Tiendas en DB después de insert: " + (tiendasEnDB != null ? tiendasEnDB.size() : 0));
                                } catch (Exception e) {
                                    Log.e("TiendaRepository", "Error al insertar/leer tiendas: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            Log.e("TiendaRepository", "Lista de tiendas vacía o nula de la API");
                        }
                    } else {
                        Log.e("TiendaRepository", "Respuesta no exitosa o body nulo: " + (response != null ? response.code() : "Respuesta nula"));
                    }
                }
                @Override
                public void onFailure(Call<TiendaDTO> call, Throwable throwable) {
                    Log.e("TiendaRepository", "Error en API: " + throwable.getMessage());
                    throwable.printStackTrace();
                }
            });
        }
        public LiveData<List<Tienda>> obtenerTiendas() {
            return db.tiendaDao().obtenerTiendas();
        }

    }
