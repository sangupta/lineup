/**
 *
 * lineup - In-Memory high-throughput queue
 * Copyright (c) 2013-2014, Sandeep Gupta
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

package com.sangupta.lineup.queues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;
import com.sangupta.lineup.domain.QueueMessage;
import com.sangupta.lineup.domain.QueueOptions;

/**
 * This class helps {@link LineUpQueue} to wrap inside a normal {@link Queue}
 * object of Java class. This helps us in using these queue's with other Java
 * framework's that utilize a normal queue.
 * 
 * This also serves as a base when rolling out a new {@link LineUpQueue} as the
 * implementing class needs to implement only a few methods that are specific to
 * the implementation.
 * 
 * @author sangupta
 * @since 0.1.0
 */
public abstract class AbstractLineUpQueue implements LineUpQueue {
	
	/**
	 * The default message priority when the incoming message has not specified any.
	 */
	protected static final int DEFAULT_MESSAGE_PRIORITY = 1;

	/**
	 * The unique name of this queue.
	 */
	protected final String name;
	
	/**
	 * The security code assigned to this queue.
	 * 
	 */
	protected final String securityCode;
	
	/**
	 * The configuration options for this queue.
	 * 
	 */
	protected final transient QueueOptions options;
	
	/**
	 * Maintains the current queue size to skip O(n) computations here
	 */
	protected final AtomicInteger queueSize = new AtomicInteger(0);
	
	/**
	 * Convenience constructor - that initializes every known 
	 * parameter to null. To be used only in case of Remote queues.
	 * 
	 */
	AbstractLineUpQueue() {
		this.name = null;
		this.securityCode = null;
		this.options = null;
	}
	
	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            the name to assign to this queue
	 * 
	 * @param securityCode
	 *            the security code to assign to this queue. If this is not
	 *            provided a random security code is generated and assigned to
	 *            this queue
	 * 
	 * @param options
	 *            the options to use for this queue
	 * 
	 * @throws IllegalArgumentException
	 *             if either the <code>name</code> or the
	 *             <code>securityCode</code> is <code>null</code> or
	 *             <code>empty</code>.
	 */
	public AbstractLineUpQueue(String name, String securityCode, QueueOptions options) {
		if(AssertUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Queue name cannot be null/empty");
		}
		
		this.name = name;
		this.options = options;
		
		// initialize other params
		if(securityCode == null) {
			this.securityCode = UUID.randomUUID().toString();
		} else {
			this.securityCode = securityCode;
		}
	}
	
	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#getSecurityCode()
	 */
	@Override
	public String getSecurityCode() {
		return this.securityCode;
	}

	/**
	 * Add a message to the internal queue.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 * 
	 * @see LineUpQueue#addMessage(String)
	 */
	@Override
	public QueueMessage addMessage(String message) {
		return this.addMessage(message, this.options.getDelaySeconds(), DEFAULT_MESSAGE_PRIORITY);
	}
	
	/**
	 * Add a message to the internal queue with the given delay.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @param delaySeconds
	 *            the time after which the message is made available in the
	 *            queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 *         
	 * @see LineUpQueue#addMessage(String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds) {
		return this.addMessage(message, delaySeconds, DEFAULT_MESSAGE_PRIORITY);
	}
	
	/**
	 * Add a message to the internal queue with the given delay.
	 * 
	 * @param message
	 *            the {@link String} message that needs to be added to the queue
	 * 
	 * @param delaySeconds
	 *            the time after which the message is made available in the
	 *            queue
	 * 
	 * @return the {@link QueueMessage} instance that was added,
	 *         <code>null</code> if nothing was added.
	 *         
	 * @see LineUpQueue#addMessage(String, int)
	 */
	@Override
	public QueueMessage addMessage(String message, int delaySeconds, int priority) {
		QueueMessage qm = new QueueMessage(message, delaySeconds, priority);
		return this.addMessage(qm);
	}
	
	@Override
	public final QueueMessage addMessage(QueueMessage queueMessage) {
		queueMessage = this.addQueueMessage(queueMessage);
		if(queueMessage != null) {
			this.queueSize.incrementAndGet();
		}
		
		return queueMessage;
	}
	
	/**
	 * Add the given message to the queue
	 * 
	 * @param queueMessage
	 *            the message to add
	 * 
	 * @return the added message back
	 */
	public abstract QueueMessage addQueueMessage(QueueMessage queueMessage);
	
	/**
	 * Return a message from the queue, without waiting. Returns
	 * <code>null</code> if the queue is currently empty.
	 * 
	 * @return the {@link QueueMessage} instance which is wrapping up the actual
	 *         message
	 * 
	 * @see LineUpQueue#getMessage()
	 */
	@Override
	public final QueueMessage getMessage() {
		try {
			return this.getMessage(0);
		} catch(InterruptedException e) {
			// eat up
		}
		
		return null;
	}
	
	/**
	 * Return a message from the queue polling for the given time.
	 * 
	 * @param longPollTime
	 *            the poll time to wait before returning a <code>null</code>
	 * @return {@link QueueMessage} as read, <code>null</code> otherwise
	 * 
	 * @throws InterruptedException
	 *             if something interrupted this thread before poll time elapsed
	 */
	@Override
	public final QueueMessage getMessage(long longPollTime) throws InterruptedException {
		QueueMessage message = this.getQueueMessage(longPollTime);
		if(message != null) {
			this.queueSize.decrementAndGet();
		}
		
		return message;
	}
	
	/**
	 * Return a message from the queue polling for the given time.
	 * 
	 * @param longPollTime
	 *            the poll time to wait before returning a <code>null</code>
	 * @return {@link QueueMessage} as read, <code>null</code> otherwise
	 * 
	 * @throws InterruptedException
	 *             if something interrupted this thread before poll time elapsed
	 */
	protected abstract QueueMessage getQueueMessage(long longPollTime) throws InterruptedException;
	
	/**
	 * @see com.sangupta.lineup.queues.LineUpQueue#getMessages(int)
	 */
	@Override
	public List<QueueMessage> getMessages(int numMessages) {
		List<QueueMessage> list = new ArrayList<QueueMessage>();
		
		QueueMessage qm;
		for(int index = 0; index < numMessages; index++) {
			qm = this.getMessage();
			if(qm == null) {
				break;
			}
			
			list.add(qm);
		}
		
		return list;
	}
	
	/**
	 * Returns the number of messages in the queue
	 * 
	 * @return the number of messages
	 * 
	 */
	@Override
	public final int numMessages() {
		return this.queueSize.get();
	}

	/**
	 * 
	 * @see com.sangupta.lineup.queues.LineUpQueue#deleteMessage(java.lang.String)
	 */
	@Override
	public boolean deleteMessage(String messageID) {
		long id = StringUtils.getLongValue(messageID, 0);
		if(id == 0) {
			return false;
		}
		
		boolean deleted = this.removeMessageID(id);
		if(deleted) {
			this.queueSize.decrementAndGet();
		}
		
		return deleted;
	}
	
	/**
	 * Remove the message identified by the given message id.
	 * 
	 * @param id
	 *            the message identifier to remove
	 * 
	 * @return <code>true</code> if message was removed, <code>false</code>
	 *         otherwise
	 */
	public abstract boolean removeMessageID(long id);

	/**
	 * Remove a message from the head of the queue.
	 * 
	 * @return the message that was removed from the queue, <code>null</code> if
	 *         nothing was removed
	 * 
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
	 * Return a message if available in the queue.
	 * 
	 * @return the message if available in the queue.
	 *  
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
		return this.numMessages();
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.numMessages() == 0;
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
	 * Method not supported.
	 * 
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
