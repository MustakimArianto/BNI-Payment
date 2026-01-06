import java.util.Properties
import java.io.FileInputStream

val localPropsFile = file("${rootDir}/local.properties")
val props = Properties()

if (localPropsFile.exists()) {
    props.load(FileInputStream(localPropsFile))
}

extra["integraMavenUsername"] = props.getProperty("integraMavenUsername")
extra["integraMavenPassword"] = props.getProperty("integraMavenPassword")