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

/**
 * @author sangupta
 *
 */
public class QueueOptions {
	
	private final int delaySeconds;
	
	private final int maximumMessageSize;
	
	private final int messageRetentionPeriod;
	
	private final int receiveMessageWaitTimeSeconds;
	
	private final int visibilityTimeout;
	
	private final QueueType queueType;
	
	/**
	 * Default constructor with default parameters.
	 * 
	 */
	public QueueOptions() {
		this(0, 65536, 30, 15, 30, QueueType.AllowDuplicates);
	}
	
	/**
	 * Constructor that allows setting all values.
	 * 
	 * @param delaySeconds
	 * @param maximumMessageSize
	 * @param messageRetentionPeriod
	 * @param receiveMessageWaitTimeSeconds
	 * @param visibilityTimeout
	 */
	public QueueOptions(int delaySeconds, int maximumMessageSize, int messageRetentionPeriod, int receiveMessageWaitTimeSeconds, int visibilityTimeout, QueueType queueType) {
		this.delaySeconds = delaySeconds;
		this.maximumMessageSize = maximumMessageSize;
		this.messageRetentionPeriod = messageRetentionPeriod;
		this.receiveMessageWaitTimeSeconds = receiveMessageWaitTimeSeconds;
		this.visibilityTimeout = visibilityTimeout;
		this.queueType = queueType;
	}
	
	// Usual accessors follow

	/**
	 * @return the delaySeconds
	 */
	public int getDelaySeconds() {
		return delaySeconds;
	}

	/**
	 * @return the maximumMessageSize
	 */
	public int getMaximumMessageSize() {
		return maximumMessageSize;
	}

	/**
	 * @return the messageRetentionPeriod
	 */
	public int getMessageRetentionPeriod() {
		return messageRetentionPeriod;
	}

	/**
	 * @return the receiveMessageWaitTimeSeconds
	 */
	public int getReceiveMessageWaitTimeSeconds() {
		return receiveMessageWaitTimeSeconds;
	}

	/**
	 * @return the visibilityTimeout
	 */
	public int getVisibilityTimeout() {
		return visibilityTimeout;
	}

	/**
	 * @return the queueType
	 */
	public QueueType getQueueType() {
		return queueType;
	}

}
