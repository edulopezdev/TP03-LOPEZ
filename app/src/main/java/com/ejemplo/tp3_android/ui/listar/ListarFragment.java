package com.ejemplo.tp3_android.ui.listar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ejemplo.tp3_android.MainActivity;
import com.ejemplo.tp3_android.ProductoAdapter;
import com.ejemplo.tp3_android.databinding.FragmentListarBinding;
import com.ejemplo.tp3_android.modelo.Producto;
import com.ejemplo.tp3_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListarFragment extends Fragment {

    private FragmentListarBinding binding;
    private ListarViewModel lvm;
    private ProductoAdapter adapter;
    private static boolean primeraVez = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar el ViewModel temprano en el ciclo de vida
        lvm = new ViewModelProvider(requireActivity()).get(ListarViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentListarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        setupFab(root);
        observeData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Si es la primera vez o si venimos de otro fragmento, forzar la recarga completa
        if (primeraVez) {
            lvm.reiniciar(); // Reiniciamos completamente el estado
            primeraVez = false;
        }

        // Siempre cargar los datos al crear la vista
        forzarRecargaDeDatos();
    }

    // Método público para que la Activity pueda forzar la recarga
    public void forzarRecargaDeDatos() {
        if (lvm != null) {
            lvm.cargarLista();
        }
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        binding.recyclerListar.setLayoutManager(layoutManager);

        // Crear un adapter vacío inicialmente para evitar nullPointerException
        adapter = new ProductoAdapter(new java.util.ArrayList<>());
        binding.recyclerListar.setAdapter(adapter);
    }

    private void setupFab(View root) {
        FloatingActionButton fabAgregar = root.findViewById(R.id.fabAgregar);
        if (fabAgregar != null) {
            fabAgregar.setOnClickListener(v -> {
                // Navegar a la vista cargar
                Navigation.findNavController(v).navigate(R.id.nav_cargar);

                // Actualizar la selección del menú en MainActivity
                if (getActivity() != null && getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).actualizarSeleccionMenu(R.id.nav_cargar);
                }
            });
        }
    }

    private void observeData() {
        lvm.getMutable().observe(getViewLifecycleOwner(), new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> productos) {
                if (productos != null && binding != null) {
                    adapter = new ProductoAdapter(productos);
                    binding.recyclerListar.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar datos cuando el fragmento vuelve a primer plano
        forzarRecargaDeDatos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}