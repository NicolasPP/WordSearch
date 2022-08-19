package com.nicolas.wordsearch.data
import android.util.Log
import com.nicolas.wordsearch.model.WordItem
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

    fun fetchRelatedWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String){
        retrofit.fetchRelatedWords(inputString, max.toString(), vocab).enqueue(object : Callback<List<WordItem>>{
            override fun onResponse(
                call: Call<List<WordItem>>,
                response: Response<List<WordItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    Log.d(TAG, "fetching related words was successful")
                    Log.d(TAG, "Response body { $inputString }: ${response.body()}")
                    dataResult.onDataFetchedSuccess(response.body()!!)
                }else{
                    dataResult.onDataFetchedFailed()
                }
            }

            override fun onFailure(call: Call<List<WordItem>>, t: Throwable) {
                Log.e(TAG, "Error fetching related words failed", t)
                dataResult.onDataFetchedFailed()
            }
        })
    }
    fun fetchLeadingWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String,
        currentWordIndex : Int
    ){
        retrofit.fetchLeadingWords(inputString, max.toString(), vocab).enqueue(object : Callback<List<WordItem>>{
            override fun onResponse(
                call: Call<List<WordItem>>,
                response: Response<List<WordItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    var r = listOf(response.body()!![0])
                    Log.d(TAG, "fetching related words was successful")
                    Log.d(TAG, "Response body { $inputString }: ${response.body()}")
                    if (response.body()!![0].word in listOf("of", "the", "same", "to")){
                        r = listOf(response.body()!!.random())
                    }

                    dataResult.onDataFetchedSuccess(r)
                    if (currentWordIndex < max){
                        fetchLeadingWords(dataResult, r[0].word, max, vocab, currentWordIndex+1)
                    }
                }else{
                    dataResult.onDataFetchedFailed()
                }
            }

            override fun onFailure(call: Call<List<WordItem>>, t: Throwable) {
                Log.e(TAG, "Error fetching leading words failed", t)
                dataResult.onDataFetchedFailed()
            }

        })
    }
    fun fetchTrailingWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String,
        currentWordIndex : Int
    ){
        retrofit.fetchTrailingWords(inputString, max.toString(), vocab).enqueue(object : Callback<List<WordItem>>{
            override fun onResponse(
                call: Call<List<WordItem>>,
                response: Response<List<WordItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    var r = listOf(response.body()!![0])
                    Log.d(TAG, "fetching Trailing words was successful")
                    Log.d(TAG, "Response body { $inputString }: ${response.body()}")
                    if (response.body()!![0].word in listOf("of", "the", "same", "to")){
                        r = listOf(response.body()!!.random())
                    }


                    dataResult.onDataFetchedSuccess(response.body()!!)
                    if (currentWordIndex < max){
                        fetchTrailingWords(dataResult, r[0].word, max, vocab, currentWordIndex+1)
                    }
                }else{
                    dataResult.onDataFetchedFailed()
                }
            }

            override fun onFailure(call: Call<List<WordItem>>, t: Throwable) {
                Log.e(TAG, "Error fetching trailing words failed", t)
                dataResult.onDataFetchedFailed()
            }

        })
    }

    fun fetchSynonymousWords(
        dataResult : DataMuseResult,
        inputString : String,
        max : Int,
        vocab : String){
        retrofit.fetchSynWords(inputString, max.toString(), vocab).enqueue(object : Callback<List<WordItem>>{
            override fun onResponse(
                call: Call<List<WordItem>>,
                response: Response<List<WordItem>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    Log.d(TAG, "fetching synonymous words was successful")
                    Log.d(TAG, "Response body { $inputString }: ${response.body()}")
                    dataResult.onDataFetchedSuccess(response.body()!!)
                }else{
                    dataResult.onDataFetchedFailed()
                }
            }

            override fun onFailure(call: Call<List<WordItem>>, t: Throwable) {
                Log.e(TAG, "Error fetching synonymous words failed", t)
                dataResult.onDataFetchedFailed()
            }

        })
    }

}
