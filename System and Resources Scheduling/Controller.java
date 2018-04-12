import java.util.Collections;
import java.util.LinkedList;


public class Controller {


  /**
   * Simulation constants
   */
  public static final double MONITOR_INTERVAL = 0.03;
  public static final int SIMULATION_TIME = 10000;
  public static final double LAMBDA = 40;
  // this is where you change the lambda for testing
  // When measuring the slowdown we first set it to 40 to calculate Tq
  // Then we set it to 0.01 to calculate Ts
  // Last we take Tq/Ts as our slowdown

  /**
   * time elapsed time before starting to log
   */
  public static final double LOGGING_START_TIME = 5000;
  // I set it to half of the simulation time to make sure it reaches
  // steady state before logging.


  /**
   * holds all the devices in the system in this case we have one
   */
  public static LinkedList<Device> devices = new LinkedList<Device>();

  /**
   * initialize the schedule with a birth and a monitor event
   * 
   * @return a schedule with two events
   */
  public static LinkedList<Event> initSchedule() {

    // initializing the schedule
    LinkedList<Event> schedule = new LinkedList<Event>();

    // initializing the CPUs
    Device cpu = new CPU("cpu", schedule, LAMBDA, 0.02);
    cpu.initializeScehduleWithOneEvent();
    devices.add(cpu);

    // Both the Disk and the Network have lambda as 0
    // since they don't have requests coming in outside
    // the whole system.

    Device disk = new Disk("disk", schedule, 0, 0.1);
    devices.add(disk);

    Device network = new Network("network", schedule, 0, 0.025);
    devices.add(network);

    schedule.add(new Event(MONITOR_INTERVAL, EventType.MONITOR));
    return schedule;
  }



  /**
   * sorts the schedule according to time, and returns the earliest event.
   * 
   * @param schedule
   * @return the earliest event in the schedule
   */
  public static Event getNextEvent(LinkedList<Event> schedule) {
    Collections.sort(schedule);
    return schedule.getFirst();
  }


  public static void main(String[] args) {
    double totalQ = 0;
    double Tq = 0;

    LinkedList<Event> schedule = initSchedule();

    double time = 0, maxTime = SIMULATION_TIME;
    while (time < maxTime) {
      Event event = getNextEvent(schedule);
      time = event.getTime();
      event.function(schedule, time);
    }


    // Applying Jackson's Theorem, we first measure
    // the total q for the whole system
    for (Device device : devices) {
      totalQ += device.calculateQ();
    }

    // Then we divide it by lambda and conclude our response time
    // for the entire system.
    Tq = totalQ / LAMBDA;
    System.out.println("Tq = " + Tq);


  }

}
