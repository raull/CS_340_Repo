package shared.proxy;

@SuppressWarnings("serial")
public class ProxyException extends Exception{
	
	public ProxyException() {
		return;
	}

	public ProxyException(String message) {
		super(message);
	}

	public ProxyException(Throwable throwable) {
		super(throwable);
	}

	public ProxyException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
