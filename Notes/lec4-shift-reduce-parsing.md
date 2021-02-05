# Lec 4: Shift Reduce Parsing
## Pushdown Automata - Bottom Up
### I. What is that
- Consists of
    - Pushdown stack (can have terminals and nonterminals)
    - Finite state automaton control
- Can do one of three actions (based on state and input):
    - Shift:
        - Shift current input symbol from input onto stack
    - Reduce:
        - If symbols on top of stack match RHS of some grammar production NT → 
        - Pop symbols () off of the stack
        - Push LHS nonterminal (NT) onto stack
    - Accept the input string
### II. Potential Conflicts - Ambiguity in Grammar
- Reduce/Reduce Conflict
    - Top of the stack may match RHS of multiple productions
    - Which production to use in the reduction?
- Shift/Reduce Conflict
    - Stack may match RHS of production
    - But that may not be the right match
    - May need to shift an input and later find a different reduction
## Constructing a Parser
- We will construct version with no lookahead
- Key Decisions
    - Shift or Reduce
    - Which Production to Reduce
- Basic Idea
    - Build a DFA: control shift and reduce actions
    - In effect, grammar -> pushdown automaton
    - Encode finite state control in parse table
### I. Parser State
- Input Token Sequence ($ for end of input)
- Current State from Finite State Automaton
- Two Stacks
    - State Stack (implements finite state automaton)
    - Symbol Stack (terminals from input and nonterminals from reductions)
### II. Integrating Finite State Control
- Actions
    - Push Symbols and States Onto Stacks
    - Reduce According to a Given Production
    - Accept
- Selected action is a function of
    - Current input symbol
    - Current state of finite state control
- Each action specifies next state
- Implement control using parse table
## Parse Table
### I. what is pt
- Implements finite state control
- At each step, look up
    - Table[top of state stack] [ input symbol]
- Then carry out the action
### II. shift to sn
- Push input token into the symbol stack
- Push sn into state stack
- Advance to next input symbol
### III. reduce(n)
- Pop both stacks as many times as the number of symbols on the RHS of rule n
- Push LHS of rule n into symbol stack
- Look up
- Table[top of the state stack][top of symbol stack]
- Push that state (in goto part of table) onto state stac
### IV. accept
- Stop parsing and report success
## Key Concepts
- Pushdown automaton for parsing
    - Stack, Finite state control
    - Parse actions: shift, reduce, accept
- Parse table for controlling parser actions
    - Indexed by parser state and input symbol
    - Entries specify action and next state
    - Use state stack to help control
- Parse tree construction
    - Reads input from left to right
    - Bottom-up construction of parse tree
## Parser Generators and Parse Tables
- Parser generator (YACC, CUP)
- grammar -> (shift-reduce) parser for that grammar
- Process grammar to synthesize a DFA
    - Contains states that the parser can be in
    - State transitions for terminals and non-terminals
- Use DFA to create an parse table
- Use parse table to generate code for parser
### I. DFA States Based on Items
capture how much of a given production we have scanned so far
### II. Items
each state is an item
### III. Closure() of a set of items
- Closure finds all the items in the same “state”
- Fixed Point Algorithm for Closure(I)
    - Every item in I is also an item in Closure(I)
    - If A→  • B  is in Closure(I) and B→ •  is an item, then add B→ •  to Closure(I)
    - Repeat until no more new items can be added to Closure(I)
### IV. Goto() of a set of items
- Goto finds the new state after consuming a grammar symbol while at the current state
- Algorithm for Goto(I, X) where I is a set of items and X is a grammar symbol
- Goto(I, X) = Closure( { A→  X •  | A→  • X  in I })
- goto is the new set obtained by “moving the dot” over X
### V. DFA states
- Start with the item S → •  $
- Create the first state to be Closure({ S → •  $})
- Pick a state I
    - for each item A→  • X  in I
        - If there exists an edge X from state I to state J, then add Goto(I,X) to J
        - Otherwise make a new state J, add edge X from state I to state J, and add Goto(I,X) to J
- Repeat until no more additions possible
### VI. Creating the Parse Tables
- For each state
    - Transition to another state using a terminal symbol is a shift to that state (shift to sn)
    - Transition to another state using a non-terminal is a goto to that state (goto sn)
    - If there is an item A →  • in the state do a reduction with that production for all terminals (reduce k)
