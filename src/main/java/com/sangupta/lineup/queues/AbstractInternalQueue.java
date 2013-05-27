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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.sangupta.lineup.domain.QueueMessage;

/**
 * @author sangupta
 *
 */
public abstract class AbstractInternalQueue implements InternalQueue {
	
	protected final BlockingQueue<QueueMessage> QUEUE;
	
	protected final AtomicLong AUTO_INCREMENTOR = new AtomicLong(1);
	
	protected int delaySeconds;
	
	/**
	 * Count the number of messages in the queue.
	 * 
	 * @see com.sangupta.lineup.queues.InternalQueue#size()
	 */
	public int size() {
		if(QUEUE != null) {
			return QUEUE.size();
		}
		
		return 0;
	}
	
	/**
	 * Create a new instance of this {@link AbstractInternalQueue} with the given value
	 * of default seconds to delay before inserting an element into the visible list
	 * of elements.
	 * 
	 * @param delaySeconds
	 */
	protected AbstractInternalQueue(int delaySeconds) {
		if(delaySeconds < 0) {
			throw new IllegalArgumentException("Delay seconds cannot be less than zero.");
		}
		
		this.delaySeconds = delaySeconds;
		this.QUEUE = getBackingQueue();
		
		if(this.QUEUE == null) {
			throw new IllegalStateException("Backing implementing queue cannot be null");
		}
	}
	
	/**
	 * Method that returns the backing queue implementation that needs to be
	 * used in the concrete class. By default this returns a {@link LinkedBlockingQueue}.
	 * 
	 * @return
	 */
	protected BlockingQueue<QueueMessage> getBackingQueue() {
		return new LinkedBlockingQueue<QueueMessage>();
	}
	
	/**
	 * Clean up the message from any other mapping and/or queue where it may
	 * be present.
	 * 
	 * @param queueMessage
	 */
	protected abstract void removeMessage(QueueMessage queueMessage);
	
	/**
	 * @see com.sangupta.lineup.service.InternalQueue#addMessage(java.lang.String)
	 */
	@Override
	public QueueMessage addMessage(String message) {
		return addMessage(message, this.delaySeconds);
	}
	
	/**
	 * 
	 * @param message
	 * @param delaySeconds
	 * @return
	 */
	public QueueMessage addMessage(String message, int delaySeconds) {
		QueueMessage queueMessage = createMessage(message, delaySeconds);
		this.QUEUE.offer(queueMessage);
		return queueMessage;
	}
	
	/**
	 * @see com.sangupta.lineup.queues.InternalQueue#getMessage()
	 */
	@Override
	public QueueMessage getMessage() {
		QueueMessage qm = this.QUEUE.poll();
		if(qm != null) {
			removeMessage(qm);
		}
		
		return qm;
	}
	
	/**
	 * @throws InterruptedException 
	 * @see com.sangupta.lineup.queues.InternalQueue#getMessage(int)
	 */
	@Override
	public QueueMessage getMessage(long longPollTime) throws InterruptedException {
		QueueMessage qm = this.QUEUE.poll(longPollTime, TimeUnit.SECONDS);
		if(qm != null) {
			removeMessage(qm);
		}
		
		return qm;
	}
	
	/**
	 * 
	 * @see com.sangupta.lineup.queues.InternalQueue#getMessages(int)
	 */
	@Override
	public List<QueueMessage> getMessages(int numMessages) {
		List<QueueMessage> list = new ArrayList<QueueMessage>();
		
		QueueMessage qm;
		for(int index = 0; index < numMessages; index++) {
			qm = this.QUEUE.poll();
			if(qm != null) {
				removeMessage(qm);
				list.add(qm);
				continue;
			}
			
			break;
		}
		
		return list;
	}
	
	/**
	 * 
	 * @see com.sangupta.lineup.queues.InternalQueue#deleteMessage(java.lang.String)
	 */
	public boolean deleteMessage(String messageID) {
		try {
			long id = Long.parseLong(messageID);
			return QUEUE.remove(QueueMessage.createMessage(id));
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("MessageID cannot be parsed into a valid entity.");
		}
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	protected final QueueMessage createMessage(String message) {
		return createMessage(message, this.delaySeconds);
	}

	/**
	 * 
	 * @param message
	 * @param delaySeconds
	 * @return
	 */
	protected final QueueMessage createMessage(String message, int delaySeconds) {
		return new QueueMessage(AUTO_INCREMENTOR.getAndIncrement(), message, delaySeconds, 1);
	}
	
	// Usual accessors follow

	/**
	 * @param delaySeconds the delaySeconds to set
	 */
	public void setDelaySeconds(int delaySeconds) {
		this.delaySeconds = delaySeconds;
	}
}
