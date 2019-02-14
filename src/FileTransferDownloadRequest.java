
//Program:			FileTransferDownloadRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of requesting a file from an approved partner that sent a file transfer request.

import java.io.File;
import java.io.IOException;

public class FileTransferDownloadRequest extends Request {

	public FileTransferDownloadRequest(File file) {
		super(null, file);
	}

	@Override
	void process() {
		try {
			JIM.messageClient.sendFile(super.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
