package gpars


import org.junit.Test
import groovyx.gpars.agent.Agent

class AgentTest {

  @Test
  void conferenceAgent() {

    final groovyx.gpars.agent.Agent conference = new Conference()  //new Conference created

    /**
     * Three external parties will try to register/unregister concurrently
     */

    final Thread t1 = Thread.start {
      conference << {register(10L)}               //send a command to register 10 attendees
    }

    final Thread t2 = Thread.start {
      conference << {register(5L)}                //send a command to register 5 attendees
    }

    final Thread t3 = Thread.start {
      conference << {unregister(3L)}              //send a command to unregister 3 attendees
    }

    [t1, t2, t3]*.join()
    assert 12L == conference.val
  }

/**
 * Conference stores number of registrations and allows parties to register and unregister.
 * It inherits from the Agent class and adds the register() and unregister() private methods,
 * which callers may use it the commands they submit to the Conference.
 */
  class Conference extends Agent<Long> {
    def Conference() { super(0) }

    private def register(long num) { data += num }

    private def unregister(long num) { data -= num }
  }


}
