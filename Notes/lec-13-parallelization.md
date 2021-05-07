# Lecture 13: Parallelization
## Types of Parallelization
1. Instruction Level Parallelism (ILP): scheduling and hardware
2. Task Level Parallelism (TLP): mainly by hand
3. Loop Level Parallelism (LLP) or Data Parallelism: Hand or Compiler Generated
4. Pipeline Parallelism : Hardware or Streaming
5. Divide and Conquer Parallelism: Recursive Functions
## Dependency Analysis
1. a loop has a distance d if there exists a data dependency from iteration i to j and d = j - i
2. if no loop carried dependency (dependency that cross a loop boundary) -> parallizable.
## Data Dependency Analysis
### I. Distance Vector Method
ith loop is parallelizable for all dependence d = [d1, d2, d3...dn] either (a) one d1, d2 ... dn > 0 or (b) all d1, d2, ... dn = 0
### II. Integer Programming