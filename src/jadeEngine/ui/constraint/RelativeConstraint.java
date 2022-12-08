package ui.constraint;

public class RelativeConstraint extends UiConstraint{
	
	private int relativeTo;
	private float multi;

	public RelativeConstraint(int toChange, float multi, int relativeTo) {
		assert toChange >= 4:"can not modify parent value";
		this.toChange = toChange;
		this.multi = multi;
		this.relativeTo = relativeTo;
	}
	
	public void adjustTransform(float[] transforms, boolean[] isSet) {
		transforms[this.toChange] = transforms[this.relativeTo]*multi;
		isSet[this.toChange] = true;
	}

	public boolean validTransform(boolean[] isSet) {
		return isSet[this.relativeTo];
	}
	
}
