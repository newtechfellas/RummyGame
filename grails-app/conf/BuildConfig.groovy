grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        compile "com.google.api-client:google-api-client:1.16.0-rc"
        compile 'com.google.http-client:google-http-client-jackson2:1.16.0-rc'
        compile 'com.google.apis:google-api-services-oauth2:v2-rev48-1.16.0-rc'
        compile 'javax.mail:mail:1.4.1'
//        compile 'org.hibernate:hibernate-core:4.1.9.Final'
//        compile 'org.hibernate:hibernate-ehcache:4.3.0.Final'
    }

    plugins {
        runtime ":jquery:1.10.2.2"
        runtime ":resources:1.2.1"

        compile ':hibernate4:4.1.11.4'
        compile ":spring-security-core:1.2.7.3"
        compile ":spring-security-facebook:0.15.2"

        compile ":console:1.2"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.5"

        build ":tomcat:7.0.47"

        runtime ":database-migration:1.3.8"

        compile ':cache:1.0.1'
    }
}
