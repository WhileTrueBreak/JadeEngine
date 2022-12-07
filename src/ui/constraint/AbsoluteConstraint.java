package ui.constraint;

public class AbsoluteConstraint extends UiConstraint{

	private float value;
	
	public AbsoluteConstraint(int toChange, float value) {
		assert toChange >= 4:"can not modify parent value";
		this.toChange = toChange;
		this.value = value;
	}
	
	@Override
	public void adjustTransform(float[] transforms, boolean[] isSet) {
		transforms[this.toChange] = value;
		isSet[this.toChange] = true;
	}

	@Override
	public boolean validTransform(boolean[] isSet) {
		return true;
	}

}
