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

import com.sangupta.lineup.queues.InternalQueue;

/**
 * @author sangupta
 *
 */
public class DefaultLineUpQueue implements LineUpQueue {
	
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
	 * @param options
	 * @param internalQueue
	 */
	public DefaultLineUpQueue(String name, QueueOptions options, InternalQueue internalQueue) {
		this.name = name;
		this.options = options;
		this.internalQueue = internalQueue;
		
		// initialize other params
		this.securityCode = UUID.randomUUID().toString();
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public QueueMessage addMessage(String message) {
		return this.internalQueue.addMessage(message);
	}
	
	/**
	 *  
	 * @param message
	 * @param delaySeconds
	 * @return
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		return this.internalQueue.addMessage(message, delaySeconds);
	}
	
	/**
	 * Add a clone of this message.
	 * 
	 * @param qm
	 * @return
	 */
	@Override
	public QueueMessage addMessage(QueueMessage qm) {
		if(qm.getDelaySeconds() >= 0) {
			return addMessage(qm.getBody(), qm.getDelaySeconds());
		}
		
		return addMessage(qm.getBody());
	}

	/**
	 * Return a message from the queue without waiting.
	 * 
	 * @return
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
	public QueueMessage getMessage(int longPollTime) throws InterruptedException {
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
