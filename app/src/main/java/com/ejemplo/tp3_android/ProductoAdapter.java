package com.ejemplo.tp3_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ejemplo.tp3_android.modelo.Producto;

import java.util.List;


public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.viewHolderProducto> {

    private List<Producto> listado;

    public ProductoAdapter(List<Producto> listado) {
        this.listado = listado;
    }

    @NonNull
    @Override
    public viewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new viewHolderProducto(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderProducto holder, int position) {
        Producto ProductoActual = listado.get(position);

        holder.codigo.setText("Código: " + ProductoActual.getCodigo());
        holder.descripcion.setText("Descripción: " + ProductoActual.getDescripcion());
        holder.precio.setText("Precio: " + ProductoActual.getPrecio());
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    public class viewHolderProducto extends RecyclerView.ViewHolder {

        TextView codigo, descripcion, precio;

        public viewHolderProducto(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.tvCodigo);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            precio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
