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

import com.sangupta.lineup.domain.QueueMessage;


/**
 * @author sangupta
 *
 */
public class DuplicateAcceptingInternalQueue extends AbstractInternalQueue {
	
	/**
	 * Default constructor
	 * 
	 */
	public DuplicateAcceptingInternalQueue(int delaySeconds) {
		super(delaySeconds);
	}

	/**
	 * @see com.sangupta.lineup.queues.AbstractInternalQueue#removeMessage(com.sangupta.lineup.domain.QueueMessage)
	 */
	@Override
	protected void removeMessage(QueueMessage queueMessage) {
		// do nothing
	}

}
