
//Program:			Neighbors.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class serves as the controller of the NeighborServer and NeighborClient threads

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;

public class Neighbors {
	public final HashSet<User> neighboringUsers = new HashSet<>();
	private volatile boolean clientRunning = true;
	private volatile boolean serverRunning = true;

	public Neighbors(String address, int port) throws IOException {

		InetAddress mCastAddress = InetAddress.getByName(address);

		Runnable neighborServer = new NeighborServer(mCastAddress, port, this);
		Thread neighborServerThread = new Thread(neighborServer);
		neighborServerThread.start();

		Runnable neighborClient = new NeighborClient(mCastAddress, port, this);
		Thread neighborClientThread = new Thread(neighborClient);
		neighborClientThread.start();
	}

	public void addNeighbor(User neighboringUser) {
		neighboringUsers.add(neighboringUser);
	}

	public HashSet<User> getNeighboringUsers() {
		return neighboringUsers;
	}

	public boolean isClientRunning() {
		return clientRunning;
	}

	public boolean isServerRunning() {
		return serverRunning;
	}

	public void stopAllNeighbors() {
		serverRunning = false;
		clientRunning = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

// Program: NeighborClient.java
// Author: Mark Hedrick
// Last modified: April 21, 2018
// Desc: This class provides the function of receiving neighboring user beacons
// from neighborServer

class NeighborClient implements Runnable {
	private InetAddress mCastAddress;
	private Neighbors parent;
	private final int port;

	public NeighborClient(InetAddress mCastAddress, int port, Neighbors parent) {
		this.port = port;
		this.mCastAddress = mCastAddress;
		this.parent = parent;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			try (MulticastSocket clientSocket = new MulticastSocket(port)) {
				try {
					clientSocket.joinGroup(mCastAddress);
				} catch (IOException e) {
					e.printStackTrace();
					parent.stopAllNeighbors();
				}
				while (parent.isClientRunning()) {
					DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
					try {
						clientSocket.receive(msgPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						User otherUser = (User) MessageUtils.deserializeObject(buffer);
						if (!parent.getNeighboringUsers().contains(otherUser)) {
							parent.addNeighbor(otherUser);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final int BUFFER_SIZE = 512;
}

// Program: NeighborServer.java
// Author: Mark Hedrick
// Last modified: April 21, 2018
// Desc: This class provides the function of sending neighboring user beacons.
class NeighborServer implements Runnable {
	private InetAddress mCastAddress;
	private Neighbors parent;
	private final int port;

	public NeighborServer(InetAddress mCastAddress, int port, Neighbors parent) {
		this.port = port;
		this.mCastAddress = mCastAddress;
		this.parent = parent;
	}

	@Override
	public void run() {
		while (parent.isServerRunning()) {

			try (DatagramSocket serverSocket = new DatagramSocket()) {
				byte[] myUser = MessageUtils.serializeObject(JIM.user);

				DatagramPacket beaconPacket = new DatagramPacket(myUser, myUser.length, mCastAddress, port);
				serverSocket.send(beaconPacket);
				Thread.sleep(BEACON_DELAY);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static final int BEACON_DELAY = 1000;
}
