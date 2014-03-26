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

package com.sangupta.lineup.queues;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.DateUtils;
import com.sangupta.jerry.util.UriUtils;
import com.sangupta.jerry.util.XStreamUtils;
import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.domain.QueueType;

/**
 * @author sangupta
 *
 */
public class RemoteLineUpQueue extends AbstractLineUpQueue {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteLineUpQueue.class);
	
	private static final long DEFAULT_POLL_TIME = DateUtils.ONE_SECOND;
	
	/**
	 * The URL of the remote queue.
	 * 
	 */
	private final String remoteQueue;
	
	/**
	 * The default poll time to use for this queue
	 */
	private final long messagePollTime;
	
	// Various constructors follow
	
	/**
	 * Create a {@link LineUpQueue} that connects to the given remote queue.
	 * 
	 * @param queueURL
	 */
	public RemoteLineUpQueue(String queueURL) {
		super("RemoteQueue: "+ queueURL, null, null);
		
		if(AssertUtils.isEmpty(queueURL)) {
			throw new IllegalArgumentException("Remote server URL cannot be null/empty");
		}
		
		this.remoteQueue = queueURL;
		this.messagePollTime = DEFAULT_POLL_TIME;
	}
	
	/**
	 * Create a {@link LineUpQueue} that connects to the given remote server, and connects
	 * to an already existing queue.
	 * 
	 * @param lineUpServer
	 * @param queueName
	 */
	public RemoteLineUpQueue(String lineUpServer, String queueName) {
		this(lineUpServer, queueName, QueueOptions.getOptions(QueueType.AllowDuplicates));
	}
	
	/**
	 * Create a {@link LineUpQueue} that connects to the given remote server, and creates
	 * a new queue with the given name and options. 
	 * 
	 * @param lineUpServer
	 * @param queueName
	 * @param queueOptions
	 */
	public RemoteLineUpQueue(String lineUpServer, String queueName, QueueOptions queueOptions) {
		if(queueOptions == null) {
			throw new IllegalArgumentException("Queue options cannot be null");
		}
		
		final String xml = XStreamUtils.getXStream(QueueOptions.class).toXML(queueOptions);
		final String endPoint = UriUtils.addWebPaths(lineUpServer, "queue/" + queueName + "?queueType=" + queueOptions.getQueueType());
		
		WebResponse response = WebInvoker.invokeUrl(endPoint, WebRequestMethod.PUT, MediaType.TEXT_XML, xml);
		if(response == null) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}

		if(response.getResponseCode() != 200) {
			throw new RuntimeException("Unable to connect to create a new remote queue."); 
		}
		
		LineUpQueue queue = (LineUpQueue) XStreamUtils.getXStream(LineUpQueue.class).fromXML(response.asStream());
		if(queue == null) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}
		
		this.remoteQueue = UriUtils.addWebPaths(lineUpServer, "messages/" + queue.getSecurityCode() + "/" + queueName);
		this.messagePollTime = DEFAULT_POLL_TIME;
	}
	
	/**
	 * Create a {@link LineUpQueue} that connects to the given remote server, and connects
	 * to an already existing queue with the given name and security code.
	 * 
	 * @param lineUpServer
	 * @param queueName
	 * @param securityCode
	 */
	public RemoteLineUpQueue(String lineUpServer, String queueName, String securityCode) {
		this(lineUpServer, queueName, securityCode, DEFAULT_POLL_TIME);
	}
	
	public RemoteLineUpQueue(String lineUpServer, String queueName, String securityCode, long defaultPollTime) {
		this.remoteQueue = UriUtils.addWebPaths(lineUpServer, "messages/" + securityCode + "/" + queueName);
		this.messagePollTime = defaultPollTime;
	}
	
	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#addMessage(java.lang.String)
	 */
	@Override
	public QueueMessage addMessage(String message) {
		WebResponse response = WebInvoker.postXML(this.remoteQueue, new QueueMessage(message, 0, 1));
		if(response == null) {
			return null;
		}
		
		if(!response.isSuccess()) {
			LOGGER.error("Error posting message to remote queue for reason: ", response);
			return null;
		}
		
		return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
	}
	
	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#addMessage(java.lang.String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		return addMessage(message, delaySeconds, 1);
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#addMessage(java.lang.String, int, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds, int priority) {
		WebResponse response = WebInvoker.postXML(this.remoteQueue, new QueueMessage(message, delaySeconds, priority));
		if(response == null) {
			return null;
		}
		
		return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#addMessage(com.sangupta.lineup.domain.QueueMessage)
	 */
	@Override
	public QueueMessage addMessage(QueueMessage queueMessage) {
		WebResponse response = WebInvoker.postXML(this.remoteQueue, queueMessage);
		if(response == null) {
			return null;
		}
		
		return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#getMessage()
	 */
	@Override
	public QueueMessage getMessage() {
		return getMessage(this.messagePollTime);
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#getMessage(int)
	 */
	@Override
	public QueueMessage getMessage(long longPollTime) {
		WebResponse response = WebInvoker.getResponse(this.remoteQueue + "?pollTime=" + longPollTime);
		if(response == null) {
			return null;
		}
		
		if(response.getResponseCode() == 200) {
			return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
		}
		
		// consume the entity so that the connection can be closed
		response.close();
		
		// return null
		return null;
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#getMessages(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<QueueMessage> getMessages(int numMessages) {
		WebResponse response = WebInvoker.getResponse(this.remoteQueue + "?numMessages=" + numMessages);
		if(response == null) {
			return null;
		}
		
		return (List<QueueMessage>) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#deleteMessage(java.lang.String)
	 */
	@Override
	public boolean deleteMessage(String messageID) {
		String xml = XStreamUtils.getXStream(QueueMessage.class).toXML(messageID);
		WebResponse response = WebInvoker.invokeUrl(this.remoteQueue, WebRequestMethod.DELETE, MediaType.TEXT_XML, xml);
		if(response == null) {
			return false;
		}
		
		if(response.isSuccess()) {
			return true;
		}
		
		return false;
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#numMessages()
	 */
	@Override
	public int numMessages() {
		throw new RuntimeException("Not yet implemented");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.remoteQueue;
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see com.sangupta.lineup.queues.AbstractLineUpQueue#removeMessageID(long)
	 */
	@Override
	public boolean removeMessageID(long id) {
		return false;
	}
	
}
