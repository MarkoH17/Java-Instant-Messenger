
//Program:			WrongKeyRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the ability to reject a peer connection when the common

import javax.swing.JOptionPane;

public class WrongKeyRequest extends Request {

	public WrongKeyRequest(byte[] userKeyHash) {
		super(userKeyHash);
	}

	@Override
	void process() {
		JOptionPane.showMessageDialog(super.getMessenger(), "Wrong Key!", "JIM - Security Error",
				JOptionPane.ERROR_MESSAGE);
		super.getMessenger().dispose();
		new ViewUsers();
	}
}