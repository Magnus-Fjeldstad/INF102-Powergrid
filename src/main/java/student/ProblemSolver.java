package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import graph.*;

public class ProblemSolver implements IProblem {

    public <V, E extends Comparable<E>> ArrayList<Edge<V>> mst(WeightedGraph<V, E> g) { // O(M log M)
        ArrayList<Edge<V>> result = new ArrayList<>(); // O(1)

        ArrayList<Edge<V>> edges = new ArrayList<>(); // O(1)
        for (Edge<V> edge : g.edges()) { // O(M)
            edges.add(edge); // O(1)
        }

        Collections.sort(edges, g); // O(M log M)

        UnionFindBySize<V> ds = new UnionFindBySize<>(g.vertices()); // O(N)

        for (Edge<V> edge : edges) { // O(M)
            V set1 = ds.find(edge.a); // O(α(N))
            V set2 = ds.find(edge.b); // O(α(N))

            if (!set1.equals(set2)) {
                result.add(edge); // O(1)
                ds.union(set1, set2); // O(α(N))
            }

            if (result.size() == g.numVertices() - 1) { // O(1)
                break; // O(1)
            }
        }

        return result; // O(1)
    }

    // @Override
    // public <V> V lca(Graph<V> g, V root, V u, V v) {
    // HashMap<V, V> parentMapU = dfsPathToTarget(g, root, u);
    // HashMap<V, V> parentMapV = dfsPathToTarget(g, root, v);

    // HashMap<V, Boolean> pathFromU = new HashMap<>(); //O(1)
    // V current = u; //O(1)

    // while (current != null) {//O(n)
    // pathFromU.put(current, true); //O(1)
    // current = parentMapU.get(current); //O(1)
    // }

    // current = v;
    // while (current != null) { //O(n)
    // if (pathFromU.containsKey(current)) { //O(1)
    // return current;
    // }
    // current = parentMapV.get(current); //O(1)
    // }

    // return null;
    // }
    public <V> V lca(Graph<V> g, V root, V u, V v) {
        Map<V, V> parentMap = new HashMap<>();
        dfs(g, root, null, parentMap);

        Set<V> ancestorsU = new HashSet<>();
        Set<V> ancestorsV = new HashSet<>();

        V currentNode = u;
        while (currentNode != null) {
            ancestorsU.add(currentNode);
            currentNode = parentMap.get(currentNode);
        }

        currentNode = v;
        while (currentNode != null) {
            if (ancestorsU.contains(currentNode)) {
                return currentNode;
            }
            ancestorsV.add(currentNode);
            currentNode = parentMap.get(currentNode);
        }

        return null;
    }

    private static <V> void dfs(Graph<V> g, V node, V parent, Map<V, V> parentMap) { // Total: O(n)

        parentMap.put(node, parent); // O(1)

        for (V neighbor : g.neighbours(node)) { // O(n)
            if (!parentMap.containsKey(neighbor)) {
                dfs(g, neighbor, node, parentMap);
            }
        }
    }

    private <V> HashMap<V, V> dfsPathToTarget(Graph<V> g, V root, V target) {
        HashMap<V, V> parentMap = new HashMap<>(); // O(1)
        Stack<V> stack = new Stack<>(); // O(1)

        stack.push(root); // O(1)
        parentMap.put(root, null); // O(1)

        while (!stack.isEmpty()) {
            V current = stack.pop();

            if (current.equals(target)) {
                return parentMap;
            }

            for (V neighbour : g.neighbours(current)) {
                if (!parentMap.containsKey(neighbour)) {
                    stack.push(neighbour);
                    parentMap.put(neighbour, current);
                }
            }
        }
        return parentMap;
    }

    @Override
    public <V> Edge<V> addRedundant(Graph<V> g, V root) {
        LinkedList<Graph<V>> trees = getLargestSubTrees(g, root);

        V node1 = getNodeFromTree(trees.poll());
        V node2 = getNodeFromTree(trees.poll());

        if (node1 != null && node2 != null) {
            return new Edge<>(node1, node2);
        }
        if (node1 != null) {
            return new Edge<>(root, node1);
        }
        if (node2 != null) {
            return new Edge<>(root, node2);
        } else {
            throw new IllegalStateException("No subtrees found");
        }
    }

    private <V> V getNodeFromTree(Graph<V> tree) {
        if (tree != null) {
            return getDeepestNodeWithMostChildren(tree);
        }
        return null;
    }

    private <V> LinkedList<Graph<V>> getLargestSubTrees(Graph<V> g, V root) {
        Graph<V> largestSubtree = null;
        Graph<V> secondLargestSubtree = null;

        for (V node : g.neighbours(root)) {
            Graph<V> currentSubtree = buildSubTreeFromNode(node, g, root);

            if (largestSubtree == null || currentSubtree.numVertices() > largestSubtree.numVertices()) {
                secondLargestSubtree = largestSubtree;
                largestSubtree = currentSubtree;
            } else if (secondLargestSubtree == null
                    || currentSubtree.numVertices() > secondLargestSubtree.numVertices()) {
                secondLargestSubtree = currentSubtree;
            }
        }

        LinkedList<Graph<V>> result = new LinkedList<>();
        if (largestSubtree != null) {
            result.add(largestSubtree);
        }
        if (secondLargestSubtree != null) {
            result.add(secondLargestSubtree);
        }

        return result;
    }

    private <V> Graph<V> buildSubTreeFromNode(V startNode, Graph<V> g, V root) {
        Graph<V> subTree = new Graph<>();
        subTree.addVertex(startNode);

        Set<V> visited = new HashSet<>();
        PriorityQueue<V> queue = new PriorityQueue<>();

        visited.add(startNode);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            V current = queue.poll();
            for (V neighbor : g.neighbours(current)) {
                if (!current.equals(neighbor) && !visited.contains(neighbor) && !current.equals(root)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    subTree.addVertex(neighbor);
                    subTree.addEdge(current, neighbor);
                }
            }
        }
        return subTree;
    }

    private static <V> V getDeepestNodeWithMostChildren(Graph<V> graph) {
        Map<V, Integer> depthMap = new HashMap<>();
        Map<V, Integer> scoreMap = new HashMap<>();
        PriorityQueue<V> queue = new PriorityQueue<>();

        V root = graph.getFirstNode();
        queue.add(root);
        depthMap.put(root, 0);
        scoreMap.put(root, graph.degree(root));

        int maxDepth = 0;

        while (!queue.isEmpty()) {
            V currentNode = queue.poll();
            for (V neighbour : graph.neighbours(currentNode)) {
                if (!depthMap.containsKey(neighbour)) {
                    int depth = depthMap.get(currentNode) + 1;
                    depthMap.put(neighbour, depth);
                    scoreMap.put(neighbour, scoreMap.get(currentNode) + graph.degree(neighbour));
                    queue.add(neighbour);
                    if (depth > maxDepth) {
                        maxDepth = depth;
                    }
                }
            }
        }

        V bestNode = null;
        int maxScore = 0;

        for (Map.Entry<V, Integer> entry : depthMap.entrySet()) {
            if (entry.getValue() == maxDepth - 1) {
                int currentScore = scoreMap.get(entry.getKey());
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    bestNode = entry.getKey();
                }
            }
        }

        return bestNode;
    }
}
