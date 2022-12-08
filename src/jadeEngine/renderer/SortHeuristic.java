package jadeEngine.renderer;

import jadeEngine.gameobject.GameObject;

public interface SortHeuristic {
	float heuristic(GameObject obj);
}
