package breffi.storyclm.maven.storyclmsdk;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.StoryCLMConnectorsGenerator;
import ru.breffi.storyclmsdk.StoryCLMUserService;
import ru.breffi.storyclmsdk.connectors.StoryCLMServiceConnector;
import ru.breffi.storyclmsdk.Models.CreateUser;
import ru.breffi.storyclmsdk.Models.Group;
import ru.breffi.storyclmsdk.Models.Password;
import ru.breffi.storyclmsdk.Models.Presentation;
import ru.breffi.storyclmsdk.Models.User;
import ru.breffi.storyclmsdk.Models.SimpleUser;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;


public class SDKUserServiceTest extends TestCase{
	StoryCLMUserService userService = null;
	
	public  SDKUserServiceTest()throws Exception{
		
	}
	 
	public static Test suite()
    {
        return new TestSuite( SDKUserServiceTest.class );
    }
	final String userName = "79188628061";
	final String oldPassword = "1234";
	final String newPassword = "new password";
	final Integer groupId = 88;
	final Integer presentationId = 4991;
	public void testUpdateUser() throws Exception{
		
		StoryCLMServiceConnector clientConnector =  StoryCLMConnectorsGenerator.CreateStoryCLMServiceConnector("client_18_4", "1cdbbf4374634314bfd5607a79a0b5578d05130732dc4a37ac8c046525a27075","tselofan1@yandex.ru", "jTL96D", null);;
    	userService = clientConnector.GetUserService();
    		
    	CreateUser expectedCreateUser  = createUser();
    	User expectedUser;
    
    	
    	User actual  = userService.Exists(expectedCreateUser.username).GetResult();
    	Assert.assertNull("Пользователь "+ userName +" присутствует в системе. Для корректного прохождения теста пользователя необходимо удалить!",actual);
    	final AsyncResultContainer<User> resultContainer = new AsyncResultContainer<User>();
    	userService.Exists(expectedCreateUser.username).OnResult(new  OnResultCallback<User>() {
			@Override
			public void OnSuccess(User result) {
				resultContainer.result = result;
				resultContainer.Completed();
				
			}
			
			@Override
			public void OnFail(Throwable t) {
				resultContainer.Completed();
				
			}
		});
    	
    	//Дождемся окончания асинхронного вызова
    	while(!resultContainer.completed){
   		 Thread.sleep(200);
    	}
    	Assert.assertNull("Пользователь присутствует в системе. Для корректного прохождения теста пользователя необходимо удалить!",resultContainer.result);
    	
    	actual = userService.Create(expectedCreateUser).GetResult();
   
    	Assert.assertNotNull(actual.id);
    	expectedCreateUser.id = actual.id;
    	ReflectionAssert.assertReflectionEquals(actual, (User)expectedCreateUser);
    	expectedUser = expectedCreateUser;
	
    	//попробуем аутентифицироваться новым пользователем
		StoryCLMServiceConnector connectorForNewUser =  StoryCLMConnectorsGenerator.CreateStoryCLMServiceConnector("client_18_4", "1cdbbf4374634314bfd5607a79a0b5578d05130732dc4a37ac8c046525a27075",expectedCreateUser.username, expectedCreateUser.password, null);
    	String accessToken = connectorForNewUser.getAccessTokenManager().getAccessToken();
      	assertNotNull(accessToken);
    	
    	expectedUser = updatedUser(expectedUser.id);
		actual = userService.Update(expectedUser).GetResult();
		ReflectionAssert.assertReflectionEquals(actual, expectedUser,ReflectionComparatorMode.LENIENT_DATES);
		
		//test exists
		actual = userService.Exists(expectedUser.username).GetResult();
		ReflectionAssert.assertReflectionEquals(actual, expectedUser, ReflectionComparatorMode.LENIENT_DATES);
			
		//get users
		List<SimpleUser> actualUsers = Arrays.asList(userService.GetUsers().GetResult());
		assertFalse(actualUsers.size()==0);
		assertTrue(actualUsers.stream().anyMatch(x->x.username.equals(userName)));
		
		//get user
		actual = userService.Get(expectedUser.id).GetResult();
		ReflectionAssert.assertReflectionEquals(actual, expectedUser, ReflectionComparatorMode.LENIENT_DATES);
		
		//Password
		userService.UpdatePassword(expectedUser.id, new Password(newPassword)).GetResult();
		
		//add to group
		userService.AddToGroup(expectedUser.id, groupId).GetResult();
		
		List<Group> groups = Arrays.asList(userService.GetGroups(expectedUser.id).GetResult());
		assertTrue(groups.stream().anyMatch(x->x.id==groupId));
		
		//Remove from group
		userService.RemoveFromGroup(expectedUser.id, groupId).GetResult();
		groups = Arrays.asList(userService.GetGroups(expectedUser.id).GetResult());
		assertFalse(groups.stream().anyMatch(x->x.id==groupId));
		
		//add to presentation
		userService.AddToPresentation(expectedUser.id, presentationId).GetResult();
		List<Presentation>  presentations = Arrays.asList(userService.GetPresentations(expectedUser.id).GetResult());
		assertTrue(presentations.stream().anyMatch(x->x.id.equals(presentationId)));
		
		//remove from presentation
		userService.RemoveFromPresentation(expectedUser.id, presentationId).GetResult();
		presentations = Arrays.asList(userService.GetPresentations(expectedUser.id).GetResult());
		assertFalse(presentations.stream().anyMatch(x->x.id.equals(presentationId)));
				
	}
	
	
	CreateUser createUser(){
		CreateUser createuser = new CreateUser();
		createuser.username = userName;
		createuser.birthDate = new Date(0);
		createuser.gender = true;
		createuser.email = "testuser@breffi.ru";
		createuser.password = oldPassword;
		createuser.phone = "89188628062";
		createuser.location = "Stavropol";
		createuser.name = "dsfsd";
		return createuser;
	}
	
	User updatedUser(String id){
		User createuser = new CreateUser();
		createuser.id = id;
		createuser.birthDate = new Date();
		createuser.gender = true;
		createuser.email = "newtestuser@breffi.ru";
		createuser.username = userName;
		createuser.phone = "89188628060";
		createuser.location = "NewYork";
		createuser.name = "dsfsd";
		return createuser;
	}
	
}
