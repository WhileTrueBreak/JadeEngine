package jadeEngine.ui.constraint;

public class CompoundConstraint extends UiConstraint{
	
	private UiConstraint constraint1, constraint2;
	
	public CompoundConstraint(UiConstraint constraint1, UiConstraint constraint2) {
		assert constraint1.toChange == constraint2.toChange:"Compound constraints must change the same value";
		this.toChange = constraint1.toChange;
		this.constraint1 = constraint1;
		this.constraint2 = constraint2;
	}
	
	@Override
	public void adjustTransform(float[] transforms, boolean[] isSet) {
		float prev = transforms[this.toChange];
		constraint1.adjustTransform(transforms, isSet);
		float change = transforms[this.toChange] - prev;
		transforms[this.toChange] = prev;
		constraint2.adjustTransform(transforms, isSet);
		transforms[this.toChange] += change;
	}

	@Override
	public boolean validTransform(boolean[] isSet) {
		return this.constraint1.validTransform(isSet)&&this.constraint2.validTransform(isSet);
	}

}
