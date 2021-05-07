# Lecture 12: Fundations of Dataflow Analysis
## Partial Orders
### I. definitions
1. reflexive: `x <= x`
2. asymmetric: `x <= y` and `y <= x` implies `x = y`
3. transitive: `x <= y` and `y <= z` implies `x <= z`
### II. other concepts
1. upper bounds:
2. lower bounds:
3. covering: y covers x if no other elements between x and y
## Lattice
### I. definition
1. if `x meet y` and `x join y` exists for all `x, y belongs to P` => P is a lattice
2. if `meet S` and `join S` exists for all `S belongs to P` => P is a complete lattice
3. all finite lattice are complete
### II. other concepts
1. top: greatest element
2. bottom: least element
### III. algebraic properties of lattices
1. associativity of `meet` and `join`
2. commutativity of `meet` and `join`
3. idempotence of `meet` and `join`
4. absorption of `meet` over `join` (also `join` over `meet`)
## Transfer Functions
### I. definitions
1. must be closed under composition
2. monotone
3. sometimes distributive: `f(x join y) = f(x) join f(y)`
4. distributivity implies monotonicity
## Forward Analysis and Backward Analysis
...
