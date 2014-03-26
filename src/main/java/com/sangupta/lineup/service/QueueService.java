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

import java.util.List;

import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.exceptions.QueueAlreadyDeletedException;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.exceptions.QueueNotFoundException;
import com.sangupta.lineup.queues.LineUpQueue;

/**
 * Contract for implementations that need to provide queue services
 * as part of lineup.
 * 
 * @author sangupta
 *
 */
public interface QueueService {

	/**
	 * Create a queue with the given name
	 * 
	 * @param name
	 *            the name to use for the queue
	 * 
	 * @return the newly created queue
	 * 
	 * @throws QueueAlreadyExistsException
	 *             if a queue already exists with that name
	 */
	public LineUpQueue createQueue(String name) throws QueueAlreadyExistsException;
	
	/**
	 * Create a queue with the given name and given security code.
	 * 
	 * @param name
	 *            the name to use for the queue
	 * 
	 * @param securityCode
	 *            the security code to use for the queue
	 * 
	 * @return the newly created queue
	 * 
	 * @throws QueueAlreadyExistsException
	 *             if a queue already exists with that name
	 * 
	 */
	public LineUpQueue createQueue(String name, String securityCode) throws QueueAlreadyExistsException;
	
	/**
	 * Create a queue with the given name. Return the queue url that needs to be
	 * used when accessing it.
	 * 
	 * @param name
	 *            the name to use for the queue
	 * 
	 * @param options
	 *            the options to use when creating the queue
	 * 
	 * @return the newly created queue
	 * 
	 * @throws QueueAlreadyExistsException
	 *             if a queue already exists with that name
	 * 
	 */
	public LineUpQueue createQueue(String name, QueueOptions options) throws QueueAlreadyExistsException;
	
	/**
	 * Create a queue with the given name. Return the queue url that needs to be
	 * used when accessing it.
	 * 
	 * @param name
	 *            the name to use for the queue
	 * 
	 * @param securityCode
	 *            the security code to use for the queue
	 * 
	 * @param options
	 *            the options to use when creating the queue
	 * 
	 * @return the newly created queue
	 * 
	 * @throws QueueAlreadyExistsException
	 *             if a queue already exists with that name
	 * 
	 */
	public LineUpQueue createQueue(String name, String securityCode, QueueOptions options) throws QueueAlreadyExistsException;

	/**
	 * Delete a queue from the memory.
	 * 
	 * @param name
	 *            the name of the queue to be deleted
	 * 
	 * @return <code>true</code> if we could successfully delete the queue,
	 *         <code>false</code> otherwise
	 * 
	 * @throws QueueAlreadyDeletedException
	 *             if the queue was very recently deleted
	 * 
	 */
	public boolean deleteQueue(String name) throws QueueAlreadyDeletedException;

	/**
	 * Get the queue URL for the given name.
	 * 
	 * @param name
	 *            the name of the queue
	 * 
	 * @return the URL to use when connecting to this queue remotely
	 * 
	 * @throws QueueNotFoundException
	 *             if no queue was found with the given name
	 */
	public String getQueueUrl(String name) throws QueueNotFoundException;
	
	/**
	 * Return a list of all queue's that match the specified prefix. If not
	 * prefix is given then return a list of all queues.
	 * 
	 * @param prefix
	 *            the prefix to test startWith with
	 * 
	 * @return a list of all queue names as found.
	 * 
	 */
	public List<String> getAllQueues(String prefix);

	/**
	 * Get a previously created queue with the given name and security code
	 * 
	 * @param name
	 *            the name of the queue we are looking for
	 * 
	 * @param securityCode
	 *            the security code associated with the queue
	 * 
	 * @return the queue instance if found
	 * 
	 * @throws QueueNotFoundException
	 *             if no such queue was found
	 */
	public LineUpQueue getQueue(String name, String securityCode) throws QueueNotFoundException;

}
