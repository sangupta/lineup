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

package com.sangupta.lineup.queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueOptions;


/**
 * This class extends {@link AbstractLineUpQueue} and provides extra methods
 * to work with messages with assigned priority levels.
 * 
 * @author sangupta
 *
 */
public class PriorityLineUpQueue extends AbstractLineUpQueue {
	
	/**
	 * The internal backing queue
	 */
	protected final BlockingQueue<QueueMessage> internalQueue;
	
	/**
	 * Construct an instance of queue which has priority for each message
	 * 
	 * @param name
	 *            the name of the queue
	 * 
	 * @param securityCode
	 *            the security code
	 * 
	 * @param options
	 *            the queue options
	 */
	public PriorityLineUpQueue(String name, String securityCode, QueueOptions options) {
		super(name, securityCode, options);
		
		this.internalQueue = new PriorityBlockingQueue<QueueMessage>();
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#addMessage(com.sangupta.lineup.domain.QueueMessage)
	 */
	@Override
	public QueueMessage addQueueMessage(QueueMessage queueMessage) {
		boolean added = this.internalQueue.add(queueMessage);
		if(!added) {
			return null;
		}
		
		return queueMessage;
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#getMessage(long)
	 */
	@Override
	public QueueMessage getQueueMessage(long longPollTime) throws InterruptedException {
		QueueMessage qm = this.internalQueue.poll(longPollTime, TimeUnit.SECONDS);
		if(qm == null) {
			return null;
		}
		
		return qm;
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.internalQueue.clear();
	}

	/**
	 * @see com.sangupta.lineup.queues.AbstractLineUpQueue#removeMessageID(long)
	 */
	@Override
	public boolean removeMessageID(long id) {
		this.internalQueue.remove(id);
		return true;
	}
	
}
