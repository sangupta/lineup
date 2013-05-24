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

package com.sangupta.lineup.domain;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import com.sangupta.jerry.util.AssertUtils;

/**
 * This class helps {@link LineUpQueue} to wrap inside a normal {@link Queue} object
 * of Java class. This helps us in using these queue's with other Java framework's
 * that utilize a normal queue.
 * 
 * @author sangupta
 *
 */
public abstract class AbstractLineUpBlockingQueue implements LineUpQueue {

	/**
	 * @see java.util.Queue#remove()
	 */
	@Override
	public QueueMessage remove() {
		QueueMessage message = getMessage();
		if(message != null) {
			return message;
		}
		
		throw new NoSuchElementException();
	}

	/**
	 * @see java.util.Queue#poll()
	 */
	@Override
	public QueueMessage poll() {
		return getMessage();
	}

	/**
	 * @see java.util.Queue#element()
	 */
	@Override
	public QueueMessage element() {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Queue#peek()
	 */
	@Override
	public QueueMessage peek() {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<QueueMessage> iterator() {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends QueueMessage> collection) {
		if(AssertUtils.isEmpty(collection)) {
			return false;
		}
		
		for(QueueMessage queueMessage : collection) {
			add(queueMessage);
		}
		
		return true;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		throw new RuntimeException("Method not supported");		
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#add(java.lang.Object)
	 */
	@Override
	public boolean add(QueueMessage queueMessage) {
		addMessage(queueMessage);
		return true;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(QueueMessage queueMessage) {
		queueMessage = addMessage(queueMessage);
		if(queueMessage != null) {
			return true;
		}
		
		return false;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
	 */
	@Override
	public void put(QueueMessage queueMessage) throws InterruptedException {
		addMessage(queueMessage);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean offer(QueueMessage e, long timeout, TimeUnit unit) throws InterruptedException {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#take()
	 */
	@Override
	public QueueMessage take() throws InterruptedException {
		QueueMessage qm = null;
		while(qm == null) {
			qm = getMessage();
			if(qm != null) {
				break;
			}
		}
		
		return qm;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public QueueMessage poll(long timeout, TimeUnit unit) throws InterruptedException {
		long seconds = 0;
		switch(unit) {
			case MILLISECONDS:
			case MICROSECONDS:
			case NANOSECONDS:
				break;
			
			case SECONDS:
				seconds = timeout;
				break;
				
			case MINUTES:
				seconds = timeout * 60;
				break;
				
			case HOURS:
				seconds = timeout * 3600;
				break;
				
			case DAYS:
				seconds = timeout * 86400l;
				break;
		}
		
		return getMessage(seconds);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#remainingCapacity()
	 */
	@Override
	public int remainingCapacity() {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
	 */
	@Override
	public int drainTo(Collection<? super QueueMessage> c) {
		throw new RuntimeException("Method not supported");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
	 */
	@Override
	public int drainTo(Collection<? super QueueMessage> c, int maxElements) {
		throw new RuntimeException("Method not supported");
	}

}
