
//Program:			MessageServer.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class provides the functionality of receiving requests from a partner

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.zip.GZIPInputStream;

public class MessageServer implements Runnable {

	private int port;

	public MessageServer(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processRequest(ObjectInputStream objectInputStream) throws IOException {
		try {
			Request receivedObject = (Request) objectInputStream.readObject();
			receivedObject.setMessenger(JIM.messengerWindow);
			receivedObject.process();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void server() throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println(MessageFormat.format("MessageRequest Server started and listening on: {0}", port));
		while (true) {
			Socket socket = serverSocket.accept();
			try {
				GZIPInputStream gzipInputStream = new GZIPInputStream(socket.getInputStream());
				ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
				new Thread(() -> {
					try {
						processRequest(objectInputStream);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}