package com.example.wordsearch.data
import android.util.Log
import com.example.wordsearch.model.WordItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.datamuse.com/"
private const val TAG = "WordProvider"
class WordProvider {

    private val retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create<WordApi>()
    }

    fun fetchWord(query : String) {
        retrofit.fetchWords(query).enqueue(object : Callback<List<WordItem>>{
            override fun onResponse(
                call: Call<List<WordItem>>,
                response: Response<List<WordItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    Log.d(TAG, "Response : ${response.body()}" )
//                    cb.onDataFetchedSuccess(response.body()!!)
                }else{
//                    cb.onDataFetchedFailed()
                }
            }

            override fun onFailure(call: Call<List<WordItem>>, t: Throwable) {
                Log.e(TAG, "Error loading images", t)
//                cb.onDataFetchedFailed()
            }
        })
    }

}
