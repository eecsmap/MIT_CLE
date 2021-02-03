# Lec 2 regex grammer
## Regular Expression are Built From
1. s - alphabet
2. e - empty string
3. r1r2 -> regex sequence
4. r1 | r2 -> regex choice
5. r* iterated sequence and choice e | r | rr ...
6. parentheses to indicate grouping/precedence
## Final State Automaton - Accepting String
Conceptually, run string through automaton
- Have current state and current letter in string
- Start with start state and first letter in string
- At each step, match current letter against a transition
- whose label is same as letter
- Continue until reach end of string or match fails
- If end in accept state, automaton accepts string
- Language of automaton is set of strings it accepts
## DFA vs. NFA
- DFA – only one possible transition at each state
- NFA – may have multiple possible transitions
- 2 or more transitions with same label
- Transitions labeled with empty string
- Rule – string accepted if any execution accepts
- Angelic vs. Demonic nondeterminism
- Angelic – all decisions made to accept
- Demonic – all decisions made to not accept
- NFA uses Angelic nondeterminism
### difference 2
DFA
- No e transitions
- At most one transition from each state for each letter
NFA
- neither restriction
## NFA to DFA
### I. pros vs cons
- DFA makes recognition simpler
- DFA may be exponentially larger than NFA
### II. NFA to DFA construction
1. DFA has a state for each subset of states in NFA
- DFA `start state` corresponds to `set of states` reachable by following `e` transitions from `NFA start state`
- DFA state is an `accept state` if an `NFA accept state` is in its `set of NFA states`
2. To compute the transition for a given DFA state D and letter a
- Set S to empty set
- Find the set N of D’s NFA states
    - For all NFA states n in N
        - Compute set of states N’ that the NFA may be in after matching a
        - Set S to S union N'
- If S is nonempty, there is a transition for a from D to the DFA state that has the set S of NFA states
- Otherwise, there is no transition for a from D
## Lexical Structure in Languages
1. Each language typically has several categories of words. In a typical programming language
- Keywords
- Arithmetic Operations
- Integer numbers
- Floating point numbers
- Identifiers
2. Typically have a lexical category for each keyword and/or each category
3. Each lexical category defined by regexp
### I. Production Game
```
have a current string
start with Start nonterminal
loop until no more nonterminals
    choose a nonterminal in current string
    choose a production with nonterminal in LHS
    replace nonterminal with RHS of production
    substitute regular expressions with corresponding strings
generated string is in language
```
Note: different choices produce different strings
### II. Parse Tree
- Internal Nodes: Nonterminals
- Leaves: Terminals
- Edges:
    - From Nonterminal of LHS of production
    - To Nodes from RHS of production
- Captures derivation of string
### III. Ambiguity in Grammar
1. Grammar is ambiguous if there are multiple derivations (therefore multiple parse trees) for a single string
2. Derivation and parse tree usually reflect semantics of the program
3. Ambiguity in grammar often reflects ambiguity in semantics of language (which is considered undesirable)
### IV. Precedence
- Group Operators into Precedence Levels
    - \* and / are at top level, bind strongest
    - \+ and - are at next level, bind next strongest
- Nonterminal for each Precedence Level
    - Term is nonterminal for * and /
    - Expr is nonterminal for + and -
- Can make operators left or right associative within each level
- Generalizes for arbitrary levels of precedence
### V. Parser
- Converts program into a parse tree
- Can be written by hand
- Or produced automatically by parser generator
    - Accepts a grammar as input
    - Produces a parser as output
- Practical problem
    - Parse tree for hacked grammar is complicated
    - Would like to start with more intuitive parse tree
#### A. Solution
- Abstract versus Concrete Syntax
    - Abstract -> intuitive, ambiguous
    - Concrete -> full grammar
- Parsers are often written to produce abstract syntax trees
## Summary
- Lexical and Syntactic Levels of Structure
    - Lexical – regular expressions and automata
    - Syntactic – grammars
- Grammar ambiguities
    - Hacked grammars
    - Abstract syntax trees
- Generation versus Recognition Approaches
    - Generation more convenient for specification
    - Recognition required in implementation
### I. Grammar Vocabulary
- Leftmost derivation
    - Always expands leftmost remaining nonterminal
    - Similarly for rightmost derivation
- Sentential form
    - Partially or fully derived string from a step in valid derivation
    - 0 + Expr Op Expr
    - 0 + Exp
### II. Defining a Language
- Grammar
    - Generative approach
    - All strings that grammar generates (How many are there for grammar in previous example?)
- Automaton
    - Recognition approach
    - All strings that automaton accepts
### III. Grammar
1. Regular Grammar: states, alphabet, transition function, start state, final state
2. Context-Free Grammar: terminals, nonterminals, start nonterminal, productions, RHS of production can have any sequence of terminals/nonterminals
3. Context-Sensitive Grammar: allow productions to use context: P: (T.NT)+ → (T.NT)*
### IV. Automaton
1. Finite-State Automaton: DFA, NFA
2. Push-Down Automaton: DFA + Stack. states, alphabet, stack alphabet, transition function, start state, final state
3. Turing Machine: Finite State Control, Two-Way Tape Instead of A Stack


