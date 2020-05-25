name := "plotting"

version := "0.1"

scalaVersion := "2.12.10"
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"
// https://mvnrepository.com/artifact/software.amazon.awssdk/s3
//libraryDependencies += "software.amazon.awssdk" % "s3" % "2.11.7" % Test
libraryDependencies+= "com.amazonaws" % "aws-java-sdk" % "1.3.32"

// https://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "commons-io" % "commons-io" % "2.6"

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "11" classifier osName
)

