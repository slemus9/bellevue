# BelleVue: A Collaborative Graphical Editor

A Collaborative Graphical Editor written in Scala, inspired by [Gueuze](https://dl.acm.org/doi/10.5555/1894424.1894431) and the [Eloquent Javascript Paint Program project](https://eloquentjavascript.net/2nd_edition/19_paint.html)

## Requirements

* [sbt](https://www.scala-sbt.org/) >= 1.10.7
* [node.js](https://nodejs.org/en) >= 22.13.1
* [Z3](https://github.com/Z3Prover/z3) = 4.14.1
* Stainless. Install it by following the instructions from the [Usage Within An Existing Project¶](https://epfl-lara.github.io/stainless/installation.html#usage-within-an-existing-project) section

## Building the Project

1. Go to the root of the project
2. Run `sbt`
3. Run `bellevue-front-end/fastLinkJS` from inside the **sbt** session to generate the javascript code with Scala.js
3. Go to the `modules/bellevue-front-end` folder
4. Run `npm install`

## Running Locally

1. Go to the `modules/bellevue-front-end` folder
2. Run `npm run start`
3. Navigate to http://localhost:1234
4. This project supports hot-reloading. You can keep the server running and recompile the code using `fastLinkJS` to automatically load your new code changes
