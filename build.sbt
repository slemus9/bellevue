import org.typelevel.sbt.tpolecat.DevMode

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
  .aggregate(bellevueFrontEnd)

lazy val bellevueFrontEnd = module("bellevue-front-end")
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "dev.optics"         %%% "monocle-core" % "3.1.0",
      "io.github.iltotore" %%% "iron"         % "2.6.0",
      "io.indigoengine"    %%% "tyrian-io"    % "0.12.0",
      "org.scala-js"       %%% "scalajs-dom"  % "2.2.0"
    ),
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )

def module(name: String): Project =
  Project(id = name, base = file(s"modules/$name"))
