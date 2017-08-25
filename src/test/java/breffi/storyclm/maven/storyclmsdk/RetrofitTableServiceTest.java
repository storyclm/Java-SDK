package breffi.storyclm.maven.storyclmsdk;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMTableServiceRetrofitProxy;
import ru.breffi.storyclmsdk.connectors.RetrofitConnector;

public class RetrofitTableServiceTest extends TestCase {
	  public void testApp() throws IOException {
		  RetrofitConnector clientConnector=  StoryCLMConnectorsGenerator.CreateRetrofitConnector("client_18_1", "d17ac10538ec402b9e2355dd3e2be0332b7f9dfa086645f3adcbff8c7208c94d",null, null, null);
		  StoryCLMTableServiceRetrofitProxy<Profile> tableService =  clientConnector.createStoryCLMTableService(Profile.class, 67);
		  Profile minRateprofile = CreateProfile();
	     minRateprofile = tableService.Insert(minRateprofile).execute().body();
		  
	     
	     tableService.Insert(minRateprofile).enqueue(new Callback<Profile>() {
			
			@Override
			public void onResponse(Call<Profile> call, Response<Profile> response) {
				System.out.println(response.body()._id);
				
			}
			
			@Override
			public void onFailure(Call<Profile> call, Throwable t) {
				// TODO Auto-generated method stub
				
			}
		});  
		  while(true);
	  }
	  
	  Profile CreateProfile()
	    {
	        Profile test = new Profile();
	        test.Name = "Vladimir";
	        test.Age = 22;
	        test.Gender = true;
	        test.Rating = 1D;
	        test.Created = new Date();
	        
	        return test;
	    }

}
