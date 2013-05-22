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

import com.sangupta.jerry.util.CryptoUtil;

/**
 * @author sangupta
 *
 */
public class QueueMessage {
	
	/**
	 * Unique message ID for this message
	 */
	private final long messageID;
	
	/**
	 * Actual contents of the message
	 */
	private final String body;
	
	/**
	 * Delay in seconds before the element is served out
	 */
	private final int delaySeconds;
	
	/**
	 * MD5 hash of the contents
	 */
	private final String md5;
	
	/**
	 * Time at which the message was created
	 */
	private final long created;

	public QueueMessage(long messageID, String body, int delaySeconds) {
		this.messageID = messageID;
		this.body = body;
		this.delaySeconds = delaySeconds;
		this.md5 = CryptoUtil.getMD5Hex(body);
		
		this.created = System.currentTimeMillis();
	}
	
	// Usual accessors follow

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return the delaySeconds
	 */
	public int getDelaySeconds() {
		return delaySeconds;
	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @return the created
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @return the messageID
	 */
	public long getMessageID() {
		return messageID;
	}
}
