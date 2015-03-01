package shared.model.game;

public class MessageLine {
	private String message;
	private String source;
	
	public MessageLine(String message, String source) {
		super();
		this.message = message;
		this.source = source;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getSource()
	{
		return source;
	}
	
}
