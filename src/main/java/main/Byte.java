package main;

/**
 * <b>Byte class.</b> The byte class is essentially a wrapper for a value that
 * ranges from 0 to 63. Initially, I thought this created unnecessary wrapping,
 * but then I realized that I can bypass precondition checking (every single
 * time I work with an int!!! yeah it was annoying). Hopefully, this makes my
 * life easier... (and hopefully I don't have to change this anymore?)
 * 
 * Also, if one wanted to extend this MIX simulator to a different byte size (I
 * have no idea why you would want to do that though), then simply change the
 * value of the precondition checking.
 *
 * @author Tae Hyung Kim
 */

public class Byte implements Comparable<Byte>{
	int myValue;

	/**
	 * <b>Constructor for Byte class</b>. This is the only constructor for the
	 * Byte class. Keeping in mind that most of the Byte objects will be
	 * constructed using a normal int, I let that be the type for the argument,
	 * and I assumed that anything smaller will be cast up. In addition any
	 * primitive bigger than an int will not cast down correctly (if I remember
	 * correctly), which will allow this to work pretty well :).
	 */
	public Byte(int value) {
		if (value < 0 || value > 63) {
			throw new IllegalArgumentException("A byte must be between 0 and 63, inclusive.");
		}
		myValue = value;
	}

	/**
	 * <b>getValue method.</b> This is mostly for the Word class, which is, at
	 * the time of writing, the only class that is using it to a large extent.
	 */
	public int getValue() {
		return myValue;
	}

	/**
	 * This method sets the value of the Byte object. Originally, it was intended for the
     * Byte to be immutable, but this changed with the recent large project overhaul.
	 * @param value
	 */
	public void setValue(int value) {
	    myValue = value;
    }
	
	/**
	 * <b>toString method.</b> This is very important for the toString method
	 * given in the Word class which converts every Byte to its actual value for
	 * writing into the .mix file (which currently has no purpose but may in the
	 * future).
	 */
	public String toString() {
		String a = Integer.toString(myValue);
		if(a.length() == 1) {
			a = "0" + a;
		}
		return a;
	}

	public boolean equals(Object other) {
		return this.myValue == ((Byte) other).myValue;
	}
	
	/**
	 * <b>compareTo method.</b> This method is required by Comparable. 
	 */
	public int compareTo(Byte other) {
		return this.getValue() - other.getValue();
	}
}
