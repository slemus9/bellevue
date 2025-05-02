import org.typelevel.sbt.tpolecat.DevMode
import org.typelevel.scalacoptions.ScalacOptions

inThisBuild(
  Seq(
    scalaVersion               := "3.6.2",
    tpolecatDefaultOptionsMode := DevMode,
    semanticdbEnabled          := true,
    semanticdbVersion          := scalafixSemanticdb.revision
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(bellevueFrontEnd, bellevueVerified)

lazy val bellevueFrontEnd = module("bellevue-front-end")
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(bellevueVerified)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel"       %% "cats-core"    % Dependencies.cats,
      "io.github.iltotore" %%% "iron"         % Dependencies.iron,
      "dev.optics"         %%% "monocle-core" % Dependencies.monocle,
      "io.indigoengine"    %%% "tyrian-io"    % Dependencies.tyrian,
      "org.scala-js"       %%% "scalajs-dom"  % Dependencies.scalajsDom,
      "io.scalaland"       %%% "chimney"      % Dependencies.chimney
    ),
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )

lazy val bellevueVerified = module("bellevue-verified")
  .enablePlugins(StainlessPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % Dependencies.cats
    ),
    Compile / scalacOptions += "-Wconf:src=target/.*:silent"
  )

def module(name: String): Project =
  Project(id = name, base = file(s"modules/$name"))
