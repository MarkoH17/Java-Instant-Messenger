
//Program:			FileTransferRequest.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the action of offering to send a file to the messaging partner

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class FileTransferRequest extends Request {

	public FileTransferRequest(User user, File fileToSend) {
		super(user, fileToSend);
	}

	private void processReceiveFile(int port, File fileToSaveTo) throws IOException {

		System.out.println(MessageFormat.format("File Server started and listening on: {0}", port));

		ServerSocket serverSocket = new ServerSocket(port);
		Socket socket = serverSocket.accept();

		while (true) {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileToSaveTo));
			int result = MessageUtils.writeToStream(bufferedInputStream, bufferedOutputStream);
			if (result == 0) {
				JOptionPane.showMessageDialog(null, "Successfully received file!", "JIM - Receive File",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Error receiving file!", "JIM - Receive File",
						JOptionPane.ERROR_MESSAGE);
			}

			break;
		}
		socket.close();
		serverSocket.close();
	}

	@Override
	void process() {
		int result = JOptionPane
				.showConfirmDialog(null,
						MessageFormat.format("{0} would like to send you a file {1}", super.getUser().getName(),
								super.getFile().getName()),
						"JIM - File Transfer Request", JOptionPane.INFORMATION_MESSAGE);
		File parentFile = super.getFile();
		Runnable fileBrowser = new Runnable() {
			@Override
			public void run() {
				if (result == JOptionPane.YES_OPTION) {
					File fileToSave;
					JFileChooser jFileChooser = new JFileChooser();
					if (jFileChooser.showSaveDialog(JIM.messengerWindow) == JFileChooser.APPROVE_OPTION) {
						fileToSave = new File(jFileChooser.getSelectedFile().getPath()
								+ MessageUtils.getExtensionFromFile(parentFile));
						Thread receiveFileThread = new Thread(() -> {
							try {
								processReceiveFile(JIM.FILE_PORT, fileToSave);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
						receiveFileThread.start();
						try {
							JIM.messageClient.sendFileTransferDownloadRequest(parentFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						JIM.messageClient.sendFileTransferRequestDecline();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		SwingUtilities.invokeLater(fileBrowser);

	}
}