package student;

// @TODO - add actual package names

import game.EscapeState;
import game.ExplorationState;
import game.NodeStatus;
import game.Node;
import game.Edge;
import java.util.stream.*;

import java.util.*;

// import java.util.Collection;
// import java.util.Iterator;

public class Explorer {

    private TreeNode treeRoot;

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {

        TreeNode treeRoot    = new TreeNode(state.getCurrentLocation());
        TreeNode currentNode = treeRoot;
        Map<Long, TreeNode> visitedNodes = new HashMap<Long, TreeNode>();

        this.treeRoot = treeRoot;
        int distance  = state.getDistanceToTarget();

        while (distance > 0) {
            // Get neighbours ordered by their distance from the target
            List<NodeStatus> neighbours = state.getNeighbours().stream().sorted((a, b) -> Integer.compare(a.getDistanceToTarget(), b.getDistanceToTarget())).collect(Collectors.toList());

            // Get iterators for the neighbours collection
            Iterator<NodeStatus> neighbourIterator = neighbours.iterator();
            // Get the existing branches on the currentNode
            Map<Long, TreeNode> existingBranches   = currentNode.getBranches();

            while (neighbourIterator.hasNext()) {
                NodeStatus neighbour      = neighbourIterator.next();
                boolean    alreadyVisited = visitedNodes.containsKey(neighbour.getId());

                // If we have already been to the current branch...
                if (existingBranches.containsKey(neighbour.getId()) || this.allNeighboursVisited(neighbours, visitedNodes)) {
                    // ...and we have been to all the neighbours then we should go to the parent
                    if (this.allNeighboursVisited(neighbours, visitedNodes)) {
                        state.moveTo(currentNode.getParent().getId());
                        currentNode = currentNode.getParent();
                        break;
                    } 
                }

                /**
                 * If the current ID is not the parent and the node ID is not currently
                 * in the list of visited nodes then move to that node. 
                 */
                if (currentNode.getParent() == null || currentNode.getParent().getId() != neighbour.getId() && !alreadyVisited) {
                    // Turn neighbour into a TreeNode
                    TreeNode branch = new TreeNode(neighbour.getId());
                    // Add the current node as the parent
                    branch.setParent(currentNode);
                    // Add the neighbour as a branch of the currentNode
                    currentNode.addBranch(neighbour.getId(), branch);
                    // Move to the current neighbour
                    state.moveTo(neighbour.getId());
                    visitedNodes.put(neighbour.getId(), branch);
                    // Make the currentNode the neighbour branch
                    currentNode = branch;
                    distance    = neighbour.getDistanceToTarget();
                    break;
                }
            }
        }

        return;
    }

    private boolean allNeighboursVisited(Collection<NodeStatus> neighbours, Map<Long, TreeNode> visitedNodes) {
        Iterator<NodeStatus> iterator = neighbours.iterator();

        while (iterator.hasNext()) {
            NodeStatus neighbour = iterator.next();

            if (!visitedNodes.containsKey(neighbour.getId())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        
        Node endingNode       = state.getExit();
        Node startingNode     = state.getCurrentNode();
        boolean endingReached = false;

        LinkedList<Node> queue = new LinkedList<Node>();
        Map<Long, EscapeNode> visited = new HashMap<Long, EscapeNode>();

        queue.add(startingNode);
        EscapeNode escapeNode = new EscapeNode(startingNode);
        visited.put(startingNode.getId(), escapeNode);

        // Try a breath first traversal to get the shortest path
        // Need to track the nodes we have been too so we can construct the shortest path
        while (!endingReached && !queue.isEmpty()) {
            Node current = queue.poll();

            Set<Node> neighbours = current.getNeighbours();
            Set<Node> unvisitedNeighbours = neighbours.stream().filter(w -> {
                return !visited.containsKey(w.getId());
            }).collect(Collectors.toSet());

            Iterator<Node> unvisitedNeighboursIterator = unvisitedNeighbours.iterator();

            while (!endingReached && unvisitedNeighboursIterator.hasNext()) {
                Node neighbour = unvisitedNeighboursIterator.next();
                EscapeNode escapeNeighbour = new EscapeNode(neighbour, current);
                visited.put(neighbour.getId(), escapeNeighbour);

                queue.add(neighbour);

                if (neighbour.getId() == endingNode.getId()) {
                    endingReached = true;
                }
            }
        }

        // Now loop through and build the path
        List<Node> pathway = new ArrayList<Node>();
        pathway.add(endingNode);
        boolean pathwayComplete = false;
        Node n = endingNode;

        while (!pathwayComplete) {
            Node previous = visited.get(n.getId()).getParent();

            if (previous == null || previous.getId() == startingNode.getId()) {
                pathwayComplete = true;
            } else {
                n = previous;
                pathway.add(n);
            }
        }

        Collections.reverse(pathway);
        Iterator<Node> pathwayIterator = pathway.iterator();

        while (pathwayIterator.hasNext()) {
            Node next = pathwayIterator.next();
            state.moveTo(next);
            // state.pickUpGold();
        }

        return;
    }
}
