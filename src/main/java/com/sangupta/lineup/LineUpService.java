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

package com.sangupta.lineup;

import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.exceptions.QueueAlreadyDeletedException;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.exceptions.QueueNotFoundException;
import com.sangupta.lineup.service.QueueService;
import com.sangupta.lineup.service.impl.DefaultQueueService;

/**
 * Utility class to help use <strong>LineUp</strong> framework
 * in the embedded mode.
 * 
 * @author sangupta
 *
 */
public class LineUpService {
	
	/**
	 * Our own single-ton instance
	 */
	private static final QueueService QUEUE_SERVICE = new DefaultQueueService();

	/**
	 * Get a new queue with default options or retrieve an existing queue
	 * with whatever options that were provided.
	 * 
	 * @param name
	 * @return
	 * @throws QueueAlreadyExistsException 
	 */
	public static LineUpQueue createMessageQueue(String name) throws QueueAlreadyExistsException {
		return QUEUE_SERVICE.createQueue(name);
	}
	
	/**
	 * Retrieve a queue with the given name and security code. The queue MUST be pre-created.
	 * 
	 * @param name
	 * @param securityCode
	 * @return
	 * @throws QueueNotFoundException
	 */
	public static LineUpQueue getQueue(String name, String securityCode) throws QueueNotFoundException {
		return QUEUE_SERVICE.getQueue(name, securityCode);
	}
	
	/**
	 * Delete a queue with the given name.
	 * 
	 * @param name
	 * @return
	 * @throws QueueAlreadyDeletedException 
	 */
	public static boolean deleteMessageQueue(String name) throws QueueAlreadyDeletedException {
		return QUEUE_SERVICE.deleteQueue(name);
	}
	
}
