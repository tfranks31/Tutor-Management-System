package model;

/**
 * The Pair class holds two objects, a left and a right. Both sides can be 
 * stored and accessed.
 * @param <LeftType> The object type in the left side.
 * @param <RightType> The object type in the right side.
 */
public class Pair<LeftType, RightType> {
	private LeftType left;
	private RightType right;
	
	/**
	 * Initialize Pair constructor. 
	 * @param left This Pair's left object.
	 * @param right THis Pair's right object.
	 */
	public Pair(LeftType left, RightType right) {
		this.left = left;
		this.right = right;
	}
	
	/**
	 * Set this Pair's left object.
	 * @param left This Pair's new left object.
	 */
	public void setLeft(LeftType left) {
		this.left = left;
	}
	
	/**
	 * Get this Pair's left object.
	 * @return This Pair's left object.
	 */
	public LeftType getLeft() {
		return left;
	}
	
	/**
	 * Set this Pair's right object.
	 * @param right This Pair's new right object.
	 */
	public void setRight(RightType right) {
		this.right = right;
	}
	
	/**
	 * Get this Pair's right object.
	 * @return This Pair's right object.
	 */
	public RightType getRight() {
		return right;
	}
}