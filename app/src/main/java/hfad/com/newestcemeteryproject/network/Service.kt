package hfad.com.newestcemeteryproject.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hfad.com.newestcemeteryproject.data.Cemetery
import hfad.com.newestcemeteryproject.data.Grave
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST



interface RestApi {

    @GET("/cgi-bin/stuTest.pl")
    fun getCemeteriesFromNetwork(): Deferred<NetworkCemeteryContainer>

    @FormUrlEncoded
    @POST("/cgi-bin/addCem.pl")
    fun addCemetery(
        @Field("cem_id") cemId: String,
        @Field("name") cemName: String,
        @Field("loc") location: String,
        @Field("state") state: String,
        @Field("county") county: String,
        @Field("twnsp") township: String,
        @Field("range") range: String,
        @Field("spot") spot: String,
        @Field("fyear") yearFounded: String,
        @Field("section") section: String
                ): Call<Cemetery>

    @FormUrlEncoded
    @POST("/cgi-bin/addGrave.pl")
    fun addGrave(
        @Field("id") id: Int,
        @Field("cemetery_id") cemeteryId: Int,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("born_data") bornDate: String,
        @Field("death_data") deathDate: String,
        @Field("married") marriageYear: String,
        @Field("comment") comment: String,
        @Field("grave_number") graveNum: String
    ): Call<Grave>
}

private val moshi = Moshi.Builder() //can use later
    .add(KotlinJsonAdapterFactory())
    .build()

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://dts.scott.net") // change this IP for testing by your actual machine IP
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)




        .build()



    //MoshiConverterFactory.create(moshi) if we are to use moshi converter this needs
    //to be in .addConverterFactory

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

//    val networkAccessor = retrofit.create(   this will be for a get request?
//        RestApi::class.java)
}

