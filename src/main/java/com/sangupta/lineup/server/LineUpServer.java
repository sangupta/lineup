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

import com.sangupta.jerry.jersey.JerseyGrizzlyServer;
import com.sangupta.jerry.util.AssertUtils;

/**
 * A Grizzly based webserver that can accept REST based incoming
 * connections for operations on message queues.
 * 
 * @author sangupta
 *
 */
public class LineUpServer {

	/**
	 * Default webservices packages to load  
	 */
	private static final String[] DEFAULT_WEBSERVICES_PACKAGES = { "com.sangupta.lineup", "com.sangupta.jerry.jersey" };

	/**
	 * The thread selector obtained from Grizzly container
	 */
    private final JerseyGrizzlyServer server;
    
    /**
     * Create a new {@link LineUpServer} instance with default
     * webservices.
     * 
     * @param serverURL
     */
	public LineUpServer(String serverURL) {
		this(serverURL, null);
	}
	
	/**
	 * Create a new {@link LineUpServer} instance also loading
	 * custom webservices from the package provided. The webservices
	 * must be Jersey-enabled to work properly.
	 * 
	 * @param serverURL
	 * @param customJerseyWebservices
	 */
	public LineUpServer(final String serverURL, final String[] customJerseyWebservices) {
		if(AssertUtils.isEmpty(customJerseyWebservices)) {
			this.server = new JerseyGrizzlyServer(serverURL, DEFAULT_WEBSERVICES_PACKAGES);
			return;
		}
		
		int total = DEFAULT_WEBSERVICES_PACKAGES.length + customJerseyWebservices.length;
		String[] array = new String[total];
		for(int index = 0; index < DEFAULT_WEBSERVICES_PACKAGES.length; index++) {
			array[index] = DEFAULT_WEBSERVICES_PACKAGES[index];
		}
		for(int index = DEFAULT_WEBSERVICES_PACKAGES.length; index < total; index++) {
			array[index] = customJerseyWebservices[index - DEFAULT_WEBSERVICES_PACKAGES.length];
		}
		
		this.server = new JerseyGrizzlyServer(serverURL, array);
	}
	
	public void startServer() throws IOException {
		this.server.startServer();
	}
	
	public void stopServer() {
		this.server.stopServer();
	}
	
	public boolean isRunning() {
		return this.server.isRunning();
	}
}
