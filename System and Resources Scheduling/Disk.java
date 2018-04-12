import java.util.LinkedList;
import java.util.Random;


public class Disk extends MM1 {

  public Disk(String name, LinkedList<Event> schedule, double lambda, double ts) {
    super(name, schedule, lambda, ts);
  }

  @Override
  public void onDeath(double timestamp, Request request) {


    // random number generator
    Random random = new Random();
    double probability = random.nextDouble();

    /**
     * request has finished and left the mm1
     */
    Request req = queue.remove();
    req.setEndedProcessing(timestamp);
    log.add(req);

    /**
     * look for another blocked event in the queue that wants to execute and schedule it's death. at
     * this time the waiting request enters processing time.
     */

    if (probability <= 0.5) {
      // Go to CPU
      // Give a birth event to CPU
      Event event = new Event(timestamp, EventType.BIRTH, Controller.devices.get(0));
      event.setTag(false);
      schedule.add(event);
    } else {
      // Go to Network
      // Give a birth event to Network
      Event event = new Event(timestamp, EventType.BIRTH, Controller.devices.get(2));
      event.setTag(false);
      schedule.add(event);
    }

    if (queue.size() > 0) {
      double timeOfNextDeath = timestamp + getTimeOfNextDeath();
      queue.peek().setStartedProcessing(timestamp);
      Event event = new Event(timeOfNextDeath, EventType.DEATH, this);
      schedule.add(event);
    }
  }
}
