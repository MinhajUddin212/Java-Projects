package bigint;

/**
  This class works with a BigInteger, i.e. a positive or negative integer with 
  any number of digits, which overcomes the computer storage length limitation of 
  an integer. 
 */
public class BigInteger {

	/**
	  True if this is a negative integer
	 */
	boolean negative;
	
	/**
	  Number of digits in this integer
	 */
	int numDigits;
	
	/**
	  Reference to the first node of this integer's linked list representation
	  NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	  For instance, the integer 235 would be stored as:
	     5 --> 3  --> 2
	     
	  Insignificant digits are not stored. So the integer 00235 will be stored as:
	     5 --> 3 --> 2        
	 */
	DigitNode front;
	
	/**
	  Initializes this integer to a positive number with zero digits, in other
	  words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	  Parses an input integer string into a corresponding BigInteger instance.
	  A correctly formatted integer would have an optional sign as the first 
	  character (no sign means positive), and at least one digit character
	  (including zero). 

	  Leading and trailing spaces are ignored. So "  +123  " will still parse 
	  correctly, as +123, after ignoring leading and trailing spaces in the input
	  string.
	  
	  Spaces between digits are not ignored. So "12  345" will not parse as
	  an integer - the input is incorrectly formatted.
	  
	  @param1 integer Integer string that is to be parsed
	  @return BigInteger instance that stores the input integer.
	 */
	
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		BigInteger result = new BigInteger();
		integer = integer.trim();
		
		// This for loop goes through and finds if the input is positive or not and removes the sign from the string
		for (int i =0; i< integer.length(); i++) {
			if(i==0) {
				if (integer.charAt(i) == '-') {
					result.negative = true;
					integer = integer.replaceFirst("[-]","");
					continue;
				}else if (integer.charAt(i) == '+') {
					integer = integer.replaceFirst("[+]","");
					continue;
				}
			}
		}
		
		// cleans up possible zeroes after the - or + sign
		integer = integer.replaceAll("^0+(?!$)", ""); 
		result.numDigits = integer.length();
		// if the only value is zero then will ignore sign
		if(integer.length() == 1) {
			for(int i = 0; i < integer.length(); i++) {
				char j = integer.charAt(i); 
				if (j=='0') {
					result.negative = false;
				}
			}
		}

		char first = integer.charAt(0);
		// if any character is not a digit, throw error
		if(!Character.isDigit(first)) {
			throw new IllegalArgumentException ("Incorrect Format");
		}
		int frontint = Character.getNumericValue(first);
		DigitNode frontdigitnode = new DigitNode(frontint, null);
		if(integer.length() == 1) {
			result.front = frontdigitnode;
		}
		DigitNode prevnode = new DigitNode(0,null);
		
		for (int i=1;i< integer.length(); i++) {
			if(i==1) {
				int second = Character.getNumericValue(integer.charAt(1));
				DigitNode secondnode = new DigitNode(second, null);
				secondnode.next = frontdigitnode;
				prevnode = secondnode;
				result.front = prevnode;
				continue;
			}
			char d = integer.charAt(i);
			// if any character is not a digit, throw error
			if(!Character.isDigit(d)) {
				throw new IllegalArgumentException ("Incorrect Format");
			}
			int currentint = Character.getNumericValue(integer.charAt(i));
			DigitNode currentnode = new DigitNode(currentint, null);
			currentnode.next = prevnode;
			prevnode = currentnode;
			result.front = prevnode;
		}
		
		if(result.numDigits == 1) {
			if(result.front.digit == 0) {
				result.front = null;
			}
		}
		
