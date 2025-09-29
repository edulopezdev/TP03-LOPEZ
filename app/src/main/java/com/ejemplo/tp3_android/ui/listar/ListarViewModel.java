package com.ejemplo.tp3_android.ui.listar;

import static com.ejemplo.tp3_android.MainActivity.productos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ejemplo.tp3_android.modelo.Producto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListarViewModel extends ViewModel {
    private MutableLiveData<List<Producto>> mutable = new MutableLiveData<>();

    // Flag para saber si ya se han cargado datos
    private boolean datosYaCargados = false;

    public LiveData<List<Producto>> getMutable() {
        return mutable;
    }

    // Método para reiniciar completamente el estado del ViewModel
    public void reiniciar() {
        datosYaCargados = false;
        mutable = new MutableLiveData<>();
    }

    public void cargarLista() {
        try {
            // Verificar que la lista de productos no sea nula
            if (productos == null) {
                mutable.postValue(new ArrayList<>());
                return;
            }

            // Crear una copia de la lista para evitar modificar la original
            List<Producto> productosOrdenados = new ArrayList<>(productos);

            // Ordenar la copia
            Collections.sort(productosOrdenados, new Comparator<Producto>() {
                @Override
                public int compare(Producto o1, Producto o2) {
                    if (o1 == null || o2 == null || o1.getDescripcion() == null || o2.getDescripcion() == null) {
                        return 0;
                    }
                    return o1.getDescripcion().compareTo(o2.getDescripcion());
                }
            });

            // Usar postValue en lugar de setValue para garantizar que funcione desde cualquier hilo
            mutable.postValue(productosOrdenados);
            datosYaCargados = true;
        } catch (Exception e) {
            // En caso de cualquier error, mostrar lista vacía
            mutable.postValue(new ArrayList<>());
        }
    }

    // Nuevo método que garantiza que se cargan los datos al menos una vez
    public void asegurarCargaDeDatos() {
        if (!datosYaCargados) {
            cargarLista();
        }
    }
}