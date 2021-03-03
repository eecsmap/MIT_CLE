# Lecture 8: Introduction to Program Analysis and Optimization
## Overview
- Compile-time reasoning about run-time behavior of program
    - Can discover things that are always true:
    - Can infer things that are likely to be true:
    - Distinction between data and control-flow properties
- Use analysis results to transform program
- Overall goal: improve some aspect of program
- Traditional goals:
    - Reduce number of executed instructions
    - Reduce overall code size
- Other goals emerge as space becomes more complex
    - Reduce number of cycles
- Use vector or DSP instructions
- Improve instruction or data cache hit rate
    - Reduce power consumption
    - Reduce memory usage
### I. overview
- Common SubExpression Elimination
    - a=(x+y)+z; b=x+y;
    - t=x+y; a=t+z; b=t;
- Constant Propagation
    - x=5; b=x+y;
    - x=5; b=5+y;
- Algebraic Identities
    - a=x*1;
    - a=x;
- Copy Propagation
    - a=x+y; b=a; c=b+z;
    - a=x+y; b=a; c=a+z;
- Dead Code Elimination
    - a=x+y; b=a; b=a+z;
    - a=x+y; b=a+z
- Strength Reduction
    - t=i*4;
    - t=i<<2;
- Assume normalized basic block - all statements are of the form
    - var = var op var (where op is a binary operator)
    - var = op var (where op is a unary operator)
    - var = var
## Common Subexpression Elimination
### I. value numbering
- Original Basic Block
    - a = x+y
    - b = a+z
    - b = b+y
    - c = a+z
- New Basic Block
    - a = x+y
    - t1 = a
    - b = a+z
    - t2 = b
    - b = b+y
    - t3 = b
    - c = t2
- Var to Val
    - x → v1
    - y → v2
    - a → v3
    - z → v4
    - b → v6
    - c → v5
- Exp to Val
    - v1+v2 → v3
    - v3+v4 → v5
    - v5+v2 → v6
- Exp to Val
    - v1+v2 → t1
    - v3+v4 → t2
    - v5+v2 → t3
### II. optimization
1. reduce unused temps
2. use canonicalization to see if expressions are the same
## Copy Propagation
### I. main idea
- Once again, simulate execution of program
- If can, use original variable instead of temporary
    - a=x+y; b=x+y;
    - After CSE becomes a=x+y; t=a; b=t;
    - After CP becomes a=x+y; t=a; b=a;
    - After DCE becomes a=x+y; b=a;
- Key idea:
    - determine when original variable is NOT overwritten between its assignment statement and the use of the computed value
    - If not overwritten, use original variable
### II. Copy Propagation Maps
- tmp to var: tells which variable to use instead of a given temporary variable
- var to set: inverse of tmp to var. tells which temps are mapped to a given variable by tmp to var
## Dead Code Elimination
### I. basic idea
- Process Code In Reverse Execution Order
- Maintain a set of variables that are needed later in computation
- If encounter an assignment to a temporary that is not needed, remove assignment
## Algebraic Simplification
### I. oppotunities for Algebraic Simplification
- In the code
    - Programmers are lazy to simplify expressions
    - Programs are more readable with full expressions
- After compiler expansion
    - Example: Array read A[8][12] will get expanded to
    - *(Abase + 4*(12 + 8*256)) which can be simplified
- After other optimizations
### II. usefulness of Algebraic Simplification
- Reduces the number of instructions
- Uses less expensive instructions
- Enable other optimizations
### III. use knowledge about operators
- Commutative operators
    - a op b = b op a
- Associative operators
    - (a op b) op c = b op (a op c)
### IV. Canonical Format
- Put expression trees into a canonical format
    - Sum of multiplicands
    - Variables/terms in a canonical order
    - Example
        - (a+3)*(a+8)*4  4*a*a+44*a+96
## Summary
### I. interesting properties
- Analysis and Transformation Algorithms Symbolically Simulate Execution of Program
    - CSE and Copy Propagation go forward
    - Dead Code Elimination goes backwards
- Transformations stacked
    - Group of basic transformations work together
    - Often, one transformation creates inefficient code that is cleaned up by following transformations
    - Transformations can be useful even if original code may not benefit from transformation
### II. Other Basic Block Transformations
- Constant Propagation
- Strength Reduction
    - a<<2 = a*4; a+a+a = 3*a;
- Do these in unified transformation framework, not in earlier or later phases
### III. Summary
- Basic block analyses and transformations
- Symbolically simulate execution of program
    - Forward (CSE, copy prop, constant prop)
    - Backward (Dead code elimination)
- Stacked groups of analyses and transformations that work together
    - CSE introduces excess temporaries and copy statements
    - Copy propagation often eliminates need to keep temporary variables around
    - Dead code elimination removes useless code
- Similar in spirit to many analyses and transformations that operate across basic blocks