/**
 *
 * lineup - In-Memory high-throughput queue
 * Copyright (c) 2013, Sandeep Gupta
 * 
 * http://sangupta.com/projects/lineup
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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


/**
 * A priority queue implementation that allows us to change the priority
 * of each item.
 * 
 * The clear() method if invoked from any thread will lock all threads that
 * try and perform add() or poll() calls after the clear() method was called.
 * 
 * If no clear() invocation has been made, no locks are obtained and the entire
 * queue functionality is mostly lock-free. This ensures that the usual operations
 * are faster.
 * 
 * The add() method is usually <code>O(1)</code>. The poll() method is usually
 * <code>O(1)</code>, with worst case complexity of <code>O(m)</code> where <code>m</code>
 * is the maximum priority that has been set for this queue.
 * 
 * The clear() method is always <code>O(m)</code>, where <code>m</code>
 * is the maximum priority that has been set for this queue.
 * 
 * @author sangupta
 *
 */
public class ChangingPriorityQueue<E extends Prioritizable> {
	
	protected final Node<E> SENTINEL_NODE = new Node<E>(null);

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
	
	/**
	 * All the current items that are in the queue - this makes sure that
	 * we can update the duplicates.
	 */
	protected final ConcurrentMap<E, Node<E>> currentItems;
	
	/**
	 * A guard that helps threads wait before adding something if clearing
	 * of all elements is in progress.
	 */
	protected volatile boolean clearing = false;
	
	/**
	 * Helps lock all methods when something like clearing
	 * takes place.
	 * 
	 */
	protected final ReentrantLock reentrantLock;
	
	/**
	 * Create a new queue where the maximum priority of an element is specified.
	 * 
	 * @param maxPriority
	 */
	@SuppressWarnings("unchecked")
	public ChangingPriorityQueue(int maxPriority) {
		this.maxPriority = maxPriority + 1; // we add one to make sure that the user supplied value is inclusive
		
		this.lists = new ConcurrentDoublyLinkedList[this.maxPriority];
		for(int index = 0; index < this.maxPriority; index++) {
			this.lists[index] = new ConcurrentDoublyLinkedList<E>();
		}
		
		this.currentItems = new ConcurrentHashMap<E, Node<E>>();
		this.currentQueue = new AtomicInteger(0);
		this.reentrantLock = new ReentrantLock();
	}

	/**
	 * Add the element to the queue.
	 * 
	 * @param element
	 * @return
	 */
	public boolean add(E element) {
		if(element == null) {
			throw new NullPointerException("Element to be added cannot be null");
		}
		
		int priority = element.getPriority();
		if(priority < 0) {
			throw new IllegalArgumentException("Priority of the element cannot be negative");
		}
		
		// check for clear() call
		boolean locked = false;
		if(this.clearing) {
			this.reentrantLock.lock();
			locked = true;
		}
		
		// proceed for adding
		try {
			Node<E> older = this.currentItems.putIfAbsent(element, SENTINEL_NODE);
			if(older != null) {
				while(older == SENTINEL_NODE) { // this is a memory location comparison for SENTINAL NODE check
					older = this.currentItems.get(element);
				}
				
				return incrementPriority(older, element.getPriority());
			}
			
			if(priority > this.maxPriority) {
				priority = this.maxPriority - 1;
			}
			
			// add element to right list
			Node<E> node = this.lists[priority].offerLast(element);
			
			if(node != null) {
				this.currentItems.replace(element, SENTINEL_NODE, node);
				updateCurrentQueue(priority);
			}
			
			return node != null;
		} finally {
			// clear any previous lock
			if(locked) {
				this.reentrantLock.unlock();
			}
		}
	}
	
	/**
	 * Increment the priority of the element in the node by the amount that is specified.
	 * Take care of min and max priority. Also, we need to then place the element in the
	 * right queue.
	 * 
	 * There is no need for obtaining the lock here when clear() is in progress as this
	 * method is called from within the add() method which should have obtained the lock
	 * at the right time.
	 * 
	 * @param node
	 * @param deltaPriority
	 * 
	 * @return <code>true</code> if the priority was modified, <code>false</code> otherwise.
	 * 
	 */
	private boolean incrementPriority(Node<E> node, int deltaPriority) {
		if(deltaPriority == 0) {
			deltaPriority = 1;
		}
		
		synchronized(node) {
			// set the node's priority
			int currentPriority = node.element.getPriority();
			int newPriority = currentPriority + deltaPriority;
			if(newPriority > (this.maxPriority - 1)) {
				newPriority = this.maxPriority - 1;
			}
			
			if(newPriority < 0) {
				newPriority = 0;
			}
			
			if(newPriority == currentPriority) {
				return false;
			}
			
			node.element.incrementPriority(newPriority - currentPriority);
			
			// now remove it from the list
			if(!node.isDeleted()) {
				boolean deleted = node.delete();
				if(!deleted) {
					return false;
				}
			}
			
			// move this to the new list
			this.lists[newPriority].add(node);
			
			// update the current queue
			this.updateCurrentQueue(newPriority);
		}
		
		return false;
	}

	/**
	 * Remove the element from the queue.
	 * 
	 * @return
	 */
	public E poll() {
		// check for clear() call
		boolean locked = false;
		if(this.clearing) {
			this.reentrantLock.lock();
			locked = true;
		}
		
		try {
			return pollUnlocked();
		} finally {
			// clear any previous lock
			if(locked) {
				this.reentrantLock.unlock();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private E pollUnlocked() {
		do {
			int current = this.currentQueue.get();
			
			E element = this.lists[current].pollFirst();
			if(element != null)  {
				// remove it form current elements
				this.currentItems.remove(element);
				
				// return it back
				return element;
			}
			
			// check if we have come to the lowest priority queue
			if(current == 0 && this.currentQueue.get() == 0) {
				return null;
			}
			
			// only switch if the value hasn't changed since then
			this.currentQueue.compareAndSet(current, current - 1);
		} while(true);
	}
	
	/**
	 * 
	 * @param timeout
	 * @param timeUnit
	 * @return
	 */
	public E poll(long timeout, TimeUnit timeUnit) {
		if(timeout == 0) {
			return poll();
		}
		
		long nanos = timeUnit.toNanos(timeout);
		long expireAt = System.nanoTime() + nanos;
		E result = null;
		while ((result = poll()) == null && nanos > 0) {
			if(System.nanoTime() > expireAt) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * Remove an element from the queue.
	 * 
	 * @param o
	 * @return
	 */
	public E remove(Object o) {
		Node<E> node = this.currentItems.remove(o);
		if(node == null) {
			return null;
		}
		
		node.delete();
		return node.element;
	}
	
	/**
	 * Clear all lists right away. We need to make sure that all the items from
	 * all existing lists can be cleared, and
	 * 
	 */
	public void clear() {
		this.clearing = true;
		try {
			// obtain a lock so that nothing else can happen
			this.reentrantLock.lock();
			
			// clear all lists
			for(int index = 0; index < this.lists.length; index++) {
				this.lists[index].clear();
			}
			
			this.currentQueue.set(0);
			this.currentItems.clear();
		} finally {
			this.clearing = false;
			this.reentrantLock.unlock();
		}
	}
	
	/**
	 * Update the current queue pointer to the right list in case.
	 * 
	 * @param value
	 */
	private void updateCurrentQueue(final int value) {
		do {
			int current = this.currentQueue.get();
			if(current >= value) {
				return;
			}
			
			if(current < value) {
				boolean changed = this.currentQueue.compareAndSet(current, value);
				if(changed) {
					return;
				}
			}
		} while(true);
	}
}
