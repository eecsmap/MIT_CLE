# Lecture 9: Introduction to Dataflow Analysis
## Reaching Definitions
### I. overview
- A definition reaches a use if
    - value written by definition
    - may be read by use
- Is a use of a variable a constant?
    - Check all reaching definitions
    - If all assign variable to same constant
    - Then use is in fact a constant
- Can replace variable with constant
### II. Computing Reaching Definitions
- Compute with sets of definitions
    - represent sets using bit vectors
    - each definition has a position in bit vector
- At each basic block, compute
    - definitions that reach start of block
    - definitions that reach end of block
- Do computation by simulating execution of
program until reach fixed point
### III. Formalizing Analysis
- Each basic block has
    - IN - set of definitions that reach beginning of block
    - OUT - set of definitions that reach end of block
    - GEN - set of definitions generated in block
    - KILL - set of definitions killed in block
- GEN[s = s + a*b; i = i + 1;] = 0000011
- KILL[s = s + a*b; i = i + 1;] = 1010000
- Compiler scans each basic block to derive GEN and KILL sets
### IV. Dataflow Equations
- IN[b] = OUT[b1] U ... U OUT[bn]
    - where b1, ..., bn are predecessors of b in CFG
- OUT[b] = (IN[b] - KILL[b]) U GEN[b]
- IN[entry] = 0000000
- Result: system of equations
### V. Solving Equations
- Use fixed point algorithm
- Initialize with solution of OUT[b] = 0000000
- Repeatedly apply equations
    - IN[b] = OUT[b1] U ... U OUT[bn]
    - OUT[b] = (IN[b] - KILL[b]) U GEN[b]
- Until reach fixed point
- Until equation application has no further effect
- Use a worklist to track which equation applications may have a further effect
### VI. Algorithm
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
### VII. Questions
- Does the algorithm halt?
    - yes, because transfer function is monotonic
    - if increase IN, increase OUT
    - in limit, all bits are 1
- If bit is 0, does the corresponding definition ever reach basic block?
- If bit is 1, is does the corresponding definition always reach the basic block?
## Available Expressions
### I. overview
- An expression x+y is available at a point p if
    - every path from the initial node to p must evaluate x+y before reaching p,
    - and there are no assignments to x or y after the evaluation but before p.
- Available Expression information can be used t do global (across basic blocks) CSE
- If expression is available at use, no need to reevaluate it
### II. computing available expressions
- Represent sets of expressions using bit vectors
- Each expression corresponds to a bit
- Run dataflow algorithm similar to reaching definitions
- Big difference
    - definition reaches a basic block if it comes from ANY predecessor in CFG
    - expression is available at a basic block only if it is available from ALL predecessors in CFG
### III. Formalizing Analysis
- Each basic block has
    - IN - set of expressions available at start of block
    - OUT - set of expressions available at end of block
    - GEN - set of expressions computed in block
    - KILL - set of expressions killed in in block
- GEN[x = z; b = x+y] = 1000
- KILL[x = z; b = x+y] = 1001
- Compiler scans each basic block to derive GEN and KILL sets
### IV. Dataflow Equations
- IN[b] = OUT[b1] ∩ ... ∩ OUT[bn]
    - where b1, ..., bn are predecessors of b in CFG
- OUT[b] = (IN[b] - KILL[b]) U GEN[b]
- IN[entry] = 0000
- Result: system of equations
### VI. Solving Equations
- Use fixed point algorithm
- IN[entry] = 0000
- Initialize OUT[b] = 1111
- Repeatedly apply equations
    - IN[b] = OUT[b1] ∩ ... ∩ OUT[bn]
    - OUT[b] = (IN[b] - KILL[b]) U GEN[b]
- Use a worklist algorithm to reach fixed point
### VII. Algorithm
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
### VIII. Questions
- Does algorithm always halt?
- If expression is available in some execution, is it always marked as available in analysis?
- If expression is not available in some execution, can it be marked as available in analysis?
### IX. Duality in Two Algorithms
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
- A variable v is live at point p if
    - v is used along some path starting at p, and
    - no definition of v along the path before the use.
- When is a variable v dead at point p?
    - No use of v on any path from p to exit node, or
    - If all paths from p redefine v before using v.
### II. What use is Liveness Information
- Register allocation.
    - If a variable is dead, can reassign its register
- Dead code elimination.
    - Eliminate assignments to variables not read later.
    - But must not eliminate last assignment to variable (such as instance variable) visible outside CFG.
    - Can eliminate other dead assignments.
    - Handle by making all externally visible variables live on exit from CFG
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
