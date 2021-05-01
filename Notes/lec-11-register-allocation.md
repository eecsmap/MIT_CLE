# Lecture 11: Register Allocation
## Webs
### I. starting point - def-use chain (DU chain)
1. def and all its reachable uses must be in same web
2. all defs that reach the same use must be in same web
3. uses a union-find algorithm
### II. Usage
1. web is the unit of register allocation
2. if web is allocated to register R / memory M
    - all reads must be from R/M
    - all stores must be to R/M
## Graph Interference
### I. Convex Set and Live Range of a web
1. convex set S: if A and B in S, and C in the path from A to B, C must be in S
2. live range: minimal convex set that includes all defs and uses in web
### II. Interference
1. two webs interfere if their live ranges overlap (nonempty intersection)
2. if two webs interfere, their values must be stored in different registers/memories
## Graph Coloring
### I. Heuristics of Register Coloring
1. N colors in total
2. if degree < N, always can be colored
3. if degree >= N, still may be colorable
### II. Algorithm
1. push nodes that has degree < N to stack
2. when no more nodes with degree < N remained, find a node to spill then remove
3. when empty, pop nodes from stack and color
### III. when color is not enough
1. option a: pick a web and store/load using memory
2. option b: split a web into multiple webs
3. both options need do re-coloring
### IV. Option A: using memory - which web to pick
1. one with degree >= N
2. one with minimal `spill cost`
    - calculate: static approximation
        - suppose 10 times of iteration per loop
        - calculate sum of store/load operations
### V. Option B: split web
1. split the web into multiple webs
2. when each sub-web is ending, store to memory; beginning, load from memory
3. heauristic:
    - identify a program point where the program is not R-colorable (# of webs > N)
    - pick a web that is not used for the largest enclosing background
    - split the web at corresponding edge
    - redo the interference graph
    - try to re-color the graph
4. cost and benifit of splitting
    - cost:
        - proportional to number of times splitted edge has to be crossed dynamically
        - estimated by its loop nesting
    - benifits:
        - increase the colorability
        - can approximate by its degree
    - greedy heuristic:
        - pick the live-range with the highest benifit-to-cost ratio
## More Optimizations
### I. Register Coalescing
1. find copy instructions `si = sj`
2. if sj and si not interfere, combine the webs
3. Pros:
    - similar to copy propagation
    - reduce the number of instructions
- Cons:
    - may increase the degree of combined node
    - a colorable graph may be non-colorable
### II. Register Targeting
1. some registers have special usage (i.e first 6 arguments to function)
2. pre-color those to the right ones to reduce copy instructions
### III. Presplitting of Webs
1. some live-range has very large dead region
2. break up those - pay small cost of splitting, but the graph would be easier to color
3. can find a strategic location to break-up (call-site, large loop)
### IV. Interprocedual Register Allocation
1. saving registers across procedure boundaries can be expensive, especially for those program having many small functions
2. calling conventions are too generic and inefficient
3. customize calling conventions per function by doing interprocedural register allocation
    
