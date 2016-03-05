package student;

import game.EscapeState;
import game.ExplorationState;
import game.NodeStatus;
import game.Node;

import java.util.*;

// import java.util.Collection;
// import java.util.Iterator;

public class Explorer {

    TreeNode treeRoot;
    Map<Long, TreeNode> visitedNodes = new HashMap<Long, TreeNode>();

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

        TreeNode treeRoot    = new TreeNode(state.getCurrentLocation(), state.getDistanceToTarget());
        TreeNode currentNode = treeRoot;

        this.treeRoot = treeRoot;

        // @todo - Take into account the distance from target when deciding which node to move to.

        while (state.getDistanceToTarget() > 0) {
            // Get neighbours
            Collection<NodeStatus> neighbours      = state.getNeighbours();
            // Get iterators for the neighbours collection
            Iterator<NodeStatus> neighbourIterator = neighbours.iterator();
            // Get the existing branches on the currentNode
            Map<Long, TreeNode> existingBranches   = currentNode.getBranches();

            while (neighbourIterator.hasNext()) {
                NodeStatus neighbour      = neighbourIterator.next();
                boolean    alreadyVisited = this.visitedNodes.containsKey(neighbour.getId());

                // If we have already been to the current branch...
                if (existingBranches.containsKey(neighbour.getId()) || this.allNeighboursVisited(neighbours)) {                    
                    // ...and we have been to all the neighbours then we should go to the parent
                    if (this.allNeighboursVisited(neighbours)) {
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
                    TreeNode branch = new TreeNode(neighbour.getId(), neighbour.getDistanceToTarget());
                    // Add the current node as the parent
                    branch.setParent(currentNode);
                    // Add the neighbour as a branch of the currentNode
                    currentNode.addBranch(neighbour.getId(), branch);
                    // Move to the current neighbour
                    state.moveTo(neighbour.getId());
                    this.visitedNodes.put(neighbour.getId(), branch);
                    // Make the currentNode the neighbour branch
                    currentNode = branch;
                    break;
                }
            }
        }

        return;
    }

    private boolean allNeighboursVisited(Collection<NodeStatus> neighbours) {
        Iterator<NodeStatus> iterator = neighbours.iterator();

        while (iterator.hasNext()) {
            NodeStatus neighbour = iterator.next();

            if (!this.visitedNodes.containsKey(neighbour.getId())) {
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
        


    }
}