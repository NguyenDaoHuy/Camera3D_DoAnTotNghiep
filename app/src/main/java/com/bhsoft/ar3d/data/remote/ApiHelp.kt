@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.bhsoft.ar3d.data.remote

import android.util.Log
import com.bhsoft.ar3d.BuildConfig
import com.google.gson.*
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
//******************************
//******************************
//***** Create by cuongpq  *****
//******************************
//******************************


object ApiHelp {
    private fun createOkHttp(timeOut: Int = 4, vararg header: Pair<String, String>): OkHttpClient {
        val client = OkHttpClient.Builder()
        val logging = LoggingInterceptor.Builder()
//            .loggable(Constants.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .addHeader("version", BuildConfig.VERSION_NAME)
        if (header.isNotEmpty()) {
            for (s in header) {
                logging.addHeader(s.first, s.second)
            }
        }
        client.addInterceptor(logging.build())
        return client.retryOnConnectionFailure(true)
            .connectTimeout(timeout = timeOut.toLong(), TimeUnit.MINUTES)
            .readTimeout(timeout = timeOut.toLong(), TimeUnit.MINUTES)
            .writeTimeout(timeout = timeOut.toLong(), TimeUnit.MINUTES)
            .build()
    }

    fun createRetrofit(
        timeOut: Int = 4,
        endpoint: String,
        formatDate: List<String>,
        vararg header: Pair<String, String>
    ): Retrofit {
        var gson = GsonBuilder()
        for (format in formatDate) {
            gson = gson.registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): Date {
                    try {
                        return SimpleDateFormat(format, Locale.getDefault()).parse(json!!.asString)
                    } catch (e: ParseException) {
                        Log.e("deserialize", e.message.toString())
                    }
                    throw  JsonParseException("Unparseable date: \"" + json!!.asString + "  format: " + format)
                }
            })
        }
        return Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(createOkHttp(timeOut, *header))
            .build()
    }
}