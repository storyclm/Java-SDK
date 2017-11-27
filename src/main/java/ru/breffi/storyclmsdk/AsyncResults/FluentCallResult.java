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
 */
public class FluentCallResult<Tprev, Tcurrent> implements IAsyncResult<Tcurrent> {

	IAsyncResult<Tprev> prevReq;

	CallCreator<Tprev,IAsyncResult<Tcurrent>> creator;

	public FluentCallResult(IAsyncResult<Tprev> prevReq, CallCreator<Tprev,IAsyncResult<Tcurrent>>creator){
		this.prevReq = prevReq;
		this.creator = creator;
	}
	
	public <Tnext> FluentCallResult<Tcurrent, Tnext> Then(CallCreator<Tcurrent,IAsyncResult<Tnext>> creator){
		return new FluentCallResult<Tcurrent, Tnext>(this,creator); 
	}
	
	public <Tnext> FluentCallResult<Tcurrent, Tnext> ThenResult(CallCreator<Tcurrent,Tnext> syncResultCreator){
		return Then(current->new ValueAsyncResult<Tnext>(syncResultCreator.Create(current)));
	}
	
	
	public static <T> FluentCallResult<Void, T> AtFirst(IAsyncResult<T> firstResult){
		return new FluentCallResult<>(null, (n)->firstResult);
	}
	
	@Override
	public Tcurrent GetResult() throws AsyncResultException, AuthFaliException {
		Tprev previousResult = (prevReq==null)? null:prevReq.GetResult();
		return creator.Create(previousResult).GetResult();
	}

	@Override
	public void OnResult(final OnResultCallback<Tcurrent> callback) {
		if (prevReq== null) 
			creator.Create(null).OnResult(callback);
		else
			prevReq.OnResult(new OnResultCallback<Tprev>() {
	
				@Override
				public void OnSuccess(Tprev result) {
					creator.Create(result).OnResult(callback);
					
				}
	
				@Override
				public void OnFail(Throwable t) {
					callback.OnFail(t);
					
				}
			});
	}


	
}
