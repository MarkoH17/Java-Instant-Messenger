
//Program:			DeclineMessagingRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of declining a messaging request.

import java.io.Serializable;
import java.text.MessageFormat;

import javax.swing.JTextArea;

public class DeclineMessagingRequest extends Request implements Serializable {

	public DeclineMessagingRequest(User decliningUser) {
		super(decliningUser);
	}

	@Override
	void process() {
		JTextArea outputViewer = super.getMessenger().textArea1;
		Messenger messenger = super.getMessenger();
		if (outputViewer != null) {
			outputViewer.append(MessageUtils.formatMessage(super.getUser().getName(), "Can't message right now..."));
			messenger.disableMessaging();
		} else {
			System.out.println(MessageFormat.format("{0}: Can't message right now...", super.getUser().getName()));
		}
	}
}
