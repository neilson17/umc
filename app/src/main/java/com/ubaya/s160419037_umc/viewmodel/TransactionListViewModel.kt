package com.ubaya.s160419037_umc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.s160419037_umc.GlobalData
import com.ubaya.s160419037_umc.model.Transaction

class TransactionListViewModel(application: Application): AndroidViewModel(application) {
    val transactionsLiveData = MutableLiveData<ArrayList<Transaction>>()
    val transactionsLoadErrorLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun refresh(username: String){
        transactionsLoadErrorLiveData.value = false
        loadingLiveData.value = true

        queue = Volley.newRequestQueue(getApplication())
        val url = GlobalData.php_base_url + "transactions.php?username=$username"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                val sType = object : TypeToken<ArrayList<Transaction>>() {}.type
                val result = Gson().fromJson<ArrayList<Transaction>>(it, sType)
                transactionsLiveData.value = result
                loadingLiveData.value = false
                Log.d("showVolley", it)
            },
            {
                loadingLiveData.value = false
                transactionsLoadErrorLiveData.value = true
                Log.d("errorVolley", it.toString())
            }
        ).apply {
            tag = "TAG"
        }
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}