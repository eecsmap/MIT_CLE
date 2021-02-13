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
### III. Program Points, Split and Join Points
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
### I. Overview of Computation
- Loads data from memory into registers
- Computes on registers
- Stores new data back into memory
- Flow of control determines what happens
- Role of compiler:
    - Orchestrate register usage
    - Generate low-level code for interfacing with machine
#### A. Typical Memory Layout
```
Dynamic             Heap
0x800 0000 0000
Stack               Local variables 
                    Temporaries 
                    Some parameters
Data                Global Variables 
                    Read-only constants
Text                Program
0x40 0000
Unmapped
0x0
```
#### B. Concept of An Object File
- The object file has:
    - Multiple Segments
    - Symbol Information
    - Relocation Information
- Segments
    - Global Offset Table
    - Procedure Linkage Table 
    - Text (code)
    - Data
    - Read Only Data
- To run program, OS reads object file, builds executable process in memory, runs process
- We will use assembler to generate object files
#### C. Basic Compilation Tasks
- Allocate space for global variables (in data segment)
- For each procedure
    - Allocate space for parameters and locals (on stack) 
    - Generate code for procedure
- Generate procedure entry prolog
- Generate code for procedure body 
- Generate procedure exit epilog
### II. Generate Code For Procedure Body
- Flatten expressions
    - Read program variables into temps before use 
    - Use temps to have all ops of form
```
temp1 = temp2 op temp3
temp1 = temp2[temp3]
if (temp1 op temp2) 
while (temp1 op temp2)
```
- For unoptimized code generation, apply code generation templates/patterns to flattened expressions
#### A. Allocate space for parameters/locals
- Each parameter/local has its own slot on stack 
- Each slot accessed via %rbp negative offset
- Iterate over parameter/local descriptors
- Assign a slot to each parameter/local
##### Questions
- Why allocate activation records on a stack?
```
LIFO, caller lives longer than callee
```
- Why not statically preallocate activation records?
```
can you determine the parameter value before executing the code?
```
- Why not dynamically allocate activation records in the heap?
```
also can, but need to maintain a stack-like data structure
```
### III. Generate procedure entry prologue
- Push base pointer (%rbp) onto stack
- Copy stack pointer (%rsp) to base pointer (%rbp)
- Decrease stack pointer by activation record size
- All done by:
```
enter <stack frame size in bytes>, <lexical nesting level> 
enter $48, $0
```
- For now (will optimize later) move parameters to slots in activation record (top of call stack)
```
movq %rdi, -24(%rbp)
```
#### A. x86 Register Usage
- 64 bit registers (16 of them)
```
%rax, %rbx, %rcx, %rdx, %rdi, %rsi, %rbp, %rsp, %r8-%r15
```
- Stack pointer %rsp, base pointer %rbp 
- Parameters
    - First six integer/pointer parameters in `%rdi, %rsi, %rdx, %rcx, %r8, %r9`
    - Rest passed on the stack 
- Return value
    - 64 bits or less in %rax
    - Longer return values passed on the stack
##### Questions
- Why have %rbp if also have %rsp?
```
we need two numbers to decide the range of a frame (begin, end), in this case, rbp is the begin, rsp is the end
```
- Why not pass all parameters in registers?
```
registers are limited lol
```
- Why not pass all parameters on stack?
```
pass by register is way faster than pass by stack (which is in memory)
```
- Why not pass return value in register(s) regardless of size?
```
actually it will be ok if we want to pass 2 registers, but it may be slower cuz every time you need to check 2 register value even if the return value is very small.
```
- Why not pass return value on stack regardless of size?
```
for small size, directly return in register is faster 
```
#### B. Callee vs caller save registers
- Registers used to compute values in procedure
- Should registers have same value after procedure as before procedure?
    - Callee save registers (must have same value) %rsp, %rbx, %rbp, %r12-%r15
    - Caller save registers (procedure can change value) %rax, %rcx, %rdx, %rsi, %rdi, %r8-%r11
