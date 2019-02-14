
//Program:			GoodbyeRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of sending a goodbye message to the partner when leaving a chat session.

import java.text.MessageFormat;

import javax.swing.JTextArea;

public class GoodbyeRequest extends Request {

	public GoodbyeRequest(User user, String encKey) {
		super(user, MessageFormat.format("{0} left the chat", user.getName()), encKey);
	}

	@Override
	void process() {
		JTextArea outputViewer = super.getMessenger().textArea1;
		String message = MessageUtils.formatMessage("", super.getMessage().getMessage(JIM.key));
		if (outputViewer != null) {
			outputViewer.append(message);
		} else {
			System.out.println(message);
		}
	}
}
