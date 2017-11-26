# StoryCLM Java SDK

StoryCLM Java SDK позволяет легко интегрировать систему [StoryCLM](http://breffi.ru/ru/storyclm) c Вашим приложением на Java.
Данная библиотека сделана на базе [REST API](https://github.com/storyclm/documentation/blob/master/RESTAPI.md) сервиса [StoryCLM](http://breffi.ru/ru/storyclm) и максимально упращает работу с API.

Ниже будет рассказано как устаносить библиотеку, настроить проект для работы с ней и показана работа SDK на конкретных примерах.

## Установка

Библиотека StoryCLM Java SDK доступна в центральном maven репозитории.

При использовании системы maven в вашем проекте в pom файле укажите 

      <dependency>
    	   	<groupId>com.storyclm</groupId>
       	    <artifactId>storyclmsdk</artifactId>
       	    <version>1.0.1</version>
    	    <scope>compile</scope>
      </dependency>

Если используется система grandle (например, в проектах для Android), необходимо сначала указать локальный maven репозиторий в качестве источника. Для этого в файле  build.gradle добавьте в свойство repositories:
    
    repositories {
      mavenCentral()
    }

Затем в dependencies добавьте ссылку на проект:

    dependencies {
        compile 'com.storyclm:storyclmsdk:1.0.1'
    }


## Настройка и получение ключей

**Подключение пространств имен**

В файл, в котором Вы хотите использовать SDK, необходимо добавить следующий код:

```java
import ru.breffi.storyclmsdk.*;
import ru.breffi.storyclmsdk.Exceptions.*;
```
**Активация API и получение ключей доступа**

Для того что бы получить доступ к API своего клиента нужно его активировать на панели администрирования и получить ключ доступа. 

*Что бы узнать как активировать API, получить ключи доступа и узнать подробную информацию о аутентификации и авторизации в системе нужно ознакомиться с документацией по [REST API](https://github.com/storyclm/documentation/blob/master/RESTAPI.md#Активация).*

## Аутентификация

С помощью фасадного класса *StoryCLMConnectorsGenerator* для клиента в StoryCLM создается коннектор.

    public static StoryCLMServiceConnector GetStoryCLMServiceConnector(String сlientId, String сlientSecret, String username, String password, GsonBuilder gbuilder);
 
**Описание:**

По аутентификационным данным создает коннектор для доступа к данным определенного клиента StoryCLM.
При каждом запросе к StoryCLM автоматически проверяется наличие и актуальность токена и при необходимости запрашивается новый токен, либо продлевается старый.

**Параметры:**
* clientId - идентификатор клиента.
* clientSecret - ключ доступа к API.
* username - логин пользователя, под учеткой которого подключается приложение
* password - пароль пользователя.
* gbuilder -  В SDK для сериализации/десериализации объектов используется библиотека [GSON](https://github.com/google/gson).В данном параметре передаются специфические настройки *GsonBuilder*.

В случае отсутствия *username* и *password* указывается *null*. При этом будет использоваться аутентификация типа *Service*. Для аутентификации приложений типа *Application* необходмио указать *username* и *password* отличными от нуля.


**Возвращаемое значение:**

коннектор для доступа к данным

Пример 

    StoryCLMServiceConnector clientConnector = GetStoryCLMServiceConnector("your_сlientId", "your_сlientSecret","user_login","user_password", null);

В данном примере дополнительная настройка конвертации *GsonBuilder* не требуется.

Коннектор используется для получения сервисов доступа к контенту (презентации, слайды, медиафайлы) и доступа к таблицам.

### Токен доступа 

Коннектор предоставляет сервисы работы с системой StoryCLM, которые описаны ниже. Помимо этого он позволяет получить токен доступа для авторизации на ресурсах, использующих  StoryCLM SSO (Single Sign-On). Для этого используется метод *getAccessTokenManager()* , возвращающий объект класса *AccessTokenManager*. Данный менеджер используется для аутентификации в StoryCLM. Его метод *getAuthority()* возвращает данные для авторизации, в том числе токен доступа. Перед возвращением токен проверяется на время жизни и при необходимости обновляется.

Пример 
    
    StoryCLMServiceConnector clientConnector = StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("your_сlientId", "your_сlientSecret", "login", "password", null);
    AccessTokenManager accessTokenManager = clientConnector.getAccessTokenManager();
    String access_token = accessTokenManager.checkAndReturnAuthEntityAsync().GetResult().access_token;

### Сервис доступа к контенту
Коннектор, полученный при аутентификации на предыдущем шаге позволяет получить сервис доступа к контенту. Используется следующий метод:

    public StoryCLMContentService GetContentService()

**Описание:**
Метод получения сервиса доступа к контенту.

**Параметры**
отсутствуют

**Возвращаемое значение** 
Сервис доступа к контенту StoryCLM. Данный сервис предоставляет ряд методов для получения информации о клиентах, презентациях, слайдах, медиафайлах и .д.

**Пример**
      
      StoryCLMContentService contentService = clientConnector.GetContentService();

**Методы доступа к контенту**

Все методы сервисов доступа возвращают объект удовлетворяющий интерфейсу IAsyncResult<T>, который позволяет получать данные в асинхронном режиме. Примеры использования в [интеграционных тестах](https://github.com/storyclm/Java-SDK/tree/master/src/test/java/breffi/storyclm/maven/storyclmsdk).

    public interface IAsyncResult<T> {

	    /**
	     * Возвращает данные в синхронном режиме. Результат непосредственно необходимое значение 
	     * @return
	     * @throws AsyncResultException - ошибка при получении данных
	     * @throws AuthFaliException - ошибка аутентификации
	     */
	    public T GetResult()  throws AsyncResultException, AuthFaliException;

	    /**
	     * Метод асинхронного доступа к данным. Не блокирует вызывающий поток.
	     * Результаты запроса передаются объекту callback
	     * @param callback 
	     * Объект обратного вызова. 
	     * При успешном получении данных вызывается метод OnSuccess(T result), куда передается полученный результат
	     * При ошибке вызывается метод OnFail(Throwable t) с информацией об ошибке.
	     */
	    public void OnResult(OnResultCallback<T> callback);
	
    }


#### Метод IAsyncResult<Client[]> GetClients()

**Описание**

Возвращает информацию о клиентах, которым принадлежит пользователь. [Описание типа Client](https://github.com/storyclm/Java-SDK/blob/master/src/main/java/ru/breffi/storyclmsdk/Models/Client.java)

#### Метод IAsyncResult\<Client> GetClient(int clientid)

**Описание**
Возвращает информацию о клиенте с идентификатором *clientId*

#### Метод IAsyncResult\<StoryPresentation> GetPresentation(int presentationid)

**Описание**
Возвращает информацию о презентации с идентификатором *presentationid*.  [Описание типа StoryPresentation](https://github.com/storyclm/Java-SDK/blob/master/src/main/java/ru/breffi/storyclmsdk/Models/StoryPresentation.java)


#### Метод IAsyncResult\<PresentationUser[]> AddPresentationUsers(int presentationId, String[] usersIds)
**Описание**
Добавляет пользователей к презентации. Возвращает список пользователей, которым доступна презентация.

#### Метод IAsyncResult\<PresentationUser[]> RemovePresentationUsers(int presentationId, String[] usersIds)
**Описание**
Удаляет пользователей из презентации. Возвращает список пользователей, которым доступна презентация.

#### Метод IAsyncResult\<PresentationUser[]> SetPresentationUsers(int presentationId, String[] usersIds)
**Описание**
Устанавливает пользователей для презентации. В результате презентация станет доступна тем и только тем пользователям, которые указаны в аргументе *usersIds*.  Возвращает список пользователей, которым доступна презентация.


#### Метод IAsyncResult\<StoryMediafile> GetMediafile(int mediafileId)

**Описание**
Возвращает информацию о медиафайле с идентификатором *mediafileId*.  [Описание типа StoryMediafile](https://github.com/storyclm/Java-SDK/blob/master/src/main/java/ru/breffi/storyclmsdk/Models/StoryMediafile.java)

#### Метод IAsyncResult\<StorySlide> GetSlide(int slideId)

**Описание**
Возвращает информацию о слайде с идентификатором *slideId*.  [Описание типа StorySlide](https://github.com/storyclm/Java-SDK/blob/master/src/main/java/ru/breffi/storyclmsdk/Models/StorySlide.java)
 
 #### Метод IAsyncResult\<StoryContentPackage> GetSlide(int presentationId)

**Описание**
Возвращает информацию о пакете презентации с идентификатором *presentationId*.  [Описание типа StoryContentPackage](https://github.com/storyclm/Java-SDK/blob/master/src/main/java/ru/breffi/storyclmsdk/Models/StoryContentPackage.java)

### Сервис доступа к данным пользователя

Коннектор (класс *StoryCLMServiceConnector*), полученный при аутентификации позволяет получить сервис доступа к пользовательским данным. Используется следующий метод:

    public StoryCLMUserService GetUserService()

**Описание:**
Метод получения сервиса доступа к пользовательским данным.

**Параметры**
отсутствуют

**Возвращаемое значение** 
Сервис доступа к пользователям StoryCLM. Данный сервис предоставляет ряд методов для получения информации о пользователях, и управления ими.

**Пример**
      
      StoryCLMUserService userService = clientConnector.GetUserService();

**Методы доступа к данным пользователя**

#### Метод  IAsyncResult\<CreateUser> Create(CreateUser user)
**Описание**
Создает пользователя в StoryCLM по модели *user*
		
**Параметры**
CreateUser user - данные пользователя

**Возвращаемое значение**  
Объект класса *CreateUser*, содержащий данные о созданном пользователе, включая полученный идентификатор (поле id)

#### Метод  IAsyncResult\<User> Update(User user)
**Описание**
Обновляет данные пользователя в StoryCLM по модели *user*
		
**Параметры**
User user - новые данные пользователя

**Возвращаемое значение**  
Объект класса *User*, содержащий обновленные данные

#### Метод  IAsyncResult\<SimpleUser[]> GetUsers()
**Описание**
Возвращает список краткой информации о всех пользователях клиента
		
**Параметры**
отсутствуют

**Возвращаемое значение**  
Массив SimpleUser[], содержащий краткую информацию о пользователях

#### Метод  IAsyncResult\<User> Exists(String username)
**Описание**
Проверяет наличие пользователя в системе StoryCLM
		
**Параметры**
String username - имя пользователя

**Возвращаемое значение**  
Объект класса *User*, если пользователь существует и null в противном случае

#### Метод  IAsyncResult\<User> Get(String userId)
**Описание**
Возвращает пользователя с идентификатором userId
		
**Параметры**
String userId - идентификатор пользователя

**Возвращаемое значение**  
Объект класса *User*, содержащий данные пользователя


#### Метод  IAsyncResult\<Void> UpdatePassword(String userId, Password password)
**Описание**
Обновляет пароль пользователя с идентификатором userId
		
**Параметры**
String userId - идентификатор пользователя,
Password password - новый пароль

**Возвращаемое значение**  
Void



#### Метод IAsyncResult\<User> AddToGroup(String userId, Integer groupId)
**Описание**
Добавляет пользователя к группе
		
**Параметры**
String userId - идентификатор пользователя,
 Integer groupId - идентификатор группы

**Возвращаемое значение**  
Void


#### Метод  IAsyncResult\<User> RemoveFromGroup(String userId, Integer groupId)
**Описание**
Удаляет пользователя из группы
		
**Параметры**
String userId - идентификатор пользователя,
 Integer groupId - идентификатор группы

**Возвращаемое значение**  
Void



#### Метод IAsyncResult<Void> AddToPresentation(String userId, Integer presentationId)
**Описание**
Добавляет пользователя к презентации
		
**Параметры**
String userId - идентификатор пользователя,
 Integer presentationId - идентификатор презентации

**Возвращаемое значение**  
Void


#### Метод  IAsyncResult\<Void> RemoveFromPresentation(String userId, Integer presentationId)
**Описание**
Удаляет пользователя из презентации
		
**Параметры**
String userId - идентификатор пользователя,
 Integer presentationId - идентификатор презентации

**Возвращаемое значение**  
Void	 

#### Метод  IAsyncResult\<Presentation[]> GetPresentations( String userId)
**Описание**
 возвращает презентации, в которые добавлен пользователь

**Параметры**
String userId - идентификатор пользователя

**Возвращаемое значение**  
Массив презентаций	 Presentation[]


#### Метод   IAsyncResult\<int[]> AddToPresentations(String userId, Integer[] presentationsIds)
**Описание**
Добавляет пользователя к списку презентаций. 

**Параметры**
String userId - идентификатор пользователя. 
Integer[] presentationsIds - список идентификаторов презентаций.

**Возвращаемое значение**  
Идентификаторы презентаций, доступных пользователю

#### Метод  IAsyncResult\<int[]> RemoveFromPresentations( String userId, Integer[] presentationsIds){
**Описание**
Удаляет пользователя из списка презентаций. 

**Параметры**
String userId - идентификатор пользователя. 
Integer[] presentationsIds - список идентификаторов презентаций.

**Возвращаемое значение**  
Идентификаторы презентаций, доступных пользователю.

#### Метод  IAsyncResult\<int[]> SetUserPresentations( String userId, Integer[] presentationsIds){
**Описание**
Устанавливает список доступных для пользователя презентаций. В результате пользователю будут доступны те и только те презентации, идентификаторы которых указаны в аргументе *presentationsIds*.

**Параметры**
String userId - идентификатор пользователя. 
Integer[] presentationsIds - список идентификаторов презентаций.

**Возвращаемое значение**  
Идентификаторы презентаций, доступных пользователю.

#### Метод   IAsyncResult\<Group[]> GetGroups(String userId)
**Описание**
 возвращает группы, в которые добавлен пользователь

**Параметры**
String userId - идентификатор пользователя

**Возвращаемое значение**  
Массив групп	 Group[]
	

### Сервисы доступа к таблицам

Метод *GetTableService* коннектора (класс *StoryCLMServiceConnector*), служит фабрикой для получения сервисов строготипизированного доступа к конкретным таблицам клиента. Сигнатура метода:

    public <T> StoryCLMTableService<T> GetTableService(Type entityType, int tableId)

**Описание:**

Метод-фабрика сервиса строготипизированного доступа к табличным данным StoryCLM.

**Параметры:**
* entityType - тип данных соответствующий табличным данным.
* tableId - идентификатор таблицы


**Возвращаемое значение:**

Сервис строготипизированного доступа к табличным данным StoryCLM.

**Пример** (используется clientConnector, полученный в предыдущем примере):

    StoryCLMTableService<StoryLog> slogService = clientConnector.GetTableService(StoryLog.class, tableId);
  
**Примечание**

Каждый сервис соответствует одной таблице, представляя ее данные как объекты указанного типа. В качестве типа можно использовать набор ключ/значение (Map<String,Object>). Для этого используется класс [TypeToken<T>](http://google.github.io/gson/apidocs/com/google/gson/reflect/TypeToken.html) библиотеки [GSON](https://github.com/google/gson). Создание сервиса выглядит следующим образом:

    StoryCLMTableService<Map<String,Object>> dynamicTable = clientConnector.GetTableService(new TypeToken<Map<String,Object>>{}.getType(), tableId);
  
## Таблицы


[Таблицы](https://github.com/storyclm/documentation/blob/master/TABLES.md) - это реляционное хранилище данных.

*Более подробная информация содержится в разделе ["Таблицы"](https://github.com/storyclm/documentation/blob/master/TABLES.md) документации.*

Для получения списка таблиц используется метод *GetTables* класса *StoryCLMServiceConnector*:

#### Method: IAsyncResult<ApiTable[]> GetTables(int clientid)

**Описание:**

Получает cписок таблиц клиента.

**Параметры:**

* clientId - Идентификатор клиента в базе данных.

**Возвращаемое значение:**

Список таблиц в виде массива объектов ApiTable.

**Пример**

```java
    StoryCLMServiceConnector clientConnector = GetStoryCLMServiceConnector("your_сlientId", "your_сlientSecret", "username","password", null);
    ApiTable[] tables = clientConnector.GetTables(int 18);
```
  
**Методы сервиса StoryCLMTableService<T>**

Методы сервиса соответствуют [REST API](https://github.com/storyclm/documentation/blob/master/RESTAPI.md) StoryCLM и возвращают объект асинхронного вызова, реализующий интерфейс IAsyncResult. 
С помощью данного объекта можно получить ответ синхронно (метод GetResult), либо подписаться на получение ответа (метод OnResult). 
Обращаем ваше внимание на то, что методы не являются обобщенными (generic). Тип *Т* получает значение при создании объекта обобщенного класса *StoryCLMTableService\<T>*


#### Method: IAsyncResult<T> Insert(T record)

**Описание:**

Добавляет новый объект в таблицу.
Объект должен соответствовать схеме таблицы.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* record - Новый объект.

**Возвращаемое значение:**

Новый объект в таблице.

**Пример:**

предположим у нас имеется сервис, полученный из рассмотренных выше примеров:

```java
    StoryCLMServiceConnector clientConnector = GetStoryCLMServiceConnector("your_сlientId", "your_сlientSecret", null);
    StoryCLMTableService<StoryLog> slogService = clientConnector.GetService(StoryLog.class, tableId);
```

Класс StoryLog  выглядит следующим образом:

```java
    public class StoryLog {
        public int Updated;
        public int Inserted;
        public int Deleted;
        public String Errors;
        }
```

Чтобы использовать данный класс в панели администрирования необходимо создать таблицы с соотвествующими полями.


```Java
StoryLog profile = slogService.InsertAsync(new StoryLog()).GetResult();
```

#### Method: IAsyncResult<List\<T>> InsertMany(T[] records)

**Описание:**

Добавляет коллекцию новых объектов в таблицу.
Каждая объект должен соответствовать схеме таблицы.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* records - коллекция новых объектов

**Возвращаемое значение:**

Коллекция новых объектов.

**Пример:**
```java
StoryLog[] logs = new StoryLog[2];
logs[0] = new StoryLog();
logs[1] = new StoryLog();
List<StoryLog> profiles = slogService.InsertMany(logs).GetResult();
```

#### Method:  IAsyncResult\<T> Update(T record)

**Описание:**

Обновляет объект в таблице.
Идентификатор записи остается неизменным.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* record - обновляемый объект.

**Возвращаемое значение:**

Обновленный объект.

**Пример:**
```java
StoryLog log = slogService.Update(logs[0]).GetREsult();
```

#### Method:  IAsyncResult<List\<T>> UpdateMany(T[] records)

**Описание:**

Обновляет коллекцию объектов в таблице.
Идентификатор записи остается неизменным.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* records - коллекция обновляемых объектов.

**Возвращаемое значение:**

Коллекция обновленных объектов.

**Пример:**
```java
List<StoryLog> log = slogService.UpdateMany(logs);
```

#### Method: IAsyncResult<ApiLog[]> Delete(String id)

**Описание:**

Удаляет объект таблицы по идентификатору.

**Параметры:**
* id - Идентификатор записи.

**Возвращаемое значение:**

Удаленный объект.

**Пример:**
```java
ApiLog[] deleteResult = slogService.Delete(log._id).GetResult();
```

#### Method: IAsyncResult<ApiLog[]> Delete(String[] ids)

**Описание:**

Удаляет коллекцию объектов таблицы по поллекции идентификаторов.

**Параметры:**
* ids - массив идентификаторов.

**Возвращаемое значение:**

Коллекция удаленных объектов.

**Пример:**
```java
String[] logids = new String[]{"id1","id2"};
ApiLog[] deleteResult = slogService.Delete(logids).GetResult();
```

#### Method: IAsyncResult\<Integer> Count()

**Описание:**

Получает колличество записей в таблице.

**Параметры:**
Отсутствуют

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```java
Integer count = slogService.Count().GetResult();
```

#### Method: IAsyncResult\<Integer> CountByQuery(String query)

**Описание:**

Получает колличество записей в таблице по запросу.
Запрос должен быть в формате [TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md).

**Параметры:**
* query - запрос в формате [TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md)

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```java
Integer count = slogService.CountByQuery("[age][gt][30]").GetResult();
```

#### Method:  IAsyncResult\<Integer> CountByLog()

**Описание:**

Получает колличество записей лога таблицы.

**Параметры:**
Отсутствуют

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```java
Integer count = slogService.CountByLog().GetResult();
```

#### Method: IAsyncResult\<Integer> CountByLog(Date date)

**Описание:**

Получает колличество записей лога после указанной даты.

**Параметры:**
* tableId - Идентификатор таблицы в базе данных.
* date - Дата, после которой будет произведена выборка.

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```java
Integer count = slogService.CountByLog(new Date(0)).GetResult();
```

#### Method: IAsyncResult<ApiLog[]> Log(int skip , int take)

**Описание:**

Получает записи лога.

**Параметры:**
* skip - Отступ в запросе. Сколько первых элементов нужно пропустить. По умолчанию - 0.
* take - Максимальное количество записей, которые будут получены. По умолчанию - 100, максимально 1000.

**Возвращаемое значение:**

Коллекция записей лога.

**Пример:**
```java
ApiLog[]  logs= slogService.Log(0,1000).GetResult();
```

#### Method: IAsyncResult<ApiLog[]> Log(Date date, int skip , int take)

**Описание:**

Получает записи лога, после указаной даты.

**Параметры:**
* date - Дата, после которой будет произведена выборка.
* skip - Отступ в запросе. Сколько первых элементов нужно пропустить. По умолчанию - 0.
* take - Максимальное количество записей, которые будут получены. По умолчанию - 100, максимально 1000.

**Возвращаемое значение:**

Коллекция записей лога.

**Пример:**
```java
SCLM sclm = SCLM.Instance;
ApiLog[] logs = slogService.Log(new Date(0),0,1000).GetResult();
```

#### Method: IAsyncResult<List\<T>> Find(String id)

**Описание:**

Получает запись таблицы по идентификатору.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.

**Возвращаемое значение:**

Объект в таблице.

**Пример:**
```java
StoryLog slog =  slogService.Find(id).GetResult();
```

#### Method:  IAsyncResult<List\<T>> Find(String[] ids)

**Описание:**

Получает коллекцию записей по списку идентификаторов.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* ids - коллекция идентификаторов.

**Возвращаемое значение:**

Коллекция объектов.

**Пример:**
```java
List<StoryLog> slog =  slogService.Find(id).GetResult();
```

#### Method: IAsyncResult<List\<T>>  Find(int skip , int take)

**Описание:**

Получает постранично все данные таблицы.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* skip - Отступ в запросе. Сколько первых элементов нужно пропустить. По умолчанию - 0.
* take - Максимальное количество записей, которые будут получены. По умолчанию - 100, максимально 1000.

**Возвращаемое значение:**

Коллекция объектов.

**Пример:**
```java
List<StoryLog> slog =  slogService.Find(0,1000).GetResult();
```

#### Method: IAsyncResult<List\<T>> Find(String query, String sortfield, int sort,   int skip , int take)

**Описание:**

Получает постранично данные по запросу.
Формат запроса - [TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md).

[TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md) - это язык запросов, разработанного специально для StoryCLM. 
Запрос в данном формате легко транслируется в любы другие языки запросов.

Параметры из которых создается запрос могут быть двух типов: Comparison и Logical.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* query - Запрос в формате [TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md).
* sortfield - Поле, по которому нужно произвести сортировку.
* sort - Тип сортировки.
* skip - Отступ в запросе. Сколько первых элементов нужно пропустить. По умолчанию - 0.
* take - Максимальное количество записей, которые будут получены. По умолчанию - 100, максимально 1000.

**Возвращаемое значение:**

Коллекция объектов.

**Пример:**
```java
StoryCLMServiceConnector clientConnector=  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("****", "****",null);
StoryCLMTableService<Profile> StoryCLMProfileService = clientConnector.GetService(Profile.class, 23);
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
		
```






