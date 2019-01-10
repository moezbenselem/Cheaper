package moezbenselem.cheaper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Moez on 17/06/2018.
 */

public class HolderProduct extends RecyclerView.ViewHolder {

    public TextView itemn_lebelle,item_magasin,item_ville,item_prix;
    static Context context;

    public HolderProduct(View itemView, final ArrayList<Product> rows, final Context context){
        super(itemView);
        this.context = context;
        this.item_magasin = (TextView)itemView.findViewById(R.id.item_magasin);
        this.item_prix = (TextView)itemView.findViewById(R.id.item_prix);
        this.item_ville = (TextView)itemView.findViewById(R.id.item_ville);
        this.itemn_lebelle = (TextView)itemView.findViewById(R.id.item_libelle);

    }
}
