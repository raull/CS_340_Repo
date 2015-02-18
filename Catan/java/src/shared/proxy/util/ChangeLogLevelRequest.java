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
	private LogLevel logLevel;

	/**
	 * Constructor to create ChangeLogLevelRequest object
	 * @param level
	 */
	public ChangeLogLevelRequest(LogLevel level) {
		super();
		this.logLevel = level;
	}

	public LogLevel getLevel() {
		return logLevel;
	}

	public void setLevel(LogLevel level) {
		this.logLevel = level;
	}
	
	
}
