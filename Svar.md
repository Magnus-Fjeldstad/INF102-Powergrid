# Answer File - Semester 2
# Description of each Implementation
Briefly describe your implementation of the different methods. What was your idea and how did you execute it? If there were any problems and/or failed implementations please add a description.

## Task 1 - mst
*I got my idea from the lecuture describing the minimal spanning three. My first implementation was to use the prim's algortihm, i got it to work, but i didnt quite understand the algortihm so i opted to use the Kruskals algorithm instead. I found it more intuative and easier to understand since it uses a sorted list of weighted edges and chooses the edge with minimal weight whitout starting from a root or arbitrary node. I used the UnionFindBySize class which was given/shown to us in one of the lectures and modified it slightly to work more seamlessly with my code.*

## Task 2 - lca
*In task 2 i´ve used dfs to create a map for the node and its parent. The dfs traverses from the given Root untill it gets to its target node. It creates a parentmap in the traversal proscess.Then starting from node u, the code backtracks using the parentMap and stores all the ancestors of u in a set called ancestorsU. Then Starting from node v, the code backtracks using the parentMap and checks at each step if the current node is an ancestor of u (by checking its presence in ancestorsU).
The first ancestor of v that is also an ancestor of u is the LCA.*

## Task 3 - addRedundant
*My implementation Uses BFS to map each of the subtrees from the root node. Then i calculate a score based on the lowest nodes and the degree of the node. The two largest subtrees gets put into my getDeepestNodeWithMostChildren method. In this method i calculate the two best nodes to add an edge between. I got my idea from the picture in the task. I figured that the best edge to add was the lowest node in the two biggest subtrees, then i though tat it wasent best to put the edge between the two lowest, but the lowest node with the most children.*


# Runtime Analysis
For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented any helper methods you must add these as well.**

* ``mst(WeightedGraph<T, E> g)``: O(m log m)
    * The `mst` method finds the Minimum Spanning Tree of a weighted graph using Kruskal's algorithm. The edges of the graph are first copied to a list in (O(m) time. These edges are then sorted in `(O(m log m))` time. The algorithm uses a disjoint set data structure initialized in (O(n)) time. The main loop iterates through the sorted edges and, for each edge, performs union and find operations on the disjoint set data structure in `(O(α(n))` time, where `(α(n))` is the inverse Ackermann function. Since `(α(n))` is almost constant for all practical input sizes, the dominant term in the time complexity is the sorting step, making the overall time complexity of the method `(O(m log m))`.

* ``lca(Graph<T> g, T root, T u, T v)``: O(n)
    * *The `lca` method finds the Lowest Common Ancestor (LCA) of two nodes `u` and `v` in a tree represented by a graph. It does this by performing a Depth-First Search (DFS) traversal of the tree from the `root` node. During the DFS traversal, a `parentMap` is built to keep track of the parent of each node. The DFS traversal itself has a time complexity of `(O(n))`, where `(n)` is the number of nodes in the tree. The while loops that follow have a worst-case time complexity of `(O(n))` each, as they iterate through the height of the tree. Therefore, the overall time complexity of the method is `(O(n))`.*

    
* ``addRedundant(Graph<T> g, T root)``: O(?)
    * *Insert description of why the method has the given runtime*






