import net.logstash.logback.encoder.LogstashEncoder


def final LOCATION = "/data/log/app/"
def final SERVER_NAME = "sprite-zuul"
def final SAVE_TIME_RANGE = 7
String ENV = System.getProperty("env")

if (ENV.equals("dev")) {
    appender('CONSOLE', ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %relative [%thread] %-5level %logger{36} %X{requestId} - %msg%n"
        }
    }

    root(INFO, ['CONSOLE'])

} else {
    appender('FILE-INFO', RollingFileAppender) {
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = String.format("%s%s/%s%s%s", LOCATION, SERVER_NAME, "logFile.", ENV, ".%d{yyyy-MM-dd}.log.gz")
            maxHistory = SAVE_TIME_RANGE
        }

        encoder(LogstashEncoder) {
            includeMdcKeyNames = ["requestId"]
        }
    }

    root(INFO, ['FILE-INFO'])
}