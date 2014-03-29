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

package com.sangupta.lineup.service;

import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.queues.DuplicateAcceptingLineUpQueue;
import com.sangupta.lineup.queues.DuplicateRejectingLineUpQueue;
import com.sangupta.lineup.queues.LineUpQueue;
import com.sangupta.lineup.queues.MergingPriorityLineUpQueue;
import com.sangupta.lineup.queues.PriorityLineUpQueue;
import com.sangupta.lineup.queues.PriorityNoDuplicateLineUpQueue;

/**
 * Factory class to generate new queues depending on the options.
 * 
 * @author sangupta
 * @since 0.1.0
 */
public class QueueGenerationFactory {
	
	/**
	 * A simple factory method to get the right type of queue based on the {@link QueueOptions}
	 * given.
	 * 
	 * @param name
	 * @param securityCode
	 * @param options
	 * @return
	 */
	public static LineUpQueue getLineUpQueue(String name, String securityCode, QueueOptions options) {
		final QueueType queueType = options.getQueueType();
		
		if(queueType == null) {
			throw new IllegalArgumentException("Queue type cannot be null");
		}
		
		switch (queueType) {
			case AllowDuplicates:
				return new DuplicateAcceptingLineUpQueue(name, securityCode, options);
				
			case RejectDuplicates:
				return new DuplicateRejectingLineUpQueue(name, securityCode, options);

			case PriorityQueueWithDuplicates:
				return new PriorityLineUpQueue(name, securityCode, options);
				
			case PriorityQueueWithoutDuplicates:
				return new PriorityNoDuplicateLineUpQueue(name, securityCode, options);

			case PriorityQueueMergingDuplicates:
				return new MergingPriorityLineUpQueue(name, securityCode, options);

			default:
				break;
		}
		
		throw new IllegalArgumentException("Queue type is neither null/nor recognized by the system");
	}

}
