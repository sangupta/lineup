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

package com.sangupta.lineup.queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import com.sangupta.lineup.domain.QueueMessage;


/**
 * @author sangupta
 *
 */
public class PriorityLineUpQueue extends AbstractQueue {
	
	private final ConcurrentHashMap<String, QueueMessage> myMessages = new ConcurrentHashMap<String, QueueMessage>();
	
	/**
	 * Construct this queue.
	 * 
	 */
	public PriorityLineUpQueue(int delaySeconds) {
		super(delaySeconds);
	}

	/**
	 * Create a new instance of the backing {@link PriorityBlockingQueue}.
	 * 
	 * @see com.sangupta.lineup.queues.AbstractQueue#getBackingQueue()
	 */
	protected BlockingQueue<QueueMessage> getBackingQueue() {
		return new PriorityBlockingQueue<QueueMessage>();
	}

	/**
	 * @see com.sangupta.lineup.queues.AbstractQueue#addMessage(java.lang.String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		if(this.myMessages.containsKey(message)) {
			// increase its priority
			QueueMessage qm = this.myMessages.get(message);
			if(qm != null) {
				qm.incrementPriority();
			}
			
			return qm;
		}
		
		return super.addMessage(message, delaySeconds);
	}
	
	/**
	 * @see com.sangupta.lineup.queues.AbstractQueue#removeMessage(com.sangupta.lineup.domain.QueueMessage)
	 */
	@Override
	protected void removeMessage(QueueMessage queueMessage) {
		this.myMessages.remove(queueMessage.getBody());
	}
}
