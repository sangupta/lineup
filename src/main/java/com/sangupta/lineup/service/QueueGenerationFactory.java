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

import com.sangupta.lineup.domain.DefaultLineUpQueue;
import com.sangupta.lineup.domain.DefaultPriorityLineUpQueue;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.queues.DuplicateAcceptingInternalQueue;
import com.sangupta.lineup.queues.DuplicateRejectingInternalQueue;
import com.sangupta.lineup.queues.PriorityInternalQueue;

/**
 * @author sangupta
 *
 */
public class QueueGenerationFactory {
	
	public static DefaultLineUpQueue getLineUpQueue(String name, QueueOptions options) {
		final QueueType queueType = options.getQueueType();
		
		if(queueType == null) {
			throw new IllegalArgumentException("Queue type cannot be null");
		}
		
		switch (queueType) {
			case AllowDuplicates:
				return new DefaultLineUpQueue(name, options, new DuplicateAcceptingInternalQueue(options.getDelaySeconds()));
				
			case PriorityQueue:
				return new DefaultPriorityLineUpQueue(name, options, new PriorityInternalQueue(options.getDelaySeconds()));
				
			case RejectDuplicates:
				return new DefaultLineUpQueue(name, options, new DuplicateRejectingInternalQueue(options.getDelaySeconds()));
		}
		
		throw new IllegalArgumentException("Queue type is neither null/nor recognized by the system");
	}

}
