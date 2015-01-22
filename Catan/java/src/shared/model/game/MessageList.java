package shared.model.game;

import java.util.ArrayList;

public class MessageList {
	//list of messages for chat and log
	
	ArrayList<MessageLine> lines;
	
	class MessageLine {
		String message;
		String source;
	}
}
