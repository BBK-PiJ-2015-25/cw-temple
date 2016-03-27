package student;

import game.Node;

public class EscapeNode {

	private Node node;
	private Node parent;

	EscapeNode(Node node) {
		this.node = node;
		this.parent = null;
	}

	EscapeNode(Node node, Node parent) {
		this.node = node;
		this.parent = parent;
	}

	public Node getNode() {
		return this.node;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
}