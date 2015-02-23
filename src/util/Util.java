package util;

public class Util {

	public boolean intToBoolean(int n) {
		if (n == 1)
			return true;
		else
			return false;
	}
	
	public double official(double number) {
		double official = (int) number;
		if ((number - official) >= 0.75)
			official += 1;
		else if ((number - official) >= 0.25)
			official += 0.5;
		return official;
	}
}