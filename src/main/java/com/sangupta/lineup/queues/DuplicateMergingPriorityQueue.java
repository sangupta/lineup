/*************************************************************************
 *
 * MultiPLX Confidential
 * _____________________
 *
 * Copyright (C) 2012-2013, MultiPLX Founders.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the 
 * property of MultiPLX founders and original developers. The 
 * intellectual and technical concepts contained herein are proprietary 
 * to the owners mentioned before, and may be covered by U.S. and 
 * Foreign Patents, patents in process, and are protected by trade 
 * secret or copyright law. Dissemination of this information or 
 * reproduction of this material is strictly forbidden unless prior 
 * written permission is obtained from all persons mentioned before. 
 * 
 * Please see project license for more details.
 *
 **************************************************************************/

package com.sangupta.lineup.queues;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sangupta.lineup.domain.QueueMessage;

/**
 * A {@link PriorityBlockingQueue} implementation that merges duplicate elements and increases
 * the priority to the combined priority of the duplicate elements.
 * 
 * @author sangupta
 *
 */
public class DuplicateMergingPriorityQueue extends PriorityBlockingQueue<QueueMessage> implements BlockingQueue<QueueMessage> {
	
	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = 3802487984979985654L;
	
	/**
	 * The internal reference set that we use to base our implementation upon
	 */
	private final ConcurrentSkipListSet<QueueMessage> internalSet = new ConcurrentSkipListSet<QueueMessage>();
	
	/**
	 * @see java.util.Queue#remove()
	 */
	@Override
	public QueueMessage remove() {
		QueueMessage qm = this.internalSet.last();
		this.internalSet.remove(qm);
		return qm;
	}

	/**
	 * @see java.util.Queue#poll()
	 */
	@Override
	public QueueMessage poll() {
		return this.internalSet.pollLast();
	}

	/**
	 * @see java.util.Queue#element()
	 */
	@Override
	public QueueMessage element() {
		return this.internalSet.last();
	}

	/**
	 * @see java.util.Queue#peek()
	 */
	@Override
	public QueueMessage peek() {
		try {
			return this.internalSet.last();
		} catch(NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return this.internalSet.size();
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.internalSet.isEmpty();
	}

	/**
	 * Returns an iterator that retrieves the elements with highest priority first.
	 * 
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<QueueMessage> iterator() {
		return this.internalSet.descendingIterator();
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return this.internalSet.toArray();
	}

	/**
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return this.internalSet.toArray(a);
	}

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.internalSet.containsAll(c);
	}

	/**
	 * This is a trickier method. Adding elements may reject duplicates
	 * and thus we need to do it one by one, so that we can increase priority
	 * of duplicates.
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends QueueMessage> c) {
		boolean success = true;
		for(QueueMessage qm : c) {
			boolean added = add(qm);
			if(!added) {
				success = false;
			}
		}
		
		return success;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return this.internalSet.removeAll(c);
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return this.internalSet.retainAll(c);
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.internalSet.clear();
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#add(java.lang.Object)
	 */
	@Override
	public boolean add(QueueMessage e) {
		return this.internalSet.add(e);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(QueueMessage e) {
		return this.internalSet.add(e);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
	 */
	@Override
	public void put(QueueMessage e) {
		this.internalSet.add(e);		
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean offer(QueueMessage e, long timeout, TimeUnit unit) {
		return this.internalSet.add(e);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#take()
	 */
	@Override
	public QueueMessage take() throws InterruptedException {
		return null;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public QueueMessage poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#remainingCapacity()
	 */
	@Override
	public int remainingCapacity() {
		throw new RuntimeException("Method not yet implemented");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		return this.internalSet.remove(o);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return this.internalSet.contains(o);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
	 */
	@Override
	public int drainTo(Collection<? super QueueMessage> c) {
		Iterator<QueueMessage> iterator = this.internalSet.descendingIterator();
		
		int count = 0;
		while(iterator.hasNext()) {
			c.add(iterator.next());
			count++;
		}
		
		return count;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
	 */
	@Override
	public int drainTo(Collection<? super QueueMessage> c, int maxElements) {
		Iterator<QueueMessage> iterator = this.internalSet.descendingIterator();
		
		int count = 0;
		while(iterator.hasNext()) {
			c.add(iterator.next());
			count++;
			
			if(count == maxElements) {
				return count;
			}
		}
		
		return count;
	}

}
