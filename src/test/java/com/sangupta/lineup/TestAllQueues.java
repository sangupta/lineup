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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.queues.DuplicateAcceptingLineUpQueue;
import com.sangupta.lineup.queues.DuplicateRejectingLineUpQueue;
import com.sangupta.lineup.queues.LineUpQueue;

/**
 * Unit tests for duplicate accepting queue type.
 * 
 * @author sangupta
 *
 */
public class TestAllQueues {
	
	private static final int MAX = 10 * 1000;
	
	/**
	 * Test the default queue called {@link QueueType#AllowDuplicates}.
	 * 
	 */
	@Test
	public void testDuplicateAcceptingQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-dups", QueueType.AllowDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}
		
		Assert.assertTrue(queue instanceof DuplicateAcceptingLineUpQueue);
		testQueue(queue, true, 2);
	}
	
	/**
	 * Test the default queue called {@link QueueType#RejectDuplicates}.
	 * 
	 */
	@Test
	public void testDuplicateRejectingQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-nodups", QueueType.RejectDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}

		Assert.assertTrue(queue instanceof DuplicateRejectingLineUpQueue);
		testQueue(queue, true, 1);
	}
	
	/**
	 * Test the default queue called {@link QueueType#PriorityQueueWithDuplicates}.
	 * 
	 */
	@Test
	public void testPriorityQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-priority", QueueType.PriorityQueueWithDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}
		
		testQueue(queue, true, 2);
	}
	
	/**
	 * Test the default queue called {@link QueueType#PriorityQueueWithoutDuplicates}.
	 * 
	 */
	@Test
	public void testPriorityNoDuplicateQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-priority-no-dup", QueueType.PriorityQueueWithoutDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}
		
		testQueue(queue, true, 1);
	}
	
	/**
	 * Test the default queue called {@link QueueType#PriorityQueueMergingDuplicates}.
	 * 
	 */
	@Test
	public void testPriorityMergingQueue() {
		LineUpQueue queue = null;
		
		try {
			queue = LineUp.createMessageQueue("test-priority-merge", QueueType.PriorityQueueMergingDuplicates);
		} catch (QueueAlreadyExistsException e) {
			Assert.assertTrue("Queue creation failed", false);
		}
		
		testQueue(queue, true, 1);
	}

	private void testQueue(LineUpQueue queue, boolean testDuplicates, int duplicateSizeFactor) {
		Assert.assertNotNull("Queue cannot be null", queue);
		
		// build a list of a million messages
		Set<String> messages = new HashSet<String>();
		for(int index = 0; index < MAX; index++) {
			String msg = UUID.randomUUID().toString();
			messages.add(msg);
			queue.addMessage(msg);
			
			if(testDuplicates) {
				queue.addMessage(msg);
			}
		}
		
		// check size
		final int size = queue.numMessages();
		Assert.assertEquals("Queue size does not match", duplicateSizeFactor * MAX, size);
		Assert.assertEquals("Queue is empty", false, queue.isEmpty());
		
		// check that messages are retrieved in the same order
		for(int index = 0; index < size; index++) {
			QueueMessage qm = queue.getMessage();
			boolean same = messages.contains(qm.getBody());
			Assert.assertTrue("Different message received", same);
		}
		
		// check size
		Assert.assertEquals("Queue is not empty", 0, queue.numMessages());
		
		// check isEmpty
		Assert.assertEquals("Queue is not empty", true, queue.isEmpty());
		
		// clear everything
		queue.clear();
		messages.clear();
		System.gc();
	}

}
