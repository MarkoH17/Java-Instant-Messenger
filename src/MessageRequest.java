
//Program:			MessageClient.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class serves as the wrapper request that is used to send message objects to a partner.

import java.io.Serializable;

import javax.swing.JTextArea;

public class MessageRequest extends Request implements Serializable {

	public MessageRequest(User user, String message, String encKey) {
		super(user, message, encKey);
	}

	@Override
	void process() {
		JTextArea outputViewer = super.getMessenger().textArea1;
		String message = MessageUtils.formatMessage(super.getUser().getName(), super.getMessage().getMessage(JIM.key));
		if (outputViewer != null) {
			outputViewer.append(message);
		} else {
			System.out.println(message);
		}
	}
}
