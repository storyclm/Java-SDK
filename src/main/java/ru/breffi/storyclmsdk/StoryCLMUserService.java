package ru.breffi.storyclmsdk;

import ru.breffi.storyclmsdk.AsyncResults.IAsyncResult;
import ru.breffi.storyclmsdk.AsyncResults.NullIf404;
import ru.breffi.storyclmsdk.AsyncResults.ProxyCallResult;
import ru.breffi.storyclmsdk.AsyncResults.ProxyConvertCallResult;
import ru.breffi.storyclmsdk.Models.CreateUser;
import ru.breffi.storyclmsdk.Models.Group;
import ru.breffi.storyclmsdk.Models.Password;
import ru.breffi.storyclmsdk.Models.Presentation;
import ru.breffi.storyclmsdk.Models.SimpleUser;
import ru.breffi.storyclmsdk.Models.User;
import ru.breffi.storyclmsdk.retrofitservices.IStoryCLMUserServiceRetrofit;

public class StoryCLMUserService{
	final private IStoryCLMUserServiceRetrofit _storyCLMUserServiceRetrofit;
	public StoryCLMUserService(IStoryCLMUserServiceRetrofit storyCLMContentServiceRetrofit)
	{
		_storyCLMUserServiceRetrofit = storyCLMContentServiceRetrofit;
	}
		 
	 
	 public IAsyncResult<CreateUser> Create(CreateUser user){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.Create(user));
	 }
	
	 public IAsyncResult<User> Update( User user){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.Update(user));
	 }
	
	 
	 public IAsyncResult<SimpleUser[]> GetUsers(){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.Get());
	 }
	 	
	 public IAsyncResult<User> Exists(String username){
		 return new NullIf404<>(new ProxyConvertCallResult<User,User>(_storyCLMUserServiceRetrofit.Exists(username)));
	 }

	 public IAsyncResult<User> Get(String userId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.Get(userId));
	 }
	 
	 public IAsyncResult<Void> UpdatePassword(String userId,Password password){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.UpdatePassword(userId,password));
	 }
	 
	 public IAsyncResult<Void> AddToGroup(String userId,Integer groupId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.AddToGroup(userId,groupId));
	 }
	 
	 public IAsyncResult<Void> RemoveFromGroup(String userId, Integer groupId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.RemoveFromGroup(userId,groupId));
	 }
	 
	 public IAsyncResult<Void> AddToPresentation(String userId, Integer presentationId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.AddToPresentation(userId,presentationId));
	 }

	 public IAsyncResult<Void> RemoveFromPresentation(String userId, Integer presentationId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.RemoveFromPresentation(userId, presentationId));
	 }
	
	 public IAsyncResult<Presentation[]> GetPresentations( String userId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.GetPresentations(userId));
	 }

	 public IAsyncResult<Group[]> GetGroups(String userId){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.GetGroups(userId));
	 }

	 
	 
	 public IAsyncResult<int[]> AddToPresentations(String userId, Integer[] presentationsIds){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.AddToPresentations(userId,presentationsIds));
	 }

	 public IAsyncResult<int[]> RemoveFromPresentations( String userId, Integer[] presentationsIds){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.RemoveFromPresentations(userId,presentationsIds));
	 }
	
	 public IAsyncResult<int[]> SetUserPresentations(String userId, Integer[] presentationsIds){
		 return new ProxyCallResult<>(_storyCLMUserServiceRetrofit.SetUserPresentations(userId,presentationsIds));
	 }


}
