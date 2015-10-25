public class RC4 {
    private static byte[] S = new byte[256];
    private static byte[] T = new byte[256];
    private static int keylen;
	
	public static void main(String[] args) {
		String desPlainText = "bbf316e8d940af0ad3";
		String desKey = "4b6579";
		String msg1, temp;
		
		byte[] bytePlainText = new byte[desPlainText.length()/2];
		for (int i = 0; i < desPlainText.length(); i+=2) {
			bytePlainText[i/2] = (byte) ((Character.digit(desPlainText.charAt(i), 16) << 4) + Character.digit(desPlainText.charAt(i+1), 16));
		}
		byte[] byteKey = new byte[desKey.length()/2];
		for (int g = 0; g < desKey.length(); g+=2) {
			byteKey[g/2] = (byte) ((Character.digit(desKey.charAt(g), 16) << 4) + Character.digit(desKey.charAt(g+1), 16));
		}
		
		try {
			RC4(byteKey);
			byte[] PTByte = decrypt(bytePlainText);
			String PTString = "";
			int[] p = new int[desPlainText.length()/2];
			for (int g = 0; g < desPlainText.length()/2; g++) {
				PTString += Integer.toString((PTByte[g] & 0xff) + 0x100, 16).substring(1);
			}
			for (int g = 0; g < PTString.length(); g+=2) {
				p[g/2] = (int)((Character.digit(PTString.charAt(g), 16) << 4) + Character.digit(PTString.charAt(g+1), 16));
			}
			
			System.out.println("Plaintext:  " + desPlainText);
			System.out.println("Backtext:   " + PTString);
			msg1 = "";
			for (int g=0; g<desPlainText.length()/2; g++) {
				temp = new Character((char)p[g]).toString();
				msg1 += temp;
				System.out.print(temp + " " + p[g] + " ");
			}
			System.out.println("\n" + msg1);
			/*if (msg1.equals("Plaintext")) {
				System.out.println("Key:        " + desKey);
				System.out.println(msg1);
			}
			if (j == 75 && k == 101 && l == 121) {
				System.out.println(desKey + " " + msg1 + " ");
			}*/
		} catch (Exception e) {
			System.out.println(e);
		}
	}

    public static void RC4(byte[] key) {
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
            keylen = key.length;
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                T[i] = key[i % keylen];
            }
            int j = 0;
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + T[i]) & 0xFF;
                S[i] ^= S[j];
                S[j] ^= S[i];
                S[i] ^= S[j];
            }
        }
    }

    public static byte[] encrypt(byte[] plaintext) {
        byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            S[i] ^= S[j];
            S[j] ^= S[i];
            S[i] ^= S[j];
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[counter] = (byte) (plaintext[counter] ^ k);
        }
        return ciphertext;
    }

    public static byte[] decrypt(byte[] ciphertext) {
        return encrypt(ciphertext);
    }
}