# Lecture 9: Introduction to Dataflow Analysis
## Reaching Definitions
### I. Computing Reaching Definitions
- represent sets using bit vectors, each definition has a position in bit vector
- At each basic block, compute start vector and end vector
### II. Formalizing Analysis
- Each basic block has
    - IN - set of definitions that reach beginning of block
    - OUT - set of definitions that reach end of block
    - GEN - set of definitions generated in block
    - KILL - set of definitions killed in block
- GEN[s = s + a*b; i = i + 1;] = 0000011
- KILL[s = s + a*b; i = i + 1;] = 1010000
- Compiler scans each basic block to derive GEN and KILL sets
### III. Dataflow Equations
#### A. initial conditions
- IN[entry] = 0000000
#### B. deriving executions
- IN[b] = OUT[b1] U ... U OUT[bn]
    - where b1, ..., bn are predecessors of b in CFG
- OUT[b] = (IN[b] - KILL[b]) U GEN[b]
- Until reached fixed point
### IV. Algorithm
```
for all nodes n in N
    OUT[n] = emptyset; // OUT[n] = GEN[n];
IN[Entry] = emptyset;
OUT[Entry] = GEN[Entry];
Changed = N - { Entry }; // N = all nodes in graph

while (Changed != emptyset)
    choose a node n in Changed;
    Changed = Changed - { n };

    IN[n] = emptyset;
    for all nodes p in predecessors(n)
        IN[n] = IN[n] U OUT[p];

    OUT[n] = GEN[n] U (IN[n] - KILL[n]);

    if (OUT[n] changed)
        for all nodes s in successors(n)
            Changed = Changed U { s };
```
### V. Questions
- Does the algorithm halt?
    - yes, because transfer function is monotonic
    - if increase IN, increase OUT
    - in limit, all bits are 1
- If bit is 0, does the corresponding definition ever reach basic block?
- If bit is 1, is does the corresponding definition always reach the basic block?
## Available Expressions
### I. overview
- can be used for Global CSE
- differences from Reaching Definitions: Available means available from ALL predecessors, but Reaching definitions can be reached by ANY predecessors.
### II. Dataflow Equations
- IN[b] = OUT[b1] ∩ ... ∩ OUT[bn]
    - where b1, ..., bn are predecessors of b in CFG
- OUT[b] = (IN[b] - KILL[b]) U GEN[b]
- IN[entry] = 0000
- Result: system of equations
### III. Algorithm
```
for all nodes n in N
    OUT[n] = E; // OUT[n] = E - KILL[n];
IN[Entry] = emptyset;
OUT[Entry] = GEN[Entry];
Changed = N - { Entry }; // N = all nodes in graph

while (Changed != emptyset)
    choose a node n in Changed;
    Changed = Changed - { n };

    IN[n] = E; // E is set of all expressions
    for all nodes p in predecessors(n)
        IN[n] = IN[n] ∩ OUT[p];

    OUT[n] = GEN[n] U (IN[n] - KILL[n]);

    if (OUT[n] changed)
        for all nodes s in successors(n)
            Changed = Changed U { s };
```
### IV Duality in Two Algorithms
- Reaching definitions
    - Confluence operation is set union
    - OUT[b] initialized to empty set
- Available expressions
    - Confluence operation is set intersection
    - OUT[b] initialized to set of available expressions
- General framework for dataflow algorithms.
- Build parameterized dataflow analyzer once, use for all dataflow problems
## Liveness
### I. overview
- variable is dead at some point if:
    - No use later, or
    - Redefined later
### II. What use is Liveness Information
- Register allocation. If a variable is dead, can reassign its register
- Dead code elimination.
### III. Conceptual Idea of Analysis
- Simulate execution
- But start from exit and go backwards in CFG
- Compute liveness information from end to beginning of basic blocks
### IV. Formalizing Analysis
- Each basic block has
    - IN - set of variables live at start of block
    - OUT - set of variables live at end of block
    - USE - set of variables with upwards exposed uses in block
    - DEF - set of variables defined in block
- USE[x = z; x = x+1;] = { z } (x not in USE)
- DEF[x = z; x = x+1;y = 1;] = {x, y}
- Compiler scans each basic block to derive USE and DEF sets
### V. Algorithm
```
for all nodes n in N - { Exit }
    IN[n] = emptyset;
OUT[Exit] = emptyset;
IN[Exit] = use[Exit];
Changed = N - { Exit };

while (Changed != emptyset)
    choose a node n in Changed;
    Changed = Changed - { n };

    OUT[n] = emptyset;
    for all nodes s in successors(n)
        OUT[n] = OUT[n] U IN[p];

    IN[n] = use[n] U (out[n] - def[n]);

    if (IN[n] changed)
        for all nodes p in predecessors(n)
            Changed = Changed U { p };
```
### VI. Similar to Other Dataflow Algo
- Backwards analysis, not forwards
- Still have transfer functions
- Still have confluence operators
- Can generalize framework to work for both forwards and backwards analyses
## Summary
### I. Pessimistic vs Optimistic Analysis
- Available expressions is optimistic (for common sub-expression elimination)
    - Assume expressions are available at start of analysis
    - Analysis eliminates all that are not available
    - Cannot stop analysis early and use current result
- Live variables is pessimistic (for dead code elimination)
    - Assume all variables are live at start of analysis
    - Analysis finds variables that are dead
    - Can stop analysis early and use current result
- Dataflow setup same for both analyses
- Optimism/pessimism depends on intended use
### II. Summary
- Basic Blocks and Basic Block Optimizations
    - Copy and constant propagation
    - Common sub-expression elimination
    - Dead code elimination
- Dataflow Analysis
    - Control flow graph
    - IN[b], OUT[b], transfer functions, join points
- Paired analyses and transformations
    - Reaching definitions/constant propagation
    - Available expressions/common sub-expression elimination
    - Liveness analysis/Dead code elimination
- Stacked analysis and transformations work together