- Why have both kinds of registers?
### IV. Generate procedure call epilogue
- Put return value in %rax 
```
mov -32(%rbp), %rax
```
- Undo procedure call
    - Move base pointer (%rbp) to stack pointer (%rsp) 
    - Pop base pointer from caller off stack into %rbp 
    - Return to caller (return address on stack)
    - All done by
```
leave 
ret
```
### V. Procedure Linkage
- Pre-call:
    - Save caller-saved registers 
    - Set up arguments
        - Registers (1-6)
        - Stack (7-N) Prolog:
- Push old frame pointer 
    - Save callee-saved registers
    - Make room for parameters, temporaries, and locals
- Epilog:
    - Restore callee-saved registers 
    - Pop old frame pointer
    - Store return value
- Post-return:
    - Restore caller-saved registers 
    - Pop arguments
### VI. Generate code for procedure body
Evaluate expressions with a temp for each subexpression
#### A. Evaluating Expression Trees
##### Flat List Model
- The idea is to linearize the expression tree
- Left to Right Depth-First Traversal of the expression tree
    - Allocate temporaries for intermediates (all the nodes of the tree)
- New temporary for each intermediate
- All the temporaries on the stack (for now) 
- Each expression is a single 3-addr op
    - x = y op z
    - Code generation for the 3-addr expression
- Load y into register %rax
- Perform op z, %rax
- Store %rax to x
- Another option
- Load y into register %rax 
- Load z into register %r10 
- Perform op %r10,%rax
- Store %rax to x
#### B. Issues in Lowering Expressions
- Map intermediates to registers? 
    - registers are limited
- When the tree is large, registers may be insufficient => allocate space in the stack
- Veryinefficient
    - too many copies
    - don’t worry, we’ll take care of them in the optimization passes
    - keep the code generator very simple
#### C. Basic Ideas
- Temps, locals, parameters all have a “home” on stack •When compute, use %rax as working storage
- All subexpressions are computed into temps
- For each computation in expression
    - Fetch first operand (on stack) into %rax
    - Apply operator to second operand (on stack) and %rax 
    - Result goes back into %rax
    - Store result (in %rax) back onto stack
#### D. Implementation
1. Accessing an array element
2. Array bounds checks (performed before array access)
3. Control Flow via comparisons and jumps
### VII. Code For Conditional Branch in CFG
- Each basic block has a label
- Each conditional branch in CFG has
    - True edge (goes to basic block with label LT) 
    - False edge (goes to basic block with label LF)
- Emitted code for CFG tests condition 
    - If true, jump to LT
    - If false, jump to LF
- Emit all basic blocks (in some order), jumps link everything together
#### A. Quick Peephole Optimization
- Emitted code can look something like: 
```
jmp .BasicBlock0
.BasicBlock0:
```
- In this case can remove jmp instruction
## Guidelines for the code generator
- Lower the abstraction level slowly
    - Do many passes, that do few things (or one thing)
    - Easier to break the project down, generate and debug
- Keep the abstraction level consistent
    - IR should have ‘correct’ semantics at all time
    - At least you should know the semantics
    - You may want to run some of the optimizations between the passes.
- Write sanity checks, consistency checks, use often
- Do the simplest but dumb thing
    - it is ok to generate 0 + 1*x + 0*y
    - Code is painful to look at; let optimizations improve it
- Make sure you know want can be done at...
    - Compile time in the compiler
    - Runtime using generated code
- Remember that optimizations will come later
    - Let the optimizer do the optimizations
    - Think about what optimizer will need and structure your code accordingly
    - Example: Register allocation, algebraic simplification, constant propagation
- Setup a good testing infrastructure
    - regression tests
- If a input program creates a bug, use it as a regression test
    - Learn good bug hunting procedures
- Example: binary search , delta debugging
## Assembly language
### I. Overview
#### A. pros and cons
- Advantages
    - Simplifies code generation due to use of symbolic instructions and symbolic names
    - Logical abstraction layer
    - Multiple Architectures can describe by a single assembly language => can modify the implementation
