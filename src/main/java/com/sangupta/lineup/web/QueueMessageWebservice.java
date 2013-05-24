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

package com.sangupta.lineup.web;

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

import com.sangupta.jerry.http.HttpStatusCode;
import com.sangupta.lineup.LineUp;
import com.sangupta.lineup.domain.DefaultLineUpQueue;
import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.exceptions.QueueNotFoundException;

/**
 * @author sangupta
 *
 */
public class QueueMessageWebservice {
	
	@GET
	@Path("/queue/{secureCode}/{queue}")
	@Produces(MediaType.APPLICATION_XML)
	public Object getMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, @DefaultValue("1") @QueryParam("numMessages") int numMessages) { 
		try {
			DefaultLineUpQueue queue = LineUp.getQueue(queueName, securityCode);
			
			if(numMessages <= 0) {
				throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
			}
			
			if(numMessages == 1) {
				return queue.getMessage();
			}
			
			return queue.getMessages(numMessages);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
	}
	
	@POST
	@Path("/queue/{secureCode}/{queue}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public QueueMessage postMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, QueueMessage message) {
		if(message == null) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}
		
		try {
			DefaultLineUpQueue queue = LineUp.getQueue(queueName, securityCode);
			return queue.addMessage(message);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
	}
	
	@DELETE
	@Path("/queue/{secureCode}/{queue}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_XML)
	public String deleteMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, QueueMessage message) {
		if(message == null) {
			throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
		}
		
		try {
			DefaultLineUpQueue queue = LineUp.getQueue(queueName, securityCode);
			boolean success = queue.deleteMessage(String.valueOf(message.getMessageID()));
			if(success) {
				return "done";
			}
			
			throw new WebApplicationException(HttpStatusCode.NOT_MODIFIED);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
	}

}
