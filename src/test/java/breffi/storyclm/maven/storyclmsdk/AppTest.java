package breffi.storyclm.maven.storyclmsdk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import Models.Profile;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMServiceConnector;
import ru.breffi.storyclmsdk.StoryCLMServiceGeneric;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Models.ApiTable;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	
	 StoryCLMServiceConnector clientConnector;
	 StoryCLMServiceGeneric<Profile> StoryCLMProfileService;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
        clientConnector=  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("client_18", "595a2fb724604e51a1f9e43b808c76c915c2e0f74e8840b384218a0e354f6de6",null);
    	StoryCLMProfileService = clientConnector.GetService(Profile.class, 23);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws AsyncResultException 
     * @throws AuthFaliException 
     */
    public void testApp() throws AuthFaliException, AsyncResultException
    {
    	
    	ApiTable[] tables= clientConnector.GetTables(18).GetResult();
    	assertTrue("Таблицы отсутствуют у клиента", tables.length>0);
    	
    	//Ищем таблицу Profile
    	ApiTable ptable=null;
    	for(ApiTable t:tables)
    		if (t.name.contains("Profile")) ptable = t;
    	assertFalse("ТАблица Profile отсутствует у клиента", ptable==null);
    	
    	
    	//Создаем сервис для Profile
    	StoryCLMProfileService = clientConnector.GetService(Profile.class, ptable.id);
    	
    	
    	  ////Добавить новую запись в таблицу
        ////Вернет запись с идентификатором
        Profile profile = StoryCLMProfileService.Insert(CreateProfile()).GetResult();
        profile = StoryCLMProfileService.Insert(CreateProfile1()).GetResult();
        profile = StoryCLMProfileService.Insert(CreateProfile2()).GetResult();
        profile = StoryCLMProfileService.Insert(CreateProfile3()).GetResult();
        ////Добавить коллекцию записей в таблицу
        ////Вернет коллекцию записей с идентификаторами
        List<Profile> profiles = StoryCLMProfileService.InsertMany(CreateProfiles()).GetResult();

        ////Обновить запись в таблице
        ////Перепишет все поля записи кроме идентификаторов
        profiles.add(StoryCLMProfileService.Update(UpdateProfile(profile)).GetResult());

        ////Обновить коллекцию записей в таблице
        profiles.addAll(StoryCLMProfileService.UpdateMany(UpdateProfiles(profiles)).GetResult());

        //Добавим еще данных 
        profiles.addAll(StoryCLMProfileService.InsertMany(CreateProfiles()).GetResult());


        ////Колличество записей в таблице
        long count = StoryCLMProfileService.Count().GetResult();

     

        ////Получить колличесво записей в логе таблицы
        count = StoryCLMProfileService.CountByLog(new Date(0)).GetResult();

        ////Получить все записи постранично
        List<Profile> results = StoryCLMProfileService.Find(0, 100).GetResult();

        //Получить все записи постранично по запросу
        //Несколько вариантов запроса
        profile = StoryCLMProfileService.Insert(CreateProfile1()).GetResult();
        profile = StoryCLMProfileService.Insert(CreateProfile2()).GetResult();
        profile = StoryCLMProfileService.Insert(CreateProfile3()).GetResult();
        profile = StoryCLMProfileService.Insert(CreateProfile4()).GetResult();
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	////////Query 
    	
    	
    	
    	
    	
    	
    	
    	
        ////Добавить новую запись в таблицу
        ////Вернет запись с идентификатором
        profile =StoryCLMProfileService.Insert(CreateProfile()).GetResult();
        String id = profile._id;
    	
    	StoryCLMProfileService.Delete("58ca82198047e227c41b65c8").GetResult();
		profiles = StoryCLMProfileService.Find(0, 10).GetResult();

		profiles.get(0).Name = "JavaUser222333";
		profiles.get(0).Created = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
		profiles.get(1).Name = "JavaUser444455555";
		profiles.get(1).Created = new Date();
		//profiles.get(1).Created.toLocaleString();
		StoryCLMProfileService.UpdateMany(new Profile[]{profiles.get(0),profiles.get(1)}).GetResult();
		List<Profile> profilesNew = StoryCLMProfileService.Find(0, 10).GetResult();
    	
    	
        assertTrue( true );
    }
    
    
  
    Profile CreateProfile()
    {
        Profile test = new Profile();
        test.Name = "Vladimir";
        test.Age = 22;
        test.Gender = true;
        test.Rating = 2.2D;
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

    Profile CreateProfile2()
    {
        Profile test = new Profile();
        test.Name = "Vladimir";
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
          test.Rating = 2.2D;
          test.Created = new Date();
          return test;
    }
    
    Profile[] CreateProfiles()
    {
        List<Profile> result = new ArrayList<Profile>();
        for(int i = 0; i < 3; i++)
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
