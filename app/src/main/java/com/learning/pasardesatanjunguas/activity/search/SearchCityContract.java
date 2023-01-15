package com.learning.pasardesatanjunguas.activity.search;

import android.widget.EditText;

import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;
import com.learning.pasardesatanjunguas.data.city.DataCity;
import com.learning.pasardesatanjunguas.data.city.ResponseCity;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

public interface SearchCityContract {
    interface View{
        void onLoadingSearch(boolean loading);
        void onResultSearch(ResponseCity response);
        void onErrorSearch();

        void onResultInstantSearch(List<DataCity> data);

        void showMessage(String msg);
    }

    interface Presenter{
        void getCity();
        void instantSearch(EditText editText, List<DataCity> data);
        void searchCity(List<DataCity> data, String keyword);
        DisposableObserver<TextViewTextChangeEvent> observer(List<DataCity> data);
    }
}
