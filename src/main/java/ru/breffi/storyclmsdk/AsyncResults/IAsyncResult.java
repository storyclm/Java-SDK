package ru.breffi.storyclmsdk.AsyncResults;

import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

public interface IAsyncResult<T> {

	/**
	 * Возвращает данные в синхронном режиме. Результат непосредственно необходимое значение 
	 * @return
	 * @throws AsyncResultException  ошибка при получении данных
	 * @throws AuthFaliException ошибка аутентификации
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
