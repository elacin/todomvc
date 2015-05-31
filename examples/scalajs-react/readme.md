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

###scala-js-dom
Scala-js-dom provides a nice statically typed interface to the DOM such that it can be called from Scala code.

###scalatags
scalajs-react bundles a version of [scalatags](http://lihaoyi.github.io/scalatags/). 
Scalatags is a small, fast XML/HTML/CSS construction library for Scala that lets us
construct HTML, again in a typesafe way. This replaces other templating libraries,
JSX in the React case, with plain old functions.

###scalaz
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

The scala.js plugin for sbt supports two compilation modes:
 
* `fullOptJS` is a full program optimization, which is annoyingly slow when developing
* `fastOptJS` is fast, but produces large generated javascript files. This is the one we use for development.

There is a bundled `sbt` launcher which can be started like this:

`./sbt`

After `sbt` has downloaded the internet, you can compile once like this:

`fastOptJS` 

or enable continuous compilation by prefixing a tilde

`~fastOptJS`

The two compilation modes generate two differently named files, so there is a separate `index-dev.html`
 which references the files generated from `fastOptJS`.

 
Happy hacking! :)
