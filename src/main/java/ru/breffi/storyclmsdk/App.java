package ru.breffi.storyclmsdk;

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import Models.Profile;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, AsyncResultException 
    {
    	
    	StoryCLMServiceConnector clientConnector=  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("client_18", "595a2fb724604e51a1f9e43b808c76c915c2e0f74e8840b384218a0e354f6de6",null);
    	StoryCLMServiceGeneric<Profile> StoryCLMProfileService = clientConnector.GetService(Profile.class, 23);
		StoryCLMProfileService.Delete("58ca82198047e227c41b65c8").GetResult();
		List<Profile> profiles = StoryCLMProfileService.Find(null, null, null, 0, 10).GetResult();

		profiles.get(0).Name = "JavaUser222333";
		profiles.get(0).Created = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
		profiles.get(1).Name = "JavaUser444455555";
		profiles.get(1).Created = new Date();
		//profiles.get(1).Created.toLocaleString();
		StoryCLMProfileService.UpdateMany(new Profile[]{profiles.get(0),profiles.get(1)}).GetResult();
		List<Profile> profilesNew = StoryCLMProfileService.Find(null, null, null, 0, 10).GetResult();
		System.out.println(profilesNew.get(0));

		/*StoryCLMServiceGeneric<Map<String,Object>> servicem = clientConnector.GetService(new TypeToken<Map<String,Object>>(){}.getType(), 23);

		 List<Map<String,Object>> profilesm = servicem.Find(0, 10).GetResult();
    	 System.out.println(profilesm.get(0).get("Created"));
    	 profilesm.get(0).replace("Created",  new GregorianCalendar(2015, Calendar.FEBRUARY, 1).getTime());
    	 System.out.println(profilesm.get(0).get("Created"));
    	
    	//*/
	
/*
		//возраст меньше или равен 30
		List<Profile> profiles  = StoryCLMProfileService.Find( "[age][lte][30]", "age", 1, 0, 100).GetResult();

		//поле "name" начинается с символа "T"
		profiles = StoryCLMProfileService.Find("[name][sw][\"T\"]", "age", 1, 0, 100).GetResult();

		//поле "name" содержит строку "ad"
		profiles = StoryCLMProfileService.Find("[Name][cn][\"ad\"]", "age", 1, 0, 100).GetResult();

		//поиск имен из списка
		profiles = StoryCLMProfileService.Find("[Name][in][\"Stanislav\",\"Tamerlan\"]", "age", 1, 0, 100).GetResult();

		//Выбрать женщин, имя ("name") которых начинается со строки "V" 
		profiles = StoryCLMProfileService.Find("[gender][eq][false][and][Name][sw][\"V\"]", "age", 1, 0, 100).GetResult();

		//Выбрать мужчин младше 30 и женщин старше 30
		profiles =StoryCLMProfileService.Find("[gender][eq][true][and][age][lt][30][or][gender][eq][false][and][age][gt][30]", "age", 1, 0, 100).GetResult();

		//поле "name" начинается с символов "T" или "S" при этом возраст должен быть равен 22
		profiles = StoryCLMProfileService.Find("([name][sw][\"S\"][or][name][sw][\"T\"])[and][age][eq][22]", "age", 1, 0, 100).GetResult();

		//Выбрать всех с возрастом НЕ в интервале [25,30] и с именами на "S" и "Т"
		profiles = StoryCLMProfileService.Find( "([age][lt][22][or][age][gt][30])[and]([name][sw][\"S\"][or][name][sw][\"T\"])", "age", 1, 0, 100).GetResult();
		
	*/	
    
    }
}
