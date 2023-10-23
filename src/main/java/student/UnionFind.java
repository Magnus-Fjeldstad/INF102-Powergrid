package student;

public interface UnionFind<E> {

	
	/**
	 * Joins two groups such that later find operations will return
	 * return same root for all elements in either groups.
	 * @param elem1, elem2 the two elements to join
	 */
	public void union(E elem1, E elem2);
	
	/**
	 * Returns the root element of this group, all elements in the group 
	 * must return same root element
	 */
	public E find(E elem);
	
	
	/**
	 * @return number of different groups
	 */
	public int numGroups();
	
	
	/**
	 * @param elem - any element in the group you want
	 * @return - an Iterable of all elements in the group.
	 */
	Iterable<E> group(E elem);

	/**
	 * Checks if two elements belong to same group
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean same(E a, E b);
}
