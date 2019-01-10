package moezbenselem.cheaper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Moez on 17/06/2018.
 */

public class RecyclerProduct extends RecyclerView.Adapter {

    Context context;
    ArrayList<Product> listRows;
    HolderProduct viewHolder;

    public RecyclerProduct(ArrayList<Product> listRows,Context context) {
        this.context = context;
        this.listRows = listRows;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false);
        viewHolder = new HolderProduct(v,listRows,context);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        viewHolder = (HolderProduct) holder;
        viewHolder.item_ville.append(listRows.get(position).ville);
        viewHolder.item_prix.append(String.valueOf(listRows.get(position).prix)+ " DT");
        viewHolder.item_magasin.append(listRows.get(position).magasin);
        viewHolder.itemn_lebelle.append(listRows.get(position).lebelle);

    }

    @Override
    public int getItemCount() {
        return listRows.size();
    }
}