
//Program:			DeclineFileTransferRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of declining a file transfer request.

import java.io.Serializable;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

public class DeclineFileTransferRequest extends Request implements Serializable {

	public DeclineFileTransferRequest(User decliningUser) {
		super(decliningUser);
	}

	@Override
	public void process() {
		JOptionPane.showMessageDialog(super.getMessenger(),
				MessageFormat.format("{0} did not accept your file", super.getUser().getName()),
				"JIM - Declined File Transfer", JOptionPane.INFORMATION_MESSAGE);
	}
}
