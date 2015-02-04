package shared.model.game;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
	//list of messages for chat and log
	
	List<MessageLine> lines;
	
	public MessageList(){
		lines = new ArrayList<MessageLine>();
	}
	public MessageList(List<MessageLine> messages){
		lines = messages;
	}
	
	
	class MessageLine {
		String message;
		String source;
	}
}
