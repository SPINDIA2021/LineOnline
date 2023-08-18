package com.spindia.rechargeapp.data;




import com.spindia.rechargeapp.network.NetworkCall;
import com.spindia.rechargeapp.network.ServiceCallBack;

import okhttp3.RequestBody;
import retrofit2.http.Part;


public interface DataSource {

    void getCategory(ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

    void saveRetry(String txnid, ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

    void getPanForm(String username,String password, ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);


}

