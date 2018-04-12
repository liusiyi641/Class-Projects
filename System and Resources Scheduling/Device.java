
public abstract class Device {

  /**
   * this class is extended by MM1
   */

  String name;

  /**
   * used for logging, for distinguishing between devices
   * 
   * @param name
   */
  public Device(String name) {
    this.name = name;
  }

  // add a parameter in onDeath function to distinguish the requests
  public abstract void onDeath(double timestamp, Request request);

  public abstract void onBirth(Event ev, double timestamp);

  public abstract void onMonitor(double timestamp, double startTime);

  public abstract void initializeScehduleWithOneEvent();

  public abstract void printStats();

  public abstract double calculateQ();

  /**
   * @return the name of the device
   */
  public String getName() {
    return name;
  }

}
