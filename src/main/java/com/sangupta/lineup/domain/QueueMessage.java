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

package com.sangupta.lineup.domain;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.sangupta.jerry.ds.Prioritizable;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.HashUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A simple value object that holds an incoming message and its various properties
 * like a unique identifier which is assigned to it, the MD5 hash of the message,
 * the time when it was received and more.
 * 
 * @author sangupta
 * @since 0.1.0
 */
@XStreamAlias("queueMessage")
public class QueueMessage implements Comparable<QueueMessage>, Prioritizable {
	
	/**
	 * Auto incrementing message ID that allows us to always use a unique message ID
	 */
	private static final AtomicLong AUTO_INCREMENT_MESSAGE_ID = new AtomicLong(1);
	
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
	 * The priority of this message, in case it being added to a queue
	 * which support priority
	 */
	private final AtomicInteger priority;
	
	/**
	 * Create a new queue message. This is the only method that is available to
	 * clients for constructing a new queue message.
	 * 
	 * @param body
	 *            the body of the message
	 * 
	 * @param delaySeconds
	 *            the delay in seconds before the message is added to the queues
	 * 
	 */
	public QueueMessage(String body, int delaySeconds, int priority) {
		if(AssertUtils.isEmpty(body)) {
			throw new IllegalArgumentException("Message body cannot be null or empty.");
		}
		
		if(delaySeconds < 0) {
			throw new IllegalArgumentException("Delay seconds cannot be less than zero.");
		}
		
		this.messageID = AUTO_INCREMENT_MESSAGE_ID.incrementAndGet();
		this.body = body;
		this.delaySeconds = delaySeconds;
		this.md5 = HashUtils.getMD5Hex(body);
		
		this.created = System.currentTimeMillis();
		this.priority = new AtomicInteger(priority);
	}
	
	/**
	 * Increment the priority of this message by one. This method only increases
	 * the priority within this object, moving it to the top of the queue is the
	 * responsibility of the callee.
	 * 
	 */
	public int incrementPriority() {
		return this.priority.incrementAndGet();
	}
	
	/**
	 * Increment the priority of this message by given amount. This method only
	 * increases the priority within this object, moving it to the top of the
	 * queue is the responsibility of the callee.
	 * 
	 * @param additive
	 * @return
	 */
	public int incrementPriority(int additive) {
		return this.priority.addAndGet(additive);
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
		
		// if we are checking against an id
		if(obj instanceof Long) {
			return this.messageID == (Long) obj;
		}
		
		// if we are checking against a message body
		if(obj instanceof String) {
			return this.body.equals((String) obj);
		}
		
		// is this a valid message?
		if(!(obj instanceof QueueMessage)) {
			return false;
		}
		
		QueueMessage qm = (QueueMessage) obj;
		if(this.messageID == qm.messageID) {
			return true;
		}
		
		if(!this.md5.equals(qm.md5)) {
			return false;
		}
			
		return this.body.equals(qm.body);
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO: revisit this to see if we need to compute
		// hashCode() based on 
		return ((Long) messageID).hashCode();
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(QueueMessage queueMessage) {
		if(queueMessage == null) {
			return -1;
		}
		
		if(this == queueMessage) {
			return 0;
		}
		
		int myPriority = this.priority.get();
		int hisPriority = queueMessage.priority.get();
		
		if(myPriority == hisPriority) {
			if(this.body.equals(queueMessage.body)) {
				return 0;
			}
			
			return (int) (this.messageID - queueMessage.messageID);
		}
		
		return 0 - (myPriority - hisPriority);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[QueueMessage: " + this.body + "]";
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
