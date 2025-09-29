package com.ejemplo.tp3_android.ui.cargar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ejemplo.tp3_android.databinding.FragmentCargarBinding;

public class CargarFragment extends Fragment {

    private FragmentCargarBinding binding;
    private CargarViewModel vm; //variable de tipo ViewModel

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CargarViewModel.class);

        binding = FragmentCargarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Observar el LiveData de mensajes de error y éxito
        vm.getErrorMsg().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                binding.tvError.setText(mensaje);
                binding.tvError.setVisibility(View.VISIBLE);

                // Cambiar color según el tipo de mensaje
                if (mensaje.startsWith("✓")) {
                    // Mensaje de éxito - color verde
                    binding.tvError.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    // Mensaje de error - color rojo
                    binding.tvError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            } else {
                binding.tvError.setVisibility(View.GONE);
            }
        });

        // Observar cuando se guarda exitosamente para limpiar campos
        vm.getProductoGuardado().observe(getViewLifecycleOwner(), guardado -> {
            if (guardado != null && guardado) {
                limpiarCampos();
            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = binding.etCodigo.getText().toString();
                String descripcion = binding.etDescripcion.getText().toString();
                String precioCad = binding.etPrecio.getText().toString();

                vm.cargarProducto(codigo, descripcion, precioCad);
            }
        });

        return root;
    }

    private void limpiarCampos() {
        binding.etCodigo.setText("");
        binding.etDescripcion.setText("");
        binding.etPrecio.setText("");

        // Opcional: quitar el foco de los campos
        binding.etDescripcion.clearFocus();
        binding.etCodigo.clearFocus();
        binding.etPrecio.clearFocus();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}