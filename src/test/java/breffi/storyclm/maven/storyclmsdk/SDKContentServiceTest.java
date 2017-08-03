package breffi.storyclm.maven.storyclmsdk;



import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMContentService;
import ru.breffi.storyclmsdk.StoryCLMServiceConnector;
import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.Models.Client;
import ru.breffi.storyclmsdk.Models.StoryContentPackage;
import ru.breffi.storyclmsdk.Models.StoryMediafile;
import ru.breffi.storyclmsdk.Models.StoryPresentation;
import ru.breffi.storyclmsdk.Models.StorySlide;

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
	
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testContentService() throws Exception
    {
    	StoryCLMServiceConnector clientConnector =  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("client_18_4", "1cdbbf4374634314bfd5607a79a0b5578d05130732dc4a37ac8c046525a27075","tselofan1@yandex.ru", "jTL96D", null);;
    	StoryCLMContentService contentService = clientConnector.GetContentService();
    	    	
    	//Синхронные вызовы
    	Client[] clients = contentService.GetClients().GetResult();
    	assertTrue(clients.length>0);
    	Client client  = contentService.GetClient(18).GetResult();
    	System.out.println(client.id);
    	assertEquals(client.id, 18);
    	StoryPresentation presentation = contentService.GetPresentation(4949).GetResult();
    	assertEquals(presentation.id, 4949);
    	StoryMediafile mediafile = contentService.GetMediafile(10080).GetResult();
    	assertEquals(mediafile.id, 10080);
    	StorySlide slide = contentService.GetSlide(234861).GetResult();
    	assertEquals(slide.id, 234861);
    	
    	
    	
    	//Пример асинхронного вызова
    	//Для отслеживания асинхронного вызова создадим спец объект для мониторинга
		final AsyncResultContainer<StoryContentPackage> resultContainer = new AsyncResultContainer<StoryContentPackage>();
    	IAsyncResult<StoryContentPackage> contentPacckageResult= contentService.GetСontentpackage(4949);
    	contentPacckageResult.OnResult(new OnResultCallback<StoryContentPackage>() {
             @Override
             public void OnSuccess(StoryContentPackage result) {
            	 resultContainer.completed = true;
				 resultContainer.result = result;
            	
            	 
             }
             @Override
             public void OnFail(Throwable throwable) {
                System.out.println(throwable.getMessage());
             }
         });
    
    	//Дождемся окончания асинхронного вызова
    	while(!resultContainer.completed){
   		 Thread.sleep(200);
    	}
    	assertEquals(resultContainer.result.id, 2926);
    	
   }

}
