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

import com.sangupta.jerry.util.AssertUtils;

/**
 * @author sangupta
 *
 */
public enum QueueType {
	
	AllowDuplicates,
	
	RejectDuplicates,
	
	PriorityQueue;

	/**
	 * @param queueType
	 * @return
	 */
	public static QueueType fromString(String queueType) {
		if(AssertUtils.isEmpty(queueType)) {
			return null;
		}
		
		queueType = queueType.toLowerCase();
		
		if(AllowDuplicates.toString().toLowerCase().equals(queueType)) {
			return AllowDuplicates;
		}
		
		if(RejectDuplicates.toString().toLowerCase().equals(queueType)) {
			return RejectDuplicates;
		}
		if(PriorityQueue.toString().toLowerCase().equals(queueType)) {
			return PriorityQueue;
		}
		return null;
	}

}
