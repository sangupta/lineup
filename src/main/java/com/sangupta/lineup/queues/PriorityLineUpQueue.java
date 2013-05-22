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

import java.util.List;

import com.sangupta.lineup.domain.QueueMessage;

/**
 * @author sangupta
 *
 */
public class PriorityLineUpQueue implements InternalQueue {

	/**
	 * @see com.sangupta.lineup.service.InternalQueue#addMessage(java.lang.String)
	 */
	@Override
	public QueueMessage addMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.sangupta.lineup.service.InternalQueue#addMessage(java.lang.String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.sangupta.lineup.service.InternalQueue#getMessage(int)
	 */
	@Override
	public QueueMessage getMessage(int longPollTime) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.sangupta.lineup.service.InternalQueue#getMessages(int)
	 */
	@Override
	public List<QueueMessage> getMessages(int numMessages) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.sangupta.lineup.service.InternalQueue#deleteMessage(java.lang.String)
	 */
	@Override
	public QueueMessage deleteMessage(String messageID) {
		// TODO Auto-generated method stub
		return null;
	}

}
