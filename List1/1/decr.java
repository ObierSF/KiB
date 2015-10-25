import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class decr {
	public static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		String in = "";
		int l = 0;
		String[] message = null;
		
		System.out.println("Enter the number of cryptograms to use. (3-20)");
		
		try {
			while (l > 21 || l < 1) {
				in = scanner.nextLine();
				l = Integer.parseInt(in);
				if (l > 21 || l < 1)
					System.out.println("Number must be from 3 to 21.");
			}
			
			in = "";
			int leng, L;
			int[] msg;
			message = readFile("k0.in");
			leng = message.length;
			int[][] M = new int[l][leng];
			for (int i=0; i<leng; i++) {
				M[0][i] = Integer.parseInt(message[i], 2);
			}
			for (int i=1; i<l; i++) {
				message = null;
				in = "k"+i+".in";
				message = readFile(in);
				msg = new int[message.length];
				for (int j=0; j<message.length; j++) {
					msg[j] = Integer.parseInt(message[j], 2);
				}
				
				if (msg.length < leng) {
					L = msg.length;
				} else {
					L = leng;
				}
				
				for (int j=0; j<L; j++) {
					M[i][j] = msg[j];
				}
			}
			
			
			String str;
			int out = 0;
			int[][] mes = new int[l-1][leng];
				
			for (int j=0; j<l-1; j++) {
				//System.out.println("\nm0 ^ m" + (j+1));
				for (int i=0; i<leng; i++) {
					mes[j][i] = M[0][i] ^ M[j+1][i];
					//mes[j][i] = out = mes[j][i] ^ 97;
					//System.out.print((str = new Character((char)mes[j][i]).toString()) + mes[j][i] + " ");
				}
			}
			int c=0;
			//System.out.println("\nCohesiva:");
			for (int i=0; i<leng; i++) {
				for (int j=1; j<l-1; j++) {
					if (mes[0][i] == mes[j][i]) {
						//System.out.print((str = new Character((char)mes[0][i]).toString()) + " ");
						c++;
					} else {
						//System.out.print("  ");
						c++;
					}
				}
			}
			System.out.println("\nKey:");
			int[] p = new int[leng];
			int temp = 0;
			int[][] ok = new int[255][leng];
			int g=0, h=0, max=0;
			for (int i=0; i<leng; i++) {
				h=0;
				for (int j=1; j<255; j++){
					int k = 0;
					while (k < l) {
						temp = M[k][i] ^ j;
						if (temp >= 97 && temp <= 122) {}
						else if (temp >= 65 && temp <= 90) {} 
						else if (temp >= 46 && temp <= 59) {}  
						else if (temp >= 32 && temp <= 34) {} 
						else if (temp >= 39 && temp <= 42) {}
						else if (temp == 63 || temp == 44) {}
						else {
							break;
						}
						k++;
					}
							
					if (k == l) {
						g = M[0][i] ^ j;
						ok[h][i] = j;
						h++;
						if (h > max) {
							max = h;
						}
					}
					ok[h][i] = h;
				}
						//System.out.print(ok[i] + "+");
			}
			//System.out.print("\nKey:");
			for (int i=0; i<leng; i++) {
				
				//System.out.print((str = new Character((char)ok[i]).toString()) + "+");
			}
			
			//System.out.println("\n" + c + "\nCoherence:");
			/*for(int i=0; i<leng; i++) {
				c=0;
				for (int j=0; j<l-1; j++) {
					if (mes[j][i] == 0) {
						ok[i] = 0;
						System.out.print("did");
						c=1;
						break;
					}
				}
				if (c == 0) {
					//System.out.print("  ");
				}
			}*/
			/*for (int i=0; i<leng; i++) {
				for (int j=0; j<max+1; j++) {
					System.out.print(ok[j][i] + ".");
				}
				System.out.println("");
			}*/
			int[] kk = new int[leng];
			//for (int j=0; j<max; j++) {
				System.out.println("\nM:" );
				for (int i=0; i<leng; i++) {
					g = ok[0][i]^M[0][i];
					kk[i] = ok[0][i];
					System.out.print((str = new Character((char)g).toString()) + ".");
				}
			//}
			
			System.out.println("\nMx: ");
			for (int i=0; i<leng; i++) {
				for (int j=0; j<max; j++) {
					g = ok[j][i]^M[0][i];
					if ((g >= 97 && g <= 122) || g == 32) {
						System.out.print((str = new Character((char)g).toString()));
						break;
					} else if (g >= 65 && g <= 90) {
						System.out.print((str = new Character((char)g).toString()));
						break;
					}
				}
			}
			System.out.println();
			l = 0;
			g=0;
			c=0;
			int go = 0;
			temp = 0;
			while (l < 2) {
				in = scanner.nextLine();
				l = Integer.parseInt(in);
				if (l == 0) {
					go = 0;
					while (go == 0) {
						c++;
						g = 0;
						if (ok[0][c] != 0) { 
							while (ok[g][c] != 0) {
								g++;
							}
							if ((g-1) == 1) {}
							else {
								max = ok[g-1][c];
								go = 1;
							}
						}
					}
					System.out.println("Now you can change " + c + " position.");
					System.out.println("\nM:" );
					for (int i=0; i<leng; i++) {
						g = kk[i]^M[0][i];
						if (i == c) {
							System.out.print("." + (str = new Character((char)g).toString()) + ".");
						} else {
							System.out.print((str = new Character((char)g).toString()));
						}
					}
					temp = 0;
				} else if (l ==1) {
					g = 0;
					while (ok[g][c] != 0) {
						g++;
					}
					if ((g-1) > 1) {
						temp = (temp+1)%max;
							kk[c] = ok[temp][c];
							System.out.println("\nM:" );
							for (int i=0; i<leng; i++) {
								g = kk[i]^M[0][i];
								if (i == c) {
									System.out.print("." + (str = new Character((char)g).toString()) + ".");
								} else {
									System.out.print((str = new Character((char)g).toString()));
								}
							}
							System.out.println();
					} else {
						System.out.println("You can't change this position.");
					}
				}
			}
			
			
		} catch (NumberFormatException e) {
			System.out.println(in + " is not a number.");
		}
		//System.out.println(msg.length + " " + (str = new Character((char)0).toString()) + " " + Integer.toBinaryString(msg[0] ^ msg1[0]));
		
		
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
