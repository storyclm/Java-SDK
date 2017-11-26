package ru.breffi.storyclmsdk.OAuth;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
	private String BASE_URL = "https://auth.storyclm.com/";
	
	
	
  /*  private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
*/
	public ServiceGenerator(){}
	public ServiceGenerator(String baseUrl){
		if (baseUrl!=null) this.BASE_URL = baseUrl;
	}
	
	private Retrofit retrofit = null;
	//private static Retrofit getRetrofit (String baseUrl){
    private Retrofit getRetrofit (){
    	return (retrofit!=null)?
    				retrofit:
    					(retrofit=new Retrofit.Builder()
    					.baseUrl(BASE_URL)
		    		    .addConverterFactory(GsonConverterFactory.create())
		    		    .client(
		    		    		new OkHttpClient.Builder()
		    	    	        .readTimeout(60, TimeUnit.SECONDS)
		    	    	        .connectTimeout(60, TimeUnit.SECONDS)
		    	    	        .build()
		    	    	)
		    		    .build());
    }


    public <S> S createService(Class<S> serviceClass) { return getRetrofit().create(serviceClass); }
}

