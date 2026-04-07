package com.snanoh.e_commerce.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val uploadDir = Paths.get("image").toAbsolutePath().toUri().toString()
        registry.addResourceHandler("/image/**")
            .addResourceLocations(uploadDir)
    }
}
