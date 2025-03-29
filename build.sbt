val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.0-M5"
val LogbackVersion = "1.2.11"
val CatsEffectVersion = "3.3.14"
val SkunkVersion = "0.3.1"
val Fs2Version = "3.2.10"

lazy val root = (project in file("."))
  .settings(
    name := "scala-pos-app",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe"        %% "circe-literal"       % CirceVersion,
      "org.typelevel"   %% "cats-effect"         % CatsEffectVersion,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion         % Runtime,
      "org.scalameta"   %% "svm-subs"            % "20.2.0",
      "org.tpolecat"    %% "skunk-core"          % SkunkVersion,
      "com.github.pureconfig" %% "pureconfig-core" % "0.17.5",
      "com.github.pureconfig" %% "pureconfig-generic" % "0.17.5",
      "co.fs2"                %% "fs2-core"               % Fs2Version,

    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
  )
