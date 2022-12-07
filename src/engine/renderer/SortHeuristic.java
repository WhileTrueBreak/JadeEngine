package engine.renderer;

import engine.gameobject.GameObject;

public interface SortHeuristic {
	float heuristic(GameObject obj);
}
