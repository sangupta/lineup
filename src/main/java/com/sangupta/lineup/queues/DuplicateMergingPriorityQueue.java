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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
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
	private static final long serialVersionUID = 6135795995864762574L;

	/**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	/**
     * The underlying map. Uses Boolean.TRUE as value for each
     * element.  This field is declared final for the sake of thread
     * safety, which entails some ugliness in clone()
     */
    private final ConcurrentNavigableMap<QueueMessage, QueueMessage> internalMap = new ConcurrentSkipListMap<QueueMessage, QueueMessage>();
	
	/**
	 * @see java.util.Queue#remove()
	 */
	@Override
	public QueueMessage remove() {
		Entry<QueueMessage, QueueMessage> entry = this.internalMap.pollLastEntry();
		if(entry != null) {
			return entry.getKey();
		}
		
		throw new NoSuchElementException();
	}

	/**
	 * @see java.util.Queue#poll()
	 */
	@Override
	public QueueMessage poll() {
		Entry<QueueMessage, QueueMessage> entry = this.internalMap.pollLastEntry();
		if(entry != null) {
			return entry.getKey();
		}
		
		return null;
	}

	/**
	 * @see java.util.Queue#element()
	 */
	@Override
	public QueueMessage element() {
		return this.internalMap.lastKey();
	}

	/**
	 * @see java.util.Queue#peek()
	 */
	@Override
	public QueueMessage peek() {
		try {
			return this.internalMap.lastKey();
		} catch(NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return this.internalMap.size();
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.internalMap.isEmpty();
	}

	/**
	 * Returns an iterator that retrieves the elements with highest priority first.
	 * 
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<QueueMessage> iterator() {
		return this.internalMap.descendingKeySet().descendingIterator();
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		// Estimate size of array; be prepared to see more or fewer elements
        Object[] r = new Object[size()];
        Iterator<QueueMessage> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
	}

	/**
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		// Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<QueueMessage> it = iterator();

        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) { // fewer elements than expected
                if (a != r)
                    return Arrays.copyOf(r, i);
                r[i] = null; // null-terminate
                return r;
            }
            r[i] = (T) it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
	}
	
	 /**
     * Reallocates the array being used within toArray when the iterator
     * returned more elements than expected, and finishes filling it from
     * the iterator.
     *
     * @param r the array, replete with previously stored elements
     * @param it the in-progress iterator over this collection
     * @return array containing the elements in the given array, plus any
     *         further elements returned by the iterator, trimmed to size
     */
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        int i = r.length;
        while (it.hasNext()) {
            int cap = r.length;
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0)
                    newCap = hugeCapacity(cap + 1);
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T) it.next();
        }
        // trim if overallocated
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError
                ("Required array size too large");
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        
        return true;

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
		boolean modified = false;
        Iterator<QueueMessage> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
        Iterator<QueueMessage> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.internalMap.clear();
	}

	/**
	 * This is a special method - which takes care that if the message already
	 * exists in the queue it will increment the existing message priority by 
	 * the priority of the message provided to be added.
	 * 
	 * @see java.util.concurrent.BlockingQueue#add(java.lang.Object)
	 */
	@Override
	public boolean add(QueueMessage e) {
		QueueMessage older = this.internalMap.putIfAbsent(e, e);
		if(older == null) {
			return true;
		}
		
		// retrieve the original message itself and increase priority
		older.incrementPriority(e.getPriority());
		return true;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(QueueMessage e) {
		return this.add(e);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
	 */
	@Override
	public void put(QueueMessage e) {
		this.add(e);		
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean offer(QueueMessage e, long timeout, TimeUnit unit) {
		return this.add(e);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#take()
	 */
	@Override
	public QueueMessage take() throws InterruptedException {
		Entry<QueueMessage, QueueMessage> qm = null;
		do {
			if(!this.internalMap.isEmpty()) {
				try {
					qm = this.internalMap.pollLastEntry();
				} catch(NoSuchElementException e) {
					// eat up
				}
				
				if(qm != null) {
					return qm.getKey();
				}
			}
		} while(true);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public QueueMessage poll(long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		long start = System.nanoTime();
		
		Entry<QueueMessage, QueueMessage> qm = null;
		do {
			if((System.nanoTime() - start) > nanos) {
				return null;
			}
			
			if(!this.internalMap.isEmpty()) {
				try {
					qm = this.internalMap.pollLastEntry();
				} catch(NoSuchElementException e) {
					// eat up
				}
				
				if(qm != null) {
					return qm.getKey();
				}
			}
		} while(true);
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
		QueueMessage qm = this.internalMap.remove(o);
		if(qm != null) {
			return true;
		}
		
		return false;
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return this.internalMap.containsKey(o);
	}

	/**
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
	 */
	@Override
	public int drainTo(Collection<? super QueueMessage> c) {
		Iterator<QueueMessage> iterator = this.iterator();
		
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
		Iterator<QueueMessage> iterator = this.iterator();
		
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
