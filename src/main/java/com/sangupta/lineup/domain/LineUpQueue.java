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

import java.util.UUID;

import com.sangupta.lineup.queues.InternalQueue;

/**
 * @author sangupta
 *
 */
public class LineUpQueue {
	
	private final String name;
	
	private final String securityCode;
	
	private final QueueOptions options;
	
	private final InternalQueue internalQueue;
	
	public LineUpQueue(String name, QueueOptions options, InternalQueue internalQueue) {
		this.name = name;
		this.options = options;
		this.internalQueue = internalQueue;
		
		// initialize other params
		this.securityCode = UUID.randomUUID().toString();
	}
	
	// Usual accessors follow

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the options
	 */
	public QueueOptions getOptions() {
		return options;
	}

	/**
	 * @return the internalQueue
	 */
	public InternalQueue getInternalQueue() {
		return internalQueue;
	}

	/**
	 * @return the securityCode
	 */
	public String getSecurityCode() {
		return securityCode;
	}

}
