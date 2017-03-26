package test.raynigon.lib.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.raynigon.lib.events.handling.ContentBasedEvent;
import com.raynigon.lib.events.handling.ContentEventHandler;
import com.raynigon.lib.events.handling.Event;
import com.raynigon.lib.events.handling.EventHandler;
import com.raynigon.lib.events.handling.EventListener;
import com.raynigon.lib.events.handling.EventManager;

public class SynchronEventTest {

	private EventManager ev;
	private SimpleListener listener;
	
	public SynchronEventTest() {
		ev = new EventManager();
	}
	
	@Before
	public void setUp(){
		listener = new SimpleListener();
		ev.registerListener(listener);
	}
	
	@After
	public void tearDown(){
		ev.removeListener(listener);
		listener = null;
	}
	
	@Test
	public void testSimpleEvent() {
		ev.fireSyncEvent(new SimpleEvent());
		assertEquals(listener.averallEventCount, 	1);
		assertEquals(listener.simpleEventCount, 	1);
		assertEquals(listener.contentEventCount, 	0);
		assertEquals(listener.contentEvent0Count,	0);
		assertEquals(listener.contentEvent1Count, 	0);
		ev.fireSyncEvent(new SimpleEvent());
		assertEquals(listener.averallEventCount, 	2);
		assertEquals(listener.simpleEventCount, 	2);
		assertEquals(listener.contentEventCount, 	0);
		assertEquals(listener.contentEvent0Count, 	0);
		assertEquals(listener.contentEvent1Count, 	0);
	}

	@Test
	public void testContentEvent() {
		ev.fireSyncEvent(new ContentEvent(1));
		assertEquals(listener.averallEventCount, 	1);
		assertEquals(listener.simpleEventCount, 	0);
		assertEquals(listener.contentEventCount, 	1);
		assertEquals(listener.contentEvent0Count, 	1);
		assertEquals(listener.contentEvent1Count, 	0);
		ev.fireSyncEvent(new ContentEvent(2));
		assertEquals(listener.averallEventCount, 	2);
		assertEquals(listener.simpleEventCount, 	0);
		assertEquals(listener.contentEventCount, 	2);
		assertEquals(listener.contentEvent0Count, 	1);
		assertEquals(listener.contentEvent1Count, 	1);
	}
	
	@Test
	public void testMixedEvents() {
		ev.fireSyncEvent(new SimpleEvent());
		assertEquals(listener.averallEventCount, 	1);
		assertEquals(listener.simpleEventCount, 	1);
		assertEquals(listener.contentEvent0Count, 	0);
		assertEquals(listener.contentEvent0Count,	0);
		assertEquals(listener.contentEvent1Count, 	0);
		ev.fireSyncEvent(new ContentEvent(1));
		assertEquals(listener.averallEventCount, 	2);
		assertEquals(listener.simpleEventCount, 	1);
		assertEquals(listener.contentEventCount, 	1);
		assertEquals(listener.contentEvent0Count, 	1);
		assertEquals(listener.contentEvent1Count, 	0);
		ev.fireSyncEvent(new ContentEvent(2));
		assertEquals(listener.averallEventCount, 	3);
		assertEquals(listener.simpleEventCount, 	1);
		assertEquals(listener.contentEventCount, 	2);
		assertEquals(listener.contentEvent0Count, 	1);
		assertEquals(listener.contentEvent1Count, 	1);
		ev.fireSyncEvent(new SimpleEvent());
		assertEquals(listener.averallEventCount, 	4);
		assertEquals(listener.simpleEventCount, 	2);
		assertEquals(listener.contentEventCount, 	2);
		assertEquals(listener.contentEvent0Count, 	1);
		assertEquals(listener.contentEvent1Count, 	1);
		ev.fireSyncEvent(new ContentEvent(2));
		assertEquals(listener.averallEventCount, 	5);
		assertEquals(listener.simpleEventCount, 	2);
		assertEquals(listener.contentEventCount, 	3);
		assertEquals(listener.contentEvent0Count, 	1);
		assertEquals(listener.contentEvent1Count, 	2);
		ev.fireSyncEvent(new ContentEvent(1));
		assertEquals(listener.averallEventCount, 	6);
		assertEquals(listener.simpleEventCount, 	2);
		assertEquals(listener.contentEventCount, 	4);
		assertEquals(listener.contentEvent0Count, 	2);
		assertEquals(listener.contentEvent1Count, 	2);
	}
	
	class SimpleEvent implements Event{
		
	}
	
	public class ContentEvent implements Event, ContentBasedEvent{

		private int contentId;
		
		public ContentEvent(int inContentId) {
			contentId = inContentId;
		}
		
		@Override
		public int getContentId() {
			return contentId;
		}
		
	}
	
	public class SimpleListener implements EventListener{
		
		public int averallEventCount = 0;
		public int simpleEventCount = 0;
		public int contentEventCount = 0;
		public int contentEvent0Count = 0;
		public int contentEvent1Count = 0;
		
		@EventHandler
		public void onEvent(Event e){
			averallEventCount++;
		}
		
		@EventHandler
		public void onSimpleEvent(SimpleEvent e){
			simpleEventCount++;
		}
		
		@EventHandler
		public void onContentEvent(ContentEvent e){
			contentEventCount++;
		}
		
		@EventHandler
		@ContentEventHandler(contentId=1)
		public void onContent0Event(ContentEvent e){
			contentEvent0Count++;
		}
		
		@EventHandler
		@ContentEventHandler(contentId=2)
		public void onContent1Event(ContentEvent e){
			contentEvent1Count++;
		}
	}
}
