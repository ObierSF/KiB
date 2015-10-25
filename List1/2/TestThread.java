import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class RC4T implements Runnable {
   private Thread t;
   private String threadName;
   int x;
   int y;
   byte[] text;
   String kkey;
   
   RC4T( String name, int x, int y, byte[] text, String kkey){
	   this.x = x;
	   this.y = y;
	   this.text = text;
	   this.kkey = kkey;
       threadName = name;
   }
   
	public void run() {
		System.out.println("Running " +  threadName );
		
		String a, b, c, d, desKey = "start", temp, msg;
		ARC4 rc = new ARC4();
		int counter;
		for (int i = x; i<y; i++) {
			System.out.println(threadName + " Key: " + desKey);
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
							rc.ARC4(byteKey);
							byte[] PTByte = rc.decrypt(text);
							String PTString = "";
							for (int g = 0; g < text.length; g++) {
								PTString += Integer.toString((PTByte[g] & 0xff) + 0x100, 16).substring(1);
							}
							int[] p = new int[text.length];
							for (int g = 0; g < PTString.length(); g+=2) {
								p[g/2] = (int)((Character.digit(PTString.charAt(g), 16) << 4) + Character.digit(PTString.charAt(g+1), 16));
							}
							
							msg = "";
							for (int g=0; g<text.length; g++) {
								temp = new Character((char)p[g]).toString();
								msg += temp;
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
								System.out.println("Msg:   " + msg);
								System.out.println("C:   " + (p.length));
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			}
		}
      
		System.out.println("Thread " +  threadName + " exiting.");
	}
   
   public void start ()
   {
      System.out.println("Starting " +  threadName );
      if (t == null)
      {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}

public class TestThread {
   public static void main(String args[]) {
		String desPlainText = "bbf316e8d940af0ad3";
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
		
   
		RC4T R1 = new RC4T( "Thread-1", 0, 64, bytePlainText, kkey);
		R1.start();
	  
		RC4T R2 = new RC4T( "Thread-2", 64, 128, bytePlainText, kkey);
		R2.start();
		
		RC4T R3 = new RC4T( "Thread-3", 128, 192, bytePlainText, kkey);
		R3.start();
	  
		RC4T R4 = new RC4T( "Thread-4", 192, 256, bytePlainText, kkey);
		R4.start();
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

class ARC4 {
	private byte[] S = new byte[256];
    private byte[] T = new byte[256];
    private int keylen;
	
	public void ARC4(byte[] key) {
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

    public byte[] encrypt(byte[] plaintext) {
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

    public byte[] decrypt(byte[] ciphertext) {
        return encrypt(ciphertext);
    }
}