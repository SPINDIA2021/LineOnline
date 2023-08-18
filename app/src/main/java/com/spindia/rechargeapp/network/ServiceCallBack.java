package com.spindia.rechargeapp.network;


import retrofit2.Response;


public interface ServiceCallBack
{
    public void onSuccess(int tag, Response<BaseResponse> baseResponse);
    public void onFail(int requestTag, Throwable t);
}
