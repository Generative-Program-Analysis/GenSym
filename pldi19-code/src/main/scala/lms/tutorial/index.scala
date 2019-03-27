/**

Tutorials and Documentation
===========================

This tutorial series introduces _Lightweight Modular Staging_ (LMS), a framework for
runtime code generation in Scala.

Outline:
<div id="tableofcontents"></div>


Quick Start
-----------

These docs are a collection of literate Scala files. Clone the [GitHub repo](http://github.com/scala-lms/tutorials):

    git clone https://github.com/scala-lms/tutorials.git

Check the [`README.md`](https://github.com/scala-lms/tutorials/blob/master/README.md) file for prerequisites and start hacking!


### Why Generative Programming ?

Because it enables **abstraction without regret**: the key idea is to write very high-level
and generic programs that generate specialized and extremely fast low-level code at runtime.

Programming abstractions that would usually be avoided in performance-sensitive code
(objects, type classes, higher-order functions) can be used **during generation** without
affecting performance of the **generated code**.


### Why LMS ?

Because it is **lightweight** and **modular**: LMS is just a Scala library. With types, it distinguishes
expressions that are evaluated _now_ (type `T`) vs _later_ (type `Rep[T]`).

LMS comes with **batteries included**: it provides many optimizations such as common subexpression elimination out of the box, and it goes beyond purely generative approaches by
providing an extensible intermediate representation that can be used to implement
sophisticated domain-specific compiler pipelines.

At the same time LMS is **hackable**: since it is just a library, all aspects can be
modified or extended to suit a particular purpose.



Self-Contained Tutorials
------------------------

The following tutorials are available:

- [Getting Started](start.html)
    Rep[T] vs T

- [Shonan HMM Challenge](shonan.html)
    Sparse matrix vector multiplication
    Selective unrolling and precomputation

- [Regular Expressions](regex.html)
    From interpreters to compilers using staging

- [Ackermann's Function](ack.html)
    From recursive functions to automata
    Automatic specialization using staging

- [Automata-Based Regex Matcher](automata.html)
    NFA to DFA conversion using staging

- [SQL Engine](query.html)
    Efficient data processing

- [Fast Fourier Transform (FFT)](fft.html)
    Numeric kernels and rewriting optimizations

- [Sliding Stencil](stencil.html)
    Rearranging loop shapes


<!--
LMS In-Depth
------------

<div class="alert alert-danger" role="alert"> <strong>TODO:</strong> flesh out </div>


### Getting Started

- Why generative programming?
    _Because abstraction without regret!_
- Why LMS?
    _Because batteries included and hackable!_
- Hellow World


### Design Patterns

Purely generative:

- Specialization on static data
- Abstractions in the meta-language
- From interpreter to compiler
- Mixed-stage data structures
- Type classes and generic programming

Including IR:

- DSLs with multiple abstraction levels


### From Zero to LMS

- Program generation with Strings
- Program generation with Trees
- Program generation with Graphs


### LMS Internals

- Graph IR
- Effects
- Code Motion
- Transformers
- Code Generators

Advanced:

- Functions
- Structs


### Integration

- LMS for Scala
- LMS for JavaScript
- LMS for C

Advanced:

- LMS for Scala Macros
- LMS for Vector Intrinsics
- LMS for GPUs
- LMS for FPGAs
- LMS for SMT Solvers
- LMS for Truffle / Graal
-->

LMS In Depth
------------

The material below has been extracted from Tiark's PhD thesis ([PDF](http://infoscience.epfl.ch/record/180642/files/EPFL_TH5456.pdf)) and is still undergoing 
revision for the web.

1. [About generative programming](01_overview.html)
1. [Generative programming and LMS basics](02_basics.html)
1. [Intermediate representation and optimizations](03_compiler.html)
1. [Staging and LMS at work](04_atwork.html)

*/
