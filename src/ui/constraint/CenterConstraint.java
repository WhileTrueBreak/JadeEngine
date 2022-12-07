package ui.constraint;

public class CenterConstraint extends UiConstraint{
	
	int parentDim, thisDim;
	
	public CenterConstraint(int toChange) {
		assert toChange >= 4:"can not modify parent value";
		assert toChange == UiConstraint.T_X || toChange == UiConstraint.T_Y:"can only modify x or y";
		this.toChange = toChange;
		this.parentDim = toChange - 2;
		this.thisDim = toChange + 2;
	}
	
	@Override
	public void adjustTransform(float[] transforms, boolean[] isSet) {
		transforms[this.toChange] = (transforms[this.parentDim] - transforms[this.thisDim])/2;
		isSet[this.toChange] = true;
	}

	@Override
	public boolean validTransform(boolean[] isSet) {
		return isSet[parentDim] && isSet[thisDim];
	}

}
