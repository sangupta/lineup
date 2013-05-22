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
import java.util.HashMap;
import java.util.Map;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

/**
 * 
 * Command-line options that are supported:
 * 
 * port: the port on which LineUp will run.
 * 
 * securedQueues: whether to perform security check when retrieving queue
 * 
 * @author sangupta
 *
 */
public class LineUp {
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
    public static void main( String[] args ) throws IOException {
        final String baseURI = "http://localhost:9998/";
        
        final Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.property.packages", "com.sangupta.lineup");
        
        System.out.println("Starting LineUp... ");
        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(baseURI, initParams);
        System.out.println("done!");
        System.out.println("LineUp is now readily accepting connections.");
        // wait for the user to close down the server
        System.in.read();
        
        System.out.print("Stopping LineUp...");
        threadSelector.stopEndpoint();
        System.out.println(" done!");
    }
    
}
