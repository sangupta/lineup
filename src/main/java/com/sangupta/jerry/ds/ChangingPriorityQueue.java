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

package com.sangupta.jerry.ds;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * A priority queue implementation that allows us to change the priority
 * of each item.
 * 
 * @author sangupta
 *
 */
public class ChangingPriorityQueue<E extends Prioritizable> {

	/**
	 * The maximum priority that an element can achieve. Once this value is
	 * achieved the priority no further increases.
	 */
	protected final int maxPriority;
	
	/**
	 * The holder that keeps multiple lists for multiple priorities
	 */
	protected final ConcurrentDoublyLinkedList<E> lists[];
	
	/**
	 * Holds the index to the current queue that contains the highest
	 * priority elements
	 */
	protected final AtomicInteger currentQueue;
	
	@SuppressWarnings("unchecked")
	public ChangingPriorityQueue(int maxPriority) {
		this.maxPriority = maxPriority + 1; // we add one to make sure that the user supplied value is inclusive
		
		this.lists = new ConcurrentDoublyLinkedList[this.maxPriority];
		for(int index = 0; index < this.maxPriority; index++) {
			this.lists[index] = new ConcurrentDoublyLinkedList<E>();
		}
		
		this.currentQueue = new AtomicInteger(0);
	}
	
	public boolean add(E e) {
		if(e == null) {
			throw new NullPointerException("Element to be added cannot be null");
		}
		
		int priority = e.getPriority();
		if(priority < 0) {
			throw new IllegalArgumentException("Priority of the element cannot be negative");
		}
		
		if(priority > this.maxPriority) {
			priority = this.maxPriority - 1;
		}
		
		// add element to right list
		boolean added = this.lists[priority].add(e);
		
		if(added) {
			updateCurrentQueue(priority);
		}
		
		return added;
	}
	
	public E poll() {
		do {
			int current = this.currentQueue.get();
			
			E element = this.lists[current].pollFirst();
			if(element != null)  {
				return element;
			}
			
			if(current == 0 && this.currentQueue.get() == 0) {
				return null;
			}
			
			this.currentQueue.compareAndSet(current, current - 1);
		} while(true);
	}
	
	/**
	 * Update the current queue pointer to the right list in case.
	 * 
	 * @param value
	 */
	private void updateCurrentQueue(final int value) {
		do {
			int current = this.currentQueue.get();
			if(current < value) {
				this.currentQueue.compareAndSet(current, value);
			}
		} while(true);
	}
}
