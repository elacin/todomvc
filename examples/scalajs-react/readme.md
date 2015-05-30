# scalajs-react TodoMVC Example

#About

This example showcases a way of writing *immutable*, *type-safe*, *composable* and *functional* code for the web. 
The following technologies make this possible:

###React
For our purposes, [React](https://facebook.github.io/react/) 
is a way to build web applications consisting of small components.
Components are great because they are composable, 
and because they impose a clear separation of state, with the rest of the code being immutable.

###Scala.js
With the [Scala programming language](http://www.scala-lang.org/) 
you are free to write as type-safe and functional code as you want,
while [Scala.js](https://www.scala-js.org) compiles it to javascript for your browser.

###scalajs-react
Type-safe Scala-[bindings](https://github.com/japgolly/scalajs-react/) on top of React, 
with a few extras thrown in.

###scalaz (dependency of scalajs-react)
Though not heavily used in this example, [scalaz](https://github.com/scalaz/scalaz)
provides all you could ever dream of in terms of functional programming for scala, 
and its all (~=) supported on scala.js 

###sbt
The [scala build tool](http://www.scala-sbt.org) is coupled with a
[plugin](http://www.scala-js.org/doc/sbt-plugin.html) 
to enable compilation and packaging of Scala.js web applications. 

#Running

The example can be run by opening `index.html`.

#Development

The [sbt](/) 
 for
 supports two compilation modes, 
which matter when developing with Scala.js.
 
* `fullOptJS` is a full program optimization, which is annoyingly slow when developing
* `fastOptJS` is fast, but produces large generated javascript files, and hance better suited

There is a bundled `sbt` launcher which can be started like this:
`./sbt`

After `sbt` has downloaded the internet, you can compile once like this:
`fastOptJS` 

or enable continuous compilation by prefixing a tilde
`~fastOptJS`

The two compilation modes generates two differently named files, so there is a separate index-dev.html
 which references the files generated from `fastOptJS`.

 
Happy hacking! :)