# Lec 7: Unoptimized Code Generation
## Control Flow Graph
### I. What is CFG?
- Nodes Represent Computation
    - Each Node is a Basic Block
    - Basic Block is a Sequence of Instructions with
- No Branches Out Of Middle of Basic Block
- No Branches Into Middle of Basic Block
- Basic Blocks should be maximal
    - Execution of basic block starts with first instruction
    - Includes all instructions in basic block
- Edges Represent Control Flow
### II. Basic Block Construction
- Start with instruction control-flow graph
- Visit all edges in graph
- Merge adjacent nodes if
    - Only one edge from first node
    - Only one edge into second node
### III. Program Points, Split and Join
Points
- One program point before and after each statement in program
- Split point has multiple successors 
    - conditional branch statements only split points
- Merge point has multiple predecessors
- Each basic block
    - Either starts with a merge point or its predecessor ends with a split point
    - Either ends with a split point or its successor starts with a merge point
### IV. Short-Circuit Conditionals
- In program, conditionals have a condition written as a boolean expression
```
i < n && (v[i] != 0 || i > k)
```
- Semantics say should execute only as much as required to determine condition
    - Evaluate `v[i] != 0` only if `i < n` is true
    - Evaluate `i > k` only if `i < n && v[i] != 0` is false
- Use control-flow graph to represent this shortcircuit evaluation
### V. Routines for Destructuring Program Representation
- destruct(n)
    - generates lowered form of structured code represented by n
    - returns (b,e) - b is begin node, e is end node in destructed form
- shortcircuit(c, t, f)
    - generates short-circuit form of conditional represented by c
    - if c is true, control flows to t node
    - if c is false, control flows to f node
    - returns b - b is begin node for condition evaluation
- new kind of node - nop node
### VI. Linearizing CFG to Assembler
- Generate labels for edge targets at branches
    - Labels will correspond to branch targets
    - Can use code generation patterns for this
- Emit code for procedure entry
- Emit code for basic blocks
    - Emit code for statements/conditional expressions
    - Appropriately linearized
    - Jump/conditional jumps link basic blocks together
- Emit code for procedure exit
## Computation