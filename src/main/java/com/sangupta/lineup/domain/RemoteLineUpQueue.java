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

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.UriUtils;
import com.sangupta.jerry.util.XStreamUtils;

/**
 * @author sangupta
 *
 */
public class RemoteLineUpQueue implements LineUpQueue {
	
	/**
	 * The URL of the remote queue.
	 * 
	 */
	private final String remoteQueue;
	
	/**
	 * Create a {@link LineUpQueue} that connects to the given remote queue.
	 * 
	 * @param queueURL
	 */
	public RemoteLineUpQueue(String queueURL) {
		if(AssertUtils.isEmpty(queueURL)) {
			throw new IllegalArgumentException("Remote server URL cannot be null/empty");
		}
		
		this.remoteQueue = queueURL;
	}
	
	/**
	 * Create a {@link LineUpQueue} that connects to the given remote server, and connects
	 * to an already existing queue.
	 * 
	 * @param lineUpServer
	 * @param queueName
	 */
	public RemoteLineUpQueue(String lineUpServer, String queueName) {
		final String endPoint = UriUtils.addWebPaths(lineUpServer, "queue/" + queueName);
		
		WebResponse response = WebInvoker.invokeUrl(endPoint, WebRequestMethod.PUT);
		if(response == null) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}
		
		if(!response.isSuccess()) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}

		DefaultLineUpQueue queue = (DefaultLineUpQueue) XStreamUtils.getXStream(DefaultLineUpQueue.class).fromXML(response.asStream());
		if(queue == null) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}
		
		this.remoteQueue = UriUtils.addWebPaths(lineUpServer, "messages/" + queue.getSecurityCode() + "/" + queueName);
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
		final String xml = XStreamUtils.getXStream(QueueOptions.class).toXML(queueOptions);
		WebResponse response = WebInvoker.invokeUrl(lineUpServer, WebRequestMethod.PUT, MediaType.TEXT_XML, xml);
		if(response == null) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}

		DefaultLineUpQueue queue = (DefaultLineUpQueue) XStreamUtils.getXStream(DefaultLineUpQueue.class).fromXML(response.asStream());
		if(queue == null) {
			throw new RuntimeException("Unable to connect to create a new remote queue.");
		}
		
		this.remoteQueue = UriUtils.addWebPaths(lineUpServer, "messages/" + queue.getSecurityCode() + "/" + queueName);
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
		this.remoteQueue = UriUtils.addWebPaths(lineUpServer, "messages/" + securityCode + "/" + queueName);
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
		
		return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#addMessage(java.lang.String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		WebResponse response = WebInvoker.postXML(this.remoteQueue, new QueueMessage(message, delaySeconds, 1));
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
		WebResponse response = WebInvoker.getResponse(this.remoteQueue);
		if(response == null) {
			return null;
		}
		
		if(response.getResponseCode() == 200) {
			return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
		}
		
		return null;
	}

	/**
	 * @see com.sangupta.lineup.domain.LineUpQueue#getMessage(int)
	 */
	@Override
	public QueueMessage getMessage(int longPollTime) {
		WebResponse response = WebInvoker.getResponse(this.remoteQueue + "?pollTime=" + longPollTime);
		if(response == null) {
			return null;
		}
		
		return (QueueMessage) XStreamUtils.getXStream(QueueMessage.class).fromXML(response.asStream());
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
		QueueMessage queueMessage = QueueMessage.createMessage(messageID);
		String xml = XStreamUtils.getXStream(QueueMessage.class).toXML(queueMessage);
		WebResponse response = WebInvoker.invokeUrl(this.remoteQueue, WebRequestMethod.DELETE, MediaType.TEXT_XML, xml);
		if(response == null) {
			return false;
		}
		
		if(response.isSuccess()) {
			return true;
		}
		
		return false;
	}

}
