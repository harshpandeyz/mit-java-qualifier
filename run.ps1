$env:QUALIFIER_SUBMIT_ON_STARTUP = if ($env:QUALIFIER_SUBMIT_ON_STARTUP) { $env:QUALIFIER_SUBMIT_ON_STARTUP } else { "false" }
.\mvnw.cmd spring-boot:run
