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

import com.learning.pasardesatanjunguas.R;
import com.learning.pasardesatanjunguas.model.HistoryModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHoler> {

    private ArrayList<HistoryModel> dataList;
    NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public HistoryAdapter(ArrayList<HistoryModel> dataList) {
        this.dataList = dataList;
    }

    @Override
    public HistoryHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_history, parent, false);

        return new HistoryHoler(view);
    }

    @Override
    public void onBindViewHolder(HistoryHoler holder, int position) {

        HistoryModel data = dataList.get(position);
        double totals = Double.parseDouble(data.getTotal_jual());
        double ongkis = Double.parseDouble(data.getOngkir());
        double grands = Double.parseDouble(data.getGrand_total());

        holder.noNota.append(data.getNo_nota());
        holder.tglJual.append(data.getTgl_jual());
        holder.namaUser.append(data.getNama_user());
        holder.totalJual.append(rpFormat.format(totals));
        holder.ongkir.append(rpFormat.format(ongkis));
        holder.grandTotal.append(rpFormat.format(grands));
        holder.statusByr.append(data.getStatus());
        if (data.getStatus().equals("Lunas")) {
            holder.statusImg.setImageResource(R.drawable.ic_berhasil);
        } else {

            holder.statusImg.setImageResource(R.drawable.ic_gagal);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class HistoryHoler extends RecyclerView.ViewHolder {

        private TextView noNota, tglJual, namaUser, totalJual, ongkir, grandTotal, statusByr;
        private ImageView statusImg;

        public HistoryHoler(@NonNull View itemView) {
            super(itemView);

            noNota = itemView.findViewById(R.id.noNota);
            tglJual = itemView.findViewById(R.id.tglJual);
            namaUser = itemView.findViewById(R.id.namaUser);
            totalJual = itemView.findViewById(R.id.totalJual);
            ongkir = itemView.findViewById(R.id.ongkir);
            grandTotal = itemView.findViewById(R.id.grandTotal);
            statusByr = itemView.findViewById(R.id.statusByr);
            statusImg = itemView.findViewById(R.id.statusImg);
        }
    }
}
