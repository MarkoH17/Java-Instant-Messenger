
//Program:			User.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class represents the User object used to identify users on the JIM network.

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
	private final String address;
	private final String name;
	private final int port;

	public User(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User otherUser = (User) obj;
			return name.equals(otherUser.name) && address.equals(otherUser.address) && port == otherUser.port;
		} else {
			return false;
		}
	}

	public String getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, address, port);
	}
}
