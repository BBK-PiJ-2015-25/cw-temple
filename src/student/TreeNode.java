package student;

import java.util.*;

// import java.util.List;

public class TreeNode {

	private long nodeId;
	private int distanceToTarget;
	private TreeNode parent;
	private Map<Long, TreeNode> branches;

	TreeNode (long nodeId, int distanceToTarget) {
		this.nodeId 		  = nodeId;
		this.distanceToTarget = distanceToTarget;
		this.parent 		  = null;
		this.branches 		  = new HashMap<Long, TreeNode>();
	}

	public void addBranch(Long id, TreeNode branch) {
		this.branches.put(id, branch);
	}

	public Map<Long, TreeNode> getBranches() {
		return this.branches;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode getParent() {
		return this.parent;		
	}

	public int getDistanceToTarget() {
		return this.distanceToTarget;
	}

	public long getId() {
		return this.nodeId;
	}

	public String childrenToString() {
		String items = "[";

		if (!this.branches.isEmpty()) {
			Set<Long> keys = this.branches.keySet();
			Iterator<Long> iterator = keys.iterator();

			while (iterator.hasNext()) {
				TreeNode node = this.branches.get(iterator.next());
				items += node.getId() + ", ";
			}
		}

		items += "]";

		return items;
	}
}