package ru.breffi.storyclmsdk;

import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.Models.Client;
import ru.breffi.storyclmsdk.Models.StoryContentPackage;
import ru.breffi.storyclmsdk.Models.StoryMediafile;
import ru.breffi.storyclmsdk.Models.StoryPresentation;
import ru.breffi.storyclmsdk.Models.StorySlide;

public class StoryCLMContentService{
	final private IStoryCLMContentServiceRetrofit _storyCLMContentServiceRetrofit;
	public StoryCLMContentService(IStoryCLMContentServiceRetrofit storyCLMContentServiceRetrofit)
	{
		 _storyCLMContentServiceRetrofit = storyCLMContentServiceRetrofit;
	}
	

	 
	 public IAsyncResult<Client[]> GetClients(){
		 return new ProxyCallResult<>(_storyCLMContentServiceRetrofit.GetClients());
	 }
	 
	 
	 public IAsyncResult<Client> GetClient(int clientid){
		 return new ProxyCallResult<>(_storyCLMContentServiceRetrofit.GetClient(clientid));
	 }
	 
	 
	 public IAsyncResult<StoryPresentation> GetPresentation( int presentationid)
	 {
		 return new ProxyCallResult<>(_storyCLMContentServiceRetrofit.GetPresentation(presentationid));
	 }
	
	
	 public IAsyncResult<StoryMediafile> GetMediafile(int mediafileId)
	 {
		 return new ProxyCallResult<>(_storyCLMContentServiceRetrofit.GetMediafile(mediafileId));
	 }
	
	
	 public IAsyncResult<StorySlide> GetSlide(int slideId)
	 {
		 return new ProxyCallResult<>(_storyCLMContentServiceRetrofit.GetSlide(slideId));
	 }
	 
	
	 public IAsyncResult<StoryContentPackage> GetСontentpackage(int presentationId)
	 {
		 return new ProxyCallResult<>(_storyCLMContentServiceRetrofit.GetСontentpackage(presentationId));
	 }

}