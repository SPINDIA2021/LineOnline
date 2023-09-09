package com.spindia.rechargeapp.main_controller

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.gms.security.ProviderInstaller
import com.onesignal.OneSignal
import com.spindia.rechargeapp.network.Preferences




class VolleySingleton : Application() {
   //  val ONESIGNAL_APP_ID = "b1d1d09a-0b72-4340-b8ad-d286fc7c92d0"
   val ONESIGNAL_APP_ID = "785912ce-316b-4849-94ce-3694c5596677"

/*    ZmJkZTFiMTEtZjk4OC00MGU1LThjMjQtOTFjNDRmYjJiNGYw
    Rest API Key*/

    override fun onCreate() {
        super.onCreate()
         updateAndroidSecurityProvider()
        instance = this
        Preferences.init(this)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        // Verbose Logging set to help debug issues, remove before releasing your app.
       // OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
      //  OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        // requestPermission will show the native iOS notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission
      //  OneSignal.Notifications.requestPermission(true)
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {

                return Volley.newRequestQueue(applicationContext)

            }
            return field
        }


    fun <T> addToRequestQueue(request: Request<T>) {

        request.tag = TAG
        requestQueue?.add(request)
    }

    private fun updateAndroidSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: Exception) {
            e.message
        }
    }

    companion object {

        private val TAG = VolleySingleton::class.java.simpleName
        @get:Synchronized
        var instance: VolleySingleton? = null
            private set
    }
}