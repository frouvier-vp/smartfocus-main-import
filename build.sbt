lazy val commonSettings = Seq(
  organization := "com.fredrouvier.smartfocus",
  organizationName := "Frédéric Rouvier",
  organizationHomepage := Some(url("https://github.com/frouvier")),
  scalaVersion := "2.11.7",
  parallelExecution in Test := false,
  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
)

lazy val configuration = file("src/main/resources")

lazy val app = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "smartfocusMainImport",

    libraryDependencies ++= {
      Seq(
        "com.typesafe"                      %  "config"                   % "1.2.1",
        "org.apache.spark"                  %  "spark-core_2.11"          % "2.0.0"
      )
    }
  ).
  settings(
    unmanagedClasspath in Test += configuration,
    unmanagedClasspath in Runtime += configuration
  )
