package model;

//This Class was modeled off of the Pair class from LAB6

/**
 * The Tuple class holds three objects, a left, a middle, and a right. All can
 * be stored and accessed.
 * @param <LeftType> The object type in the left side.
 * @param <MiddleType> The object type in the middle.
 * @param <RightType> The object type in the right side.
 */
public class Tuple<LeftType, MiddleType, RightType> {
	private LeftType left;
	private MiddleType middle;
	private RightType right;
	
	/**
	 * Initialize Tuple constructor.
	 * @param left This Tuple's left object.
	 * @param middle This Tuple's middle object.
	 * @param right This Tuple's right object.
	 */
	public Tuple(LeftType left, MiddleType middle, RightType right) {
		this.left = left;
		this.middle = middle;
		this.right = right;
	}
	
	/**
	 * Set this Tuple's left object.
	 * @param left This Tuple's new left object.
	 */
	public void setLeft(LeftType left) {
		this.left = left;
	}
	
	/**
	 * Get this Tuple's left object.
	 * @return This Tuple's left object.
	 */
	public LeftType getLeft() {
		return left;
	}
	
	/**
	 * Set this Tuple's right object.
	 * @param right This Tuple's new right object.
	 */
	public void setRight(RightType right) {
		this.right = right;
	}
	
	/**
	 * Get this Tuple's right object.
	 * @return This Tuple's right object.
	 */
	public RightType getRight() {
		return right;
	}
	
	/**
	 * Get this Tuple's middle object.
	 * @return This Tuple's middle object.
	 */
	public MiddleType getMiddle() {
		return middle;
	}
	
	/**
	 * Set this Tuple's middle object.
	 * @param middle This Tuple's new middle object.
	 */
	public void setMiddle(MiddleType middle) {
		this.middle = middle;
	}
}