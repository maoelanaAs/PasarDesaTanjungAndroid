package com.learning.pasardesatanjunguas.activity.cekongkir;

import android.os.Handler;

import com.learning.pasardesatanjunguas.data.cost.DataType;
import com.learning.pasardesatanjunguas.data.cost.ResponseCost;
import com.learning.pasardesatanjunguas.network.Api;
import com.learning.pasardesatanjunguas.network.ApiEndpoint;
import com.learning.pasardesatanjunguas.network.Helper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CekOngkirPresenter implements CekOngkirContract.Presenter {

    CekOngkirContract.View view;
    ApiEndpoint endpoint;

    String origin = "";
    String destination = "";
    int weight = 0;

    List<DataType> output = new ArrayList<>();
    List<String> courier = new ArrayList<>();

    public CekOngkirPresenter(CekOngkirContract.View view) {
        this.view = view;
        endpoint = Api.getUrl().create(ApiEndpoint.class);
    }

    @Override
    public void getJNE() {
        output.clear();
        courier.clear();
        endpoint.postCost(origin, destination, weight, "jne")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseCost>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(ResponseCost responseCost) {
                        for (DataType data : responseCost.getRajaongkir()
                                .getResults().get(0).getCosts()) {
                            output.add(data);
                            courier.add("JNE");
                        }
                        Helper.jne = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void getTIKI() {
        endpoint.postCost(origin, destination, weight, "tiki")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseCost>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ResponseCost responseCost) {
                        for (DataType data : responseCost.getRajaongkir()
                                .getResults().get(0).getCosts()) {
                            output.add(data);
                            courier.add("TIKI");
                        }
                        Helper.tiki = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void getPOS() {
        endpoint.postCost(origin, destination, weight, "pos")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResponseCost>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ResponseCost responseCost) {
                        for (DataType data : responseCost.getRajaongkir()
                                .getResults().get(0).getCosts()) {
                            output.add(data);
                            courier.add("POS");
                        }

                        Helper.pos = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void setupENV(String origin, String destination, int weight) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;

        view.onLoadingCost(true, 25);

        getJNE();
        getPOS();
        getTIKI();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Helper.jne && Helper.pos && Helper.tiki) {
                    view.onLoadingCost(false, 100);
                    view.onResultCost(output, courier);
                }
            }
        }, 4000L);

    }
}
