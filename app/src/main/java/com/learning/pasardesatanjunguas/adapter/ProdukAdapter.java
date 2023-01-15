package com.learning.pasardesatanjunguas.adapter;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.learning.pasardesatanjunguas.R;
import com.learning.pasardesatanjunguas.model.ProdukModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukHoler> {

    private ArrayList<ProdukModel> dataList;
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public ProdukAdapter(ArrayList<ProdukModel> dataList) {
        this.dataList = dataList;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ProdukHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item, parent, false);

        return new ProdukHoler(view);
    }

    @Override
    public void onBindViewHolder(ProdukHoler holder, int position) {

        ProdukModel data = dataList.get(position);
        Double harga = Double.valueOf(data.getHrga_prdk());

        holder.produkNama.setText(data.getNama_prdk());
        holder.produkHrga.setText(rpFormat.format(harga));
        Glide.with(holder.produkFoto)
                .load(data.getFoto_prdk())
                .placeholder(R.drawable.loading)
                .error(R.drawable.foto_kosong)
                .into(holder.produkFoto);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ProdukHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView produkNama, produkHrga;
        private ImageView produkFoto;

        public ProdukHoler(@NonNull View itemView) {
            super(itemView);

            produkNama = itemView.findViewById(R.id.productNama);
            produkHrga = itemView.findViewById(R.id.productHrga);
            produkFoto = itemView.findViewById(R.id.productFoto);

            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            produkNama.setOnClickListener(this);
            produkFoto.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
