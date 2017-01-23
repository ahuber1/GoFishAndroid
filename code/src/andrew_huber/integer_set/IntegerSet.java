package andrew_huber.integer_set;

import andrew_huber.exceptions.InvalidArgumentException;

/**
 * Contains the necessary attributes to create an IntegerSet used 
 * for integer mapping 
 * @author andrew_huber
 *
 */
public class IntegerSet 
{
	/**
	 * Size of the IntegerSet
	 */
	private int size = 0;
	
	/**
	 * Dimension of the IntegerSet
	 */
	private int dimension;
	
	/**
	 * IntegerSet stored as an array
	 */
	private boolean numberSet[];
	
	/**
	 * Creates an IntegerSet
	 * @param dimension Dimension of the IntegerSet
	 */
	public IntegerSet(int dimension)
	{
		this.dimension = dimension;
		numberSet = new boolean[dimension];
		
		for(int i = 0; i < dimension; i++)
			numberSet[i] = false;
	}
	
	/**
	 * Inserts an element into the IntegerSet
	 * @param element Element to be inserted
	 * @throws InvalidArgumentException In case an error occurs
	 */
	public void insertElement(int element) throws InvalidArgumentException
	{	
		if((element < 0) || (element >= dimension))
		{
			throw new InvalidArgumentException("you can only insert a number" + 
					" between 1 and " + 
					Integer.toString(dimension));
		}
		else
		{
			if(numberSet[element] == false)
			{
				numberSet[element] = true;
				size++;
			}
		}
	}
	
	/**
	 * Deletes an element in the IntegerSet
	 * @param element Element to be inserted
	 * @throws InvalidArgumentException In case an error is thrown
	 */
	public void deleteElement(int element) throws InvalidArgumentException
	{
		if((element < 0) || (element >= dimension))
		{
			throw new InvalidArgumentException("you can only delete a number " + 
					"between 1 and " + 
					Integer.toString(dimension));
		}
		else
		{
			if(numberSet[element] == true)
			{
				numberSet[element] = false;
				size--;
			}
		}
	}
	
	/**
	 * Tests to see if a particular element is in the set
	 * @param element Element to be tested for its existence
	 * @return {@link Boolean#TRUE} if the element is in the set, 
	 * {@link Boolean#FALSE} 
	 * if the element was not found
	 * @throws InvalidArgumentException In case an error was thrown
	 */
	public boolean inSet(int element) throws InvalidArgumentException
	{
		if((element < 0) || (element >= dimension))
		{
			throw new InvalidArgumentException("you can only call inSet() " + 
					"while passing an " +
					"integer between 1 and " + Integer.toString(dimension));
		}
		else
			return numberSet[element];
	}
	
	/**
	 * Prints the IntegerSet
	 * @throws InvalidArgumentException In case an error occurs
	 */
	public void printSet() throws InvalidArgumentException
	{
		
		if(size == 0)
			System.out.print("---");
		else
		{
			for(int i = 0; i < dimension; i++)
			{
				if(inSet(i))
					System.out.print(i + " ");
			}
		}
		
		System.out.println();
	}
	
	/**
	 * Tests for a union of numbers
	 * @param set1 First set of numbers
	 * @param set2 Second set of numbers
	 * @return An integer set with the union of the sets expressed
	 * @throws InvalidArgumentException In case an error occurs
	 */
	public static IntegerSet unionOfSets(IntegerSet set1, IntegerSet set2) 
	throws InvalidArgumentException
	{
		int capacity   = (set1.size > set2.size ? set1.size : set2.size);
		IntegerSet set = new IntegerSet(capacity);
		
		for(int i = 0; i < capacity; i++)
		{
			System.out.println(i + ": " + ((set1.inSet(i)) || (set2.inSet(i))));
			
			if((set1.inSet(i)) || (set2.inSet(i)))
				set.insertElement(i);
		}
		
		return set;
	}
	
	/**
	 * Tests for an intersection of numbers
	 * @param set1 First set of numbers
	 * @param set2 Second set of numbers
	 * @return An integer set with the intersection of the sets expressed
	 * @throws InvalidArgumentException In case an error occurs
	 */
	public static IntegerSet intersectionOfSets(IntegerSet set1, 
			IntegerSet set2) throws InvalidArgumentException
	{
		int capacity = (set1.getDimension() < set2.getDimension() ? 
				set1.getDimension() : set2.getDimension());
		
		IntegerSet set = new IntegerSet(capacity);
		
		for(int i = 0; i < capacity; i++)
		{
			if((set1.inSet(i)) && (set2.inSet(i)))
				set.insertElement(i);
		}
		
		return set;
	}
	
	/**
	 * Gets dimension of IntegerSet
	 * @return The dimension of the IntegerSet
	 */
	public int getDimension()
	{
		return dimension;
	}
	
	/**
	 * Gets the size of the IntegerSet
	 * @return The size of the IntegerSet
	 */
	public int getSize()
	{
		return size;
	}
}
