
//Program:			MessagingRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of a messaging request.

import java.io.IOException;
import java.util.Arrays;

public class MessagingRequest extends Request {

	public MessagingRequest(User user, byte[] userKeyHash) {
		super(user, userKeyHash);
	}

	@Override
	void process() {
		if (!Arrays.equals(super.getUserKeyHash(), MessageUtils.generateHash(JIM.key))) {
			try {
				MessageClient.sendRequestWrongKey(super.getUser().getAddress(), super.getUser().getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			new MessagingPrompt(super.getUser());
		}
	}
}