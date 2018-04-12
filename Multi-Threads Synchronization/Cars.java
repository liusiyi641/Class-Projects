package threads;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cars extends Thread {

  int carID;
  boolean direction; // True = Right, False = Left

  // volatile:
  static volatile int numRight = 0;
  static volatile int numLeft = 0;


  // max # of how many more cars that entered the tunnel in the same direction than the other one.
  static Semaphore limitR = new Semaphore(4);
  static Semaphore limitL = new Semaphore(4);

  // Capacity N = 4
  static Semaphore capacity = new Semaphore(4);

  // lock that makes sure no crashing in the tunnel
  static Semaphore lock = new Semaphore(1);

  // mutex
  static Semaphore mutexR = new Semaphore(1);
  static Semaphore mutexL = new Semaphore(1);

  public Cars(int i) {
    carID = i;
    Random random = new Random();
    direction = random.nextBoolean();
  }

  public void run() {
    while (true) {
      if (direction == true) { // right

        try {
          limitR.acquire();
          mutexR.acquire();
          System.out.println("Right Car " + carID + " arrives");
          numRight++;
          if (numRight == 1) lock.acquire();

          mutexR.release();


          capacity.acquire();

          System.out.println("Right Car " + carID + " is in the tunnel");

          sleep((int) Math.random() * 100000000);
          capacity.release();

          mutexR.acquire();

          System.out.println("Right Car " + carID + " exits");
          numRight--;
          if (numRight == 0) lock.release();
          mutexR.release();
          limitL.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      else { // left
        try {
          limitL.acquire();
          mutexL.acquire();

          System.out.println("Left Car " + carID + " arrives");
          numLeft++;
          if (numLeft == 1) lock.acquire();

          mutexL.release();



          capacity.acquire();

          System.out.println("Left Car " + carID + " is in the tunnel");
          sleep((int) Math.random() * 100000000);
          capacity.release();


          mutexL.acquire();
          System.out.println("Left Car " + carID + " exits");
          numLeft--;
          if (numLeft == 0) lock.release();
          mutexL.release();
          limitR.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) {
    int N = 20;
    Cars[] p = new Cars[N];
    for (int i = 0; i < N; i++) {
      p[i] = new Cars(i);
      p[i].start();
    }
  }
}
