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
import java.util.concurrent.TimeUnit;

import com.sangupta.lineup.domain.QueueMessage;

/**
 * @author sangupta
 *
 */
public class DuplicateMergingPriorityQueue implements BlockingQueue<QueueMessage> {
	
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
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<QueueMessage> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends QueueMessage> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
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
	public void put(QueueMessage e) throws InterruptedException {
		this.internalSet.add(e);		
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean offer(QueueMessage e, long timeout, TimeUnit unit) throws InterruptedException {
		return this.internalSet.add(e);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#take()
	 */
	@Override
	public QueueMessage take() throws InterruptedException {
		// TODO Auto-generated method stub
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
		throw new RuntimeException("Method not yet implemented");
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
	 */
	@Override
	public int drainTo(Collection<? super QueueMessage> c, int maxElements) {
		throw new RuntimeException("Method not yet implemented");
	}

}
