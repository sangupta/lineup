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

package com.sangupta.lineup.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.lineup.domain.DefaultLineUpQueue;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.exceptions.QueueNotFoundException;
import com.sangupta.lineup.service.QueueGenerationFactory;
import com.sangupta.lineup.service.QueueService;

/**
 * @author sangupta
 *
 */
public class DefaultQueueService implements QueueService {
	
	/**
	 * Internal map that stores all internal {@link DefaultLineUpQueue} objects.
	 * 
	 */
	private static final ConcurrentHashMap<String, DefaultLineUpQueue> myQueues = new ConcurrentHashMap<String, DefaultLineUpQueue>();
	
	/**
	 * Create a new queue with default options.
	 * 
	 * @see com.sangupta.lineup.service.QueueService#createQueue(java.lang.String)
	 */
	public DefaultLineUpQueue createQueue(String name) throws QueueAlreadyExistsException {
		return createQueue(name, new QueueOptions());
	}

	/**
	 * @throws QueueAlreadyExistsException 
	 * @see com.sangupta.lineup.service.QueueService#createQueue(java.lang.String, com.sangupta.lineup.domain.QueueOptions)
	 */
	@Override
	public DefaultLineUpQueue createQueue(String name, QueueOptions options) throws QueueAlreadyExistsException {
		if(AssertUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Queue name cannot be empty");
		}
		
		if(options == null) {
			throw new IllegalArgumentException("Queue options cannot be null");
		}
		
		if(myQueues.containsKey(name)) {
			throw new QueueAlreadyExistsException();
		}
		
		DefaultLineUpQueue queue = QueueGenerationFactory.getLineUpQueue(name, options);
		DefaultLineUpQueue previous = myQueues.putIfAbsent(name, queue);
		if(previous != null) {
			throw new QueueAlreadyExistsException();
		}
		
		return queue;
	}

	/**
	 * @see com.sangupta.lineup.service.QueueService#deleteQueue(java.lang.String)
	 */
	@Override
	public boolean deleteQueue(String name) {
		if(myQueues.containsKey(name)) {
			myQueues.remove(name);
			return true;
		}
		
		return false;
	}

	/**
	 * @throws QueueNotFoundException 
	 * @see com.sangupta.lineup.service.QueueService#getQueueUrl(java.lang.String)
	 */
	@Override
	public String getQueueUrl(String name) throws QueueNotFoundException {
		if(!myQueues.containsKey(name)) {
			throw new QueueNotFoundException();
		}
		
		return name;
	}

	/**
	 * @see com.sangupta.lineup.service.QueueService#getAllQueues(java.lang.String)
	 */
	@Override
	public List<String> getAllQueues(String prefix) {
		if(myQueues.size() < 0) {
			// nothing found
			return null;
		}
		
		Set<String> queueNames = myQueues.keySet();
		List<String> found = new ArrayList<String>();
		
		for(String name : queueNames) {
			if(name.startsWith(prefix)) {
				found.add(name);
			}
		}
		
		return found;
	}

	/**
	 * @throws QueueNotFoundException 
	 * @see com.sangupta.lineup.service.QueueService#getQueue(java.lang.String)
	 */
	@Override
	public DefaultLineUpQueue getQueue(String name, String securityCode) throws QueueNotFoundException {
		if(!myQueues.containsKey(name)) {
			throw new QueueNotFoundException();
		}
		
		DefaultLineUpQueue queue = myQueues.get(name);
		if(queue.getSecurityCode().equals(securityCode)) {
			return queue;
		}
		
		throw new SecurityException("The security code does not match");
	}

}
