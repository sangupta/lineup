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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueOptions;

/**
 * A {@link LineUpQueue} that behaves like a normal queue. Any message added
 * again will be added to the queue, and thus we may see duplicate messages
 * being served via this queue.
 * 
 * @author sangupta
 * @since 0.2.0
 */
public class DuplicateAcceptingLineUpQueue extends AbstractLineUpQueue {
	
	/**
	 * The internal backing queue
	 */
	protected final BlockingQueue<QueueMessage> internalQueue;
	
	/**
	 * Construct an instance of queue which can accept duplicates.
	 * 
	 * @param name
	 * @param securityCode
	 * @param options
	 */
	public DuplicateAcceptingLineUpQueue(String name, String securityCode, QueueOptions options) {
		super(name, securityCode, options);
		this.internalQueue = new LinkedBlockingQueue<QueueMessage>();
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#addMessage(com.sangupta.lineup.domain.QueueMessage)
	 */
	@Override
	public QueueMessage addQueueMessage(QueueMessage queueMessage) {
		this.internalQueue.add(queueMessage);
		return queueMessage;
	}

	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#getMessage(long)
	 */
	@Override
	public QueueMessage getQueueMessage(long longPollTime) throws InterruptedException {
		return this.internalQueue.poll(longPollTime, TimeUnit.SECONDS);
	}
	
	/**
	 * 
	 * @see com.sangupta.lineup.queues.AbstractLineUpQueue#removeMessageID(long)
	 */
	@Override
	public boolean removeMessageID(long id) {
		return this.internalQueue.remove(id);
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.internalQueue.clear();
	}

}
