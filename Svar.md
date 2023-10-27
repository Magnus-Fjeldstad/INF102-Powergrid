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


* ``addRedundant(Graph<T> g, T root)``: O(d (n)) or O(n) if ve assume that the degree of the root
    node is not huge.

    * *The addRedudnat method uses two helper methods, that i will write the time complexity for below. `getLargestSubTrees` finds the two largest subtrees from the root. This method has a time complexity of `O(d (n +m)` where d is the degree of the root node and n and m is the number of nodes and edges. Since the graph is a tree like structure `m = n-1` hence we can simplify the time complexity to `O(d (n))`
    
    The `getNodeFromTree` methods uses a helper method `getDeepestNodeWithMostChildren` the helper method has a time complexity of `O(n + m)`. This methods uses a `BFS`like algorithm to find the deepest node that have the most children. It stores the result in two HashMaps, `depthMap`and `scoreMap`. It iterates over alle nodes in the subTree and finds the deepest node with most children.
    Again since the graph is a tree like structure `m = n-1` hence we can simplify the time complexity to `O(n)` 

    The `getNodeFromTree` method is hence `O(n)`.


    Now lets get the time complexity for `getLargestSubTrees`. The method find the neighBours from the root node and build subtrees using a helper method `buildSubTreeFromNode`. 
    Lets first analyse the `buildSubTreeFromNode` method. The `buildSubTreeFromNode` method uses a `BFS` like algorithm to build a subTree from a node. Building a subTree requires a way of mapping each node and edge to a new graph. Hence it iterates over all nodes and edges 
    This results in a time complecity of `O(n+m)`or agian since the grapgh is represented ass a tree we can simplify it down to `O(n)`.

    Now lets continue with the `getLargestSubTrees` method. The goal for the method is to find the two largest subtrees in the main graph. First i initate two empty subtrees. The i find the neighbour nodes from the root. The `buildSubTreeFromNode` method then builds a new subtree from the current node, this is done in `O(n)` time. When a subtree have been build, there is a series of if test that check if the current subtree is larger than already foun subtrees, if so the current subtree is set to "largest or secondLargerst subtree". 

    When all the subtrees have been checked, the resulst are added to a linkedList in `O(1)` time, and the methods return a `LinkedList<Graph<V>> "result"`.

     *






