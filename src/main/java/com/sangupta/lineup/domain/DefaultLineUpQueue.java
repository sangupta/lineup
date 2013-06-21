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

package com.sangupta.lineup.domain;

import java.util.List;
import java.util.UUID;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.lineup.queues.InternalQueue;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The default implementation of the {@link LineUpQueue} that is constructed
 * when one is asked for. The behaviour of this implementation allows duplicates
 * to be inserted and all elements are retrieved in strictly FIFO order.
 * 
 * @author sangupta
 *
 */
@XStreamAlias("lineupQueue")
public class DefaultLineUpQueue extends AbstractLineUpBlockingQueue {
	
	/**
	 * The unique name of this queue.
	 */
	private final String name;
	
	/**
	 * The security code assigned to this queue.
	 * 
	 */
	private final String securityCode;
	
	/**
	 * The configuration options for this queue.
	 * 
	 */
	private final transient QueueOptions options;
	
	/**
	 * The backing {@link InternalQueue} implemenation.
	 * 
	 */
	private final transient InternalQueue internalQueue;
	
	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            the name to assign to this queue
	 * 
	 * @param securityCode
	 *            the security code to assign to this queue
	 * 
	 * @param options
	 *            the options to use for this queue
	 * 
	 * @param internalQueue
	 *            the internal queue implementation to use
	 * 
	 * @throws IllegalArgumentException
	 *             if either the <code>name</code> or the
	 *             <code>securityCode</code> is <code>null</code> or
	 *             <code>empty</code>.
	 */
	public DefaultLineUpQueue(String name, String securityCode, QueueOptions options, InternalQueue internalQueue) {
		if(AssertUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Queue name cannot be null/empty");
		}
		
		if(AssertUtils.isEmpty(securityCode)) {
			throw new IllegalArgumentException("Queue name cannot be null/empty");
		}
		
		this.name = name;
		this.options = options;
		this.internalQueue = internalQueue;
		
		// initialize other params
		if(securityCode == null) {
			this.securityCode = UUID.randomUUID().toString();
		} else {
			this.securityCode = securityCode;
		}
	}
	
	/**
	 * Add a message to the internal queue.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 * 
	 * @see LineUpQueue#addMessage(String)
	 */
	@Override
	public QueueMessage addMessage(String message) {
		return this.internalQueue.addMessage(message);
	}
	
	/**
	 * Add a message to the internal queue with the given delay.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @param delaySeconds
	 *            the time after which the message is made available in the
	 *            queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 *         
	 * @see LineUpQueue#addMessage(String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		return this.internalQueue.addMessage(message, delaySeconds);
	}
	
	/**
	 * Add a message to the internal queue.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 * 
	 * @see LineUpQueue#add(QueueMessage)
	 */
	@Override
	public QueueMessage addMessage(QueueMessage qm) {
		if(qm.getDelaySeconds() >= 0) {
			return addMessage(qm.getBody(), qm.getDelaySeconds());
		}
		
		return addMessage(qm.getBody());
	}

	/**
	 * Return a message from the queue, without waiting. Returns
	 * <code>null</code> if the queue is currently empty.
	 * 
	 * @return the {@link QueueMessage} instance which is wrapping up the actual
	 *         message
	 * 
	 * @see LineUpQueue#getMessage()
	 */
	@Override
	public QueueMessage getMessage() {
		return this.internalQueue.getMessage();
	}
	
	/**
	 * Return a message from the queue waiting for the given time.
	 * 
	 * @param longPollTime
	 * @return
	 * @throws InterruptedException 
	 */
	@Override
	public QueueMessage getMessage(long longPollTime) throws InterruptedException {
		return this.internalQueue.getMessage(longPollTime);
	}
	
	/**
	 * Return the given number of messages from the queue without waiting.
	 * 
	 * @param numMessages
	 * @return
	 */
	@Override
	public List<QueueMessage> getMessages(int numMessages) {
		return this.internalQueue.getMessages(numMessages);
	}
	
	/**
	 * Delete the message with the given identifier.
	 * 
	 * @param messageID
	 * @return
	 */
	@Override
	public boolean deleteMessage(String messageID) {
		return this.internalQueue.deleteMessage(messageID);
	}
	
	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#numMessages()
	 */
	@Override
	public int numMessages() {
		return this.internalQueue.size();
	}

	// Usual accessors follow

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the options
	 */
	public QueueOptions getOptions() {
		return options;
	}

	/**
	 * @return the internalQueue
	 */
	public InternalQueue getInternalQueue() {
		return internalQueue;
	}

	/**
	 * @return the securityCode
	 */
	public String getSecurityCode() {
		return securityCode;
	}

}
