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

