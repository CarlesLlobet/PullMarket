package xyz.carlesllobet.pullmarket.UI;

/**
 * Created by CarlesLlobet on 01/02/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.carlesllobet.pullmarket.Domain.Llista;
import xyz.carlesllobet.pullmarket.Domain.Product;
import xyz.carlesllobet.pullmarket.R;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.AdapterViewHolder>{

    ArrayList<Product> products;
    ArrayList<Integer> cants;

    public HomeAdapter() {
        products = Llista.getInstance().getAllProducts();
        cants = Llista.getInstance().getAllCants();
    }

    @Override
    public HomeAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.list_row, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.AdapterViewHolder adapterViewholder, int position) {
        adapterViewholder.name.setText(products.get(position).getName());
        adapterViewholder.price.setText(products.get(position).getPreu().toString());
        adapterViewholder.desc.setText(products.get(position).getDescription());
        adapterViewholder.foto.setImageURI(products.get(position).getFoto());
        adapterViewholder.cant.setText(cants.get(position).toString());
    }

    @Override
    public int getItemCount(){ return products.size(); }

    public Product getItem(int pos){ return products.get(pos);}

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView foto;
        public TextView name;
        public TextView desc;
        public TextView price;
        public TextView cant;
        public View v;
        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.foto = (ImageView) itemView.findViewById(R.id.foto);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.desc = (TextView) itemView.findViewById(R.id.description);
            this.price = (TextView) itemView.findViewById(R.id.preu);
            this.cant = (TextView) itemView.findViewById(R.id.cantitat);
        }
    }
}