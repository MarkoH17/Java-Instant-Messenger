
//Program:			MessageClient.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class provides the functionality of sending requests to a partner 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

import javax.swing.JOptionPane;

public class MessageClient {

	private String address;
	private String encKey;
	private int port;
	private User user;

	public MessageClient(User user, String address, int port, String encKey) {
		this.user = user;
		this.address = address;
		this.port = port;
		this.encKey = encKey;
	}

	public void sendFile(File file) throws IOException {
		Socket socket = new Socket(address, JIM.FILE_PORT);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
		int result = MessageUtils.writeToStream(bufferedInputStream, bufferedOutputStream);
		if (result == 0) {
			JOptionPane.showMessageDialog(null, "Successfully sent file!", "JIM - Send File",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Error sending file!", "JIM - Send File", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void sendFileTransferDownloadRequest(File file) throws IOException {
		FileTransferDownloadRequest fileTransferDownloadRequest = new FileTransferDownloadRequest(file);
		sendObject(fileTransferDownloadRequest);
	}

	public void sendFileTransferRequest(User user, File fileToSend) throws IOException {
		FileTransferRequest fileTransferRequest = new FileTransferRequest(user, fileToSend);
		sendObject(fileTransferRequest);
	}

	public void sendFileTransferRequestDecline() throws IOException {
		DeclineFileTransferRequest declineFileTransferRequest = new DeclineFileTransferRequest(user);
		sendObject(declineFileTransferRequest);
	}

	public void sendGoodbye() throws IOException {
		GoodbyeRequest goodbyeRequest = new GoodbyeRequest(user, encKey);
		sendObject(goodbyeRequest);
	}

	public void sendHello() throws IOException {
		HelloRequest helloRequest = new HelloRequest(user, encKey);
		sendObject(helloRequest);
	}

	public void sendMessage(String message) throws IOException {
		MessageRequest messageRequest = new MessageRequest(user, message.trim(), encKey);
		sendObject(messageRequest);
	}

	private void sendObject(Object object) throws IOException {
		sendObject(object, address, port);
	}

	public static void sendMessagingRequest(User user, byte[] keyHash, String address, int port) throws IOException {
		MessagingRequest messagingRequest = new MessagingRequest(user, keyHash);
		sendObject(messagingRequest, address, port);
	}

	public static void sendMessagingRequestApprove(User user, String address, int port) throws IOException {
		MessagingApproveRequest messagingApproveRequest = new MessagingApproveRequest(user);
		sendObject(messagingApproveRequest, address, port);
	}

	public static void sendMessagingRequestDecline(User user, String address, int port) throws IOException {
		DeclineMessagingRequest declineMessagingRequest = new DeclineMessagingRequest(user);
		sendObject(declineMessagingRequest, address, port);
	}

	public static void sendRequestWrongKey(String address, int port) throws IOException {
		WrongKeyRequest wrongKeyRequest = new WrongKeyRequest(MessageUtils.generateHash(JIM.key));
		sendObject(wrongKeyRequest, address, port);
	}

	private static void sendObject(Object object, String address, int port) throws IOException {
		Socket socket = new Socket(address, port);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(socket.getOutputStream());
		ObjectOutputStream outputStream = new ObjectOutputStream(gzipOutputStream);
		outputStream.writeObject(object);
		outputStream.close();
	}
}
