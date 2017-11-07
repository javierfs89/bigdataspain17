name := "bigDataSpain17"

scalaVersion := "2.12.3"

organization := "org.hablapps"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1"
)

initialCommands in console := "import org.hablapps.fpinscala.typeclasses._"

enablePlugins(TutPlugin)
