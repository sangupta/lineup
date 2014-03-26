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
	
	@Test
	public void testQueueMessage() {
		QueueMessage qm1 = new QueueMessage("1", 0, 1);
		QueueMessage qm2 = new QueueMessage("2", 0, 1);
		QueueMessage qm11 = new QueueMessage("1", 0, 1);
		
		Assert.assertFalse(qm1.equals(null));
		Assert.assertFalse(qm1.equals(qm2));
		Assert.assertFalse(qm2.equals(qm1));
		Assert.assertTrue(qm1.equals(qm1));
		
		Assert.assertTrue(qm1.equals(qm11));
		Assert.assertTrue(qm1.getMessageID() < qm2.getMessageID());
		Assert.assertTrue(qm1.getMessageID() < qm11.getMessageID());
		Assert.assertTrue(qm2.getMessageID() < qm11.getMessageID());
		
		Assert.assertNotNull(qm1.getMd5());
		Assert.assertNotNull(qm2.getMd5());
		Assert.assertNotNull(qm11.getMd5());
		
		Assert.assertTrue(qm1.compareTo(qm1) == 0);
		
		Assert.assertFalse(qm1.hashCode() == qm2.hashCode());
		
		ConcurrentSkipListSet<QueueMessage> set = new ConcurrentSkipListSet<QueueMessage>();
		Assert.assertTrue(set.add(qm1));
		Assert.assertFalse(set.add(qm1));
	}

}

