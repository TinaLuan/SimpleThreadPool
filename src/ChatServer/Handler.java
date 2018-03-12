/*
 * Tian Luan 1899271
 * based on given code
 */
package ChatServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import Protocol.SimpleProtocol;

public class Handler implements Runnable{
	
	private Socket socket;
	private SimpleProtocol protocol = new SimpleProtocol();		// pack and unpack messages
	
	public Handler(Socket socket) {
		super();
		this.socket = socket;
	}
	
	private HashMap<String, String> accounts;
	private ArrayList<Message> msgList;
	private String currentUserName = null;
	
	public Handler(Socket socket, HashMap<String, String> accounts, ArrayList<Message> msgList) {
		this(socket);
		this.accounts = accounts;
		this.msgList = msgList;
	}


	@Override
	public void run() {
		System.out.println("Starting handler...");
		try {
			// creates reader/writer pairs.
			PrintWriter toClient = new PrintWriter(socket.getOutputStream());
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String response = "";
			
			while(true) {

				String read = fromClient.readLine();

				String[] request = protocol.decodeMessage(read);

				
				if (request == null || request.length < 1) {
					toClient.println(response);
					toClient.flush();
				}else{
					String reqType = request[0];
					switch (reqType) {
					case "sign-in":
						response = signin(request);
						break;
					case "sign-up":
						response = signup(request);
						break;
					case "get-message":
						response = getMessage(request);
						break;
					case "send-message":
						response = sendMessage(request);
						break;
					default:
						break;
					}
					
					toClient.println(response);
					toClient.flush();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
					
	}
	
	
	private String signin(String[] request) {
		String userName = request[1], password = request[2];
		if ( ! accounts.containsKey(userName))
			return protocol.createMessage("sign-in", "false", "The username doesn't exist");
		if (! accounts.get(userName).equals(password))
			return protocol.createMessage("sign-in", "false", "The password doesn't match username");
		currentUserName = userName;
		return protocol.createMessage("sign-in", "true", "Welcome back, "+ userName);
	}
	
	private String signup(String[] request) {
		String userName = request[1], password = request[2];
		String boolStr = "false", msg = "";
		if (  accounts.containsKey(userName)) 
			msg = "The username has been taken";
		else if (userName.length() > Server.USERNAME_MAX_LEN || userName.length() < Server.USERNAME_MIN_LEN) 
			msg = "The username must be between 5 and 20 chars long";
		else if (password.length() > Server.PASSWORD_MAX_LEN || password.length() < Server.PASSWORD_MIN_LEN)
			msg = "The password must be between 8 and 32 chars long";
		else {
			accounts.put(userName, password);
			boolStr = "true";
			msg ="Sign-up Succeeded";
		}

		return protocol.createMessage("sign-up", boolStr, msg);
	}
	
	private String getMessage(String[] request) {
		
		int offset = Integer.parseInt(request[1]);
		String response = protocol.createMessage("get-message");
		for (int i = offset+1; i < msgList.size(); i++) {
			Message oneMsg = msgList.get(i);
			response += protocol.createMessage("", String.valueOf(i), oneMsg.getUserName(), oneMsg.getTime(), oneMsg.getContent());
		}
		return response;
	}
	
	private String sendMessage(String[] request) {
		if (request.length > 1 && !request[1].equals("")) {
			String offset = String.valueOf(msgList.size());
			
			// get time
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
			LocalDateTime now = LocalDateTime.now();
			
			Message newMsg = new Message(offset, currentUserName,  dtf.format(now), request[1]);
			msgList.add(newMsg);
			return protocol.createMessage("send-message", "true", offset);
		} else {
			return protocol.createMessage("send-message", "false", "Msg contenct can't be empty");
		}
	}
}
