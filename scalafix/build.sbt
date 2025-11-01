import sbt.Compile

import scala.collection.Seq

lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val rulesCrossVersions = Seq(V.scala213, V.scala212)
lazy val scala3Version = "3.3.6"

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(
  List(
    organization := "io.github.tanin47",
    version := "1.0.0",
    homepage := Some(url("https://github.com/tanin47/scalafix-forbidden-symbol")),
    licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
    developers := List(
      Developer(
        "tanin",
        "Tanin Na Nakorn",
        "@tanin",
        url("https://github.com/tanin47")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/tanin47/scalafix-forbidden-symbol"),
        "scm:git@github.com:tanin47/scalafix-forbidden-symbol.git"
      )
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    versionScheme := Some("semver-spec"),
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_central_credentials"),
    Test / publishArtifact := false,
    Compile / packageDoc / publishArtifact := true,
    publishMavenStyle := true,
    pomIncludeRepository := { _ => false },
    publishTo := localStaging.value,
  )
)

lazy val `scalafix-forbidden-symbol` = (project in file("."))
  .aggregate(
    (rules.projectRefs ++
      input.projectRefs ++
      output.projectRefs ++
      tests.projectRefs) *
  )
  .settings(
    publish / skip := true
  )

lazy val rules = projectMatrix
  .settings(
    moduleName := "scalafix-forbidden-symbol",
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(rulesCrossVersions)

lazy val input = projectMatrix
  .settings(
    publish / skip := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val output = projectMatrix
  .settings(
    publish / skip := true
  )
  .defaultAxes(VirtualAxis.jvm)
  .jvmPlatform(scalaVersions = rulesCrossVersions :+ scala3Version)

lazy val testsAggregate = Project("tests", file("target/testsAggregate"))
  .aggregate(tests.projectRefs *)
  .settings(
    publish / skip := true
  )

lazy val tests = projectMatrix
  .settings(
    publish / skip := true,
    scalafixTestkitOutputSourceDirectories :=
      TargetAxis
        .resolve(output, Compile / unmanagedSourceDirectories)
        .value,
    scalafixTestkitInputSourceDirectories :=
      TargetAxis
        .resolve(input, Compile / unmanagedSourceDirectories)
        .value,
    scalafixTestkitInputClasspath :=
      TargetAxis.resolve(input, Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      TargetAxis.resolve(input, Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      TargetAxis.resolve(input, Compile / scalaVersion).value
  )
  .defaultAxes(
    rulesCrossVersions.map(VirtualAxis.scalaABIVersion) :+ VirtualAxis.jvm: _*
  )
  .jvmPlatform(
    scalaVersions = Seq(V.scala212),
    axisValues = Seq(TargetAxis(scala3Version)),
    settings = Seq()
  )
  .jvmPlatform(
    scalaVersions = Seq(V.scala213),
    axisValues = Seq(TargetAxis(V.scala213)),
    settings = Seq()
  )
  .jvmPlatform(
    scalaVersions = Seq(V.scala212),
    axisValues = Seq(TargetAxis(V.scala212)),
    settings = Seq()
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
