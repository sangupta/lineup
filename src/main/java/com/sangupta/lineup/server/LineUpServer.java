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

package com.sangupta.lineup.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

/**
 * @author sangupta
 *
 */
public class LineUpServer {

	private static final Map<String, String> initParams = new HashMap<String, String>();
	
	static {
		initParams.put("com.sun.jersey.config.property.packages", "com.sangupta.lineup com.sangupta.jerry.jersey");
	}
	
	/**
	 * The server URL where we will hook up.
	 */
	private final String serverURL;
	
	/**
	 * Keeps track of whether the server is running or not.
	 * 
	 */
	private volatile boolean started = false;
	
    private SelectorThread threadSelector = null;
    
	public LineUpServer(String serverURL) {
		this.serverURL = serverURL;
	}
	
	/**
	 * Start the server.
	 *  
	 * @throws IOException
	 *  
	 * @throws IllegalArgumentException 
	 * 
	 * @throws IllegalStateException if the server is already running.
	 * 
	 */
	public void startServer() throws IllegalArgumentException, IOException {
		if(this.started) {
			throw new IllegalStateException("Server is already running.");
		}
		
        
		this.threadSelector = GrizzlyWebContainerFactory.create(serverURL, initParams);
		this.started = true;
	}
	
	/**
	 * Stop the currently running server.
	 * 
	 * @throws IllegalStateException if the server is already stopped.
	 * 
	 */
	public void stopServer() {
		if(!this.started) {
			throw new IllegalStateException("Server has not yet started.");
		}
		
		if(this.threadSelector != null) {
			this.threadSelector.stopEndpoint();
		}
		
		this.started = false;
		this.threadSelector = null;
	}
	
	/**
	 * Returns whether the server is running or not.
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.started;
	}

}
