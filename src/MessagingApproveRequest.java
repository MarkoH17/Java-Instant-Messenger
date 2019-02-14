//Program:			MessagingApproveRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of approving a messaging request

public class MessagingApproveRequest extends Request {
	boolean prompted = false;

	public MessagingApproveRequest(User user) {
		super(user);
	}

	@Override
	void process() {
		if (prompted) {
			Messenger messenger = new Messenger(
					new User(super.getUser().getName(), super.getUser().getAddress(), super.getUser().getPort()), true);
			JIM.messengerWindow = messenger;
			JIM.viewUsersWindow.dispose();
		} else {
			Messenger messenger = new Messenger(
					new User(super.getUser().getName(), super.getUser().getAddress(), super.getUser().getPort()),
					false);
			JIM.messengerWindow = messenger;
			JIM.viewUsersWindow.dispose();
		}
	}

	void setPrompted(boolean prompted) {
		this.prompted = prompted;
	}
}