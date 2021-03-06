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

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.sangupta.lineup.LineUp;
import com.sangupta.lineup.domain.QueueMessage;

/**
 * Contract for any queue implementation that needs to serve in the
 * {@link LineUp} infrastructure. All {@link LineUpQueue}s are blocking in
 * nature. They are usually not bound by a limit and can accept any number of
 * incoming messages.
 * 
 * @author sangupta
 * @since 0.1.0
 */
public interface LineUpQueue extends BlockingQueue<QueueMessage> {
	
	/**
	 * Return the name associated with this queue.
	 * 
	 * @return the name of the queue
	 */
	public String getName();
	
	/**
	 * Return the security code associated with this queue.
	 * 
	 * @return the security code for this queue
	 */
	public String getSecurityCode();

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
	 * Add a message to the internal queue with the given delay.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @param delaySeconds
	 *            the time after which the message is made available in the
	 *            queue
	 * 
	 * @param priority
	 *            the priority of the incoming message, the higher the priority
	 *            the earlier it is given to the clients
	 * 
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 */
	public QueueMessage addMessage(String message, int delaySeconds, int priority);
	
	/**
	 * Add a message to the internal queue provided as a {@link QueueMessage}
	 * instance
	 * 
	 * @param queueMessage
	 *            the message to be added
	 * 
	 * @return the instance itself it was added, <code>null</code> if nothing
	 *         was added.
	 * 
	 */
	public QueueMessage addMessage(QueueMessage queueMessage);
	
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
	public int numMessages();
	
}
