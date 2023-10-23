package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
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
        UnionFindBySize<V> uf = new UnionFindBySize<>(g.vertices());


        for (Edge<V> edge : g.edges()) {
            uf.union(edge.a, edge.b);
        }


        HashMap<Edge<V>, Integer> impactMap = new HashMap<>();
        for (Edge<V> edge : g.edges()) {
            g.removeEdge(edge);
            if (!uf.same(edge.a, edge.b)) {
                int impact = ((Graph<V>) uf.group(edge.a)).size() + ((Graph<V>) uf.group(edge.b)).size();
                impactMap.put(edge, impact);
            }
            g.addEdge(edge);
        }


        Edge<V> bestEdge = null;
        int minImpact = Integer.MAX_VALUE;
        for (V u : g.vertices()) {
            for (V v : g.vertices()) {
                if (u.equals(v) || g.adjacent(u, v))
                    continue;


                g.addEdge(u, v);
                int maxImpactAfterAdding = 0;
                for (Edge<V> criticalEdge : impactMap.keySet()) {
                    g.removeEdge(criticalEdge);
                    int impact = !uf.same(criticalEdge.a, criticalEdge.b) ? ((Graph<V>) uf.group(criticalEdge.a)).size()
                            + ((Graph<V>) uf.group(criticalEdge.b)).size() : 0;
                    maxImpactAfterAdding = Math.max(maxImpactAfterAdding, impact);
                    g.addEdge(criticalEdge);
                }
                if (maxImpactAfterAdding < minImpact) {
                    minImpact = maxImpactAfterAdding;
                    bestEdge = new Edge<>(u, v);
                }
                g.removeEdge(u, v);
            }
        }
        return bestEdge;
    }

}