- macro assembly instructions
- Disadvantages
    - Additional process of assembling and linking 
    - Assembler adds overhead
#### B. Relocatable and Absolute
- Relocatable machine language (object modules) 
    - all locations(addresses) represented by symbols
    - Mapped to memory addresses at link and load time
    - Flexibility of separate compilation
- Absolute machine language
    - addresses are hard-coded
    - simple and straightforward implementation
    - inflexible -- hard to reload generated code
    - Used in interrupt handlers and device drivers
### II. Heap
#### A. Dynamic Memory Allocation
```
typedef struct { int x, y; } PointStruct, *Point; 
Point p = malloc(sizeof(PointStruct));
```
- What does allocator do?
    - returns next free big enough data block in heap
    - appropriately adjusts heap data structures
#### B. Some Heap Data Structures
1. Free List (arrows are addresses)
2. Powers of Two Lists
#### C. Getting More Heap Memory
- Scenario: Current heap goes from 0x800 0000 000 - 0x810 0000 0000 
    - Need to allocate large block of memory
    - No block that large available
- Solution: Talk to OS, increase size of heap (sbrk) 
    - Allocate block in new heap
##### Question:
Why use a stack? Why not use the heap or pre-allocated in the data segment?
### III. Stack
- Calling: Caller
    - Assume %rcx is live and is caller save
    - Call foo(A, B, C, D, E, F, G, H, I)
    - A to I are at -8(%rbp) to -72(%rbp)
- Calling: Callee
    - Assume %rbx is used in the function and is callee save
    - Assume 40 bytes are required for locals
- Arguments
- Call foo(A, B, C, D, E, F, G, H, I)
    - Passed in by pushing before the call
    - Access A to F via registers
    - or put them in local memory
    - Access rest using 16+xx(%rbp)
- Locals and Temporaries
    - Calculate the size and allocate space on the stack `sub $48, %rsp` or `enter $48, 0`
    - Access using -8-xx(%rbp)
```
mov -28(%rbp), %r10 
mov %r11, -20(%rbp)
```
- Returning Callee
    - Assume the return value is the first temporary
    - Restore the caller saved register
    - Put the return value in %rax
    - Tear-down the call stack
- Returning Caller
- Assume the return value goes to the first temporary
    - Restore the stack to reclaim the argument space
    - Restore the caller save registers
    - Save the return value
#### Question:
- Do you need the $rbp?
- What are the advantages and disadvantages of having $rbp?
## Expressions
- Expressions are represented as trees
    - Expression may produce a value
    - Or, it may set the condition codes (boolean exprs)
- How do you map expression trees to the machines? 
    - How to arrange the evaluation order?
    - Where to keep the intermediate values?
- Two approaches 
    - Stack Model
    - Flat List Model
### I. Stack model
- 1
    - Eval left-sub-tree
    - Put the results on the stack
- 2
    - Eval right-sub-tree
    - Put the results on the stack
- 3
    - Get top two values from the stack 
    - perform the operation OP
    - put the results on the stack
- Very inefficient!
### II. Flat List Model
see above
### III. What about statements?
- Assignment statements are simple
    - Generate code for RHS expression
    - Store the resulting value to the LHS address
- But what about conditionals and loops?
## Generation of Control Flow
### I. Two Techniques
- Template Matching
- Short-circuit Conditionals
- Both are based on structural induction
    - Generate a representation for the sub-parts
    - Combine them into a representation for the whole
### II. Template Matching
#### A. Template for conditionals
```
if (test) 
    true_body
else 
    false_body
```
```
    <do the test> 
    joper lab_true 
    <false_body> 
    jmp lab_end
lab_true: 
    <true_body>
lab_end:
```
#### B. Template for while loops
```
while (test) 
    body
```
```
lab_cont:
    <do the test>
    joper lab_body
    jmp lab_end 
lab_body:
    <body>
    jmp lab_cont 
lab_end:
```
An optimized template
```
lab_cont:
    <do the test>
    joper lab_end
    <body>
    jmp lab_cont 
lab_end:
```
