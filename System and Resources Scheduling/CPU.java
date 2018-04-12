

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class CPU extends MM1 {


  public CPU(String name, LinkedList<Event> schedule, double lambda, double ts) {
    super(name, schedule, lambda, ts);
  }

  @Override
  public void onDeath(double timestamp, Request req) {
    // added a parameter to distinguish each request.
    // easier remove of requests

    // random number generator
    Random random = new Random();
    double probability = random.nextDouble();

    // remove the requests from queue and add it to log
    queue.remove(req);
    req.setEndedProcessing(timestamp);
    log.add(req);

    // put a request in the waiting queue to the server
    if (queue.size() > 1) {
      double timeOfNextDeath = timestamp + getTimeOfNextDeath();
      Request r = ((LinkedList<Request>) queue).get(1);
      r.setStartedProcessing(timestamp);
      Event event = new Event(timeOfNextDeath, EventType.DEATH, this);
      event.distinguishReq(r);
      schedule.add(event);
    }


    if (probability <= 0.4) {
      // Go to Network
      // Give a birth event to Network
      Event event = new Event(timestamp, EventType.BIRTH, Controller.devices.get(2));
      event.setTag(false);
      schedule.add(event);
    } else if (probability < 0.5) {
      // Go to Disk
      // Give a birth event to Disk
      Event event = new Event(timestamp, EventType.BIRTH, Controller.devices.get(1));
      event.setTag(false);
      schedule.add(event);
    }
  }

  @Override
  public void onBirth(Event ev, double timestamp) {

    Request request = new Request(timestamp);
    queue.add(request);

    /**
     * if the queue is empty then start executing directly there is no waiting time.
     */
    // less than or equal to 2 since it's dual-core
    if (queue.size() <= 2) {


      request.setStartedProcessing(timestamp);
      Event event = new Event(timestamp + getTimeOfNextDeath(), EventType.DEATH, this);
      event.distinguishReq(request);
      schedule.add(event);
    }

    if (ev.getTag()) {
      /**
       * schedule the next arrival
       */
      double time = getTimeOfNextBirth();
      Event event = new Event(timestamp + time, EventType.BIRTH, this);
      event.setTag(true);
      schedule.add(event);

    }
  }

}
