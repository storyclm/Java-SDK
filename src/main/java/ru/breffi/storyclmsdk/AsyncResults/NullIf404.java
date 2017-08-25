package ru.breffi.storyclmsdk.AsyncResults;
import ru.breffi.storyclmsdk.OnResultCallback;
import ru.breffi.storyclmsdk.Exceptions.AsyncResultException;
import ru.breffi.storyclmsdk.Exceptions.AuthFaliException;
import ru.breffi.storyclmsdk.Exceptions.ResultServerException;
public class NullIf404<Tout> implements IAsyncResult<Tout>{

	IAsyncResult<Tout> asyncResult;
	
	
	public NullIf404(IAsyncResult<Tout> asyncResult) {
		this.asyncResult = asyncResult;
	}
	@Override
	public Tout GetResult() throws AsyncResultException, AuthFaliException {
		try{
			return asyncResult.GetResult();
		}
		catch(ResultServerException ex){
			return null;
		}
	}
	
	@Override
	 public void OnResult(final OnResultCallback<Tout> callback) {
		asyncResult.OnResult(new OnResultCallback<Tout>() {
			@Override
			public void OnSuccess(Tout result) {
				callback.OnSuccess(result);
			}

			@Override
			public void OnFail(Throwable t) {
				if (t.getClass()==ResultServerException.class && ((ResultServerException)t).Code == 404) callback.OnSuccess(null);
				else callback.OnFail(t);
				}
		});
		
	}
}
