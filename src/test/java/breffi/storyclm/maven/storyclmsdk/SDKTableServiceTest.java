package breffi.storyclm.maven.storyclmsdk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import Models.Profile;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMServiceConnector;
import ru.breffi.storyclmsdk.StoryCLMTableService;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Models.ApiLog;
import ru.breffi.storyclmsdk.Models.ApiTable;



/**
 * Unit test for simple App.
 */
public class SDKTableServiceTest 
    extends TestCase
{
	
	 StoryCLMServiceConnector clientConnector;
	 StoryCLMTableService<Profile> StoryCLMProfileService;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SDKTableServiceTest( String testName )
    {
        super( testName );
        clientConnector=  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("client_18_1", "d17ac10538ec402b9e2355dd3e2be0332b7f9dfa086645f3adcbff8c7208c94d",null, null, null);
    	StoryCLMProfileService = clientConnector.GetTableService(Profile.class, 23);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SDKTableServiceTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws AsyncResultException 
     * @throws AuthFaliException 
     * @throws InterruptedException 
     */
    public void testApp() throws AuthFaliException, AsyncResultException, InterruptedException
    {
    	
    	ApiTable[] tables= clientConnector.GetTables(18).GetResult();
    	assertTrue("Таблицы отсутствуют у клиента", tables.length>0);
    	
    	//Ищем таблицу Profile
   /* 	ApiTable ptable=null;
    	for(ApiTable t:tables)
    		if (t.name.contains("Profile")) ptable = t;
    	assertFalse("Таблица Profile отсутствует у клиента", ptable==null);
    */	
    	
    	//Создаем сервис для Profile
    	StoryCLMProfileService = clientConnector.GetTableService(Profile.class, 67);
    	
    	long count = StoryCLMProfileService.Count().GetResult();
    	if (count>0){
    	
    		
    		List<Profile> oldProfiles =StoryCLMProfileService.FindAllSync(null);
    		List<String> ids = new ArrayList<String>();
    		for(Profile p:oldProfiles){
    			ids.add(p._id);
    		}
    		StoryCLMProfileService.Delete(ids).GetResult();
    		count = StoryCLMProfileService.Count().GetResult();
    		assertEquals("Удаление старых элементов из таблицы не сработало",0,count);
    	}
    	
    	//Пока в базе нет записей
    	  Double nullminage = StoryCLMProfileService.MinOrDefault("Age", null , double.class,null).GetResult();
          assertEquals("Max не должнобыть в базе",null,nullminage);
          
          int defaultminage = StoryCLMProfileService.MinOrDefault("Age", null , int.class,-1).GetResult();
          assertEquals("Max не должнобыть в базе",-1,defaultminage);
          
    	//Пока в базе нет записей
    	  Double nullmaxage = StoryCLMProfileService.MaxOrDefault("Age", null , double.class,null).GetResult();
          assertEquals("Max не должнобыть в базе",null,nullmaxage);
          
        //асинхронно
		final AsyncResultContainer<Double> resultContainer = new AsyncResultContainer<Double>();
          IAsyncResult<Double> asr = StoryCLMProfileService.MaxOrDefault("Age", null , double.class,null);
          
          asr.OnResult(new OnResultCallback<Double>() {
        	  		@Override
					public void OnSuccess(Double result) {
        	  			resultContainer.completed = true;
						resultContainer.result = result;
					}
			
					@Override
					public void OnFail(Throwable t) {
						resultContainer.completed = true;
						resultContainer.fail = true;
						throw new AssertionFailedError("При получении дефаултного max не должно быть ошибки");
					}
		});
    	
    	while(!resultContainer.completed){
    		 Thread.sleep(200);
    	}
    	
    	assertNull("Max не должнобыть в базе", resultContainer.result);
    	assertFalse("Запрос для Max должен закончиться успешно", resultContainer.fail);
    	
    	
    	
    	
    	
    	List<String> ids = new ArrayList<String>();
    	////Добавить новую запись в таблицу
        ////Вернет запись с идентификатором
    	Profile minRateprofile = CreateProfile();
    	minRateprofile = StoryCLMProfileService.Insert(minRateprofile).GetResult();
        ids.add(minRateprofile._id);
        count = StoryCLMProfileService.Count().GetResult();
        assertEquals("Элемент не добавился",1, count);
        Profile savedProfile = StoryCLMProfileService.Find(minRateprofile._id).GetResult(); 
        assertEquals("Сохраненнный элемент не равен исходному", savedProfile,minRateprofile);
        
        
        
        Profile profile = StoryCLMProfileService.Insert(CreateProfile1()).GetResult();
        ids.add(profile._id);
        Profile timur =  StoryCLMProfileService.Insert(CreateTimur()).GetResult();
        ids.add(timur._id);
        profile = StoryCLMProfileService.Insert(CreateProfile3()).GetResult();
        ids.add(profile._id);
        Profile maxRateProfile = StoryCLMProfileService.Insert(CreateProfile4()).GetResult();
        ids.add(maxRateProfile._id);
        
        count = StoryCLMProfileService.Count().GetResult();
        assertEquals("Элементы не добавились",5, count);
        
        List<Profile> savedP = StoryCLMProfileService.Find(ids.toArray(new String[ids.size()])).GetResult();
        assertEquals("Не все элементы вернул", 5, savedP.size());

        //Функция асинхронного получения всех записей
        
    	final AsyncResultContainer<List<Profile>> findResultContainer = new AsyncResultContainer<List<Profile>>();
		IAsyncResult<List<Profile>> olas =  StoryCLMProfileService.FindAll(null, 2);
		olas.OnResult(new OnResultCallback<List<Profile>>() {
			
			@Override
			public void OnSuccess(List<Profile> result) {
				findResultContainer.completed = true;
				findResultContainer.result = result;
				
			}
			
			@Override
			public void OnFail(Throwable t) {
				findResultContainer.completed = true;
				findResultContainer.fail = true;
				
			}
		});
		while(!findResultContainer.completed){
   		 Thread.sleep(200);
		}
        
		assertEquals("Не все элементы вернул", 5, findResultContainer.result.size());
        
        
        //Агрегаты
        
        double avgage = StoryCLMProfileService.AvgOrDefault("Age", null , double.class,null).GetResult();
        assertEquals("Среднее вычисленно не корректно", 23.2,avgage);
        
        double maxage = StoryCLMProfileService.Max("Age", null , double.class).GetResult();
        assertEquals("Max вычисленно не корректно",28d,maxage);
 
        double minage = StoryCLMProfileService.Min("Age", null , double.class).GetResult();
        assertEquals("Min вычисленно не корректно",22d,minage);
        
        double sumage = StoryCLMProfileService.SumOrDefault("Age", null , double.class,null).GetResult();
        assertEquals("Sum вычисленно не корректно",116d,sumage);

        
        //Query
        
        Profile storytimur = StoryCLMProfileService.Find("[Name][eq][\"Тимур\"]", null, null, 0,1).GetResult().get(0);
        
        assertEquals("Объект по русским буквам не найде",timur,storytimur);
        
        
        
        Profile maxRateProfileSaved= StoryCLMProfileService.LastOrDefault( null ,"rating", 1, null).GetResult();
        assertEquals("Last вычисленно не корректно",maxRateProfile,maxRateProfileSaved);
  
        
        


        //Проверка first        
        Profile minSavedRateProfile= StoryCLMProfileService.FirstOrDefault( null ,"Rating", 1,null).GetResult();
        assertEquals("First вычисленно не корректно",minRateprofile,minSavedRateProfile);
        
         maxRateProfileSaved= StoryCLMProfileService.FirstOrDefault( null ,"Rating", -1, null).GetResult();
        assertEquals("First вычисленно не корректно",maxRateProfile,maxRateProfileSaved);
        
        
        
        ////Добавить коллекцию записей в таблицу
        ////Вернет коллекцию записей с идентификаторами
        List<Profile> profiles = StoryCLMProfileService.InsertMany(CreateProfiles()).GetResult();

        
        count = StoryCLMProfileService.Count().GetResult();
        assertEquals("Элементы не добавились",15, count);
        ////Обновить запись в таблице
        ////Перепишет все поля записи кроме идентификаторов
        profiles.add(StoryCLMProfileService.Update(UpdateProfile(profile)).GetResult());

        ////Обновить коллекцию записей в таблице
        profiles.addAll(StoryCLMProfileService.UpdateMany(UpdateProfiles(profiles)).GetResult());

        //Добавим еще данных 
        profiles.addAll(StoryCLMProfileService.InsertMany(CreateProfiles()).GetResult());


        ////Колличество записей в таблице
        count = StoryCLMProfileService.Count().GetResult();

     

        ////Получить колличесво записей в логе таблицы
        count = StoryCLMProfileService.CountByLog(new Date(0)).GetResult();

        ////Получить все записи постранично
        List<Profile> results = StoryCLMProfileService.Find(null, null,null, 0, 100).GetResult();


    	
    	
    	
    	
    	
    	////////Query 
    	
    	
    	
    	
    	
    	
    	
    	
        ////Добавить новую запись в таблицу
        ////Вернет запись с идентификатором
        profile =StoryCLMProfileService.Insert(CreateProfile()).GetResult();
        String id = profile._id;
    	
		profiles = StoryCLMProfileService.Find(null, null,null, 0, 10).GetResult();

		profiles.get(0).Name = "JavaUser222333";
		profiles.get(0).Created = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
		profiles.get(1).Name = "JavaUser444455555";
		profiles.get(1).Created = new Date();
		//profiles.get(1).Created.toLocaleString();
		StoryCLMProfileService.UpdateMany(new Profile[]{profiles.get(0),profiles.get(1)}).GetResult();
		List<Profile> profilesNew = StoryCLMProfileService.Find(null, null,null, 0, 10).GetResult();
    	
    	
      ///Удаление
		List<Profile> oldProfiles = StoryCLMProfileService.FindAllSync(null);
		ids = new ArrayList<String>();
		for(Profile p:oldProfiles){
			ids.add(p._id);
		}
		StoryCLMProfileService.Delete(ids).GetResult();
		count = StoryCLMProfileService.Count().GetResult();
		assertEquals("Удаление элементов из таблицы не сработало",0,count);
		
		
		
		oldProfiles = StoryCLMProfileService.InsertMany(CreateProfiles()).GetResult();
		ids = new ArrayList<String>();
		for(Profile p:oldProfiles){
			ids.add(p._id);
		}
		//Удалим порциями
		IAsyncResult<List<ApiLog>> res = StoryCLMProfileService.Delete(ids, 2);
		res.OnResult(new OnResultCallback<List<ApiLog>>() {
			
			@Override
			public void OnSuccess(List<ApiLog> result) {
				assertEquals(10, result.size());
				findResultContainer.completed = true;
			}
			
			@Override
			public void OnFail(Throwable t) {
				
				assertTrue("Неожиданное исключение", false);
				findResultContainer.completed = true;
			}
		});
		
		findResultContainer.completed = false;
		while(!findResultContainer.completed){
	   		 Thread.sleep(200);
			}
		count = StoryCLMProfileService.Count().GetResult();
		assertEquals("Удаление элементов из таблицы не сработало",0,count);
		
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


    Profile CreateProfile1()
    {
        Profile test = new Profile();
        test.Name = "Valentina";
        test.Age = 22;
        test.Gender = true;
        test.Rating = 2.2D;
        test.Created = new Date();
        
        return test;
    }

    Profile CreateTimur()
    {
        Profile test = new Profile();
        test.Name = "Тимур";
        test.Age = 28;
        test.Gender = true;
        test.Rating = 2.2D;
        test.Created = new Date();
        
        return test;
    }
    /// <summary>
    /// Создает новый профиль
    /// </summary>
    /// <returns>Профиль</returns>
    static Profile CreateProfile3()
    {
    	
    	  Profile test = new Profile();
          test.Name = "Stanislav";
          test.Age = 22;
          test.Gender = true;
          test.Rating = 2.2D;
          test.Created = new Date();
          
          return test;
    }

    static Profile CreateProfile4()
    {
    	
    	  Profile test = new Profile();
          test.Name = "Tamerlan";
          test.Age = 22;
          test.Gender = true;
          test.Rating = 5.2D;
          test.Created = new Date(0);
          return test;
    }
    
    Profile[] CreateProfiles()
    {
        List<Profile> result = new ArrayList<Profile>();
        for(int i = 0; i < 10; i++)
            result.add(CreateProfile());
        return result.toArray(new Profile[0]);
    }
    /// <summary>
    /// Обновляет профиль
    /// </summary>
    /// <param name="o">Исходный профиль</param>
    /// <returns>Обновленный профиль</returns>
    static Profile UpdateProfile(Profile o)
    {
        Profile pf =  new Profile();
        pf._id = o._id;
        pf.Name = "Anna";
            pf.Age = 33;
            pf.Gender = false;
            pf.Rating = 3.3D;
            pf.Created = new Date();
        return pf;
    }
    
    /// <summary>
    /// Обновляет коллекцию профилей
    /// </summary>
    /// <param name="o">Исходная коллекция профилей</param>
    /// <returns>Обновленная коллекция</returns>
    static Profile[] UpdateProfiles(List<Profile> o)
    {
    	for(int i=0;i<o.size();i++){
    		o.set(i, UpdateProfile(o.get(i)));
    	}
    	return o.toArray(new Profile[0]);
    }


}
