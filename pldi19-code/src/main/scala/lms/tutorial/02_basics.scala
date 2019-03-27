/**


<a name="sec:221"></a>
Generative Programming Basics
=============================

Previous staging approaches either work directly with strings that represent
concrete program syntax or make use of quasiquoting to compose abstract syntax
trees. We examine both approaches in turn, with an eye on how linguistic reuse
improves productivity and safety for the multi-stage programmer.

Outline:
<div id="tableofcontents"></div>





## Program Generation with Strings

As a simple example, let us turn the power function:

    def power(b: Double, n: Int): Double =
      if (n == 0) 1.0 else b * power(b, n - 1)

into a code generator:

    def power(b: String, n: Int): String =
      if (n == 0) "1.0" else "(" + b + " * " + power(b, n - 1) + ")"

As result of an invocation we obtain:

    power("x",4) // "(x * (x * (x * (x * 1.0)))"


However there is a problem: We can produce arbitrary strings that might not be
valid code.  It is very easy to make subtle mistakes:

    def power(b: String, n: Int): String =
      if (n == 0) "1.0" else "b * " + power(b, n - 1) + ")"

We have accidentally omitted a parenthesis, so the result is not syntactically
well formed code. Furthermore, the literal identifier `b` is part of the
output:

    power("x",4) // "b * b * b * b * 1.0)))"

This code will not compile and even if we fix the syntax, the code is no
longer well scoped. The free  identifier `b` can lead to variable capture when
the code is  spliced in somewhere else.

We have seen two problems, syntax correctness and scope correctness.  Two
other problems are type correctness and value correctness. If we cannot
guarantee to generate valid programs, we can much less guarantee that programs
are well-typed or compute correct results.


<a name="sec:220quasi"></a>
## Program Generation with Quasi-Quotes

Strings model concrete syntax, but we can also use abstract syntax. This idea
is inspired by Lisp's "code as data" model.  We start with a slightly more
convenient string notation, denoted by `s"..."` quotes:

    def power(b: String, n: Int): String =
      if (n == 0) s"1.0" else s"($b * ${ power(b, n - 1) })"

The notation `${ ... }` denotes a hole in the string, to be filled by the
string result of evaluating the enclosed expression.

The same idea applies to abstract syntax. Let `[[ ... ]]` denote the AST of
the enclosed expression, and let `Tree` be the type of AST nodes. Holes will
require an expression of type `Tree`:

    def power(b: Tree, n: Int): Tree =
      if (n == 0) [[ 1.0 ]] else [[$b * ${ power(b, n - 1) } ]]"

Now we have a program generator that assembles AST nodes.


## Syntactic Correctness through Deep Reuse of Syntax

The multi-stage language compiler parses the whole program and builds  ASTs
for all expressions, quoted or not, at once. Thus we obtain syntax
correctness. However the multi-stage language compiler must know about the
syntax of the object language. This is trivial if meta-language and object
language are the same. Otherwise it is slightly more difficult
[(*)](mainland07quasiquoting).

The `Tree` type can be left abstract. Some implementations  hide the exact
data structures to guarantee safety of optimizations on object code. Silently
modifying trees with rewrites that  maintain semantic but not structural
equality (e.g. beta reduction) can change the behavior of programs that
inspect the tree structure [(*)](DBLP:conf/pepm/Taha00). In general,
optimizations should not change the result of a program.

## Scope Correctness through Deep Reuse of Scope

The multi-stage compiler can bind identifiers at the definition site of the
quote. This avoids variable capture and ensures scope correctness (hygiene).

Another possible issue is _scope extrusion_. This happens when a variable
bound in quoted code escapes through a hole:

    var x: Tree;
    $[[$ val y = 7; $\$${ x = y }$]]$

Scope extrusion can be prevented by appropriate type systems
[(*)](DBLP:conf/pldi/WestbrookRIYAT10,DBLP:conf/pepm/KameyamaKS09), which are
beyond the scope of this thesis. Scope extrusion is a real problem for code
generators that imperatively manage staged program fragments. For generators
expressed in a functional style it it far less of an issue, regardless of
whether the object program uses effects or not.

## Type Correctness through Deep Reuse of Types

With syntax and scoping out of the way, we turn our attention to type
correctness. Fortunately, type correctness falls out naturally if parametric
types are available. We just replace type `Tree` with `Tree[T]`:

    def power(b: Tree[Double], n: Int): Tree[Double] =

Now the type system ensures that `power` is only applied to AST nodes that
compute `Double` values in the next stage.

Note that the use of parametric types alone does not prevent scope extrusion,
which can also be seen as a type error in the sense of a well-typed multi-
stage program "going wrong"
[(*)](DBLP:conf/icalp/TahaBS98,DBLP:conf/popl/TahaN03).  Thus we do not obtain
a guarantee that _all_  generated programs type check, but the slightly
weaker assurance  that all generated programs that are well-formed are also
type  correct.


## Value Correctness is an Open Problem
<a name="sec:221valcorr"></a>

The remaining big problem is what we (somewhat vaguely) call _value
correctness_ or more generally preservation of program semantics:  How can we
be reasonably certain that a program computes the same result after adding
staging annotations?  We cannot expect a strong guarantee in all cases for
reasons of  nontermination but what is troubling is that there are many
practical cases  where staging annotations change a program's behavior quite
drastically. This fact is well documented in the literature
[(*)](techreport/EckhardtKSTK04,DBLP:journals/scp/CohenDGHKP06,DBLP:conf/gpce/CaretteK05,DBLP:conf/pepm/SwadiTKP06,DBLP:conf/esop/InoueT12).

The problem manifests itself both with strings and with trees. The root cause
is that both approaches are based on syntactic expansion, irrespective of
semantics such as order of execution.

Using the regular, unstaged power implementation:

    def power(b: Double, n: Int): Double = ...

    val x = computeA()       // computeA executed here
    power(computeB() + x, 4) // computeB executed before calling power (cbv)

Both compute functions will be executed once, in order. Afterwards, power will
be applied to the result.

Let us compare this with the staged implementation:

    def power(b: Tree[Double], n: Int): Tree[Double] = ...

    val x = $[[$computeA()$]]$
    power($[[$computeB() + $\$$x$]]$, 4)

Result:

    ((computeB() + computeA()) *
     ((computeB() + computeA()) *
      ((computeB() + computeA()) *
       ((computeB() + computeA()) * 1.0))))"


In this case, the computation has been duplicated $n$ times and  the order of
the function calls has been reversed.  Effectively we ignore all bindings and
follow a call-by-name policy even though power declares its arguments as call-
by-value. If either of the compute functions depends on side effects the
staged function computation will produce a very different result. Imagine for
example:

    def computeA() = readNextInputValue()

We clearly want to read only one value, not four.

Even if both functions are pure, it will be much more expensive to compute the
result. If we applied staging to obtaining better performance we have not
achieved our goal.

As another example, let us switch to a better algorithm:

    def power(b: Tree[Double], n: Int): Tree[Double] =
      if (n == 0) $[[$ 1.0 $]]$
      else if ((n&1) == 0) { val y = power(b, n/2); $[[ \$y$ * $\$y ]]$ }
      else $[[ \$b$ * $\$\{$ power(b, n - 1) $\} ]]$

Result:

    power($[[$x$]]$) // (((x*1.0)*(x*1.0))*((x*1.0)*(x*1.0)))


Staging has turned the more efficient algorithm into a less efficient one.
This effect of staging undoing binding and memoization is widely known
[(*)](DBLP:conf/pepm/SwadiTKP06,techreport/EckhardtKSTK04).


## Let Insertion as a Remedy

One way of fixing the order of staged expressions is to insert let-bindings in
strategic places. This is frequently done by separate front ends. Staging
effectively becomes an "assembly language" for code generation.  The front
end can assemble pieces of generated code using explicit side effects, or the
code generators are written in monadic style or continuation passing style
(CPS), in which case the monadic bind operation will insert let-bindings to
maintain the desired evaluation order [(*)](DBLP:conf/pepm/SwadiTKP06).
Effectful code generators are much more likely to cause scope extrusion.
Explicit monadic style or CPS complicate code generators a lot. This dilemma
is described as an "agonizing trade-off", due to which one "cannot achieve
clarity, safety, and efficiency at the same time"
[(*)](DBLP:conf/pepm/KameyamaKS09). Only very recently have type-systems been
devised to handle both staging and effects
[(*)](DBLP:conf/pepm/KameyamaKS08,DBLP:conf/pepm/KameyamaKS09,DBLP:conf/pldi/WestbrookRIYAT10).
They are not excessively restrictive but not without restrictions either.
Mint [(*)](DBLP:conf/pldi/WestbrookRIYAT10), a multi-stage extension of Java,
restricts non-local operations within escapes  to final classes which excludes
much of the standard Java library. Languages that support both staging  and
first class delimited continuations can mitigate this overhead but front ends
that encapsulate the staging  primitives are still needed
[(*)](DBLP:conf/pepm/KameyamaKS09).

In the partial evaluation community, specialization of effectful programs has
been achieved by inserting let-bindings eagerly for each effectful  statement
[(*)](thiemann1999partial,DBLP:conf/tacs/LawallT97), achieving on-the-fly
conversion to administrative normal form  (ANF,
[(*)](DBLP:conf/pldi/FlanaganSDF93). As we will show below, a simplified
variant of this approach naturally  extends to staging with and without
quasiquotes.



# The LMS Way
<a name="sec:222"></a>

We first show how to maintain value correctness through deep reuse of
evaluation order. The key idea is similar to that employed in partial
evaluation [(*)](thiemann1999partial,DBLP:conf/tacs/LawallT97) and applies to
both quasiquoting and LMS. Our presentation differs from the partial
evaluation literature in that it is independent of any partial evaluation
mechanics such as CPS  conversion and expressed in a simple, purely
operational way. Continuing with quasiquoting, we show how we can remove
syntactic overhead and arrive at a more restricted object language by
providing a typed API over staged `Rep[T]` values that hides the internal
implementation. At this point, quasiquoting becomes an implementation detail
that is no longer strictly needed because the higher level object language
interface has taken over most of the staging guarantees. Staging can be
implemented as a library, without  specific language support. Linguistic reuse
is enabled by lifting operations from type `T` to `Rep[T]`. The object
language can be divided into reusable components. Since there is only a single
shared `Rep[T]` type, no layerings or translations between components are
necessary.  Deep reuse of type inference enables a form of semi-automatic
local BTA since method overloading will select either staged or unstaged
operations depending on the types. In many cases, methods can be staged by
just changing their parameter types.



## Value Correctness through Deep Reuse of Evaluation Order
<a name="sec220:evalOrder"></a>

The key idea is to treat quoted fragments as context-sensitive statements,
not context-free expressions. This means that we will need to explicitly
_perform_ a statement. We continue the description  with strings as the
representation type since it is the most basic.  Performing a statement will
register the side effects of this statement in the current context. The
counterpart to _perform_ is _accumulate_, which defines such a
context and returns a program fragment that captures all the effects within
the context. To make sure that all code fragments are treated in this way we
introduce the following typings:

    type Code
    def perform(stm: String): Code
    def accumulate(res: => Code): String

Note the by-name argument of accumulate. The `Code` type and the method
implementations can remain abstract for the moment.

We can put perform and accumulate to use in the power example as follows:

    def power(b: Code, n: Int): Code =
      if (n == 0) perform("1.0") else
      perform("(" + accumulate { b } + " * " + accumulate { power(b, n - 1) } + ")")

We define perform and accumulate in the following way to perform automatic
eager let insertion. The private constructor `code` builds a `Code` object
from a string:

    accumulate { E[ perform("str") ] }
        $\longrightarrow$ "{ val fresh = str; " + accumulate { E[ code("fresh") ] } + "}"
    accumulate { code("str") }
        $\longrightarrow$ "str"

Where `E` is an accumulate-free evaluation context and `fresh` a fresh
identifier. These rules can be implemented using one piece of mutable state in
a  straightforward way.

We are reusing the execution order of the meta language: In the meta language
we execute perform whenever we encounter an object program expression.  If we
force the object program to replay the order of the perform calls  by
inserting a let binding for each of them, we are sure to execute the performed
statements in the right order. Whenever we have a hole to fill in an object
program fragment, we use accumulate to gather all statements performed while
computing the fragment to splice into the hole.

Perform and accumulate form a reflect/reify pair that translates between a
syntactic and a semantic layer. Alternatively, perform could be called
reflectEffects, accumulate reifyEffects. This hints at the view that we are
embedding perform and accumulate in the (invisible) computation  monad of the
meta language using Filinski's notion of monadic reflection
[(*)](DBLP:conf/popl/Filinski94,DBLP:conf/popl/Filinski10).  Accumulate is a
left inverse of perform with respect to extensional equality ($\equiv$)  of
the generated code:

    accumulate { perform("a") } $\longrightarrow^{*}$ "{ val fresh = a; fresh }"       $\equiv$     "a"

If structural equality is desired, a simple special case can be added to  the
above definition to directly translate  `accumulate(perform("a"))` to `a`.
Within a suitable context, perform is also a left inverse of accumulate:
Performing a set of accumulated statements together  is the same as just
performing the statements individually.

Clearly, using perform and accumulate manually is tedious. However we can
incorporate them entirely inside the quasi quotation / interpolation syntax:

    s" foo $\$${ bar } baz " $\longrightarrow$ " foo " + bar + " baz "  // regular interpolation
    q" foo $\$${ bar } baz " $\longrightarrow$ perform(" foo " + accumulate { bar } + " baz ")

In essence, we identify quotation with perform and
holes with accumulate.

We get back to a power implementation using quasiquotes. This time
we use type Code, although we are still working with concrete syntax:

    def power(b: Code, n: Int): Code =
      if (n == 0) q"1.0" else  q"($\$$b * $\$${ power(b, n - 1) } )"

The same mechanism can be used to implement order preserving
versions of (type-safe) abstract syntax quotation $[[ ... ]]$.
The signatures will change from strings to trees:

    type Code[T]
    def perform(stm: Tree[T]): Code[T]
    def accumulate(res: => Code[T]): Tree[T]


We put the modified quasiquotes to test by invoking power
on the example from the [previous Section](#sec:221valcorr):

    def power(b: Code[T], n: Int): Code[T] = ...

    val x = $[[$computeA()$]]$
    power($[[$computeB() + $\$$x$]]$, 4)

We obtain as intermediate result before invoking power (dropping unnecessary
braces and replacing semicolons with newlines):

    val x1 = computeA()
    val x3 = { val x2 = computeB() + x1; x2 }
    power(x3,4)

And as the final result:

    val x1 = computeA()
    val x3 = { val x2 = computeB() + x1; x2 }
    val x4 = 1.0
    val x5 = x3 * x4
    val x6 = x3 * x5
    val x7 = x3 * x6
    val x8 = x3 * x7
    x8

It is easy to see that this is the correct sequencing of statements. No
computation is duplicated. Likewise, if we use the improved algorithm, we
actually get better performance.

We have removed the need for monadic or side-effecting front-ends (in this
case, in other cases they may still be needed but never to perform let
insertion).  Since we have extended the core staging primitives with a
controlled form of side effect, we have removed the need for uncontrolled side
effects in the generator. This makes otherwise common errors such as scope
extrusion much less likely.



## Removing Syntactic Overhead

We have seen how we can improve staging based on  quasiquotes or direct string
generation. Now we turn to other approaches of delineating embedded object
programs. Our aim is embedding domain specific  compilers. We want object
languages tailored to specific applications, with custom compiler components.
The "one size fits all" approach of having the same meta and object language
is not ideal for this purpose. In our case, we would have to inspect Scala
ASTs and reject or possibly interpret constructs that have no correspondence
in the object language (type, class or method definitions, etc).

The staged power implementations with quasi quotes look OK but they do contain
a fair bit of syntactic noise. Also, we might want stronger guarantees about
the staged code, for example that it does not use a particular language
feature, which we know is detrimental to performance. What is more, we might
want to generate code in a different language (JavaScript, CUDA, SQL).

We already hide the internal code representation from client programs. There
are good reasons to also hide the full power of  arbitrary program composition
/ quasi quoting from client programs.

Programs, such as power, use quasiquotes for two purposes: lifting primitives
and operations:

    def power(b: Code[Double], n: Int): Code[Double] =
      if (n == 0) q"1.0" else  q"($\$$b * $\$${ power(b, n - 1) } )"

We already identify object code via `Code[T]` types. Instead of quasiquotes we
can employ other ways of lifting the necessary  operations on type `T` to type
`Code[T]`:

    implicit def liftDouble(x: Double): Code[Double] = q"x"
    def infix_*(x: Code[Double], y: Code[Double]): Code[Double] = q"$\$$x * $\$$y"

Now power can be implemented like this:

    def power(b: Code[Double], n: Int): Code[Double] =
      if (n == 0) liftDouble(1.0) else infix_*(b, power(b, n - 1))

But we can simplify further. In fact, the Scala compiler will do most of  the
work for us and we can write just this:

    def power(b: Code[Double], n: Int): Code[Double] =
      if (n == 0) 1.0 else b * power(b, n - 1)

Apart from the `Code[_]` types, we have re-engineered exactly the regular,
unstaged power function! All other traces of staging annotations are gone.

We are relying on Scala's support for implicit conversions (views) and Scala-
Virtualized support for infix methods. Other expressive languages provide
similar features.



## Staging as a Library and Modular Definition of Object Languages

With the object language definition given by method signatures we can
implement staging as a library, without dedicated language support and with
roughly the same guarantees as a multi-stage language with  quasi quotation.
Furthermore, we can easily generate code in another target language, for
example emit JavaScript from staged Scala expressions.  Given that the multi-
stage program is in control of defining the object language we can model
additional guarantees about the absence of certain operations from staged
code, simply by not including these operations in the object language
interface.

The core idea is to delegate correctness issues to the implementations of the
lifted operations, i.e. the implementation of the object language interface.
Client code can access staging only through the object language API, so if the
implementation is correct, the interface ensures correctness  of the client
code.

We can use any representation we like for staged expressions. For the sake of
simplicity we will stick to strings. Where we have used type `Code[T]` above,
we will use `Rep[T]` from now on because we want to allude to thinking more
about the _representation_ of a `T` value in the next stage and less
about composing code fragments.

Where quasiquoting allowed the full language to be staged, we now have to
explicitly "white-list" all operations we want to make available. Clearly
there is a tradeoff, as explicit white-listing of operations can be tedious.
However we can remedy the white-listing effort to a large extent by providing
libraries of reusable components that contain sets of lifted operations from
which different flavors of object languages can be assembled. It is also
possible to lift whole traits or classes using reflection
[(*)](DBLP:conf/ecoop/KossakowskiARO12).


We can define a simple object language `MyStagedLanguage` as follows,  using
`private` access qualifiers to ensure that the staging primitives  perform and
accumulate are inaccessible to client code outside of package `internal`:

    package internal
    trait Base extends EmbeddedControls {
      type Rep[T]
      private[internal] def perform[T](stm: String): Rep[T]
      private[internal] def accumulate[T](res: => Rep[T]): String
    }
    trait LiftPrimitives extends Base {
      implicit def liftDouble(x: Double): Rep[Double] = perform(x.toString)
    }
    trait Arith extends Base {
      def infix_*(x: Rep[Double], y: Rep[Double]): Rep[Double] = perform(x+"*"+y)
    }
    trait IfThenElse extends Base {
      def __ifThenElse[T](c: Rep[Boolean], a: =>Rep[T], a: =>Rep[T]): Rep[T] =
        perform("if (" + c + ") " + accumulate(a) + " else " + accumulate(b))
    }
    trait MyStagedLanguage extends LiftPrimitives with Arith with IfThenElse

Note that we invoke accumulate only for by-name parameters. All others are
already object code values, so evaluating them is a no-op and cannot have side
effects. In doing so we silently assume a sensible `toString` operation on
`Rep[T]`. If we do not want to make this assumption then we need accumulate
calls everywhere a `Rep[T]` value is converted to a string representation.



Client code just needs access to an object of type `MyStagedLanguage` to call
methods on it. Common ways to achieve this include path-dependent types and
imports:

    val p: MyStagedLanguage = ...
    import p._
    def power(b: Rep[Double], n: Int): Rep[Double] = ...

In which case the desugared method signature is:

    def power(b: p.Rep[Double], n: Int): p.Rep[Double] = ...

Or by structuring the client code as traits itself:

    trait Power { this: MyStagedLanguage =>
      def power(b: Rep[Double], n: Int): Rep[Double] = ...
    }



In the following we briefly revisit the various static guarantees  and show
how they are fulfilled in LMS.

### Syntax correctness through Embedding as Methods

Generating syntactically well formed programs is delegated to  methods
implementing the object language interface. Client code never assembles pieces
of code directly. If clients only  use the API methods, and their
implementations produce syntax correct code, overall syntax correctness
follows.

### Scope Correctness through Deep Reuse Of Val Bindings


The staging primitives perform eager let insertion and perform will assign a
fresh identifier to each and every subexpression encountered, essentially
producing on object program in administrative normal form (ANF). This removes
the need for explicit val bindings in object code. Instead, programmers can
just use val bindings in the meta program. This is an example of deep
linguistic reuse, as the "feature" of val bindings is translated away.

As for scope correctness, we have not encountered any binders in object code
so far. [Below](#sec:220functions) we will introduce staged
functions  using higher order abstract syntax (HOAS)
[(*)](DBLP:conf/pldi/PfenningE88):

    def lambda[A,B](f: Rep[A] => Rep[B]): Rep[A=>B]
    lambda { (x:Rep[Int]) => ... }  // a staged function object

The essence of HOAS is to reuse meta language bindings to implement object
language bindings. Unless subverted by explicit scope extrusion,  the reuse of
meta language bindings ensures scope correctness of  object programs.

### Type Correctness through Typed Embedding (Deep Reuse of Types)

The object language API exposes only typed methods. If the implementations of
these methods produce type correct code, then overall type correctness
follows.



### Value Correctness through Deep Reuse of Evaluation Order

The perform and accumulate abstraction has been described at length
[above](#sec220:evalOrder).





## Functions and Recursion
<a name="sec:220functions"></a>

Many features can be added to the object language in a way that is analogous
to  what we have seen above but some require a bit more thought. In this
section  we will take a closer look at staged functions. Basic support for
staged function definitions and function applications can be defined in terms
of a simple higher-order abstract syntax (HOAS)
[(*)](DBLP:conf/pldi/PfenningE88) representation, similar to those of Carette
et al. [(*)](DBLP:journals/jfp/CaretteKS09)  and Hofer et al.
[(*)](DBLP:conf/gpce/HoferORM08).

The idea is to provide a `lambda` operation that transforms present-stage
functions over staged values (type `Rep[A] => Rep[B]`) to staged function
values (type `Rep[A=>B]`).

    trait Functions extends Base {
      def lambda[A,B](f: Rep[A] => Rep[B]): Rep[A=>B]
      def infix_apply[A,B](f: Rep[A=>B], x: Rep[A]): Rep[B]
    }

To give an example, the staged
recursive factorial function will look like this:

    def fac: Rep[Int => Int] = lambda { n =>
      if (n == 0) 1
      else n * fac(n - 1)
    }

As opposed to the earlier power example, an invocation `fac(m)` will not
inline the definition of `fac` but result in an actual function call in  the
generated code.

However the HOAS representation has the disadvantage of being opaque: there is
no immediate way to "look into" a Scala function object. If we want to treat
functions in the same way as other program constructs, we need a way to
transform the HOAS encoding into our string representation. We can implement
`lambda(f)` to call

    accumulate { f(fresh[A]) }

which will unfold the function definition into a block that represents the
entire computation defined by the function (assuming that `fresh[A]` creates a
fresh symbol of type `A`). But eagerly expanding function definitions is
problematic. For recursive functions, the result would be infinite, i.e. the
computation will not terminate. What we would like to do instead is to detect
recursion and generate a finite  representation that makes the recursive call
explicit. However this is difficult because recursion might be very indirect:

    def foo(x: Rep[Int]) = {
      val f = (x: Rep[Int]) => foo(x + 1)
      val g = lambda(f)
      g(x)
    }

Each incarnation of `foo` creates a new function `f`; unfolding will thus
create unboundedly many different function objects.

To detect cycles, we have to _compare_ those functions. This, of course,
is undecidable in the general case of taking equality to be defined
extensionally, i.e. saying that two functions are equal if they map equal
inputs to equal outputs. The standard reference equality, by contrast,  is too
weak for our purpose:

    def adder(x:Int) = (y: Int) => x + y
    adder(3) == adder(3)
    $\hookrightarrow$ false

However, we can approximate extensional equality by intensional (i.e.
structural) equality, which is sufficient in most cases because recursion will
cycle through a well defined code path in the program text.  Testing
intensional equality amounts to checking if two functions are defined at the
same syntactic location in the source program and whether all data referenced
by their free variables is equal. Fortunately, the implementation of first-
class functions as closure objects offers (at least in principle) access to a
"defunctionalized"  data type representation on which equality can easily be
checked. A bit of care must be taken though, because the structure can be
cyclic. On the JVM there is a particularly neat trick.  We can serialize the
function objects into a byte array and compare the serialized representations:

    serialize(adder(3)) == serialize(adder(3))
    $\hookrightarrow$ true

With this method of testing equality, we can implement _controlled_
unfolding. Unfolding functions only once at the definition site and
associating a fresh symbol with the function being unfolded allows us to
construct a block that contains a recursive call to the symbol we created.
Thus, we can create the expected representation for the factorial function
above.


## Semi-Automatic BTA through Deep Reuse of Type Inference

Given a method or function implementation:

    def power(b: _, n: Int) =
      if (n == 0) 1.0 else b * power(b, n - 1)

Scala's type inference can determine whether the operations and the result
will be staged or not. We just have to provide  the binding time for parameter
`b`. Note that staging `n` would require explicit use of `lambda` because
there is no static criterion to stop the recursion.

In some cases we need to be conservative, for example for mutable objects:

    var i = 0
    if (c)    // c: Rep[Boolean]
      i += 1

The variable `i` must be lifted because writes depend  on dynamic control
flow. We can accomplish this by implementing the virtualized var constructor
to always lift variable declarations, even if the initial right-hand side is a
static value. Packaged up in a trait, it can be selectively imported:

    trait MyProg { this: LiftVariables =>
      ... // all variables are lifted in this scope
    }



## Generating and Loading Executable Code
<a name="sec:230codegen"></a>

Code generation in LMS is an explicit operation. For the common case where
generated code is to be loaded immediately into the running program,  trait
`Compile` provides a suitable interface in form of the abstract  method
`compile`:

    trait Compile extends Base {
      def compile[A,B](f: Rep[A] => Rep[B]): A=>B
    }

The contract of `compile` is to "unstage" a function from staged to staged
values into a function operating on  present-stage values that can be used
just like any other function object in the running program. Of course this
only works for functions that do not reference externally bound `Rep[T]`
values, otherwise the generate code will not compile due to free identifiers.
The given encoding into Scala's type system does not prevent this kind of
error.

For generating Scala code, an implementation of the compilation interface is
provided by trait `CompileScala`:

    trait CompileScala extends Compile {
      def compile[A,B](f: Rep[A] => Rep[B]) = {
        val x = fresh[A]
        val y = accumulate { f(x) }
        // emit header
        emitBlock(y)
        // emit footer
        // invoke compiler
        // load generated class file
        // instantiate object of that class
      }
    }

The overall compilation logic of `CompileScala` is relatively simple: emit a
class and `apply`-method declaration header, emit instructions for each
definition node according to  the schedule, close the source file, invoke the
Scala compiler, load the generated class file and return a newly instantiated
object of that class.

*/
