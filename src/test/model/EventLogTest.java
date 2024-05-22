package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Reference: https://github.students.cs.ubc.ca/CPSC210/AlarmSystem

/**
 * Unit tests for the EventLog class
 */
public class EventLogTest {
	private Event e1;
	private Event e2;
	private Event e3;
    private Event e4;
    private Event e5;

	@BeforeEach
	public void loadEvents() {
		e1 = new Event("A1");
		e2 = new Event("A2");
		e3 = new Event("A3");
        e4 = new Event("A3");
		EventLog el = EventLog.getInstance();
		el.logEvent(e1);
		el.logEvent(e2);
		el.logEvent(e3);
	}

    @Test
    void equalsTest() {
        assertFalse(e3.equals(e5));

        assertFalse(e3.equals(EventLog.getInstance()));

        assertTrue(e3.equals(e4));


        assertEquals(e3.hashCode(), e4.hashCode());
    }

	@Test
	public void testLogEvent() {	
		List<Event> l = new ArrayList<Event>();
		
		EventLog el = EventLog.getInstance();
		for (Event next : el) {
			l.add(next);
		}
		
		assertTrue(l.contains(e1));
		assertTrue(l.contains(e2));
		assertTrue(l.contains(e3));
	}

	@Test
	public void testClear() {
		EventLog el = EventLog.getInstance();
		el.clear();
		Iterator<Event> itr = el.iterator();
		assertTrue(itr.hasNext());   // After log is cleared, the clear log event is added
		assertEquals("Event log cleared.", itr.next().getDescription());
		assertFalse(itr.hasNext());
	}
}
