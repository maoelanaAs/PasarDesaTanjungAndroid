package com.learning.pasardesatanjunguas.model;

/**
 * Dibuat oleh Maulana As'an, 10/01/23
 */

public class HistoryModel {

    String no_nota, tgl_jual, nama_user, total_jual, ongkir, grand_total, status;

    public HistoryModel() {

    }

    public HistoryModel(String no_nota, String tgl_jual, String nama_user, String total_jual,
                        String ongkir, String grand_total, String status) {
        this.no_nota = no_nota;
        this.tgl_jual = tgl_jual;
        this.nama_user = nama_user;
        this.total_jual = total_jual;
        this.ongkir = ongkir;
        this.grand_total = grand_total;
        this.status = status;
    }

    public String getNo_nota() {
        return no_nota;
    }

    public void setNo_nota(String no_nota) {
        this.no_nota = no_nota;
    }

    public String getTgl_jual() {
        return tgl_jual;
    }

    public void setTgl_jual(String tgl_jual) {
        this.tgl_jual = tgl_jual;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getTotal_jual() {
        return total_jual;
    }

    public void setTotal_jual(String total_jual) {
        this.total_jual = total_jual;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
