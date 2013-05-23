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

import java.util.List;

import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.exceptions.QueueAlreadyDeletedException;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.exceptions.QueueNotFoundException;

/**
 * Contract for implementations that need to provide queue services
 * as part of lineup.
 * 
 * @author sangupta
 *
 */
public interface QueueService {
	
	public LineUpQueue createQueue(String name) throws QueueAlreadyExistsException;
	
	/**
	 * Create a queue with the given name. Return the queue url that needs to be used when
	 * accessing it.
	 * 
	 * @param name
	 * @param options
	 * @return
	 */
	public LineUpQueue createQueue(String name, QueueOptions options) throws QueueAlreadyExistsException;

	/**
	 * Delete a queue from the memory.
	 * 
	 * @param name
	 * @return
	 */
	public boolean deleteQueue(String name) throws QueueAlreadyDeletedException;

	/**
	 * Get the queue URL for the given name.
	 * 
	 * @param name
	 * @return
	 */
	public String getQueueUrl(String name) throws QueueNotFoundException;
	
	/**
	 * Return a list of all queue's that match the specified prefix. If not prefix is given
	 * then return a list of all queues.
	 * 
	 * @param prefix
	 * @return
	 */
	public List<String> getAllQueues(String prefix);

	/**
	 * 
	 * @param name
	 * @param securityCode
	 * @return
	 * @throws QueueNotFoundException 
	 */
	public LineUpQueue getQueue(String name, String securityCode) throws QueueNotFoundException;
	
}
