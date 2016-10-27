name := "treehugger-test"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++=
  Seq("com.eed3si9n" %% "treehugger" % "0.4.1",
    "io.swagger" % "swagger-parser" % "1.0.22",
    "io.swagger" % "swagger-compat-spec-parser" % "1.0.22")
