import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.lang.Math;
import java.lang.Long;


class Gen implements Runnable {
   private Thread t;
   private String threadName;
   private int d = 256;

   	Gen(String name, int d){
		this.d = d;
       		threadName = name;
   	}

	public boolean is_primary(long n) {
		if (n == 2) {
			return true;
		} else if ( n == 1 || (n % 2) == 0 ) {
			return false;
		}
		double x = Math.sqrt(n);
		long p = (long) x;
		for (int i=3; i<p; i += 2) {
			if ((n % i) == 0)
				return false;
		}
		return true;
	}

	public void run() {
			SecureRandom randseed = new SecureRandom();
			byte seed[] = randseed.generateSeed(5);
			SecureRandom rand = new SecureRandom();
			rand.setSeed(seed);
			long n=1;
			boolean temp, isp = false;
			while (!isp) {
				for (int i=0; i<d; i++){
					temp = rand.nextBoolean();
					n += (temp) ? Math.pow((double)2,(double)i) : 0;
				}
				if (isp = is_primary(n))
					System.out.println("Number " + n + " is primary");
				//else
			 		//System.out.println( n + " is not primary");
			}
		System.out.println("Thread " +  threadName + " exiting.");
	}

	public void start () {
      		System.out.println("Starting " +  threadName );
      		if (t == null) {
         		t = new Thread (this, threadName);
         		t.start ();
      		}
   	}
}

public class generate {
   	public static void main(String args[]) {
		if (args.length < 2) {
			System.out.println("Not enough arguments. Need k 1-8 and d 256, 512, 1024, 2048, 3072, 7680.");
			return;
		}
		try {
			int k = Integer.parseInt(args[0]);
			int d = Integer.parseInt(args[1]);
			Gen[] T = new Gen[k];

			for (int i=0; i<k; i++) {
				T[i] = new Gen( "Thread-"+i, (d+i));
				T[i].start();
			}
		} catch(NumberFormatException e) {
			System.out.println("Enter numbers.");
		}
	}
}
