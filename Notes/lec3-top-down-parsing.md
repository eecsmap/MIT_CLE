# Lec 3: Top Down Parsing
## Starting Point
- Assume lexical analysis has produced a sequence of tokens
    - Each token has a type and value
    - Types correspond to terminals
    - Values to contents of token read in
### I. Basic Approach
- Start with Start symbol
- Build a leftmost derivation
    - leftmost nonterminal -> choose a production and apply
    - leftmost terminal -> match against input
    - all terminals match -> have found a parse!
    - Key: find correct productions for nonterminals
#### Summary
Three Actions (Mechanisms)
1. Apply production to expand current nonterminal in parse tree
2. Match current terminal (consuming input)
3. Accept the parse as correct\
Parser generates preorder traversal of parse tree
- visit parents before children
- visit siblings from left to right
### II. Backtracking
#### A. Policy Problem
- Which production to use for each nonterminal?
- Classical Separation of Policy and Mechanism
- One Approach: Backtracking
    - Treat it as a search problem
    - At each choice point, try next alternative
    - If it is clear that current try fails, go back to previous choice and try something different
- General technique for searching
- Used a lot in classical AI and natural language processing (parsing, speech recognition)
### III. General Search Issues
- Three components
    - Search space (parse trees)
    - Search algorithm (parsing algorithm)
    - Goal to find (parse tree for input program)
- Would like to (but can't always) ensure that
    - Find goal (hopefully quickly) if it exists
    - Search terminates if it does not
- Handled in various ways in various contexts
    - Finite search space makes it easy
    - Exploration strategies for infinite search space
    - Sometimes one goal more important (model checking)
- For parsing, hack grammar to remove left recursion
#### Eliminating Left Recursion
- Changes search space exploration algorithm
    - Eliminates direct infinite recursion
    - But grammar less intuitive
    - Sets things up for predictive parsing
### V. Predictive Parsing
- Alternative to backtracking
- Useful for programming languages, which can be designed to make parsing easier
- Basic idea
    - Look ahead in input stream
    - Decide which production to apply based on next tokens in input stream
    - We will use one token of lookahead
#### A. Choice Points
Assume Term' is current position in parse tree
- Have three possible productions to apply
```
Term' → * Int Term'
Term' → / Int Term'
Term' → e
```
- Use next token to decide
    - If next token is *, apply Term' → * Int Term'
    - If next token is /, apply Term' → / Int Term'
    - Otherwise, apply Term' → e
### VI. Predictive Parsing + Hand Coding = Recursive Descent Parser
- One procedure per nonterminal NT
    - Productions NT → β1 , …, NT → βn
    - Procedure examines the current input symbol T to determine which production to apply
    - If T ∈ First(βk)
        - Apply production k
        - Consume terminals in βk (check for correct terminal)
        - Recursively call procedures for nonterminals in βk
        - Current input symbol stored in global variable token
- Procedures return
    - true if parse succeeds
    - false if parse fails
### VII. Multiple Productions with Same Prefix in RHS
- Example Grammar
    - NT → if then
    - NT → if then else
- Assume NT is current position in parse tree, and if is the next token
- Unclear which production to apply
    - Multiple k such that T ∈ First(βk)
    - if ∈ First(if then)
    - if ∈ First(if then else)
#### A. Solution: Left Factor the Grammar
New Grammar Factors Common Prefix Into Single Production
```
NT → if then NT'
NT' → else
NT' → e
```
- No choice when next token is if!
- All choices have been unified in one production.
#### B. Nonterminals
- What about productions with nonterminals?
    - NT → NT1 α1
    - NT → NT2 α2
- Must choose based on possible first terminals that NT1 and NT2 can generate
- What if NT1 or NT2 can generate e?
    - Must choose based on α1 and α2
##### Fixed Point Algorithm for Derives e
```
for all nonterminals NT
    set NT derives e to be false
for all productions of the form NT → e
    set NT derives e to be true
while (some NT derives e changed in last iteration)
    for all productions of the form NT → NT1 ... NTn
        if (for all 1 <= i <= n NTi derives e)
            set NT derives e to be true
```
##### First(β)
- T ∈ First(β) if T can appear as the first symbol in a derivation starting from β
1. T ∈ First(T)
2. First(S) ⊆ First(Sβ)
3. NT derives e implies First(β) ⊆ First(NTβ)
4. NT → Sβ implies First(Sβ) ⊆ First(NT)
- Notation
    - T is a terminal, NT is a nonterminal, S is a terminal or nonterminal, and β is a sequence of terminals or nonterminals
## Building A Parse Tree
- Have each procedure return the section of the parse tree for the part of the string it parsed
- Use exceptions to make code structure clean
### I. Why use Hand-Coded Parser?
- Why not use parser generator?
- What do you do if your parser doesn't work?
    - Recursive descent parser – write more code
    - Parser generator
        - Hack grammar
        - But if parser generator doesn't work, nothing you can do
- If you have complicated grammar
    - Increase chance of going outside comfort zone of parser generator
    - Your parser may NEVER work
### II. Bottom Line
- Recursive descent parser properties
    - Probably more work
    - But less risk of a disaster - you can almost always make a recursive descent parser work
    - May have easier time dealing with resulting code
        - Single language system
        - No need to deal with potentially flaky parser generator
        - No integration issues with automatically generated code
- If your parser development time is small compared to rest of project, or you have a really complicated language, use hand-coded recursive descent parser
### III. Summary
- Top-Down Parsing
- Use Lookahead to Avoid Backtracking
- Parser is
    - Hand-Coded
    - Set of Mutually Recursive Procedures