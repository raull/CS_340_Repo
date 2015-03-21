package shared.model.game;

import java.util.ArrayList;
import java.util.List;

public class MessageList {
	//list of messages for chat and log
	
	private ArrayList<MessageLine> lines;
	
	public MessageList(){
		lines = new ArrayList<MessageLine>();
	}
	
	public MessageList(ArrayList<MessageLine> messages){
		lines = messages;
	}

	public ArrayList<MessageLine> getLines() {
		return lines;
	}

	public void setLines(ArrayList<MessageLine> lines) {
		this.lines = lines;
	}
	
	
	public void addMessage(MessageLine message)
	{
		lines.add(message);
	}
	
	public void addLine(MessageLine line){
		lines.add(line);
	}
}
