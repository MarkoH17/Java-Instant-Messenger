
//Program:			Request.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the various methods and types of requests used in JIM

import java.io.File;
import java.io.Serializable;

public abstract class Request implements Serializable {

	private File file;
	private Message message;
	private Messenger messenger;
	private User user;
	private byte[] userKeyHash;

	public Request(byte[] userKeyHash) {
		this.userKeyHash = userKeyHash.clone();
	}

	public Request(User user) {
		this.user = user;
	}

	public Request(User user, byte[] userKeyHash) {
		this.user = user;
		this.userKeyHash = userKeyHash.clone();
	}

	public Request(User user, File file) {
		this.user = user;
		this.file = file;
	}

	public Request(User user, String message, String encKey) {
		this.user = user;
		this.message = new Message(user.getName(), message, encKey);
	}

	public File getFile() {
		return file;
	}

	public Message getMessage() {
		return message;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public User getUser() {
		return user;
	}

	public byte[] getUserKeyHash() {
		return userKeyHash.clone();
	}

	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}

	abstract void process();
}
