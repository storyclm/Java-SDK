package ru.breffi.storyclm.maven.storyclmsdk;



import java.util.Arrays;
import java.util.stream.Collectors;

import junit.framework.Assert;
import junit.framework.Test;
import ru.breffi.storyclmsdk.Models.PresentationUser;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMContentService;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.Models.Client;
import ru.breffi.storyclmsdk.Models.StoryContentPackage;
import ru.breffi.storyclmsdk.Models.StoryMediafile;
import ru.breffi.storyclmsdk.Models.StoryPresentation;
import ru.breffi.storyclmsdk.Models.StorySlide;
import ru.breffi.storyclmsdk.connectors.StoryCLMServiceConnector;

/**
 * Unit test for simple App.
 */
public class SDKContentServiceTest 
    extends TestCase
{
	
	public static Test suite()
    {
        return new TestSuite( SDKContentServiceTest.class );
    }
	
	int presentationId = 4991;
	String[] usersIds=new String[]{"31e806b7-56b2-4560-ad39-2fa1a382a9d2"};
	String myUSer = "df01866d-0c9c-428d-bd90-3ecf52220d72";
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testContentService() throws Exception
    {
    	StoryCLMServiceConnector clientConnector =  StoryCLMConnectorsGenerator.CreateStoryCLMServiceConnector("client_18_4", "1cdbbf4374634314bfd5607a79a0b5578d05130732dc4a37ac8c046525a27075","tselofan1@yandex.ru", "jTL96D", null);;
    	StoryCLMContentService contentService = clientConnector.GetContentService();
    	    	
    	//Синхронные вызовы
    	Client[] clients = contentService.GetClients().GetResult();
    	assertTrue(clients.length>0);
    	Client client  = contentService.GetClient(18).GetResult();
    	System.out.println(client.id);
    	assertEquals(client.id, 18);
    	StoryPresentation presentation = contentService.GetPresentation(presentationId).GetResult();
    	assertEquals(presentation.id, presentationId);
    	StoryMediafile mediafile = contentService.GetMediafile(presentation.mediaFiles[0].id).GetResult();
    	assertEquals(mediafile.id, presentation.mediaFiles[0].id);
    	StorySlide slide = contentService.GetSlide(presentation.slides[0].id).GetResult();
    	assertEquals(slide.id, presentation.slides[0].id);
    	
    	PresentationUser[] users = contentService.GetPresentationUsers(presentationId).GetResult();
    	
    	//ПОльзователи из с доступом к презентации, не равные myUSer
    	String[] removingUsers = Arrays.asList(users).stream().map(x->x.id).filter(x->!x.equals(myUSer)).collect(Collectors.toList()).toArray(new String[0]);
    	//Удаляем всех пользователей (кроме нашего) из доступа к презентации  
    	users = contentService.RemovePresentationUsers(presentationId, removingUsers).GetResult();
    	//Должен остаться только наш пользователь в количестве 1
    	Assert.assertEquals(1, users.length);

    	//Добавляем пользователей из нашего списка
    	users = contentService.AddPresentationUsers(presentationId, usersIds).GetResult();
    	//Вместе с нашим их должно стать 2
    	Assert.assertEquals(2, users.length);
    	
    	//один из пользователей презентации должен быть первым из списка 
    	Assert.assertTrue(Arrays.asList(users).stream().anyMatch(x->x.id.equals(usersIds[0])));
    	
    	users = contentService.RemovePresentationUsers(presentationId, usersIds).GetResult();
    	Assert.assertEquals(1, users.length);
    	
    	users = contentService.AddPresentationUsers(presentationId, usersIds).GetResult();
    	Assert.assertEquals(2, users.length);
    	users = contentService.SetPresentationUsers(presentationId, new String[]{myUSer}).GetResult();
    	Assert.assertEquals(1, users.length);
    	Assert.assertEquals(myUSer, users[0].id);
    	
    	
    	//Пример асинхронного вызова
    	//Для отслеживания асинхронного вызова создадим спец объект для мониторинга
		final AsyncResultContainer<StoryContentPackage> resultContainer = new AsyncResultContainer<StoryContentPackage>();
    	IAsyncResult<StoryContentPackage> contentPacckageResult= contentService.GetСontentpackage(presentationId);
    	contentPacckageResult.OnResult(new OnResultCallback<StoryContentPackage>() {
             @Override
             public void OnSuccess(StoryContentPackage result) {
            	 resultContainer.completed = true;
				 resultContainer.result = result;
             }
             @Override
             public void OnFail(Throwable throwable) {
                System.out.println(throwable.getMessage());
                resultContainer.completed = true;
             }
         });
    
    	//Дождемся окончания асинхронного вызова
    	while(!resultContainer.completed){
   		 Thread.sleep(200);
    	}
    	assertNotNull(resultContainer.result);
    	
   }

}
