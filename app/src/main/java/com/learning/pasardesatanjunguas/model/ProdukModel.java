package com.learning.pasardesatanjunguas.model;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

import com.learning.pasardesatanjunguas.util.ServerAPI;

public class ProdukModel {

    String kode_prdk, nama_prdk, hrga_prdk, desk_prdk, foto_prdk;

    public ProdukModel() {
    }

    public ProdukModel(String kode_prdk, String nama_prdk, String hrga_prdk,
                       String desk_prdk, String foto_prdk) {
        this.kode_prdk = kode_prdk;
        this.nama_prdk = nama_prdk;
        this.hrga_prdk = hrga_prdk;
        this.desk_prdk = desk_prdk;
        this.foto_prdk = foto_prdk;
    }

    public String getKode_prdk() {
        return kode_prdk;
    }

    public void setKode_prdk(String kode_prdk) {
        this.kode_prdk = kode_prdk;
    }

    public String getNama_prdk() {
        return nama_prdk;
    }

    public void setNama_prdk(String nama_prdk) {
        this.nama_prdk = nama_prdk;
    }

    public String getHrga_prdk() {
        return hrga_prdk;
    }

    public void setHrga_prdk(String hrga_prdk) {
        this.hrga_prdk = hrga_prdk;
    }

    public String getDesk_prdk() {
        return desk_prdk;
    }

    public void setDesk_prdk(String desk_prdk) {
        this.desk_prdk = desk_prdk;
    }

    public String getFoto_prdk() {
        return ServerAPI.IP + "img/produk/" + foto_prdk;
    }

    public void setFoto_prdk(String foto_prdk) {
        this.foto_prdk = foto_prdk;
    }
}
