package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


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


    /**
     * Since this graph is a tree like structure and no loops or multiple edges are allowed
     * We can represent m as m= n-1 since there will allways be a node at the bottom of the tree
     * Hence we can simplyfy the Big O to O(d (n)) where d is the degree from the root node
     * and n is the number of edges. In my helper methods ive kept the time complexity notation to be
     * O(n+m) for clarity but in reality it could be simplified to O(n).
     */
    @Override
    public <V> Edge<V> addRedundant(Graph<V> g, V root) { //O(d (n))
        LinkedList<Graph<V>> trees = getLargestSubTrees(g, root); // O(d (n + m)) / O(n+m)

        V node1 = getNodeFromTree(trees.poll()); //O(n + m)
        V node2 = getNodeFromTree(trees.poll()); //O(n + m)

        if (node1 != null && node2 != null) { //O(1)
            return new Edge<>(node1, node2); //O(1)
        }
        if (node1 != null) { //O(1)
            return new Edge<>(root, node1); //O(1)
        }
        if (node2 != null) { //O(1)
            return new Edge<>(root, node2); //O(1)
        } else {
            throw new IllegalStateException("No subtrees found"); //O(1)
        }
    }

    /*
     * Helper method that cheks that a tree is not null
     * used in addRedundant
     */
    private <V> V getNodeFromTree(Graph<V> tree) { //O(n + m)
        if (tree != null) { //O(1)
            return getDeepestNodeWithMostChildren(tree); //O(n + m)
        }
        return null; //O(1)
    }

    private <V> LinkedList<Graph<V>> getLargestSubTrees(Graph<V> g, V root) { //O(d (n+m)) it could also be O(n+m) if we
                                                                            //Assume that the root node does not have
                                                                            //a huge degree
        Graph<V> largestSubtree = null; //O(1)
        Graph<V> secondLargestSubtree = null; //O(1)

        for (V node : g.neighbours(root)) { //O(d) where d is the degree of the Root Node
            Graph<V> currentSubtree = buildSubTreeFromNode(node, g, root); //O(n+m)
           
            if (largestSubtree == null || currentSubtree.numVertices() > largestSubtree.numVertices()) { //O(1)
                secondLargestSubtree = largestSubtree; //O(1)
                largestSubtree = currentSubtree; //O(1)
            } else if (secondLargestSubtree == null //O(1)
                    || currentSubtree.numVertices() > secondLargestSubtree.numVertices()) { //O(1)
                secondLargestSubtree = currentSubtree; //O(1)
            }
        }

        LinkedList<Graph<V>> result = new LinkedList<>(); //O(1)
        if (largestSubtree != null) { //O(1)
            result.add(largestSubtree); //O(1)
        }
        if (secondLargestSubtree != null) { //O(1)
            result.add(secondLargestSubtree); //O(1)
        }

        return result; //O(1)
    }

    private <V> Graph<V> buildSubTreeFromNode(V startNode, Graph<V> g, V root) { //O(n+m) 
        Graph<V> subTree = new Graph<>(); //O(1)
        subTree.addVertex(startNode); //O(1)

        Set<V> visited = new HashSet<>(); //O(1)
        PriorityQueue<V> queue = new PriorityQueue<>(); //O(1)

        visited.add(startNode); //O(1)
        queue.add(startNode); //O(log n)

        while (!queue.isEmpty()) { //O(n) nis the number of nodes
            V current = queue.poll(); //O( log n)
            for (V neighbor : g.neighbours(current)) { //O(m)
                if (!current.equals(neighbor) && !visited.contains(neighbor) && !current.equals(root)) {
                    visited.add(neighbor); //O(1)
                    queue.add(neighbor);
                    subTree.addVertex(neighbor); //O(1)
                    subTree.addEdge(current, neighbor); //O(1)
                }
            }
        }
        return subTree; //O(1)
    }

    private static <V> V getDeepestNodeWithMostChildren(Graph<V> graph) { //O(n + m)
        Map<V, Integer> depthMap = new HashMap<>(); //O(1)
        Map<V, Integer> scoreMap = new HashMap<>(); //O(1)
        PriorityQueue<V> queue = new PriorityQueue<>(); //O(1)

        V root = graph.getFirstNode(); //O(1)
        queue.add(root);//O(1)
        depthMap.put(root, 0);//O(1)
        scoreMap.put(root, graph.degree(root)); //O(1)

        int maxDepth = 0; //O(1)

        while (!queue.isEmpty()) { //O(n)
            V currentNode = queue.poll(); //O( log n)
            for (V neighbour : graph.neighbours(currentNode)) { //O(m) since it iterates over all nodes exaclty once
                if (!depthMap.containsKey(neighbour)) { //O(1)
                    int depth = depthMap.get(currentNode) + 1; //O(1)
                    depthMap.put(neighbour, depth); //O(1)
                    scoreMap.put(neighbour, scoreMap.get(currentNode) + graph.degree(neighbour)); //O(1)
                    queue.add(neighbour); //O( log n)
                    if (depth > maxDepth) { //O(1)
                        maxDepth = depth; //O(1)
                    }
                }
            }
        }

        V bestNode = null; //O(1)
        int maxScore = 0; //O(1)

        for (Map.Entry<V, Integer> entry : depthMap.entrySet()) { //O(n) n is the number of nodes
            if (entry.getValue() == maxDepth - 1) { //O(1)
                int currentScore = scoreMap.get(entry.getKey()); //O(1)
                if (currentScore > maxScore) { //O(1)
                    maxScore = currentScore; //O(1)
                    bestNode = entry.getKey(); //O(1)
                }
            }
        }

        return bestNode; //O(1)
    }
    
}
