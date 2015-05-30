import sbt._
import Keys._

import org.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin.autoImport._

object Build extends Build {

  def commonSettings: Project => Project =
    _.enablePlugins(ScalaJSPlugin)
    .settings(
      organization       := "com.olvind.todomvc",
      version            := "1-SNAPSHOT",
      licenses           += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0")),
      scalaVersion       := "2.11.6",
      scalacOptions     ++= Seq("-deprecation", "-unchecked", "-feature",
                              "-language:postfixOps", "-language:implicitConversions",
                              "-language:higherKinds", "-language:existentials"),
      updateOptions      := updateOptions.value.withCachedResolution(true)
  )

  val todomvc = Project("todomvc", file("."))
    .configure(commonSettings)
    .settings(
      sbt.Keys.test in Test := (),
      emitSourceMaps := false,
      libraryDependencies ++= Seq(
        "com.github.japgolly.scalajs-react" %%% "ext-scalaz71" % "0.9.0",
        "com.github.japgolly.scalajs-react" %%% "extra"        % "0.9.0",
        "com.lihaoyi"                       %%% "upickle"      % "0.2.8"
      )
    )
}

