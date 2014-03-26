/**
 *
 * lineup - In-Memory high-throughput queue
 * Copyright (c) 2013-2014, Sandeep Gupta
 * 
 * http://sangupta.com/projects/lineup
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.lineup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.queues.LineUpQueue;

/**
 * Unit tests for duplicate accepting queue type.
 * 
 * @author sangupta
 *
 */
public class TestAllQueues {
	
	private static final int MAX = 1000 * 1000;
	
	@Test
	public void testDuplicateAcceptingQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-dups", QueueType.AllowDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}
		
		testQueue(queue, false);
	}
	
	@Test
	public void testDuplicateRejectingQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-nodups", QueueType.RejectDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}
		
		testQueue(queue, true);
	}
	
//	@Test
//	public void testPriorityQueue() {
//		LineUpQueue queue = null;
//		
//		try {
//			queue = LineUp.createMessageQueue("test-priority", QueueType.PriorityQueue);
//		} catch (QueueAlreadyExistsException e) {
//			Assert.assertTrue("Queue creation failed", false);
//		}
//		
//		testQueue(queue);
//	}
	
	private void testQueue(LineUpQueue queue, boolean testDuplicates) {
		Assert.assertNotNull("Queue cannot be null", queue);
		
		// build a list of a million messages
		List<String> messages = new ArrayList<String>();
		for(int index = 0; index < MAX; index++) {
			String msg = UUID.randomUUID().toString();
			messages.add(msg);
			queue.addMessage(msg);
			
			if(testDuplicates) {
				queue.addMessage(msg);
			}
		}
		
		// check size
		Assert.assertEquals("Queue size does not match", MAX, queue.numMessages());
		Assert.assertEquals("Queue is empty", false, queue.isEmpty());
		
		// check that messages are retrieved in the same order
		for(int index = 0; index < MAX; index++) {
			QueueMessage qm = queue.getMessage();
			boolean same = messages.get(index).equals(qm.getBody());
			Assert.assertTrue("Different message received", same);
		}
		
		// check size
		Assert.assertEquals("Queue is not empty", 0, queue.numMessages());
		
		// check isEmpty
		Assert.assertEquals("Queue is not empty", true, queue.isEmpty());
	}

}
