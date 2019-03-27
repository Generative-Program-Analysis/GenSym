/**

About Generative Programming
============================

Outline:
<div id="tableofcontents"></div>

<!--
Getting Started
===============

This chapter will be about getting started with LMS. We will begin by explaining some background on generative programming, then move on to how to get LMS running on your system and finally how to get it set up to start working with. At the end of this chapter you should understand why LMS is around, why you should use it and you should be all set up to do so.

Outline:
<div id="tableofcontents"></div>

About Generative Programming
----------------------------
-->

### Productivity vs Performance

One of the biggest challenges in software development today is that software performance is increasingly at odds with programmer productivity. This problem is aggravated by a number of trends:

First, processor clock speeds are no longer doubling every 18 months and instead, computer hardware is rapidly becoming more and more parallel, heterogeneous and distributed. Multi-core CPUs, SIMD units and GPUs are commonplace from servers in data centers all the way down to mobile phones. Programmers need to turn to a variety of low-level programming models to make best use of the available hardware resources.

Second, programming languages and methodologies focus increasingly on generality and abstraction, enabling programmers to build large systems from simple but versatile parts. This makes it fundamentally difficult for compilers to translate high-level programs to efficient code, because they do not have the capability to reason about domain-specific operations.

Third, with a shift towards "Big Data", workloads in the cloud, and a proliferation of mobile devices and embedded systems, there is a growing demand for highly efficient software.

These trends cause software development teams to invest large efforts into carefully optimizing their code. From a software engineering perspective, this is disconcerting because manual optimization requires programmers to essentially abandon all best practices and benefits of high-level programming, such as abstraction, generality and modularity. Hand-optimized programs are hard to read, hard to maintain, hard to verify and thus likely to attract bugs and security vulnerabilities.


### Programming Generation and Meta-Programming

As an alternative to counting on a "sufficiently smart" compiler to optimize a program, programmers can write a program generator -- a program that when run, produces the code of the target program as its output. A program generator can perform all kinds of computation while composing the target program, and thus emit highly efficient code.

Generative programming, as a subdomain of meta-programming, describes the practice of writing programs that generate other programs as part of their execution. Generative programming makes a pattern explicit that is found in many programs, namely a division into computational _stages_, separated by frequency of execution or availability of information. Therefore, we also speak of multi-stage programming or _staging_ for short.

Generative programming can overcome the performance and productivity gap by leveraging productivity where performance does not matter and vice versa: Generating and compiling a piece of code is not performance critical but productivity is paramount to make the development of the multi-stage program affordable. The generated code needs to run fast but since it is generated programmatically and not written by humans, programmer productivity is not a concern.


### Generative Programming Patterns

Generative programming enables a number of interesting programming patterns. Among these are mixed-stage data structures that contain both static and dynamic parts. Another pattern is to specialize generated code based on statically available information.

A key application is to turn interpreters into compilers: if we specialize an interpreter to a particular program, we obtain the same effect as compiling the program. Therefore, staging an interpreter, which enables specialization for arbitrary programs, effectively turns this interpreter into a compiler!

The benefit of the staged interpreter pattern is huge. For most languages, an interpreter is almost trivial to write but building a compiler using traditional techniques is a much larger effort by comparison.


### Staging and Extensible Compilers

The staged interpreter pattern can be used to build multi-pass compiler pipelines using generative programming. The key idea is to generated an intermediate representation (IR) instead of target code, and then use a staged IR interpreter to implement a transformation.

Again, expressing transformations as interpretations is often simpler than implementing low-level IR to IR transformers. Thus, generative programming extends in a natural way from one-step code generation to pipelines that perform multiple translation passes between possibly independent intermediate languages.


### Domain-Specific Languages

A key use for such multi-pass compiler pipelines is in implementing highly efficient domain-specific languages (DSLs). DSLs can outperform general purpose languages because they offer more optimization opportunities. But to be effective, DSL compilers need to perform optimizations on multiple abstraction levels.

With compilers based on staging, it is easy to first perform purely domain-specific optimizations, say, on the level of linear algebra operations, before lowering the domain-specific operations and data structures to a more generic representation (say, as arrays and loops) on which a different set of optimizations apply.


<!--
LMS Basics
----------

### Rep Types

### Staging as a Scala library

Hence lightweight (no baked-in language support) and modular (flexible set of staged operations)

### Comparing LMS to other systems

Type safety, evaluation order, multi-pass architecture


Installing LMS
--------------

Clone tutorial repo. Use sbt. Maybe increase memory.


Getting Help
------------

Community, mailing lists, ...


Summary
-------

You should have a basic understanding of what LMS is and how it’s different from the program generation approach you may have been using. You should also now have a working version of LMS on your system. It’s now time to learn some LMS basics.
-->
*/

