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

import java.util.List;

import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.domain.QueueMessage;

/**
 * Contract for all implementations that will provide the functionality
 * of an {@link InternalQueue} which can be embedded inside an actual
 * {@link LineUpQueue} instance.
 * 
 * @author sangupta
 *
 */
public interface InternalQueue {
	
	/**
	 * Add a message to the internal queue.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 * 
	 */
	public QueueMessage addMessage(String message);

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
	 */
	public QueueMessage addMessage(String message, int delaySeconds);
	
	/**
	 * Return a message from the queue, without waiting. Returns
	 * <code>null</code> if the queue is currently empty.
	 * 
	 * @return the {@link QueueMessage} instance which is wrapping up the actual
	 *         message
	 * 
	 */
	public QueueMessage getMessage();
	
	/**
	 * Return a message from the queue, waiting for the specified poll time
	 * before returning <code>null</code>.
	 * 
	 * @param longPollTime
	 *            the time to wait before which the call would return
	 *            <code>null</code> if no message is available
	 * 
	 * @return the {@link QueueMessage} if one was available, <code>null</code>
	 *         otherwise
	 * 
	 * @throws InterruptedException
	 *             if the method was interrupted as part of shutdown or thread
	 *             closure or otherwise
	 */
	public QueueMessage getMessage(long longPollTime) throws InterruptedException;
	
	/**
	 * Return given number of messages from the queue.
	 * 
	 * @param numMessages
	 *            the number of message to read from the queue
	 * 
	 * @return a {@link List} object containing either the equal number of
	 *         object, or less if there is a scarcity
	 */
	public List<QueueMessage> getMessages(int numMessages);
	
	/**
	 * Delete the message with the given identifier from the queue.
	 * 
	 * @param messageID
	 *            the message identifier uniquely identifying the message
	 * @return <code>true</code> if the message was deleted successfully,
	 *         <code>false</code> otherwise.
	 * 
	 */
	public boolean deleteMessage(String messageID);
	
	/**
	 * Returns the total number of messages in the queue.
	 * 
	 * @return the number of messages in the queue
	 * 
	 */
	public int size();
	
}
