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

package com.sangupta.lineup.web;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.constants.HttpStatusCode;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.lineup.LineUp;
import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.exceptions.QueueNotFoundException;
import com.sangupta.lineup.queues.LineUpQueue;

/**
 * @author sangupta
 *
 */
@Path("/messages/")
public class QueueMessageWebservice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueueMessageWebservice.class);
	
	@GET
	@Path("available")
	@Produces(MediaType.TEXT_PLAIN)
	public String available() {
		return "Yes";
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Path("{secureCode}/{queue}")
	@Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Object getMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, 
			@DefaultValue("1") @QueryParam("numMessages") int numMessages, @DefaultValue("0") @QueryParam("pollTime") long pollTime) {
		
		LineUpQueue queue;
		try {
			queue = LineUp.getQueue(queueName, securityCode);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}
		
		if(queue == null) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}

		if(numMessages <= 0) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}
		
		long start = System.currentTimeMillis();
		Object messages = getMessageFromQueue(queue, numMessages, pollTime);
		long end = System.currentTimeMillis();
		
		if(messages == null) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
		
		if(messages instanceof QueueMessage) {
			LOGGER.debug("Read message {} in {} ms.", ((QueueMessage) messages).getBody(), end - start);
		} else if(messages instanceof List){
			LOGGER.debug("Read (multiple) {} messages in {} ms.", ((List<QueueMessage>) messages).size(), end - start);
		} else {
			LOGGER.debug("Unknown type of message {} read in {} ms.", messages, end - start);
		}
		
		return messages;
	}
	
	/**
	 * Read from the queue the given number of messages in given poll time
	 * 
	 * @param queue
	 *            the queue from where to read
	 * 
	 * @param numMessages
	 *            the number of messages to read
	 * 
	 * @param pollTime
	 *            the time in which to return the results
	 * 
	 * @return the message or messages thus read, <code>null</code> if the queue
	 *         was empty
	 */
	private Object getMessageFromQueue(LineUpQueue queue, int numMessages, long pollTime) {
		if(numMessages == 1) {
			if(pollTime == 0) {
				return queue.getMessage();
			}
			
			try {
				return queue.getMessage(pollTime);
			} catch (InterruptedException e) {
				return null;
			}
		}
			
		return queue.getMessages(numMessages);
	}
	
	@POST
	@Path("{secureCode}/{queue}")
	@Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public QueueMessage postMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, QueueMessage message) {
		if(message == null) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}
		
		try {
			LineUpQueue queue = LineUp.getQueue(queueName, securityCode);
			return queue.addMessage(message);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
	}
	
	@DELETE
	@Path("{secureCode}/{queue}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String deleteMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, String messageID) {
		if(AssertUtils.isEmpty(messageID)) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}
		
		try {
			LineUpQueue queue = LineUp.getQueue(queueName, securityCode);
			boolean success = queue.deleteMessage(messageID);
			if(success) {
				return "done";
			}
			
			throw new WebApplicationException(HttpStatusCode.NOT_MODIFIED);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
	}

}
