package ui.constraint;

import java.util.HashMap;

public abstract class UiConstraint {
	/*create array length 8 for each bit of infomation of x,y,w,h of parent and current
	 * create array length 8 to tell if they are set or not (boolean)
	 * 
	 * create constraint absolute and relative
	 * loop through all constrants and complete all absolutes and than all relatives
	 * if constraint info is based on unset info try next constraint
	 * if all constraints require unset info assert error
	 * 
	 * constraints will take a number for which value to change and the value of change
	 * absolute - (index to change, num)
	 * relative - (index to change, multi, index to base of)
	 * both extends UiConstraint
	 */

	public static final int P_X = 0;
	public static final int P_Y = 1;
	public static final int P_W = 2;
	public static final int P_H = 3;
	public static final int T_X = 4;
	public static final int T_Y = 5;
	public static final int T_W = 6;
	public static final int T_H = 7;

	public static final UiConstraint CENTER_X = new CenterConstraint(UiConstraint.T_X);
	public static final UiConstraint CENTER_Y = new CenterConstraint(UiConstraint.T_Y);
	public static final UiConstraint SHIFT_LEFT = new RelativeConstraint(UiConstraint.T_X, -1, UiConstraint.T_W);
	public static final UiConstraint SHIFT_DOWN = new RelativeConstraint(UiConstraint.T_Y, -1, UiConstraint.T_H);
	public static final UiConstraint SHIFT_RIGHT = new RelativeConstraint(UiConstraint.T_X, 1, UiConstraint.T_W);
	public static final UiConstraint SHIFT_UP = new RelativeConstraint(UiConstraint.T_Y, 1, UiConstraint.T_H);
	public static final UiConstraint ALIGN_LEFT = new AbsoluteConstraint(UiConstraint.T_X, 0);
	public static final UiConstraint ALIGN_BOTTOM = new AbsoluteConstraint(UiConstraint.T_Y, 0);
	public static final UiConstraint PARENT_WIDTH = new RelativeConstraint(UiConstraint.T_W, 1, UiConstraint.P_W);
	public static final UiConstraint PARENT_HEIGHT = new RelativeConstraint(UiConstraint.T_H, 1, UiConstraint.P_H);
	public static final UiConstraint SHIFT_PARENT_WIDTH = new RelativeConstraint(UiConstraint.T_X, 1, UiConstraint.P_W);
	public static final UiConstraint SHIFT_PARENT_HEIGHT = new RelativeConstraint(UiConstraint.T_Y, 1, UiConstraint.P_H);
	public static final UiConstraint ALIGN_RIGHT = new CompoundConstraint(UiConstraint.SHIFT_PARENT_WIDTH, UiConstraint.SHIFT_LEFT);
	public static final UiConstraint ALIGN_TOP = new CompoundConstraint(UiConstraint.SHIFT_PARENT_HEIGHT, UiConstraint.SHIFT_DOWN);
	
	private static HashMap<Float, UiConstraint> WIDTH_ASPECT_MAP = new HashMap<Float, UiConstraint>();
	private static HashMap<Float, UiConstraint> HEIGHT_ASPECT_MAP = new HashMap<Float, UiConstraint>();
	
	public static UiConstraint WIDTH_ASPECT(float ratio) {
		Float key = new Float(ratio);
		if (WIDTH_ASPECT_MAP.containsKey(key)) return WIDTH_ASPECT_MAP.get(key);
		UiConstraint newAspect = new RelativeConstraint(UiConstraint.T_W, ratio, UiConstraint.T_H);
		WIDTH_ASPECT_MAP.put(key, newAspect);
		return newAspect;
	}
	public static UiConstraint HEIGHT_ASPECT(float ratio) {
		Float key = new Float(ratio);
		if (HEIGHT_ASPECT_MAP.containsKey(key)) return HEIGHT_ASPECT_MAP.get(key);
		UiConstraint newAspect = new RelativeConstraint(UiConstraint.T_H, ratio, UiConstraint.T_W);
		HEIGHT_ASPECT_MAP.put(key, newAspect);
		return newAspect;
	}
	
	public UiConstraint() {}
	
	public int toChange;
	
	public abstract void adjustTransform(float[] transforms, boolean[] isSet);
	
	public abstract boolean validTransform(boolean[] isSet);
}
