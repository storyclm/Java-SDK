package breffi.storyclm.maven.storyclmsdk;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMTableServiceRetrofitProxy;
import org.unitils.reflectionassert.ReflectionAssert;
import ru.breffi.storyclmsdk.connectors.RetrofitConnector;

public class RetrofitTableServiceTest extends TestCase {
	  public void testApp() throws IOException, InterruptedException {
		  RetrofitConnector clientConnector=  StoryCLMConnectorsGenerator.CreateRetrofitConnector("client_18_1", "d17ac10538ec402b9e2355dd3e2be0332b7f9dfa086645f3adcbff8c7208c94d",null, null, null);
		  StoryCLMTableServiceRetrofitProxy<Profile> tableService =  clientConnector.createStoryCLMTableService(Profile.class, 67);
		  Profile minRateprofile = CreateProfile();
	     minRateprofile = tableService.Insert(minRateprofile).execute().body();
	 	final AsyncResultContainer<Profile> resultContainer = new AsyncResultContainer<Profile>();
	     
	     tableService.Insert(minRateprofile).enqueue(new Callback<Profile>() {
			
			@Override
			public void onResponse(Call<Profile> call, Response<Profile> response) {
				resultContainer.Completed();
				resultContainer.result = response.body();
				
			}
			
			@Override
			public void onFailure(Call<Profile> call, Throwable t) {
				resultContainer.Failed();
				
			}
		});  
		  while(!resultContainer.completed&!resultContainer.fail)
			  Thread.sleep(200);
		  assertTrue(resultContainer.completed);
		  minRateprofile._id = resultContainer.result._id;
		  ReflectionAssert.assertReflectionEquals(minRateprofile,  resultContainer.result);
		  
		  Long count = tableService.Count().execute().body();
		  assertFalse(count == 0);
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
