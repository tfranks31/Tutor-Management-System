package model;

public class Tuple<LeftType, MiddleType, RightType> {
	private LeftType left;
	private MiddleType middle;
	private RightType right;
	
	public Tuple(LeftType left, MiddleType middle, RightType right) {
		this.left = left;
		this.middle = middle;
		this.right = right;
	}
	
	public void setLeft(LeftType left) {
		this.left = left;
	}
	
	public LeftType getLeft() {
		return left;
	}
	
	public void setRight(RightType right) {
		this.right = right;
	}
	
	public RightType getRight() {
		return right;
	}
	
	public MiddleType getMiddle() {
		return middle;
	}
	
	public void setMiddle(MiddleType middle) {
		this.middle = middle;
	}
}
