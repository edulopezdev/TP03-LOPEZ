package com.ejemplo.tp3_android.ui.salir;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.ejemplo.tp3_android.databinding.FragmentSalirBinding;

public class SalirFragment extends Fragment {

    private FragmentSalirBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SalirViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SalirViewModel.class);

        binding = FragmentSalirBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        muestraDialog(getActivity());
        return root;
    }

    private void muestraDialog(Context donde){
        new AlertDialog.Builder(donde)
                .setTitle("Salir")
                .setMessage("Cerrar aplicacion?")
                .setIcon(android.R.drawable.ic_lock_power_off)
                .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface di,int i){
                        getActivity().finish();
                    }
                })
                .setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface di,int i){
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }).show();}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}