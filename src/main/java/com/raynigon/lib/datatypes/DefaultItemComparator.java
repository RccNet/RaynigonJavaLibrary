package com.raynigon.lib.datatypes;

public class DefaultItemComparator<T> implements ItemComparator<T> {
	
	@Override
	public int equals(T item0, T item1) {
		if (item0 == null)
			return item1 == null ? 0 : Integer.MIN_VALUE;
		if (item1 == null)
			return Integer.MAX_VALUE;
		return item0.equals(item1) ? 0 : 1;
	}
}
