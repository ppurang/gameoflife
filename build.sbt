name := "gameoflife"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies += "org.scalatest" % "scalatest_2.9.1" % "1.6.1" % "test"

// reduce the maximum number of errors shown by the Scala compiler
maxErrors := 20

// increase the time between polling for file changes when using continuous execution
pollInterval := 1000

// append several options to the list of options passed to the Java compiler
//javacOptions ++= Seq("-source", "1.5", "-target", "1.5")

// append -deprecation to the options passed to the Scala compiler
//scalacOptions ++= Seq("-deprecation", "-unchecked", "-verbose", "-explaintypes", "-optimise", "-Xcheck-null", "-Xcheckinit", "-Xlog-implicits", "-Ydead-code", "-Yinline", "-Ystatistics", "-Ywarn-dead-code", "-Ytyper-debug")
//scalacOptions ++= Seq("-Xprint:typer","-deprecation", "-unchecked","-explaintypes", "-optimise", "-Xlog-implicits", "-Ydead-code", "-Yinline", "-Ywarn-dead-code")
scalacOptions ++= Seq("-deprecation", "-unchecked","-explaintypes", "-optimise", "-Ydead-code", "-Yinline", "-Ywarn-dead-code")