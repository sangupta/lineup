package com.sangupta.lineup;

import org.junit.Before;

import com.sangupta.lineup.queues.LineUpQueue;
import com.sangupta.lineup.web.QueueMessageWebservice;
import com.sangupta.lineup.web.QueueWebservice;

public class TestQueueMessageWebservice {
	
	private LineUpQueue q;
	
	private QueueWebservice service;
	
	private QueueMessageWebservice webservice;
	
	@Before
	public void setup() {
		q = service.create(getQueueName(), null);
		
		// add two messages
	}

	private String getQueueName() {
		return "test-queue-" + String.valueOf(System.currentTimeMillis() + String.valueOf(System.nanoTime()));
	}

}
