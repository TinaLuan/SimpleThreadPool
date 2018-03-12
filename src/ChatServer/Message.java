/*
 * Tian Luan 1899271
 */
package ChatServer;

public class Message {
	private String offset;
	private String userName;
	private String time;
	private String content;
	
	public Message(String offset, String userName, String time, String content) {
		this.offset = offset;
		this.userName = userName;
		this.time = time;
		this.content = content;
	}
	
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
