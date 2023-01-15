package com.learning.pasardesatanjunguas.activity.cekongkir;

import com.learning.pasardesatanjunguas.data.cost.DataType;

import java.util.List;

public interface CekOngkirContract {

    interface Presenter{
        void getJNE();
        void getTIKI();
        void getPOS();
        void setupENV(String origin, String destination, int weight);
    }

    interface View{
        void onLoadingCost(boolean loadng, int progress);
        void onResultCost(List<DataType> data, List<String> courier);
        void onErrorCost();

        void showMessage(String msg);
        String getOrigin();
        String getDestination();
    }
}
