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

package com.sangupta.lineup.domain;

import com.sangupta.jerry.util.AssertUtils;

/**
 * The various types of queues that are supported.
 * 
 * @author sangupta
 * @since 0.1.0
 */
public enum QueueType {
	
	/**
	 * A queue that allows duplicate elements to be added to the queue.
	 */
	AllowDuplicates,
	
	/**
	 * A queue that rejects incoming duplicate messages and keeps only unique
	 * ones. Once a message is consumed, a similar incoming message will again
	 * be added to the queue.
	 */
	RejectDuplicates,
	
	/**
	 * A queue that works as a priority queue and allows duplicates elements
	 * to be inserted.
	 */
	PriorityQueueWithDuplicates,
	
	/**
	 * A queue that works as a priority queue but does not allow duplicates. A
	 * duplicate element if received will be ignored and not added to the queue.
	 * 
	 */
	PriorityQueueWithoutDuplicates,

	/**
	 * A queue that on receiving a duplicate message, increments the priority of
	 * the existing message and moves it ahead in the queue. This helps in
	 * making sure that such a message is sent to clients for consumption
	 * earlier than others in the queue.
	 */
	PriorityQueueMergingDuplicates;

	/**
	 * A method to convert 
	 * @param queueType
	 * @return
	 */
	public static QueueType fromString(String queueType) {
		if(AssertUtils.isEmpty(queueType)) {
			throw new IllegalArgumentException("Queue type cannot be null/empty");
		}
		
		if("AllowDuplicates".equalsIgnoreCase(queueType)) {
			return AllowDuplicates;
		}
		
		if("RejectDuplicates".equalsIgnoreCase(queueType)) {
			return RejectDuplicates;
		}
		
		if("PriorityQueue".equalsIgnoreCase(queueType)) {
			return PriorityQueueMergingDuplicates;
		}
		
		throw new IllegalArgumentException("Unknown queue type specified as: " + queueType);
	}

}
