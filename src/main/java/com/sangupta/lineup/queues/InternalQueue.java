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

import com.sangupta.lineup.domain.QueueMessage;

/**
 * @author sangupta
 *
 */
public interface InternalQueue {
	
	/**
	 * Create a new message in the queue
	 * 
	 * @param message
	 * @return
	 */
	public QueueMessage addMessage(String message);

	/**
	 * 
	 * @param message
	 * @param delaySeconds
	 * @return
	 */
	public QueueMessage addMessage(String message, int delaySeconds);
	
	/**
	 * Return back a message without waiting.
	 * 
	 * @return
	 */
	public QueueMessage getMessage();
	
	/**
	 * 
	 * @param longPollTime
	 * @return
	 * @throws InterruptedException
	 */
	public QueueMessage getMessage(long longPollTime) throws InterruptedException;
	
	/**
	 * Return N number of messages from this queue.
	 * 
	 * @param numMessages
	 * @return
	 */
	public List<QueueMessage> getMessages(int numMessages);
	
	/**
	 * Delete the message as specified by the provided message identifier.
	 * 
	 * @param messageID
	 * @return
	 */
	public boolean deleteMessage(String messageID);
	
}
