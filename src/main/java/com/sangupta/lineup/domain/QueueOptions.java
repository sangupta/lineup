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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Various options that specify how a queue should behave.
 * 
 * @author sangupta
 * @since 0.1.0
 */
@XStreamAlias("queueOptions")
public class QueueOptions {
	
	public static final int DEFAULT_DELAY_SECONDS = 0;
	
	public static final int DEFAULT_MAX_MESSAGE_SIZE = 65536;
	
	public static final int DEFALT_MESSAGE_RETENTION_PERIOD = 30;
	
	public static final int DEFAULT_RECEIVE_MESSAGE_WAIT_TIMEOUT = 15;
	
	public static final int DEFAULT_VISIBILITY_TIMEOUT = 30;
	
	public static final int DEFAULT_MAX_MERGED_PRIORITY = 50;
	
	private final int delaySeconds;
	
	private final int maximumMessageSize;
	
	private final int messageRetentionPeriod;
	
	private final int receiveMessageWaitTimeSeconds;
	
	private final int visibilityTimeout;
	
	private final int maxMergedPriority;
	
	private final QueueType queueType;
	
	/**
	 * Default constructor with default parameters.
	 * 
	 */
	public QueueOptions() {
		this(DEFAULT_DELAY_SECONDS, DEFAULT_MAX_MESSAGE_SIZE, DEFALT_MESSAGE_RETENTION_PERIOD, DEFAULT_RECEIVE_MESSAGE_WAIT_TIMEOUT, DEFAULT_VISIBILITY_TIMEOUT, DEFAULT_MAX_MERGED_PRIORITY, QueueType.AllowDuplicates);
	}
	
	/**
	 * Constructor that allows setting all values.
	 * 
	 * @param delaySeconds
	 *            the delay in seconds
	 * 
	 * @param maximumMessageSize
	 *            the max size of message
	 * 
	 * @param messageRetentionPeriod
	 *            the time for which to retain the message
	 * 
	 * @param receiveMessageWaitTimeSeconds
	 *            the message recieve wait time
	 * 
	 * @param visibilityTimeout
	 *            the visibility timeout of message
	 * 
	 * @param maxMergedPriority
	 * 			  the maximum merged priority a message can have
	 * 
	 * @param queueType
	 * 			  the type of the queue
	 */
	public QueueOptions(int delaySeconds, int maximumMessageSize, int messageRetentionPeriod, int receiveMessageWaitTimeSeconds, int visibilityTimeout, int maxMergedPriority, QueueType queueType) {
		this.delaySeconds = delaySeconds;
		this.maximumMessageSize = maximumMessageSize;
		this.messageRetentionPeriod = messageRetentionPeriod;
		this.receiveMessageWaitTimeSeconds = receiveMessageWaitTimeSeconds;
		this.visibilityTimeout = visibilityTimeout;
		this.maxMergedPriority = maxMergedPriority;
		this.queueType = queueType;
	}
	
	// static methods follow
	
	/**
	 * Create default queue options.
	 * 
	 * @return the {@link QueueOptions} instance set with default values
	 */
	public static QueueOptions getDefaultOptions() {
		return new QueueOptions();
	}
	
	/**
	 * Create default queue options for the given queue type.
	 * 
	 * @param queueType
	 *            the type of queue to use
	 * 
	 * @return the default {@link QueueOptions} but for the given
	 *         {@link QueueType}
	 */
	public static QueueOptions getOptions(QueueType queueType) {
		return new QueueOptions(DEFAULT_DELAY_SECONDS, DEFAULT_MAX_MESSAGE_SIZE, DEFALT_MESSAGE_RETENTION_PERIOD, DEFAULT_RECEIVE_MESSAGE_WAIT_TIMEOUT, DEFAULT_VISIBILITY_TIMEOUT, DEFAULT_MAX_MERGED_PRIORITY, queueType);
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

	/**
	 * @return the maxMergedPriority
	 */
	public int getMaxMergedPriority() {
		return maxMergedPriority;
	}

}
