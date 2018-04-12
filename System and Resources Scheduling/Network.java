import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Network extends MM1 {

  public Network(String name, LinkedList<Event> schedule, double lambda, double ts) {
    super(name, schedule, lambda, ts);
  }

  @Override
  public void onDeath(double timestamp, Request request) {

    /**
     * request has finished and left the mm1
     */
    Request req = queue.remove();
    req.setEndedProcessing(timestamp);
    log.add(req);

    // Go to CPU
    // Give a birth event to CPU
    Event ev = new Event(timestamp, EventType.BIRTH, Controller.devices.get(0));
    ev.setTag(false);
    schedule.add(ev);

    if (queue.size() > 0) {
      double timeOfNextDeath = timestamp + getTimeOfNextDeath();
      queue.peek().setStartedProcessing(timestamp);
      Event event = new Event(timeOfNextDeath, EventType.DEATH, this);
      schedule.add(event);
    }
  }

}
