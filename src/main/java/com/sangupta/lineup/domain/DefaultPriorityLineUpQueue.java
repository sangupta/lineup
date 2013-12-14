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

import com.sangupta.lineup.queues.InternalQueue;

/**
 * This class extends {@link DefaultLineUpQueue} and provides extra methods
 * to work with messages with assigned priority levels.
 * 
 * @author sangupta
 *
 */
public class DefaultPriorityLineUpQueue extends DefaultLineUpQueue {

	/**
	 * @param name
	 * @param options
	 * @param internalQueue
	 */
	public DefaultPriorityLineUpQueue(String name, String securityCode, QueueOptions options, InternalQueue internalQueue) {
		super(name, securityCode, options, internalQueue);
	}

	public QueueMessage addPriorityMessage(String message, int priority) {
		return addPriorityMessage(message, 0, priority);
	}
	
	public QueueMessage addPriorityMessage(String message, int delaySeconds, int priority) {
		return addMessage(message, delaySeconds, priority);
	}
}
