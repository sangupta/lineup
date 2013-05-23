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

import java.util.concurrent.atomic.AtomicInteger;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.CryptoUtil;
import com.sangupta.lineup.queues.PriorityLineUpQueue;

/**
 * @author sangupta
 *
 */
public class QueueMessage implements Comparable<QueueMessage> {
	
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
	private final transient String md5;
	
	/**
	 * Time at which the message was created
	 */
	private final transient long created;
	
	/**
	 * The priority of this message, in case it being added to the {@link PriorityLineUpQueue}.
	 */
	private final AtomicInteger priority;
	
	/**
	 * Default internal constructor
	 * 
	 */
	private QueueMessage(long messageID, String body, int delaySeconds) {
		this.messageID = messageID;
		this.body = body;
		this.delaySeconds = delaySeconds;
		
		if(body != null) {
			this.md5 = CryptoUtil.getMD5Hex(body);
		} else {
			this.md5 = null;
		}
		
		this.created = System.currentTimeMillis();
		this.priority = new AtomicInteger(0);
	}
	
	/**
	 * Increment the priority of this message by one.
	 * 
	 */
	public int incrementPriority() {
		return this.priority.incrementAndGet();
	}
	
	/**
	 * Create a dummy queue message.
	 * 
	 * @param messageID
	 */
	public static QueueMessage createMessage(long messageID) {
		if(messageID <= 0) {
			throw new IllegalArgumentException("MessageID cannot be less than or equal to ZERO");
		}
		
		return new QueueMessage(messageID, null, -1); 
	}
	
	/**
	 * Create a dummy queue message that can be used to compare
	 * the message bodies.
	 * 
	 * @param body
	 */
	public static QueueMessage createMessage(String body) {
		if(AssertUtils.isEmpty(body)) {
			throw new IllegalArgumentException("Message body cannot be null or empty.");
		}

		return new QueueMessage(-1, null, -1);
	}

	/**
	 * Create a new queue message.
	 * 
	 * @param messageID
	 * @param body
	 * @param delaySeconds
	 */
	public QueueMessage(long messageID, String body, int delaySeconds, int priority) {
		if(messageID <= 0) {
			throw new IllegalArgumentException("MessageID cannot be less than or equal to ZERO.");
		}
		
		if(AssertUtils.isEmpty(body)) {
			throw new IllegalArgumentException("Message body cannot be null or empty.");
		}
		
		if(delaySeconds < 0) {
			throw new IllegalArgumentException("Delay seconds cannot be less than zero.");
		}
		
		this.messageID = messageID;
		this.body = body;
		this.delaySeconds = delaySeconds;
		this.md5 = CryptoUtil.getMD5Hex(body);
		
		this.created = System.currentTimeMillis();
		this.priority = new AtomicInteger(priority);
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof QueueMessage)) {
			return false;
		}
		
		QueueMessage qm = (QueueMessage) obj;
		if(this.messageID != -1 && qm.messageID != -1) {
			return this.messageID == qm.messageID;
		}
		
		if(this.md5 != null && qm.md5 != null) {
			if(this.md5.equals(qm.md5)) {
				return false;
			}
			
			return this.body.equals(qm.body);
		}
		
		return false;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) this.messageID;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(QueueMessage queueMessage) {
		if(queueMessage == null) {
			return -1;
		}
		
		return 0 - (this.priority.get() - queueMessage.priority.get());
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

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return this.priority.get();
	}

}
