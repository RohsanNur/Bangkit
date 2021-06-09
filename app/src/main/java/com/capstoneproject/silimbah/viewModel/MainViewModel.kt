package com.capstoneproject.silimbah.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.silimbah.api.RetrofitClient
import com.capstoneproject.silimbah.model.DataCraft
import com.capstoneproject.silimbah.model.craftResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    val listCraft = MutableLiveData<ArrayList<DataCraft>>()

    fun setSearch(query: String){
        RetrofitClient.appIntance
            .getSearchCraft(query)
            .enqueue(object : Callback<craftResponse>{
                override fun onResponse(
                    call: Call<craftResponse>,
                    response: Response<craftResponse>
                ) {
                    if (response.isSuccessful){
                        listCraft.postValue(response.body()?.data)
                    }
                }

                override fun onFailure(call: Call<craftResponse>, t: Throwable) {
                    Log.d("Failure", t.message)
                }

            })
    }

    fun getsearch(): LiveData<ArrayList<DataCraft>>{
        return listCraft
    }
}