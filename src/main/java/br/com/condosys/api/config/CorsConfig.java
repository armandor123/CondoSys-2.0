package br.com.condosys.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configura o acesso do frontend à API.
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(
            CorsRegistry registry
    ) {

        registry.addMapping("/**")

                // Endereço padrão do Vite.
                .allowedOrigins(
                        "http://localhost:5173"
                )

                // Métodos utilizados pelo frontend.
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )

                // Permite os headers enviados pelo frontend.
                .allowedHeaders("*")

                // Permite envio de credenciais futuramente.
                .allowCredentials(true);
    }
}