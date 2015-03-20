package shared.model.game;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
	//list of messages for chat and log
	
	public ArrayList<MessageLine> lines;
	
	public MessageList(){
		lines = new ArrayList<MessageLine>();
	}
	public MessageList(ArrayList<MessageLine> messages){
		lines = messages;
	}
	
	public void addLine(MessageLine line){
		lines.add(line);
	}
}
