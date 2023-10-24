package student;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import graph.*;

public class ProblemSolver implements IProblem {

    @Override
    public <V, E extends Comparable<E>> ArrayList<Edge<V>> mst(WeightedGraph<V, E> g) {
        ArrayList<Edge<V>> mstEdges = new ArrayList<>();
        HashSet<V> visited = new HashSet<>();

        PriorityQueue<Edge<V>> priorityQueue = new PriorityQueue<>(
                (e1, e2) -> g.getWeight(e1.a, e1.b).compareTo(g.getWeight(e2.a, e2.b)));

        V startVertex = g.getFirstNode();
        visited.add(startVertex);

        for (Edge<V> edge : g.adjacentEdges(startVertex)) {
            priorityQueue.add(edge);
        }

        while (!priorityQueue.isEmpty()) {
            Edge<V> currentEdge = priorityQueue.poll();

            if (visited.contains(currentEdge.a) && visited.contains(currentEdge.b)) {
                continue;
            }

            mstEdges.add(currentEdge);
            V newVertex = visited.contains(currentEdge.a) ? currentEdge.b : currentEdge.a;
            visited.add(newVertex);

            for (Edge<V> edge : g.adjacentEdges(newVertex)) {
                if (!visited.contains(edge.a) || !visited.contains(edge.b)) {
                    priorityQueue.add(edge);
                }
            }
        }

        return mstEdges;
    }

    @Override
    public <V> V lca(Graph<V> g, V root, V u, V v) {
        HashMap<V, V> parentMap = dfs(g, root);

        HashSet<V> ancestorsOfU = new HashSet<>();

        V current = u;
        while (current != null) {
            ancestorsOfU.add(current);
            current = parentMap.get(current);
        }

        current = v;
        while (current != null) {
            if (ancestorsOfU.contains(current)) {
                return current;
            }
            current = parentMap.get(current);
        }

        return null;
    }

    // Non recursive dfs
    private <V> HashMap<V, V> dfs(Graph<V> g, V root) {
        HashMap<V, V> parentMap = new HashMap<>();
        HashSet<V> visited = new HashSet<>();
        Stack<V> stack = new Stack<>();

        stack.push(root);
        visited.add(root);
        parentMap.put(root, null);

        while (!stack.isEmpty()) {
            V current = stack.pop();
            for (V neighbour : g.neighbours(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    stack.push(neighbour);
                    parentMap.put(neighbour, current);
                }
            }
        }
        return parentMap;
    }

    @Override
    public <V> Edge<V> addRedundant(Graph<V> g, V root) {
        LinkedList<Graph<V>> Trees = biggestSubTreeList(g, root);

        Graph<V> firstBiggestTree = Trees.poll();
        Graph<V> secondBiggestTree = Trees.poll();

        V node1 = null;
        V node2 = null;

        if (firstBiggestTree != null) {
            node1 = getDeepestNodeWithMostNeighbours(firstBiggestTree);
        }

        if (secondBiggestTree != null) {
            node2 = getDeepestNodeWithMostNeighbours(secondBiggestTree);
        }

        Edge<V> edge;
        if (node1 != null && node2 != null) {
            // Both subtrees exist
            edge = new Edge<V>(node1, node2);
        } else if (node1 != null) {
            // Only the first subtree exists
            edge = new Edge<V>(root, node1);
        } else if (node2 != null) {
            // Only the second subtree exists
            edge = new Edge<V>(root, node2);
        } else {
            // No subtrees exist, handle this case as necessary
            throw new IllegalStateException("No subtrees found");
        }
        return edge;
    }

    // fester til groveste foreldrer
    private static <V> V getDeepestNodeWithMostNeighbours(Graph<V> graph) {
        Map<V, Integer> depthMap = new HashMap<>();// map med dybden til en node
        Queue<V> queue = new LinkedList<>();
        V root = graph.getFirstNode();// rooten
        queue.add(root);// legger til root i køen
        depthMap.put(root, 0);// root har dybde 0

        int maxDepth = 0; // max depth settes til 0

        // bryr oss bare om dybden
        while (!queue.isEmpty()) {// imens køen ikke er tom
            V currentNode = queue.poll();// henter og fjerner frøste node fra kjøen
            for (V neighbour : graph.neighbours(currentNode)) {// for naboene til current
                if (!depthMap.containsKey(neighbour)) {// hvis naboen ikke finnes i depthMap
                    int depth = depthMap.get(currentNode) + 1;// dybden plusses med 1
                    depthMap.put(neighbour, depth);// legger til naboen og dybden i hashmap
                    queue.add(neighbour);// legger til naboen i køen
                    if (depth > maxDepth) {// om dybden er større en max
                        maxDepth = depth; // blir max den gitte dybden
                    }
                }
            }
        }

        V champNode = null;// seiers node
        int maxNeighbours = 0;// highscore variabel

        for (Map.Entry<V, Integer> entry : depthMap.entrySet()) {// entry gjør det lett å iterere over
            if (entry.getValue() == maxDepth - 1) {// bare noder som har max dybde, minus 1 så får jeg foreldre
                int currentNeighbourCount = graph.degree(entry.getKey());// teller naboene til en gitt node
                if (currentNeighbourCount > maxNeighbours) {// hvis currentGradtall er større en maxGradtall
                    maxNeighbours = currentNeighbourCount;// settes currentGradtall til maxGradtall
                    champNode = entry.getKey();// champNode blir kandidat for noden som skal returneres
                }
            }
        }

        return champNode;
    }

    private <V> LinkedList<Graph<V>> biggestSubTreeList(Graph<V> g, V root) {
        LinkedList<Graph<V>> treeList = new LinkedList<>();
        for (V node : g.neighbours(root)) {
            treeList.add(bfsSubTree(node, g, root));
        }

        treeList.sort(Comparator.comparingInt((Graph<V> graph) -> graph.numVertices()).reversed());

        return treeList;
    }

    // må være feil her
    private <V> Graph<V> bfsSubTree(V startNode, Graph<V> g, V root) {// lagger subtree fra en gitt node. Vil at den
                                                                      // skal finne
        Graph<V> subTree = new Graph<>();// nytt tomt tree
        subTree.addVertex(startNode);// legger til ny root

        Set<V> visited = new HashSet<>();// tomt hashset
        Queue<V> queue = new LinkedList<>();// tom queue

        visited.add(startNode);// legger til startNode i visited
        queue.add(startNode);// legger til startNode i køen

        while (!queue.isEmpty()) {// i mens køen ikke er tom
            V current = queue.poll();// current er køens førstemann

            for (V neighbor : g.neighbours(current)) {// for barna til current
                if (!current.equals(neighbor) && !visited.contains(neighbor) && !current.equals(root)) {// hvis naboene
                                                                                                        // ikke er
                                                                                                        // besøkt
                    visited.add(neighbor);// legg til nabo i besøkt
                    queue.add(neighbor);// legg til nabo i kø

                    subTree.addVertex(neighbor);// legger til noden i treet
                    subTree.addEdge(current, neighbor);// legger til kanten i treet
                }
            }
        }
        return subTree;
    }

}
