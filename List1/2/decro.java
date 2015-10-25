import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class decro {
	private static String algorithm = "RC4";
  
	public static void main(String []args) throws Exception {
		int leng, k;
		String[] message = null;
		String msg = "";
		message = readFile("k22.in");
		leng = message.length;
		byte[] encrypted = new byte[leng];
		int[] M = new int[leng];
		for (int i=0; i<leng; i++) {
			M[i] = Integer.parseInt(message[i], 2) - 128; 
			encrypted[i] = (byte)M[i];
			msg += (new Character((char)M[i]).toString());
		}
		for (int i = 0; i < desPlainText.length(); i+=2) {
			bytePlainText[i/2] = (byte) ((Character.digit(desPlainText.charAt(i), 16) << 4) + Character.digit(desPlainText.charAt(i+1), 16));
		}
		String password = "0000";
		k = Integer.parseInt("39", 16);
		password += (new Character((char)k).toString());
		k = Integer.parseInt("95", 16);
		password += (new Character((char)k).toString());
		k = Integer.parseInt("b0", 16);
		password += (new Character((char)k).toString());
		k = Integer.parseInt("6e", 16);
		password += (new Character((char)k).toString());
		msg = "Plaintext";//"The shorter you live, the longer you're dead!";
		password = "Key";
		System.out.println("Encrypting... " + password);
		encrypted = encrypt(msg, password);

		StringBuilder sb = new StringBuilder();
		for (byte b : encrypted) {
			sb.append(String.format("%02X ", b));
		}
		System.out.println(sb.toString());
		
		System.out.println("Decrypting... " + encrypted[0] + encrypted[7] + " " + encrypted.length);
		String decrypted = decrypt(encrypted, password);
		 
		System.out.println("Decrypted text: " + decrypted);
	}
  
	public static byte[] encrypt(String toEncrypt, String key) throws Exception {
		// create a binary key from the argument key (seed)
		SecureRandom sr = new SecureRandom(key.getBytes());
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
		kg.init(sr);
		SecretKey sk = kg.generateKey();

		// create an instance of cipher
		Cipher cipher = Cipher.getInstance(algorithm);

		// initialize the cipher with the key
		cipher.init(Cipher.ENCRYPT_MODE, sk);

		// enctypt!
		byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

		return encrypted;
	}

	public static String decrypt(byte[] toDecrypt, String key) throws Exception {
		  // create a binary key from the argument key (seed)
		  SecureRandom sr = new SecureRandom(key.getBytes());
		  KeyGenerator kg = KeyGenerator.getInstance(algorithm);
		  kg.init(sr);
		  SecretKey sk = kg.generateKey();

		  // do the decryption with that key
		  Cipher cipher = Cipher.getInstance(algorithm);
		  cipher.init(Cipher.DECRYPT_MODE, sk);
		  byte[] decrypted = cipher.doFinal(toDecrypt);

		  return new String(decrypted);
	}
	
	public static String[] readFile(String filename) {
		String[] message = null;
		try {
			File file = new File(filename);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();
			line = stringBuffer.toString();
			
			message = line.split("\\s+");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
								 + Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
}
