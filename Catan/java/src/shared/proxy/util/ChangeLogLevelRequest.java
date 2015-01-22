package shared.proxy.util;
/**
 * Used to set the Server's log level
 * @author Kent
 *
 */
public class ChangeLogLevelRequest {

	/**
	 * The level to which the server's log will be set.
	 */
	private LogLevel level;

	/**
	 * Constructor to create ChangeLogLevelRequest object
	 * @param level
	 */
	public ChangeLogLevelRequest(LogLevel level) {
		super();
		this.level = level;
	}

	public LogLevel getLevel() {
		return level;
	}

	public void setLevel(LogLevel level) {
		this.level = level;
	}
	
	
}
