
//Program:			MessageUtils.java
//Author: 			Mark Hedrick
//Last modified:		April 21, 2018
//Desc:				This class serves as a utility class used for various functions through the Java Instant Messenger application

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JTextArea;

public class MessageUtils implements Serializable {

	private Key key;

	public MessageUtils(String encKey) {
		try {
			key = generateKey(encKey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public byte[] decryptMessage(byte[] message) throws GeneralSecurityException {
		Cipher c = Cipher.getInstance(ALGORITHM);
		byte[] decMessage;
		c.init(Cipher.DECRYPT_MODE, key);
		decMessage = Base64.getDecoder().decode(message);
		try {
			return c.doFinal(decMessage);
		} catch (Exception e) {
			return new byte[] {};
		}
	}

	public byte[] encryptMessage(String message) throws GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encMessage = c.doFinal(message.getBytes("UTF-8"));
		return Base64.getEncoder().encode(encMessage);
	}

	private Key generateKey(String encKey) throws UnsupportedEncodingException {
		return new SecretKeySpec(encKey.getBytes("UTF-8"), ALGORITHM);
	}

	private static final String ALGORITHM = "AES";

	public static Object deserializeObject(byte[] input) {

		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(input);
		GZIPInputStream gzipInputStream;
		ObjectInputStream objectInputStream;
		try {
			gzipInputStream = new GZIPInputStream(arrayInputStream);
			objectInputStream = new ObjectInputStream(gzipInputStream);
			Object deserializedObject = objectInputStream.readObject();
			objectInputStream.close();
			return deserializedObject;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new Object();
	}

	public static void displayMessage(JTextArea textArea, String message) {
		textArea.append(message);
	}

	public static String formatMessage(String user, String message) {
		String currentTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
		return MessageFormat.format("[{0}] {1}: {2} {3}", currentTime, user, message,
				System.getProperty("line.separator"));
	}

	public static byte[] generateHash(String encKey) {
		byte[] keyHash = new byte[] {};

		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(encKey.getBytes("UTF-8"));
			keyHash = messageDigest.digest();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return keyHash;
	}

	public static String getExtensionFromFile(File file) {
		return file.getName().substring(file.getName().lastIndexOf('.'));
	}

	public static byte[] serializeObject(Object object) {
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream;
		ObjectOutputStream objectOutputStream;

		try {
			gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
			objectOutputStream = new ObjectOutputStream(gzipOutputStream);
			objectOutputStream.writeObject(object);

			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayOutputStream.toByteArray();
	}

	public static int writeToStream(BufferedInputStream bufferedInputStream, BufferedOutputStream bufferedOutputStream)
			throws IOException {
		byte[] fileBuffer = new byte[1024];

		try {
			for (int fileLength; (fileLength = bufferedInputStream.read(fileBuffer)) != -1;) {
				bufferedOutputStream.write(fileBuffer, 0, fileLength);
			}
			bufferedInputStream.close();
			bufferedOutputStream.close();
			return 0;
		} catch (IOException ioe) {
			bufferedInputStream.close();
			bufferedOutputStream.close();
		}
		return 1;
	}
}
