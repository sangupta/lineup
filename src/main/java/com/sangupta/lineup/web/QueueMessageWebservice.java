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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.sangupta.jerry.http.HttpStatusCode;
import com.sangupta.lineup.LineUpService;
import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.exceptions.QueueNotFoundException;

/**
 * @author sangupta
 *
 */
public class QueueMessageWebservice {

	@POST
	@Path("/queue/{secureCode}/{queue}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public void postMessage(@PathParam("secureCode") String securityCode, @PathParam("queue") String queueName, QueueMessage message) {
		LineUpQueue queue = null;
		try {
			queue = LineUpService.getQueue(queueName, securityCode);
		} catch (QueueNotFoundException e) {
			throw new WebApplicationException(HttpStatusCode.NOT_FOUND);
		}
		
		// queue will not be null here
		//return queue.addMessage(message);
	}

}
