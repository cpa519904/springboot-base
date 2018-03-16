import org.springframework.util.StringUtils

String env = System.getProperty("spring.profiles.active")

if (StringUtils.isEmpty(env)) {
    appender("CONSOLE", ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class{5}:%line>>%msg%n"
        }
    }

    root(INFO, ['CONSOLE'])
} else {
    appender('FILE', RollingFileAppender) {
        def location = "/data/log/app/base/"

        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = location + "logFile.%d{yyyy-MM-dd}.log.gz"
            if (env.equals('prod')) {
                fileNamePattern = location + "logFile.dev.%d{yyyy-MM-dd}.log.gz"
            }
            maxHistory = 15
        }

        encoder(PatternLayoutEncoder) {
            pattern = "%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class{5}:%line>>%msg%n"
        }
    }

    root(INFO, ['FILE'])
}
