/**
 *
 * lineup - In-Memory high-throughput queue
 * Copyright (c) 2013, Sandeep Gupta
 * 
 * http://www.sangupta/projects/lineup
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

import java.util.concurrent.ConcurrentSkipListSet;

import junit.framework.Assert;

import org.junit.Test;

import com.sangupta.lineup.domain.QueueMessage;

/**
 * @author sangupta
 *
 */
public class TestQueueMessage {
	
	private static final int MILLION = 1000 * 1000;
	
	@Test
	public void testQueueMessage() {
		QueueMessage qm1 = new QueueMessage("1", 0, 1);
		QueueMessage qm2 = new QueueMessage("2", 0, 1);
		QueueMessage qm11 = new QueueMessage("1", 0, 1);
		QueueMessage qm12 = new QueueMessage("1", 0, 1);
		
		// null check
		Assert.assertFalse(qm1.equals(null));
		
		// reflexive
		Assert.assertTrue(qm1.equals(qm1));
		
		// symmetric
		Assert.assertTrue(qm1.equals(qm11));
		Assert.assertTrue(qm11.equals(qm1));
		
		// transitive
		Assert.assertTrue(qm1.equals(qm11));
		Assert.assertTrue(qm11.equals(qm12));
		Assert.assertTrue(qm12.equals(qm11));
		
		// others
		Assert.assertFalse(qm1.equals(qm2));
		Assert.assertFalse(qm2.equals(qm1));
		
		Assert.assertTrue(qm1.getMessageID() < qm2.getMessageID());
		Assert.assertTrue(qm1.getMessageID() < qm11.getMessageID());
		Assert.assertTrue(qm2.getMessageID() < qm11.getMessageID());
		
		Assert.assertNotNull(qm1.getMd5());
		Assert.assertNotNull(qm2.getMd5());
		Assert.assertNotNull(qm11.getMd5());
		
		// compare to
		Assert.assertTrue(qm1.compareTo(qm1) == 0); // message is same
		Assert.assertTrue(qm1.compareTo(qm2) < 0); // messages are different, text is different
		Assert.assertTrue(qm1.compareTo(qm11) == 0); // messages are same 
		Assert.assertTrue(qm1.compareTo(qm12) == 0); // messages are same
		
		// hash code
//		Assert.assertTrue(qm1.hashCode() != qm2.hashCode());
//		Assert.assertTrue(qm1.hashCode() == qm11.hashCode());
//		Assert.assertTrue(qm1.hashCode() == qm12.hashCode());
		
		ConcurrentSkipListSet<QueueMessage> set = new ConcurrentSkipListSet<QueueMessage>();
		Assert.assertTrue(set.add(qm1));
		Assert.assertFalse(set.add(qm1));
	}
	
	@Test
	public void testMillionMessages() {
		ConcurrentSkipListSet<QueueMessage> messages = new ConcurrentSkipListSet<QueueMessage>();
		QueueMessage qm = new QueueMessage("text", 0, 1);
		messages.add(qm);
		for(int index = 0; index < 2 * MILLION; index++) {
			qm = new QueueMessage("text", 0, 1);
			if(!messages.contains(qm)) {
				Assert.assertTrue("failed for index: " + index, false);
			}
		}
	}
	
}
