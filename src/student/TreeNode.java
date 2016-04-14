package student;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class TreeNode {

	private long nodeId;
	private TreeNode parent;
	private Map<Long, TreeNode> branches;

	TreeNode (long nodeId) {
		this.nodeId 		  = nodeId;
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