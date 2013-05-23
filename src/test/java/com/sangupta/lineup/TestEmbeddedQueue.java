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

import com.sangupta.lineup.domain.LineUpQueue;
import com.sangupta.lineup.domain.QueueMessage;


/**
 * @author sangupta
 *
 */
public class TestEmbeddedQueue {

	public static void main(String[] args) throws Exception {
		 LineUpQueue queue = LineUpService.createMessageQueue("sangupta");
		 
		 queue.addMessage("one");
		 queue.addMessage("two");
		 
		 QueueMessage msg = queue.getMessage();
		 System.out.println(msg.getBody());
		 
		 queue.addMessage("three");
		 
		 msg = queue.getMessage();
		 System.out.println(msg.getBody());
		 
		 msg = queue.getMessage();
		 System.out.println(msg.getBody());
	}
}
