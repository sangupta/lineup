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
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author sangupta
 *
 */
public interface LineUpQueue extends BlockingQueue<QueueMessage> {
	
	/**
	 * Add a message to the internal queue.
	 * 
	 * @param message
	 * @return
	 */
	public QueueMessage addMessage(String message);
	
	/**
	 * Add a message to the internal queue.
	 * 
	 * @param message
	 * @param delaySeconds
	 * @return
	 */
	public QueueMessage addMessage(String message, int delaySeconds);
	
	/**
	 * Add a message to the internal queue.
	 * 
	 * @param queueMessage
	 * @return
	 */
	public QueueMessage addMessage(QueueMessage queueMessage);
	
	/**
	 * Return a message from the queue, without waiting. Returns <code>null</code>
	 * if the queue is currently empty.
	 * 
	 * @return
	 */
	public QueueMessage getMessage();
	
	/**
	 * Return a message from the queue, waiting for the specified poll time
	 * before returning <code>null</code>.
	 * 
	 * @param longPollTime
	 * @return
	 * @throws InterruptedException
	 */
	public QueueMessage getMessage(long longPollTime) throws InterruptedException;
	
	/**
	 * Return given number of messages from the queue.
	 * 
	 * @param numMessages
	 * @return
	 */
	public List<QueueMessage> getMessages(int numMessages);
	
	/**
	 * Delete the message with the given identifier from the queue.
	 * 
	 * @param messageID
	 * @return
	 */
	public boolean deleteMessage(String messageID);
	
	/**
	 * Returns the number of messages in the queue.
	 * 
	 * @return
	 */
	public int numMessages();

}
