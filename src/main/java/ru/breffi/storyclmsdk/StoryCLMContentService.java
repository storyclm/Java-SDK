package ru.breffi.storyclmsdk;

import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.Models.Client;
import ru.breffi.storyclmsdk.Models.StoryContentPackage;
import ru.breffi.storyclmsdk.Models.StoryMediafile;
import ru.breffi.storyclmsdk.Models.StoryPresentation;
import ru.breffi.storyclmsdk.Models.StorySlide;

public class StoryCLMContentService{
	final private IStoryCLMContentServiceRetrofit _storyCLMContentService;
	public StoryCLMContentService(IStoryCLMContentServiceRetrofit storyCLMContentService)
	{
		 _storyCLMContentService = storyCLMContentService;
	}
	

	 
	 public IAsyncResult<Client[]> GetClients(){
		 return new ProxyCallResult<>(_storyCLMContentService.GetClients());
	 }
	 
	 
	 public IAsyncResult<Client> GetClient(int clientid){
		 return new ProxyCallResult<>(_storyCLMContentService.GetClient(clientid));
	 }
	 
	 
	 public IAsyncResult<StoryPresentation> GetPresentation( int presentationid)
	 {
		 return new ProxyCallResult<>(_storyCLMContentService.GetPresentation(presentationid));
	 }
	
	
	 public IAsyncResult<StoryMediafile> GetMediafile(int mediafileId)
	 {
		 return new ProxyCallResult<>(_storyCLMContentService.GetMediafile(mediafileId));
	 }
	
	
	 public IAsyncResult<StorySlide> GetSlide(int slideId)
	 {
		 return new ProxyCallResult<>(_storyCLMContentService.GetSlide(slideId));
	 }
	 
	
	 public IAsyncResult<StoryContentPackage> GetСontentpackage(int presentationId)
	 {
		 return new ProxyCallResult<>(_storyCLMContentService.GetСontentpackage(presentationId));
	 }

}