		return result;
	}
	
	/**
	  Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	  DO NOT modify the input big integers.
	  
	  NOTE that either or both of the input big integers could be negative.
	  (Which means this method can effectively subtract as well.)
	  
	  @param1 first First big integer
	  @param2 second Second big integer
	  @return Result big integer
	 */
	
	// Subtract method--> takes a predetermined smaller integer and subtracts it from a bigger integer 
	private static BigInteger subtract(BigInteger bigger, BigInteger smaller) {
		BigInteger result = new BigInteger();
		int carry = 0;
		int difference = 0;
		DigitNode prev = null;
		DigitNode temp = null;
		DigitNode biggerfront = bigger.front;
		DigitNode smallerfront = smaller.front;
		while (biggerfront != null || smallerfront != null) {
			if(smallerfront != null) {
				if (carry>0) {
					smallerfront.digit = smallerfront.digit + carry;
					carry=0;
				}
				difference = biggerfront.digit - smallerfront.digit; 
				if(difference <0) {
					carry =1;
					difference = 10 - Math.abs(difference);
				}
				temp = new DigitNode(difference, null);
				if(result.front == null) {
					result.front = temp;
				}else {
					prev.next = temp; 
				}
				prev = temp; 
				if(biggerfront != null) {
					biggerfront = biggerfront.next;
					smallerfront = smallerfront.next;
				}
			}else {
				if (carry>0) {
					difference = biggerfront.digit - carry;
					carry = 0;
					temp = new DigitNode(difference, null);
				}else {
					temp = new DigitNode(biggerfront.digit, null);
					}
				prev.next = temp;
				prev=temp;
				if(biggerfront != null) {
					biggerfront = biggerfront.next;
				}
				
			}
		}
		return result;
	}
	
	
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		BigInteger result = new BigInteger();
		
		if(first != null & second == null) {
			return first; 
		}
		if(second != null & first == null) {
			return second;
		}
		
		// 4 CASES TO CONSIDER!
		
		//If both numbers are positive
		if (first.negative == false & second.negative == false) {
			int carry = 0;
			int sum = 0;
			DigitNode prev = null;
			DigitNode temp = null;
			DigitNode firstfront = first.front;
			DigitNode secondfront = second.front;
			while(firstfront != null || secondfront != null) {
				sum = carry + (firstfront != null? firstfront.digit :0) + (secondfront != null? secondfront.digit :0);
				carry = (sum >= 10)? 1:0; 
				sum =sum % 10;
				temp= new DigitNode (sum, null);
				if(result.front == null) {
					result.front = temp;
				}else {
					prev.next = temp;
				}
				prev = temp;
				if(firstfront != null) {
					firstfront = firstfront.next;
				}
				if(secondfront != null) {
					secondfront = secondfront.next;
				}
			}
	        if (carry >0) {
	        	DigitNode carrynode = new DigitNode(carry, null);
	        	temp.next = carrynode;
	        }
		}
		
		
		//If both numbers are negative
		if (first.negative == true & second.negative == true) {
			result.negative = true;
			int carry = 0;
			int sum = 0;
			DigitNode prev = null;
			DigitNode temp = null;
			DigitNode firstfront = first.front;
			DigitNode secondfront = second.front;
			while(firstfront != null || secondfront != null) {
				sum = carry + (firstfront != null? firstfront.digit :0) + (secondfront != null? secondfront.digit :0);
				carry = (sum >= 10)? 1:0; 
				sum =sum % 10;
				temp= new DigitNode (sum, null);
				if(result.front == null) {
					result.front = temp;
				}else {
					prev.next = temp;
				}
				prev = temp;
				if(firstfront != null) {
					firstfront = firstfront.next;
				}
				if(secondfront != null) {
					secondfront = secondfront.next;
				}
			}
	        if (carry >0) {
	        	DigitNode carrynode = new DigitNode(carry, null);
	        	temp.next = carrynode;
	        }
		}
		
		
		// if first number is negative and the second is positive
		if(first.negative == true & second.negative == false) {
			if(first.numDigits > second.numDigits) { 
				result = subtract(first, second);
				result.negative = true;
			}
			if(first.numDigits < second.numDigits){
				result = subtract (second, first);
				result.negative = false;
			}
			if(first.numDigits == second.numDigits) {
				String firstnum = first.toString();
				//get rid of negative sign at the front
				if (firstnum.charAt(0)== '-') {
					firstnum = firstnum.replaceFirst("[-]","");
				}
				String secondnum = second.toString();
				for(int i = 0; i < firstnum.length(); i++) {
					int firstNumDigit = Character.getNumericValue(firstnum.charAt(i));
					int secondNumDigit = Character.getNumericValue(secondnum.charAt(i));
					if(firstNumDigit > secondNumDigit) {
						result = subtract(first, second);
						result.negative = true;
						break;
					}
					if(secondNumDigit > firstNumDigit) {
						result = subtract(second, first);
						result.negative = false;
						break;
					}else {
						continue;
					}
				}
			}
		}
		
		// If first number is positive and second is negative
		if(second.negative == true & first.negative == false) {
			if(second.numDigits > first.numDigits) { 
				result = subtract(second, first);
				result.negative = true;
			}
			if(second.numDigits < first.numDigits){
				result = subtract (first, second);
				result.negative = false;
			}
			if(second.numDigits == first.numDigits) {
				String secondnum = second.toString();
				//get rid of negative sign at the front
				if (secondnum.charAt(0)== '-') {
					secondnum = secondnum.replaceFirst("[-]","");
				}
				String firstnum = first.toString();
				for(int i = 0; i < secondnum.length(); i++) {
					int firstNumDigit = Character.getNumericValue(firstnum.charAt(i));
					int secondNumDigit = Character.getNumericValue(secondnum.charAt(i));
					if(firstNumDigit > secondNumDigit) {
						result = subtract(first, second);
						result.negative = false;
						break;
					}
					if(secondNumDigit > firstNumDigit) {
						result = subtract(second, first);
						result.negative = true;
						break;
					}else {
						continue;
					}
				}
			}
			
		}
		
		//parse the result to get rid of unnecessary values Example: -00001 ----> -1
		String res = result.toString();
		result = parse(res);
		return result;
	}
	
	/**
	  Returns the BigInteger obtained by multiplying the first big integer
	  with the second big integer
	  
	  @param1 first First big integer
	  @param2 second Second big integer
	  @return A new BigInteger which is the product of the first and second big integers
	 */
	
	private static BigInteger multi(DigitNode bigint, int digit) {
		BigInteger result = new BigInteger();
		int product = 0;
		int carry = 0;
		DigitNode temp = null;
		DigitNode prev= null;
		while(bigint != null) {
			product = carry + ((bigint.digit) * digit);
			carry = Math.floorDiv(product, 10); 
			product = product%10;
			temp = new DigitNode (product, null);
			if(result.front == null) {
				result.front = temp;
			}else {
				prev.next = temp; 
			}
			prev = temp;
			if(bigint != null) {
				bigint = bigint.next;
			}
		}
		if (carry >0) {
			DigitNode carrynode = new DigitNode(carry, null);
        	temp.next = carrynode;
		}
		return result;
	}
	
	
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		BigInteger result = new BigInteger(); 
		DigitNode firstcopy = first.front;
		int numzeros = 0;
		
		while(second.front != null) {
			int digit = second.front.digit; 
			BigInteger temp = multi(firstcopy, digit);
			if(numzeros >0) {
				for(int i =0; i < numzeros; i++) {
					DigitNode zeronode = new DigitNode(0,temp.front);
					temp.front = zeronode;
				}
			}
			result = add(result, temp);
			if(second.front != null) {
				second.front = second.front.next;
				numzeros +=1;
			}
		}
		
		//Assign the sign of accordingly
		if(first.negative == true || second.negative == true) {
			result.negative = true;
		}if(first.negative == true & second.negative == true) {
			result.negative = false;
		} 
		
		String res = result.toString();
		result = parse(res);
		return result; 
		
	}
	
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
