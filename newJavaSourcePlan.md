# Java Static Dependency Graph Analyzer

## Goal

Build a Java 17 static analysis tool that extracts a directed dependency graph between classes from a list of folders containing Java source files.

The analyzer must use the official JDK compiler APIs based on javax.tools.JavaCompiler and the javac semantic model.

The output must be a graph where:

- each node represents a fully qualified class name
- each directed edge represents a direct usage relationship
- anonymous classes are ignored
- all graph references use fully qualified class names

The final result is conceptually equivalent to:

List<List<String>>

Where:

- the first element is the source class
- the remaining elements are outgoing dependencies

Example:

[
  [
    "com.example.A",
    "com.example.B",
    "java.util.List"
  ],
  [
    "com.example.B",
    "com.example.C"
  ]
]

---

# High Level Architecture

The system must be divided into the following stages:

1. Source discovery
2. Java compiler initialization
3. AST parsing
4. Semantic analysis
5. Class extraction
6. Dependency extraction
7. Graph building
8. Graph export
9. Validation and testing

---

# Technical Constraints

## Java Version

Use Java 17.

## Compiler APIs

Use only official JDK compiler APIs.

Required packages:

- javax.tools
- com.sun.source.tree
- com.sun.source.util
- javax.lang.model.element
- javax.lang.model.type

Do not use:

- JavaParser
- Eclipse JDT
- Spoon
- ASM
- Reflection
- Runtime instrumentation

---

# Functional Requirements

The analyzer must detect dependencies created by:

- extends
- implements
- field types
- method return types
- method parameter types
- local variable types
- thrown exception types
- annotations
- generic type arguments
- object instantiation
- static member access
- fully qualified names used directly in code
- nested classes
- enum usage
- record usage

The analyzer must ignore:

- anonymous classes
- lambda synthetic classes
- unresolved symbols
- reflection string literals
- dynamically loaded classes

---

# Expected Output

The final graph must contain:

- one node per concrete class declaration
- outgoing edges representing direct class usage

All names must be fully qualified.

Example:

com.example.service.UserService
  -> com.example.repository.UserRepository
  -> java.util.List
  -> com.example.model.User

---

# Step 1 - Create Project Structure

Create the following package structure under src/main/java/graphbuilderplugins:

src/main/java

... .analyzer
... .discovery
... .compiler
... .graph
... .model
... .scanner
... .export

---

# Step 2 - Create Core Domain Models

Create immutable domain classes.

## SourceClassNode

Represents one class in the graph.

Fields:

- String fqcn
- Set<String> outgoingDependencies

Responsibilities:

- maintain normalized dependency names
- prevent duplicates
- expose read-only collections

---

## DependencyGraph

Represents the entire graph.

Fields:

- Map<String, SourceClassNode>

Responsibilities:

- register nodes
- register edges
- lookup nodes
- export graph

---

# Step 3 - Implement Source File Discovery

Create a recursive file scanner.

Input:

List<Path> sourceFolders

Responsibilities:

- recursively walk folders
- collect all .java files
- ignore hidden folders
- ignore build folders if needed

Use:

Files.walk

Output:

List<Path>

---

# Step 4 - Initialize JavaCompiler

Use ToolProvider.getSystemJavaCompiler.

Create:

- JavaCompiler
- StandardJavaFileManager
- JavacTask

Compiler execution must run in parsing and analysis mode only.

No class generation is required.

Use compiler options:

-proc:none

This disables annotation processing.

---

# Step 5 - Build Compilation Units

Convert discovered files into JavaFileObject instances.

Use:

StandardJavaFileManager.getJavaFileObjectsFromFiles

Then create:

JavacTask

From the task obtain:

- CompilationUnitTree instances
- semantic model access

---

# Step 6 - Parse AST

Call:

parse()

Store all CompilationUnitTree objects.

Each unit represents one source file.

---

# Step 7 - Trigger Semantic Analysis

Call:

analyze()

This is critical.

Without semantic analysis:

- symbols are unresolved
- fully qualified names are unavailable
- type resolution fails

---

# Step 8 - Create Tree Scanner Infrastructure

Create a custom scanner extending:

TreePathScanner<Void, ScanContext>

The scanner is responsible for traversing semantic AST nodes.

Create:

ClassDependencyScanner

---

# Step 9 - Create ScanContext

Create a mutable context object containing:

- current class fqcn
- current compilation unit
- current graph reference
- Trees semantic helper

This avoids global mutable state.

---

# Step 10 - Extract Class Declarations

Override:

visitClass

Ignore:

- anonymous classes
- synthetic classes

