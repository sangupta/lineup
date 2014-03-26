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

package com.sangupta.lineup;

import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.queues.LineUpQueue;
import com.sangupta.lineup.server.LineUpServer;

/**
 * @author sangupta
 *
 */
public class TestPriorityQueueWebservices {

	public static void main(String[] args) throws Exception {
		final String server = LineUp.BASE_SERVER_URL;
		final String queueName = "sangupta";
		
		// start the server
		LineUpServer lineUpServer = new LineUpServer(LineUp.BASE_SERVER_URL);
		
		try {
			lineUpServer.startServer();
			
			// create a new remote queue
			LineUpQueue queue = LineUp.createRemoteQueue(server, queueName, QueueType.PriorityQueue);
			
			// add 3 messages
			queue.addMessage("one");
			queue.addMessage("two");
			queue.addMessage("two");
			queue.addMessage("two");
			queue.addMessage("one");
			queue.addMessage("one");
			queue.addMessage("one");
			queue.addMessage("three");
			
			do {
				QueueMessage qm = queue.getMessage();
				if(qm == null) {
					break;
				}
				
				System.out.println(qm.getBody());
			} while(true);
		} catch(Throwable t) {
			t.printStackTrace();
		} finally {
			lineUpServer.stopServer();
		}
	}

}
