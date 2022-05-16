package ru.tfs.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "tax-service")
class TaxApiConfigurationProps {

    lateinit var getUserDetailsURL: String
}
