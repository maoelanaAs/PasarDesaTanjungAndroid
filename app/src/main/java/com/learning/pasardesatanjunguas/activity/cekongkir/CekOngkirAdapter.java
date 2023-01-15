package com.learning.pasardesatanjunguas.activity.cekongkir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.pasardesatanjunguas.R;
import com.learning.pasardesatanjunguas.activity.CheckOutActivity;
import com.learning.pasardesatanjunguas.data.cost.DataType;
import com.learning.pasardesatanjunguas.network.Helper;

import java.util.List;

public class CekOngkirAdapter extends RecyclerView.Adapter<CekOngkirAdapter.ViewHolder> {

    Context context;
    List<DataType> data;
    List<String> courier;
    int imgLogo;
    public static double ongkir = 0;

    public CekOngkirAdapter(Context context, List<DataType> data, List<String> courier) {
        this.context = context;
        this.data = data;
        this.courier = courier;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String strLogo = courier.get(position);

        if (strLogo.equals("JNE"))
            imgLogo = R.drawable.logo_jne;
        else if (strLogo.equals("POS"))
            imgLogo = R.drawable.logo_pos;
        else if (strLogo.equals("TIKI"))
            imgLogo = R.drawable.logo_tiki;

        holder.imgLogoKurir.setImageResource(imgLogo);
        holder.tvType.setText("Jenis Layanan : " + data.get(position).getService());
        holder.tvPrice.setText(Helper.formatRupiah(data.get(position).getCost().get(0).getValue()));
        holder.tvEst.setText(data.get(position).getCost().get(0).getEtd() + " Hari");
        holder.cvKirim.setOnClickListener(v -> {
            ongkir = data.get(position).getCost().get(0).getValue();
            Intent intent = new Intent(context, CheckOutActivity.class);
            intent.putExtra("ongkir", ongkir);
            ((Activity) context).startActivity(intent);
            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEst, tvPrice, tvType;
        ImageView imgLogoKurir;
        CardView cvKirim;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEst = itemView.findViewById(R.id.tvEst);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvType = itemView.findViewById(R.id.tvType);
            imgLogoKurir = itemView.findViewById(R.id.imgLogo);
            cvKirim = itemView.findViewById(R.id.cvKirim);
        }
    }
}
