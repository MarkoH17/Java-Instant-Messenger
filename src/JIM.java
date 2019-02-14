//Program:			JIM.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class contains the constants used for the Java Instant Messenger (JIM), and starts the GUI 

public class JIM {

	final static int CHAT_PORT = 5704;
	final static int FILE_PORT = 5706;
	static String key;
	static MessageClient messageClient;

	static MessageServer messageServer;
	static Messenger messengerWindow;
	final static String NEIGHBORS_ADDRESS = "224.0.0.3";
	final static int NEIGHBORS_PORT = 5705;
	static User user;
	static ViewUsers viewUsersWindow;

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");

		new JoinNetwork();
	}
}
