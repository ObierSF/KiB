import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ARC4 {
    private static byte[] S = new byte[256];
    private static byte[] T = new byte[256];
    private static int keylen;
	
	public static void main(String[] args) {
		String desPlainText = "bbf316e8d940af0ad3"; //= "506c61696E74657874";
		String desKey = "4b6579";
		String kkey = "3995b06e";
		String temp, msg, msg1;
		int counter;
		
		String[] text = readFile("k22.in");
		msg = "";
		msg1= "";
		for (int i=0; i<text.length; i++) {
			int decimal = Integer.parseInt(text[i],2);
			msg1 = Integer.toString(decimal,16);
			if (msg1.length() == 1) {
				msg += "0" + msg1;
			} else {
				msg += msg1;
			}
		}
		desPlainText = msg;
		System.out.println("CT: " + desPlainText);
		byte[] bytePlainText = new byte[desPlainText.length()/2];
		for (int i = 0; i < desPlainText.length(); i+=2) {
			bytePlainText[i/2] = (byte) ((Character.digit(desPlainText.charAt(i), 16) << 4) + Character.digit(desPlainText.charAt(i+1), 16));
		}
		String a, b, c, d;
		for (int i = 0; i<256; i++) {
			System.out.println("Key: " + desKey);
			a = Integer.toString(i,16);
			if (a.length() == 1) {
				a = "0" + a;
			}
			for (int j = 0; j<256; j++) {
				b = Integer.toString(j,16);
				if (b.length() == 1) {
					b = "0" + b;
				}
				for (int k=0; k<256; k++) {
					c = Integer.toString(k,16);
					if (c.length() == 1) {
						c = "0" + c;
					}
					for (int l=0; l<256; l++) {
						desKey = "";
						d = Integer.toString(l,16);
						if (d.length() == 1) {
							d = "0" + d;
						}
						desKey = a + b + c + d + kkey;
						byte[] byteKey = new byte[desKey.length()/2];
						for (int g = 0; g < desKey.length(); g+=2) {
							byteKey[g/2] = (byte) ((Character.digit(desKey.charAt(g), 16) << 4) + Character.digit(desKey.charAt(g+1), 16));
						}
						
						try {
							ARC4(byteKey);
							byte[] PTByte = decrypt(bytePlainText);
							String PTString = "";
							for (int g = 0; g < desPlainText.length()/2; g++) {
								PTString += Integer.toString((PTByte[g] & 0xff) + 0x100, 16).substring(1);
							}
							int[] p = new int[desPlainText.length()/2];
							for (int g = 0; g < PTString.length(); g+=2) {
								p[g/2] = (int)((Character.digit(PTString.charAt(g), 16) << 4) + Character.digit(PTString.charAt(g+1), 16));
							}
							
							msg1 = "";
							for (int g=0; g<desPlainText.length()/2; g++) {
								temp = new Character((char)p[g]).toString();
								msg1 += temp;
							}
							counter=0;
							for (int g=0; g<p.length; g++) {
								if (p[g] >= 97 && p[g] <= 122) {
									counter++;
								}
								else if (p[g] >= 65 && p[g] <= 90) {
									counter++;
								}
								else if (p[g] >= 46 && p[g] <= 59) {
									counter++;
								}
								else if (p[g] >= 32 && p[g] <= 34) {
									counter++;
								}
								else if (p[g] >= 39 && p[g] <= 42) {
									counter++;
								}
								else if (p[g] == 63 || p[g] == 44) {
									counter++;
								}
							}
							if (counter > (p.length * 0.95)) {
								System.out.println("Key:   " + desKey);
								System.out.println("Msg:   " + msg1);
								System.out.println("C:   " + (p.length));
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			}
		}
		desKey = "00000000" + kkey;
		
	}

    public static void ARC4(byte[] key) {
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
        final byte[] ciphertext = new byte[plaintext.length];
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
}