Accept:

- top-level classes
- nested classes
- interfaces
- enums
- records

Obtain semantic symbols using:

Trees.getElement

Cast to:

TypeElement

Extract fully qualified name using:

getQualifiedName

Register node in graph.

---

# Step 11 - Ignore Anonymous Classes

Anonymous classes have:

- empty simple names
- synthetic compiler-generated identities

Detection strategy:

if simple name is empty:
  ignore class node creation

Still continue traversal inside the anonymous body if desired.

Do not register graph nodes for anonymous classes.

---

# Step 12 - Extract Inheritance Dependencies

Inside visitClass:

Extract:

- superclass
- implemented interfaces

Resolve each symbol semantically.

Add outgoing edges.

---

# Step 13 - Extract Field Dependencies

Override:

visitVariable

Detect field declarations.

Resolve field type.

Register dependency edge.

Ignore primitives.

Ignore arrays of primitives.

---

# Step 14 - Extract Method Dependencies

Override:

visitMethod

Extract:

- return type
- parameter types
- thrown exception types
- type parameters

Resolve all semantic types.

Register edges.

---

# Step 15 - Extract Local Variable Dependencies

Inside visitVariable:

if variable is local:

resolve type
register dependency

---

# Step 16 - Extract Object Instantiation Dependencies

Override:

visitNewClass

Resolve instantiated type.

Register dependency edge.

---

# Step 17 - Extract Annotation Dependencies

Override:

visitAnnotation

Resolve annotation type.

Register dependency edge.

---

# Step 18 - Extract Generic Dependencies

When processing declared types:

inspect generic type arguments recursively

Example:

List<User>

Must generate dependencies for:

- java.util.List
- com.example.User

Use:

DeclaredType.getTypeArguments

---

# Step 19 - Extract Fully Qualified References

Some code references classes directly:

new com.example.Foo()

Semantic resolution already handles this.

No textual parsing is required.

Always rely on resolved symbols.

---

# Step 20 - Normalize Dependency Names

Before adding dependency edges:

- remove duplicates
- ignore self references
- ignore primitive types
- ignore unresolved symbols
- ignore null names

Always store canonical fully qualified names.

---

# Step 21 - Handle Nested Classes

Nested classes must use canonical names.

Examples:

com.example.Outer.Inner

or JVM binary notation depending on policy.

Choose one format and keep it consistent.

Recommended:

source-style canonical names.

---

# Step 22 - Handle Unresolved Symbols Gracefully

Some projects may not compile completely.

The analyzer must:

- continue processing
- skip unresolved symbols
- log warnings

Never terminate analysis because of one unresolved type.

---

# Step 23 - Build Graph Incrementally

As dependencies are discovered:

graph.addEdge(
  sourceClass,
  targetClass
)

Graph implementation must automatically create missing target nodes if necessary.

---

# Step 24 - Export Graph

Create export module.

Supported outputs:

- JSON
- adjacency list
- plain text

Initial implementation should support adjacency list format.

Example:

com.example.A
  com.example.B
  java.util.List

---

# Step 25 - Create Validation Tests

Prepare a synthetic source project containing:

- inheritance
- generics
- annotations
- inner classes
- records
- enums
- local variables
- nested generics

Expected graph must be asserted exactly.

---

# Step 26 - Performance Requirements

The analyzer must:

- avoid reparsing files
- reuse compiler infrastructure
- avoid duplicate symbol resolution
- store graph in hash-based collections

Target:

analyze several thousand source files efficiently.

---

# Step 27 - Future Extensions

Do not implement now, but design architecture to support:

- Maven dependency resolution
- Gradle dependency resolution
- module-info.java
- reflection heuristics
- bytecode analysis
- method-level dependency graphs
- package-level graphs
- graph visualization
- SCC detection
- cycle analysis
- graph databases

---

# Step 28 - Recommended Internal APIs

Recommended public APIs:

DependencyGraphAnalyzer.analyze

Input:

List<Path>

Output:

DependencyGraph

---

# Step 29 - Recommended Execution Flow

Execution pipeline:

1. discover source files
2. initialize compiler
3. parse AST
4. run semantic analysis
5. scan compilation units
6. build dependency graph
7. export graph

---

# Step 30 - Final Acceptance Criteria

The implementation is considered complete when:

- all source classes are discovered
- all graph nodes use fully qualified names
- outgoing dependencies are semantically resolved
- anonymous classes are ignored
- graph export works
- analysis runs successfully on Java 17 projects
- unresolved symbols do not crash execution
- dependency duplicates are eliminated
