package ru.breffi.storyclmsdk.AsyncResults;


import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;

/**
 * Класс последовательно вызывающий несколько асинхронных вызовов
 * В методе GetInnerAsyncResult задается логика следующего вызова
 * Данный класс не лучший способ разбиения запроса.
 * Лучшей альтернативой было бы построение параллельных запросов
 * @author tselo
 *
 * @param <T>
 */
public abstract class SequanceChainCallResult<T> implements IAsyncResult<T> {


	T previousResult;
	IMiddleCallBack<T,IAsyncResult<T>>  asyncCallResultGetter;
	
	/*public SequanceChainCallResult(IMiddleCallBack<T,IAsyncResult<T>> asyncCallResultGetter, T previousResult){
		this.asyncCallResultGetter = asyncCallResultGetter;
		this.previousResult = previousResult;
	}*/
	
	
	public SequanceChainCallResult(T previousResult){
		this.previousResult = previousResult;
	}
	
	@Override
	public T GetResult() throws AsyncResultException, AuthFaliException {
		IAsyncResult<T> innerCallResult= GetInnerAsyncResult(previousResult);
		if (innerCallResult.getClass() == FinalAsyncResult.class){
			return innerCallResult.GetResult();
		}
		 try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
		 
		this.previousResult = innerCallResult.GetResult();
		return this.GetResult();
		
	}

	
	public abstract IAsyncResult<T> GetInnerAsyncResult(T previouResult);
	
	@Override
	public void OnResult(final OnResultCallback<T> callback) {
		 try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
		
				e.printStackTrace();
			}
		IAsyncResult<T> innerCallResult =  this.GetInnerAsyncResult(previousResult);
		if (innerCallResult.getClass() == FinalAsyncResult.class){
			try {
				callback.OnSuccess(innerCallResult.GetResult());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		
		final SequanceChainCallResult<T> self = this;
		innerCallResult.OnResult(new OnResultCallback<T>() {
			@Override
			public void OnSuccess(final T result) {
				self.previousResult = result;
				self.OnResult(callback);
			}

			@Override
			public void OnFail(Throwable t) {
				callback.OnFail(t);
			}  		
		});
	}

}
