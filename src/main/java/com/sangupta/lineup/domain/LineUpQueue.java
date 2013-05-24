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
	
	public QueueMessage addMessage(String message);
	
	public QueueMessage addMessage(String message, int delaySeconds);
	
	public QueueMessage addMessage(QueueMessage queueMessage);
	
	public QueueMessage getMessage();
	
	public QueueMessage getMessage(long longPollTime) throws InterruptedException;
	
	public List<QueueMessage> getMessages(int numMessages);
	
	public boolean deleteMessage(String messageID);

}
