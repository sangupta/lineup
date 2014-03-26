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
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueOptions;

/**
 * A {@link LineUpQueue} that behaves like a normal queue with an additional
 * check to reject any duplicates. Any message added again will be NOT be added
 * to the queue till the first message is not consumed from the queue. Thus, it
 * is not possible to see duplicate messages in this queue at any given time.
 * 
 * @author sangupta
 * @since 0.2.0 
 */
public class DuplicateRejectingLineUpQueue extends AbstractLineUpQueue {
	
	/**
	 * The internal backing queue
	 */
	protected final BlockingQueue<QueueMessage> internalQueue;
	
	/**
	 * A set of all current messages in this {@link LineUpQueue}.
	 */
	protected final ConcurrentSkipListSet<QueueMessage> currentMessages;
	
	public DuplicateRejectingLineUpQueue(String name, String securityCode, QueueOptions options) {
		super(name, securityCode, options);
		
		this.internalQueue = new LinkedBlockingQueue<QueueMessage>();
		this.currentMessages = new ConcurrentSkipListSet<QueueMessage>();
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#addMessage(com.sangupta.lineup.domain.QueueMessage)
	 */
	@Override
	public QueueMessage addMessage(QueueMessage queueMessage) {
		if(queueMessage == null) {
			throw new IllegalArgumentException("QueueMessage to be added cannot be null");
		}
		
		if(!this.currentMessages.add(queueMessage)) {
			return null; // nothing was added
		}
		
		this.internalQueue.add(queueMessage);
		return queueMessage;
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#getMessage(long)
	 */
	@Override
	public QueueMessage getMessage(long longPollTime) throws InterruptedException {
		QueueMessage qm = this.internalQueue.poll(longPollTime, TimeUnit.SECONDS);
		if(qm == null) {
			return qm;
		}
		
		this.currentMessages.remove(qm.getBody());
		return qm;
	}

	/**
	 * 
	 * @see com.sangupta.lineup.queues.AbstractLineUpQueue#removeMessageID(long)
	 */
	@Override
	public boolean removeMessageID(long id) {
		boolean removed = this.internalQueue.remove(id);
		if(removed) {
			this.currentMessages.remove(id);
		}
		
		return removed;
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#numMessages()
	 */
	@Override
	public int numMessages() {
		return this.internalQueue.size();
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.internalQueue.clear();
		this.currentMessages.clear();
	}

}
