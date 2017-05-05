# StoryCLM Java SDK

StoryCLM Java SDK позволяет легко интегрировать систему [StoryCLM](http://breffi.ru/ru/storyclm) c Вашим приложением на Java.
Данная библиотека сделана на базе [REST API](https://github.com/storyclm/documentation/blob/master/RESTAPI.md) сервиса [StoryCLM](http://breffi.ru/ru/storyclm) и максимально упращает работу с API.

Ниже будет рассказано как устаносить библиотеку, настроить проект для работы с ней и показана работа SDK на конкретных примерах.

## Установка

Загрузите проект из данного репозитория. Для этого Перейдите в корневой каталог [репозитория](https://github.com/storyclm/Java-SDK) и нажмите на кнопку "Clone or download".

Установите проект в локальное maven хранилище командой 

    mvn clean install

  Для добавления в проект в pom файле укажите 

      <dependency>
    	    <groupId>ru.breffi</groupId>
    	    <artifactId>storyclmsdk</artifactId>
    	    <version>0.0.1-SNAPSHOT</version>
    	    <scope>compile</scope>
      </dependency>


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

**Аутентификация**

С помощью фасадного класса *StoryCLMConnectorsGenerator* для клиента в StoryCLM создается коннектор.

    public static StoryCLMServiceConnector GetStoryCLMServiceConnector(String сlientId, String сlientSecret, GsonBuilder gbuilder);

**Описание:**

По аутентификационным данным создает коннектор для доступа к данным определенного клиента StoryCLM.
При каждом запросе к StoryCLM автоматически проверяется наличие и актуальность токена и при необходимости запрашивается новый токен, либо продлевается старый.

**Параметры:**
* clientId - идентификатор клиента.
* clientSecret - ключ доступа к API.
* gbuilder -  специфические настройки *GsonBuilder* для сериализации/десериализации объектов.

**Возвращаемое значение:**

коннектор для доступа к данным

Пример 

    StoryCLMServiceConnector clientConnector = GetStoryCLMServiceConnector("your_сlientId", "your_сlientSecret", null);

В данном примере дополнительная настройка конвертации *GsonBuilder* не требуется.

**Сервисы доступа к таблицам**

Коннектор, полученный при аутентификации на предыдущем шаге служит фабрикой для получения сервисов строготипизированного доступа к конкретным таблицам клиента. Для созданмя сервиса в классе *StoryCLMServiceConnector* имеется следующий метод:

    public <T> StoryCLMServiceGeneric<T> GetService(Type entityType, int tableId)

**Описание:**

Метод-фабрика сервиса строготипизированного доступа к табличным данным StoryCLM.

**Параметры:**
* entityType - тип данных соответствующий табличным данным.
* tableId - идентификатор таблицы


**Возвращаемое значение:**

Сервис строготипизированного доступа к табличным данным StoryCLM.

Пример (используется clientConnector, полученный в предыдущем примере):

    StoryCLMServiceGeneric<StoryLog> slogService = clientConnector.GetService(StoryLog.class, tableId);
     
Каждый сервис соответствует одной таблице, представляя ее данные как объекты указанного типа.В данном примере 


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
    StoryCLMServiceConnector clientConnector = GetStoryCLMServiceConnector("your_сlientId", "your_сlientSecret", null);
    ApiTable[] tables = clientConnector.GetTables(int 18);
```
  
**Методы сервиса StoryCLMServiceGeneric\<T>**

Методы сервиса соответствуют [REST API](https://github.com/storyclm/documentation/blob/master/RESTAPI.md) StoryCLM и возвращают объект асинхронного вызова, реализующий интерфейс IAsyncResult. 
С помощью данного объекта можно получить ответ синхронно (метод GetResult), либо подписаться на получение ответа (метод OnResult). 
Обращаем ваше внимание на то, что методы не являются обобщенными (generic). Тип *Т* получает значение при создании объекта обобщенного класса *StoryCLMServiceGeneric\<T>*


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
    StoryCLMServiceGeneric<StoryLog> slogService = clientConnector.GetService(StoryLog.class, tableId);
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
```cs
StoryLog[] logs = new StoryLog[2];
logs[0] = new StoryLog();
logs[1] = new StoryLog();
List<StoryLog> profiles = slogService.InsertMany(logs).GetResult();
```

#### Method:  IAsyncResult<T> Update(T record)

**Описание:**

Обновляет объект в таблице.
Идентификатор записи остается неизменным.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* record - обновляемый объект.

**Возвращаемое значение:**

Обновленный объект.

**Пример:**
```cs
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
```cs
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
```cs
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
```cs
String[] logids = new String[]{"id1","id2"};
ApiLog[] deleteResult = slogService.Delete(logids).GetResult();
```

#### Method: IAsyncResult<Integer> Count()

**Описание:**

Получает колличество записей в таблице.

**Параметры:**
Отсутствуют

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```cs
Integer count = slogService.Count().GetResult();
```

#### Method: IAsyncResult<Integer> CountByQuery(String query)

**Описание:**

Получает колличество записей в таблице по запросу.
Запрос должен быть в формате [TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md).

**Параметры:**
* query - запрос в формате [TablesQuery](https://github.com/storyclm/documentation/blob/master/TABLES_QUERY.md)

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```cs
Integer count = slogService.CountByQuery("[age][gt][30]").GetResult();
```

#### Method:  IAsyncResult<Integer> CountByLog()

**Описание:**

Получает колличество записей лога таблицы.

**Параметры:**
Отсутствуют

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```cs
Integer count = slogService.CountByLog().GetResult();
```

#### Method: IAsyncResult<Integer> CountByLog(Date date)

**Описание:**

Получает колличество записей лога после указанной даты.

**Параметры:**
* tableId - Идентификатор таблицы в базе данных.
* date - Дата, после которой будет произведена выборка.

**Возвращаемое значение:**

Колличество записей.

**Пример:**
```cs
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
```cs
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
```cs
SCLM sclm = SCLM.Instance;
ApiLog[] logs = slogService.Log(new Date(0),0,1000).GetResult();
```

#### Method: IAsyncResult<List<T>> Find(String id)

**Описание:**

Получает запись таблицы по идентификатору.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.

**Возвращаемое значение:**

Объект в таблице.

**Пример:**
```cs
StoryLog slog =  slogService.Find(id).GetResult();
```

#### Method:  IAsyncResult<List<T>> Find(String[] ids)

**Описание:**

Получает коллекцию записей по списку идентификаторов.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* ids - коллекция идентификаторов.

**Возвращаемое значение:**

Коллекция объектов.

**Пример:**
```cs
List<StoryLog> slog =  slogService.Find(id).GetResult();
```

#### Method: IAsyncResult<List<T>>  Find(int skip , int take)

**Описание:**

Получает постранично все данные таблицы.

**Параметры:**
* T - параметризованный тип (generic), описывающий сущность в таблице.
* skip - Отступ в запросе. Сколько первых элементов нужно пропустить. По умолчанию - 0.
* take - Максимальное количество записей, которые будут получены. По умолчанию - 100, максимально 1000.

**Возвращаемое значение:**

Коллекция объектов.

**Пример:**
```cs
List<StoryLog> slog =  slogService.Find(0,1000).GetResult();
```

#### Method: IAsyncResult<List<T>> Find(String query, String sortfield, int sort,   int skip , int take)

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
```cs
StoryCLMServiceConnector clientConnector=  StoryCLMConnectorsGenerator.GetStoryCLMServiceConnector("****", "****",null);
StoryCLMServiceGeneric<Profile> StoryCLMProfileService = clientConnector.GetService(Profile.class, 23);
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






