
//Program:			HelloRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of sending a hello message to the partner when joining a chat session.

import java.text.MessageFormat;

import javax.swing.JTextArea;

public class HelloRequest extends Request {

	public HelloRequest(User user, String encKey) {
		super(user, MessageFormat.format("{0} joined the chat", user.getName()), encKey);
	}

	@Override
	void process() {
		String message = MessageUtils.formatMessage("", super.getMessage().getMessage(JIM.key));
		if (super.getMessenger() != null) {
			JTextArea outputViewer = super.getMessenger().textArea1;
			outputViewer.append(message);
		} else {
			System.out.println(message);
		}

	}
}
