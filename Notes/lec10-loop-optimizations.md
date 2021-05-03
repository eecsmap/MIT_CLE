# Lecture 10: Loop Optimizations
## Definition
### I. Dominators
1. n dominates m if all paths from start to m go through n. Then, if n dominates all predecessors of m, n dominates m, vice versa.
2. if d1 and d2 both dominates m, then either (a) d1 dominates d2; (b) d2 dominates d1
### II. Dominator Algorithm
```
D(n0) = {n0}
for n in N - {n0}; do D(n) = N; endfor
while D changes do
    for n Belongs to N - {n0}
        D(n) = {n} UNION (INTERSECTION of all D(p) where p is n's predecessor)
```
### III. Defining a Loop
1. `header` - unique entry point
2. at least one path back to headert
3. find edges which head dominates tail
    - these edges are `back edge` of loops
    - given `back edge` n -> d
    - loop consists of n, d, plus all nodes can reach n without going through d (all nodes between d and n)
    - d is `loop header`
### IV. Loop Construction Algorithm
```
insert(m)
    if m not in loop
        loop.add(m)
        stack.push(m)
construct_loop(d, n)
    loop = {d}; stack = {}; insert(n);
    while not stack.empty():
        m = stack.pop()
        for all p that is predecessor of m:
            insert(p)
```
### V. nested loop
1. two loops dont have the same header
    - disjoint
    - one loop contains the other
2. two loops have the same header, typically unioned and treated as one loop
### VI. loop preheader
put one node before loop header for optimization code reordering
## Loop-Invariant Code Motion
### I. detecting loop invariant code
1. a statement is loop invariant if:
    - constant
    - having all reachign definitions outside loop
    - having exactly one reaching definition, and that definition comes from an invariant statement
2. exit node: successor is outside loop
### II. algorithm to find invariant code
```
mark all not invariant
while (new invariant mark):
    for all statement():
        if satisfy definition in I:
            mark invariant
```
### III. conditions to move statement `x = y + z` into loop header
1. s dominates all exit nodes of loop (or definition of x from s reaches no use outside loop)
2. no other statements assign value to x
3. no use of x in loop is defined by other definitions of s
## Induction Variable Elimination
### I. what is an Induction Variable
1. Base Induction Variable - of form `i = i +/- c`
2. Derived Induction Variables - value is a linear function of of a base induction variable
### II. Three Algorithms
#### A. Detection of Invariant Variables
```
scan loop to find all base inductino variables
while (new induction variables):
    scan loop to find k = j * b where j is <i, c, d>, make k <i, c * b, d * b>
    scan loop to find k = j +/- b where j is <i, c, d>, make k <i, c, d +/- b>
```
#### B. Strength Reduction for Derived Induction Variables
```
for all induction variables j <i, c, d>
    create s, replace assignment to j with j = s
    after i += e, immediately insert s += c * e (c * e is constant)
    place s in family of i with <i, c, d>
    insert `s = i * c + d` to preheader
```
#### C. Elimination of Superfluous Induction Variables
```
choose a base induction variable that:
    only uses of i:
        termination condition of form i < n
        assignment of the form i += n
choose a derived induction variable k with <i, c, d>
    replace termination condition with k < c * n + d
```
