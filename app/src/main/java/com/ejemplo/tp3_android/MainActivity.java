package com.ejemplo.tp3_android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ejemplo.tp3_android.modelo.Producto;
import com.ejemplo.tp3_android.ui.listar.ListarFragment;
import com.ejemplo.tp3_android.ui.listar.ListarViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.ejemplo.tp3_android.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public static ArrayList<Producto> productos=new ArrayList<>();
    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cargar, R.id.nav_listar, R.id.nav_salir)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Custom navigation listener para manejar la navegación
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_listar) {
                    refreshListarFragment();
                    drawer.closeDrawers();
                    return true;
                } else {
                    // Para otros destinos, usa la navegación estándar
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    drawer.closeDrawers();
                    return handled;
                }
            }
        });
    }

    // Método para asegurarse de que el ListarFragment se carga correctamente
    private void refreshListarFragment() {
        // Si estamos en el mismo destino, necesitamos forzar la recarga
        boolean estamosEnListar = false;
        if (navController.getCurrentDestination() != null) {
            estamosEnListar = navController.getCurrentDestination().getId() == R.id.nav_listar;
        }

        // Si ya estamos en el fragmento de listar, forzar recarga
        if (estamosEnListar) {
            try {
                // Intentar obtener el fragmento actual y forzar recarga
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);

                if (navHostFragment != null) {
                    Fragment currentFragment = navHostFragment.getChildFragmentManager()
                            .getPrimaryNavigationFragment();

                    if (currentFragment instanceof ListarFragment) {
                        // Forzar recarga de datos directamente en el fragmento
                        ((ListarFragment) currentFragment).forzarRecargaDeDatos();
                    }
                }

                // También forzar recarga desde el ViewModel
                ListarViewModel listarViewModel = new ViewModelProvider(this)
                        .get(ListarViewModel.class);
                listarViewModel.cargarLista();
            } catch (Exception e) {
                // En caso de error, navegamos nuevamente al fragmento
                navController.navigate(R.id.nav_listar);
            }
        } else {
            // Si no estamos en el fragmento, navegar a él normalmente
            navController.navigate(R.id.nav_listar);
        }
    }

    // Método para actualizar la selección en el menú de navegación
    public void actualizarSeleccionMenu(int itemId) {
        NavigationView navigationView = binding.navView;
        Menu menu = navigationView.getMenu();

        // Desmarcar todos los ítems actuales
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }

        // Marcar el ítem correspondiente al ID proporcionado
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}