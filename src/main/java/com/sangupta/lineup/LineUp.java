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

import java.io.IOException;

import com.sangupta.jerry.util.ConsoleUtils;
import com.sangupta.lineup.domain.DefaultLineUpQueue;
import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.domain.QueueOptions;
import com.sangupta.lineup.domain.QueueType;
import com.sangupta.lineup.domain.RemoteLineUpQueue;
import com.sangupta.lineup.exceptions.QueueAlreadyDeletedException;
import com.sangupta.lineup.exceptions.QueueAlreadyExistsException;
import com.sangupta.lineup.exceptions.QueueNotFoundException;
import com.sangupta.lineup.server.LineUpServer;
import com.sangupta.lineup.service.QueueService;
import com.sangupta.lineup.service.impl.DefaultQueueService;

/**
 * Utility class to help use <strong>LineUp</strong> framework
 * in the various modes.
 * 
 * @author sangupta
 *
 */
public class LineUp {
	
	/**
	 * Our own singleton instance
	 */
	private static final QueueService QUEUE_SERVICE = new DefaultQueueService();

	/**
	 * The base server URL
	 */
    public static final String BASE_SERVER_URL = "http://localhost:21000/";
    
    /**
     * Reference to instance that may be currently running
     */
    private static LineUpServer lineUpServer = null;

    /**
     * Main function that starts the {@link LineUpServer}.
     * 
     * @param args the arguments from the command line.
     * 
     * @throws IOException
     *  
     * @throws IllegalArgumentException 
     */
    public static void main(String[] args) throws IllegalArgumentException, IOException {
		if(lineUpServer != null) {
			if(lineUpServer.isRunning()) {
				return;
			}
			
			lineUpServer.startServer();
			return;
		}
		
		lineUpServer = new LineUpServer(BASE_SERVER_URL);
		lineUpServer.startServer();
		
		System.out.println("LineUp server is now ready to accept connections.");
		
		do {
			String command = ConsoleUtils.readLine("Type 'exit' to stop the server: ", false);
			if("exit".equalsIgnoreCase(command)) {
				lineUpServer.stopServer();
				break;
			}
		} while(true);
		
		System.out.println("LineUp server has now stopped.");
	}
    
    /**
     * Return the default instance of the queue service.
     * 
     * @return
     */
    public static QueueService getDefaultQueueService() {
    	return QUEUE_SERVICE;
    }
    
	/**
	 * Get a new queue with default options or retrieve an existing queue
	 * with whatever options that were provided.
	 * 
	 * @param name
	 * @return
	 * @throws QueueAlreadyExistsException 
	 */
	public static DefaultLineUpQueue createMessageQueue(String name) throws QueueAlreadyExistsException {
		return QUEUE_SERVICE.createQueue(name);
	}

	/**
	 * Get a new queue with default options with the given security code.
	 * 
	 * @param name
	 * @param securityCode
	 * @return
	 * @throws QueueAlreadyExistsException
	 */
	public static DefaultLineUpQueue createMessageQueue(String name, String securityCode) throws QueueAlreadyExistsException {
		return QUEUE_SERVICE.createQueue(name, securityCode);
	}
	
	/**
	 * Get a new queue with default options but of the given type.
	 * 
	 * @param name
	 * @param queueType
	 * @return
	 * @throws QueueAlreadyExistsException
	 */
	public static DefaultLineUpQueue createMessageQueue(String name, QueueType queueType) throws QueueAlreadyExistsException {
		return QUEUE_SERVICE.createQueue(name, QueueOptions.getOptions(queueType));
	}
	
	/**
	 * Retrieve a queue with the given name and security code. The queue MUST be pre-created.
	 * 
	 * @param name
	 * @param securityCode
	 * @return
	 * @throws QueueNotFoundException
	 */
	public static DefaultLineUpQueue getQueue(String name, String securityCode) throws QueueNotFoundException {
		return QUEUE_SERVICE.getQueue(name, securityCode);
	}
	
	/**
	 * Delete a queue with the given name.
	 * 
	 * @param name
	 * @return
	 * @throws QueueAlreadyDeletedException 
	 */
	public static boolean deleteMessageQueue(String name) throws QueueAlreadyDeletedException {
		return QUEUE_SERVICE.deleteQueue(name);
	}
	
	/**
	 * Create a remote queue for the queue URL.
	 * 
	 * @param queueURL
	 * @return
	 */
	public static LineUpQueue createRemoteQueue(final String queueURL) {
		return new RemoteLineUpQueue(queueURL);
	}
	
	/**
	 * Create a remote queue at the given server address and queue name.
	 * 
	 * @param lineUpServer
	 * @param queueName
	 * @return
	 */
	public static LineUpQueue createRemoteQueue(final String lineUpServer, final String queueName) {
		return new RemoteLineUpQueue(lineUpServer, queueName);
	}
	
	/**
	 * Create a remote queue at the given server address and queue name.
	 * 
	 * @param lineUpServer
	 * @param queueName
	 * @param queueType
	 * @return
	 */
	public static LineUpQueue createRemoteQueue(final String lineUpServer, final String queueName, final QueueType queueType) {
		return new RemoteLineUpQueue(lineUpServer, queueName, QueueOptions.getOptions(queueType));
	}
	
	/**
	 * Connect to an existing code at the given server address, and queue name with
	 * the given security code.
	 *  
	 * @param lineUpServer
	 * @param queueName
	 * @param securityCode
	 * @return
	 */
	public static LineUpQueue connectRemote(final String lineUpServer, final String queueName, final String securityCode) {
		return new RemoteLineUpQueue(lineUpServer, queueName, securityCode);
	}
	
	/**
	 * Connect to an existing code at the given server address, and queue name with
	 * the given security code. The given default poll time will be used when polling
	 * the queue for messages.
	 * 
	 * @param lineUpServer
	 * @param queueName
	 * @param securityCode
	 * @param pollTime
	 * @return
	 */
	public static LineUpQueue connectRemote(final String lineUpServer, final String queueName, final String securityCode, long defaultPollTime) {
		return new RemoteLineUpQueue(lineUpServer, queueName, securityCode, defaultPollTime);
	}
}
