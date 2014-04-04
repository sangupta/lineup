/**
 *
 * lineup - In-Memory high-throughput queue
 * Copyright (c) 2013, Sandeep Gupta
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

import javax.ws.rs.WebApplicationException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sangupta.lineup.queues.DuplicateAcceptingLineUpQueue;
import com.sangupta.lineup.queues.DuplicateRejectingLineUpQueue;
import com.sangupta.lineup.queues.LineUpQueue;
import com.sangupta.lineup.queues.MergingPriorityLineUpQueue;
import com.sangupta.lineup.queues.PriorityLineUpQueue;
import com.sangupta.lineup.queues.PriorityNoDuplicateLineUpQueue;
import com.sangupta.lineup.web.LineUpHealthCheckWebservice;
import com.sangupta.lineup.web.QueueWebservice;

/**
 * @author sangupta
 *
 */
public class TestWebservices {

	QueueWebservice service;
	
	@Before
	public void setup() {
		this.service = new QueueWebservice();
	}
	
	@After
	public void tearDown() {
		this.service = null;
	}
	
	@Test
	public void testLineUpHealthCheckWebservice() {
		LineUpHealthCheckWebservice service = new LineUpHealthCheckWebservice();
		Assert.assertEquals("Yes", service.isAvailable());
	}
	
	@Test
	public void testQueueNotExists() {
		// check queue exists
		try {
			service.getQueueUrl(getQueueName());
			Assert.assertFalse(true); // queue must not exist
		} catch(WebApplicationException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testQueueCreation() {
		LineUpQueue q = null;
		
		// create default queue
		q = service.create(getQueueName(), null);
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateAcceptingLineUpQueue);
		
		// create other types
		q = service.create(getQueueName(), "");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateAcceptingLineUpQueue);
		
		q = service.create(getQueueName(), "AllowDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateAcceptingLineUpQueue);
		
		q = service.create(getQueueName(), "RejectDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateRejectingLineUpQueue);
		
		q = service.create(getQueueName(), "PriorityQueueWithDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof PriorityLineUpQueue);
		
		q = service.create(getQueueName(), "PriorityQueueNoDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof PriorityNoDuplicateLineUpQueue);
		
		q = service.create(getQueueName(), "PriorityQueue");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof MergingPriorityLineUpQueue);
	}
	
	@Test
	public void testQueueCreationViaCreatePost() {
		LineUpQueue q = null;
		
		// create default queue
		q = service.createPost(getQueueName(), null);
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateAcceptingLineUpQueue);
		
		// create other types
		q = service.createPost(getQueueName(), "");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateAcceptingLineUpQueue);
		
		q = service.createPost(getQueueName(), "AllowDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateAcceptingLineUpQueue);
		
		q = service.createPost(getQueueName(), "RejectDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof DuplicateRejectingLineUpQueue);
		
		q = service.createPost(getQueueName(), "PriorityQueueWithDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof PriorityLineUpQueue);
		
		q = service.createPost(getQueueName(), "PriorityQueueNoDuplicates");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof PriorityNoDuplicateLineUpQueue);
		
		q = service.createPost(getQueueName(), "PriorityQueue");
		Assert.assertTrue(q != null);
		Assert.assertTrue(q instanceof MergingPriorityLineUpQueue);
	}
	
	@Test
	public void testQueueDelete() {
		LineUpQueue q = null;
		String name = getQueueName();
		
		// create once
		q = service.create(name, null);
		Assert.assertTrue(q != null);
		Assert.assertNotNull(service.getQueueUrl(name));
		
		// delete
		service.delete(name);
		try {
			service.getQueueUrl(name);
			Assert.assertTrue(false);
		} catch(WebApplicationException e) {
			Assert.assertTrue(true);
		}
		
		// create once again with same name
		q = service.create(name, null);
		Assert.assertTrue(q != null);
		Assert.assertNotNull(service.getQueueUrl(name));
		
	}
	
	private String getQueueName() {
		return "test-queue-" + String.valueOf(System.currentTimeMillis() + String.valueOf(System.nanoTime()));
	}
}
