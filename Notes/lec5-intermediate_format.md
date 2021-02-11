# lec 5: Intermediate Format
## High Level IR vs Low Level IR
- High Level IR
    - Preserves Object Structure
    - Preserves Structured Flow of Control
    - Primary Goal: Analyze Program
- Low Level IR
    - Moves Data Model to Flat Address Space
    - Eliminates Structured Control Flow
    - Suitable for Low Level Compilation Tasks
        - Register Allocation
        - Instruction Selection
## Presenting Arrays and Vector
1. array: items store continuously. First word is length
2. vector: First Word points to class information (method table, garbage collector data), next word have object fields
### I. invoking vector add method
- create activation record
    - this onto stack
    - parameters onto stack
## Compilation Tasks
- Determine Format of Objects and Arrays
- Determine Format of Call Stack
- Generate Code to Read Values
    - this, parameters, locals, array elements, object fields
- Generate Code to Evaluate Expressions
- Generate Code to Write Values
- Generate Code for Control Constructs
## Symbol Tables
### I. what is symbol table
- Compiler Uses Symbol Tables to Produce
    - Object Layout in Memory
    - Code to
- Access Object Fields
- Access Local Variables
- Access Parameters
- Invoke Methods
### II. Symbol Tables During Translation
From Parse Tree to IR
- Symbol Tables Map Identifiers (strings) to Descriptors (information about identifiers)
- Basic Operation: Lookup
    - Given A String, find Descriptor
    - Typical Implementation: Hash Table
- Examples
    - Given a class name, find class descriptor
    - Given variable name, find descriptor
- local descriptor, parameter descriptor, field descriptor
### III. Hierarchy In Symbol Tables
- hierarchy comes from
    - nested scopes - local scope inside field scope
    - inheritance - child class inside parent class
- symbol table hierarchy reflects these hierarchies
- lookup proceeds up hierarchy until Descriptor is found
## Descriptors
### I. what is descriptor
- What do descriptors contain?
- Information used for code generation and semantic analysis
    - local descriptors - name, type, stack offset
    - field descriptors - name, type, object offset
    - method descriptors
- signature (type of return value, receiver, and parameters)
- reference to local symbol table
- reference to code for method
### II. Program Symbol Table
- Maps class names to class descriptors
- Typical Implementation: Hash Table
### III. Class Descriptor
- Has Two Symbol Tables
    - Symbol Table for Methods
- Parent Symbol Table is Symbol Table for Methods of Parent Class
    - Symbol Table for Fields
- Parent Symbol Table is Symbol Table for Fields of
Parent Class
- Reference to Descriptor of Parent Class
### IV. Field, Parameter and Local and Type Descriptors
- Field, Parameter and Local Descriptors Refer to Type Descriptors
    - Base type descriptor: int, boolean
    - Array type descriptor, which contains reference to type descriptor for array elements
    - Class descriptor
- Relatively Simple Type Descriptors
- Base Type Descriptors and Array Descriptors
Stored in Type Symbol Table
### V. Method Descriptors
- Contain Reference to Code for Method
- Contain Reference to Local Symbol Table for Local Variables of Method
- Parent Symbol Table of Local Symbol Table is Parameter Symbol Table for Parameters of Method
### VI. Symbol Table Summary
- Program Symbol Table (Class Descriptors)
- Class Descriptors
    - Field Symbol Table (Field Descriptors)
- Field Symbol Table for SuperClass
    - Method Symbol Table (Method Descriptors)
- Method Symbol Table for Superclass
- Method Descriptors
    - Local Variable Symbol Table (Local Variable Descriptors)
- Parameter Symbol Table (Parameter Descriptors)
    - Field Symbol Table of Receiver Class
- Local, Parameter and Field Descriptors
    - Type Descriptors in Type Symbol Table or Class Descriptors