### VII. Potential Problem
- No lookahead
- Vulnerable to unnecessary conflicts
    - `Shift/Reduce Conflicts` (may reduce too soon in some cases)
    - `Reduce/Reduce Conflicts`
- Solution: Lookahead
    - Only for reductions - reduce only when next symbol can occur after nonterminal from production 
    - Systematic lookahead, split states based on next symbol, action is always a function of next symbol
    - Can generalize to look ahead multiple symbols
### VIII. Creating parse tables with reduction-only lookahead
- For each state
    - Transition to another state using a terminal symbol is a shift to that state (shift to sn) (same as before)
    - Transition to another state using a non-terminal is a goto that state (goto sn) (same as before)
    - If there is an item X →  • in the state do a reduction with that production whenever the current input symbol T may follow X in some derivation (more precise than before)
- Eliminates useless reduce actions
### IX. More General Lookahead
- Items contain potential lookahead information, resulting in more states in finite state control
- Item of the form [A →  •  T] says
    - The parser has parsed an 
    - If it parses a  and the next symbol is T
    - Then parser should reduce by A →  
- In addition to current parser state, all parser actions are function of lookahead symbols
### X. Terminology
AB(C)
1. A: Left to Right, Right to Left
2. B: Leftmost, Rightmost
3. k: number of lookahead characters
### XI. Summary
- Parser generators – given a grammar, produce a parser
- Standard technique
    - Automatically build a pushdown automaton
    - Obtain a shift-reduce parser
        - Finite state control plus push down stack
        - Table driven implementation
- Conflicts: Shift/Reduce, Reduce/Reduce
- Use of lookahead to eliminate conflicts
    - SLR parsing (eliminates useless reduce actions)
    - LR(k) parsing (lookahead throughout parser)
## LR(1) Parser
### I. Follow() sets in SLR parsing
Follow(A) is the set of terminals that can come after A in some derivation\
- $  Follow(S ), where S is the start symbol
- If A → B  is a production then First()  Follow(B )
- If A → B is a production then Follow(A)  Follow(B )
- If A → B  is a production and  derives  then Follow(A)  Follow(B)
### II. Algorithm for Follow
```
for all nonterminals NT
    Follow(NT) = {}
Follow(S ) = { $ }
while Follow sets keep changing
    for all productions A → B 
        Follow(B ) = Follow(B )  First()
        if ( derives ) Follow(B ) = Follow(B )Follow(A )
    for all productions A → B
        Follow(B) = Follow(B)Follow(A)
```
### III. LR(1) Items
- Items will keep info on
    - production
    - right-hand-side position (the dot)
    - look ahead symbol
- LR(1) item is of the form [A →  •  T]
    - A →   is a production
    - The dot in A →  •  denotes the position
    - T is a terminal or the end marker ($)
### IV. Creating a LR(1) Parser Engine
- Need to define Closure() and Goto() functions for LR(1) items
- Need to provide an algorithm to create the DFA
- Need to provide an algorithm to create the parse table
#### A. Closure
#### B. Goto
#### C. Building the LR(1) DFA
- Start with the item [<S’> → • <S> $ I]
    - I irrelevant because we will never shift $
- Find the closure of the item and make an state
- Pick a state I
    - for each item [A→  • X  c] in I
        - find Goto(I, X)
        - if Goto(I, X) is not already a state, make one
        - Add an edge X from state I to Goto(I, X) state
- Repeat until no more additions possible
#### D. Creating the parse tables
- For each LR(1) DFA state
    - Transition to another state using a terminal symbol is a shift to that state (shift to sn)
    - Transition to another state using a non-terminal symbol is a goto that state (goto sn)
    - If there is an item [A →  • a] in the state, action for input symbol a is a reduction via the production A →  (reduce k)
### V. LALR(1) Parser
- Motivation
    - LR(1) parse engine has a large number of states
    - Simple method to eliminate states
- If two LR(1) states are identical except for the look ahead symbol of the items Then Merge the states
- Result is LALR(1) DFA
- Typically has many fewer states than LR(1)
- May also have more reduce/reduce conflicts

