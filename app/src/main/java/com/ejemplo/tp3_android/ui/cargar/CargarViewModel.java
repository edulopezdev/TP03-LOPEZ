package com.ejemplo.tp3_android.ui.cargar;

import static com.ejemplo.tp3_android.MainActivity.productos;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ejemplo.tp3_android.MainActivity;
import com.ejemplo.tp3_android.modelo.Producto;

import java.util.List;

public class CargarViewModel extends AndroidViewModel {

    private MutableLiveData<List<Producto>> mutable;
    private MutableLiveData<String> errorMsg = new MutableLiveData<>("");
    private MutableLiveData<Boolean> productoGuardado = new MutableLiveData<>();

    public CargarViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData getMutable(){
        if (mutable==null){
            mutable=new MutableLiveData<>();
        }
        return mutable;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public LiveData<Boolean> getProductoGuardado() {
        return productoGuardado;
    }

    public void cargarProducto(String codigo, String descripcion, String precioCad){
        errorMsg.setValue("");
        productoGuardado.setValue(false); // Resetear el estado

        if (codigo.isEmpty() || descripcion.isEmpty() || precioCad.isEmpty()) {
            errorMsg.setValue("Todos los campos son obligatorios");
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioCad);
        } catch (NumberFormatException e) {
            errorMsg.setValue("El precio debe ser un número válido");
            return;
        }

        Producto p = new Producto(codigo, descripcion, precio);

        if (productos.contains(p)){
            errorMsg.setValue("El código del producto está duplicado");
            return;
        }

        productos.add(p);
        errorMsg.setValue("✓ Producto cargado correctamente");
        productoGuardado.setValue(true); // Indicar que se guardó exitosamente
    }
}