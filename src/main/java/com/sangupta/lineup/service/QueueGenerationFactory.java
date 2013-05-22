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

package com.sangupta.lineup.service;

import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.queues.DefaultLineUpQueue;
import com.sangupta.lineup.queues.DuplicateRejectingLineUpQueue;
import com.sangupta.lineup.queues.InternalQueue;
import com.sangupta.lineup.queues.PriorityLineUpQueue;

/**
 * @author sangupta
 *
 */
public class QueueGenerationFactory {
	
	public static LineUpQueue getLineUpQueue(String name, QueueOptions options) {
		final QueueType queueType = options.getQueueType();
		
		if(queueType == null) {
			throw new IllegalArgumentException("Queue type cannot be null");
		}
		
		InternalQueue internalQueue = null;
		switch (queueType) {
			case AllowDuplicates:
				internalQueue = new DefaultLineUpQueue();
				break;
				
			case PriorityQueue:
				internalQueue = new PriorityLineUpQueue();
				break;
				
			case RejectDuplicates:
				internalQueue = new DuplicateRejectingLineUpQueue();
				break;
		}
		
		return new LineUpQueue(name, options, internalQueue);
	}

}
