package ru.breffi.storyclmsdk.AsyncResults;


import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;


public abstract class SequanceCallResult<Tfirst,Tnext> implements IAsyncResult<Tnext> {

	IAsyncResult<Tnext> nextCall;
	IAsyncResult<Tfirst> firstCall;
	//IMiddleCallBack<Tfirst, MiddleCallBackResult<Tnext>> middleCallback;
	
	public SequanceCallResult(IAsyncResult<Tfirst> firstCall , IAsyncResult<Tnext> nextCall){
		this.nextCall = nextCall;
		//this.middleCallback = middleCallback;
		this.firstCall = firstCall;
	}
	
	@Override
	public Tnext GetResult() throws AsyncResultException, AuthFaliException {
	
		//первый вызов
			Tfirst firstResult = firstCall.GetResult();
			
		//миддл метод
			
		
				MiddleCallBackResult<Tnext> r =  MiddleCallBack(firstResult);
				if (r.useResult) return r.result;
		
			
		//второй вызов
			return nextCall.GetResult();

	}
	
	public abstract MiddleCallBackResult<Tnext> MiddleCallBack(Tfirst firstResult);

	@Override
	public void OnResult(final OnResultCallback<Tnext> callback) {
		firstCall.OnResult(new OnResultCallback<Tfirst>() {
			@Override
			public void OnSuccess(Tfirst result) {
					MiddleCallBackResult<Tnext> r =  MiddleCallBack(result);
					if (r.useResult) callback.OnSuccess(r.result);
					else nextCall.OnResult(callback);
			}

			@Override
			public void OnFail(Throwable t) {
				callback.OnFail(t);
			}  		
		});
	}

}
