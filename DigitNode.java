package bigint;

public class DigitNode {
	int digit;
	DigitNode next;
	
	DigitNode(int digit, DigitNode next) {
		this.digit = digit;
		this.next = next;
	}
	
	public String toString() {
		return digit + "";
	}
}
