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

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.sangupta.jerry.http.HttpStatusCode;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.lineup.domain.DefaultLineUpQueue;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.exceptions.QueueAlreadyDeletedException;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.exceptions.QueueNotFoundException;
import com.sangupta.lineup.service.QueueService;
import com.sangupta.lineup.service.impl.DefaultQueueService;

/**
 * @author sangupta
 *
 */
@Path("/queue/")
public class QueueWebservice {
	
	private QueueService queueService =  new DefaultQueueService();
	
	@GET
	@Path("{queue}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getQueueUrl(@PathParam("queue") String queueName) {
		try {
			return this.queueService.getQueueUrl(queueName);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
	}
	
	@POST
	@Path("{queue}")
	@Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	public DefaultLineUpQueue createPost(@PathParam("queue") String queueName, @DefaultValue("") @QueryParam("queueType") String queueType) {
		return create(queueName, queueType);
	}
	
	@PUT
	@Path("{queue}")
	@Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	public DefaultLineUpQueue create(@PathParam("queue") String queueName, @DefaultValue("") @QueryParam("queueType") String queueType) {
		try {
			if(AssertUtils.isEmpty(queueType)) {
				return this.queueService.createQueue(queueName);
			}
			
			// try and decipher the type
			QueueType type = QueueType.fromString(queueType);
			if(type == null) {
				throw new WebApplicationException(HttpStatusCode.BAD_REQUEST);
			}
			
			return this.queueService.createQueue(queueName, QueueOptions.getOptions(type));
		} catch (QueueAlreadyExistsException e) {
			throw new WebApplicationException(HttpStatusCode.CONFLICT);
		}
	}
	
	@DELETE
	@Path("{queue}")
	@Produces(value = { MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	public boolean delete(@PathParam("queue") String queueName) {
		try {
			return this.queueService.deleteQueue(queueName);
		} catch (QueueAlreadyDeletedException e) {
			throw new WebApplicationException(HttpStatusCode.GONE);
		}
	}
	
	// Usual accessors follow

	/**
	 * @return the queueService
	 */
	public QueueService getQueueService() {
		return this.queueService;
	}

	/**
	 * @param queueService the queueService to set
	 */
	public void setQueueService(QueueService queueService) {
		this.queueService = queueService;
	}

}
