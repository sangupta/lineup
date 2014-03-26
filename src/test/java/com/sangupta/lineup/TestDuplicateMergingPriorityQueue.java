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

import java.util.Iterator;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.queues.PriorityLineUpQueue;

/**
 * @author sangupta
 *
 */
public class TestDuplicateMergingPriorityQueue {
	
	public static void main(String[] args) throws QueueAlreadyExistsException {
		PriorityLineUpQueue pq = (PriorityLineUpQueue) LineUp.createMessageQueue("test", QueueType.PriorityQueue);
		QueueMessage qm = null;
		
		// add more
		for(int index = 0; index < 100; index++) {
			qm = new QueueMessage(String.valueOf(index), 0, 1);
			pq.add(qm);
		}
		
		// print all
		Iterator<QueueMessage> iterator = pq.iterator();
		while(iterator.hasNext()) {
			qm = iterator.next();
			System.out.println(qm.getBody() + ":" + qm.getPriority());
		}
		
		// test
		pq.add(new QueueMessage("12", 0, 5));
		qm = pq.poll();
		System.out.println(qm.getBody() + ":" + qm.getPriority());
	}

}
