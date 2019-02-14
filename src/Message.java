
//Program:			Message.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the message object used for sending and receiving message objects.

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class Message implements Serializable {

	private byte[] keyHash;
	private byte[] message = new byte[] {};
	private String name;

	public Message(String name, String message, String encKey) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException();
		} else if (message == null || message.isEmpty()) {
			throw new IllegalArgumentException();
		} else if (encKey == null || encKey.isEmpty()) {
			throw new IllegalArgumentException();
		}

		this.name = name;

		MessageUtils messageUtils;
		messageUtils = new MessageUtils(encKey);

		this.keyHash = MessageUtils.generateHash(encKey);

		try {
			this.message = messageUtils.encryptMessage(message);
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getMessage(String encKey) {
		MessageUtils messageUtils = new MessageUtils(encKey);
		String decryptedMessage = "";

		if (!Arrays.equals(keyHash, MessageUtils.generateHash(encKey))) {
			return decryptedMessage;
		}

		try {
			decryptedMessage = new String(messageUtils.decryptMessage(message), "UTF-8");
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return decryptedMessage;
	}

	public String getName() {
		return name;
	}
}
