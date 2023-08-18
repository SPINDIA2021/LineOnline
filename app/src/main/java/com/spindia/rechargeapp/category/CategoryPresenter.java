package com.spindia.rechargeapp.category;

import android.content.Context;

import androidx.annotation.NonNull;

import com.myapp.onlysratchapp.category.CategoryResponse;
import com.spindia.rechargeapp.authentication.response.BaseCheckMobileResponse;
import com.spindia.rechargeapp.authentication.response.CheckMobileResponse;
import com.spindia.rechargeapp.data.DataSource;
import com.spindia.rechargeapp.data.remote.RemoteDataSource;
import com.spindia.rechargeapp.network.BaseResponse;
import com.spindia.rechargeapp.network.IApi;
import com.spindia.rechargeapp.network.NetworkCall;
import com.spindia.rechargeapp.network.ServiceCallBack;
import com.spindia.rechargeapp.network.Util;

import java.util.ArrayList;

import retrofit2.Response;


public class CategoryPresenter implements CategoryContract.Presenter, ServiceCallBack {
    private final DataSource loginDataSource;
    private final CategoryContract.View mLoginView;
    private Context context;


    public CategoryPresenter(@NonNull RemoteDataSource listDataSource, CategoryContract.View loginFragment) {
        loginDataSource = listDataSource;
        mLoginView = loginFragment;
        mLoginView.setPresenter(this);

    }


    @Override
    public void onSuccess(int tag, Response<BaseResponse> baseResponse) {
        if (tag == IApi.COMMON_TAG) {
            BaseResponse response = baseResponse.body();
            if (response != null) {
                if (response.isResponseStatus() == true) {
                    //Toast.makeText(context, response.getResponseMessage(), Toast.LENGTH_LONG).show();

                    ArrayList<CategoryResponse> userData = (ArrayList<CategoryResponse>) response.getResponsePacket();


                    mLoginView.categoryResponse(userData);


                } else {

                    Util.showAlertBox(context, response.getResponseMessage(), null);
                }

            }

        }


        if (tag == IApi.COMMON_TAG1) {
            BaseResponse response = baseResponse.body();
            if (response != null) {
                if (response.isResponseStatus() == true) {
                    //Toast.makeText(context, response.getResponseMessage(), Toast.LENGTH_LONG).show();

                    String userData = (String) response.getResponsePacket();


                    mLoginView.retryResponse(userData);


                } else {

                    Util.showAlertBox(context, response.getResponseMessage(), null);
                }

            }

        }



    }


    @Override
    public void onFail(int requestTag, Throwable t) {

    }


    @Override
    public void getCategory( Context context) {
        NetworkCall networkCall = new NetworkCall(context);
        this.context = context;
        loginDataSource.getCategory(this, networkCall);
    }

    @Override
    public void saveRetry(String txnid, Context context) {
        NetworkCall networkCall = new NetworkCall(context);
        this.context = context;
        loginDataSource.saveRetry(txnid,this, networkCall);
    }




}
