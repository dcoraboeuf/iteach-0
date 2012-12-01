import sbt._
import Keys._
import PlayProject._
import cloudbees.Plugin._

object ApplicationBuild extends Build {

    val appName         = "iTeach"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA)
		.settings(cloudBeesSettings :_*)
		.settings(
			CloudBees.applicationId := Some("iteach")
		)

}
