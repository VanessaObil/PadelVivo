//package com.padel.api_padel.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.Arrays;
//
///**
// * Configuración CORS global para permitir peticiones desde el frontend (Angular en localhost:4200).
// *
// * Esta clase define una configuración personalizada de CORS que habilita el intercambio
// * de recursos entre el servidor backend (Spring Boot) y el cliente frontend (Angular, etc.).
// */
//@Configuration // Marca esta clase como una clase de configuración para Spring
//public class CorsConfig {
//
//    /**
//     * Define un bean que configura el comportamiento de CORS a nivel global para la aplicación.
//     *
//     * @return Una instancia de WebMvcConfigurer con las reglas de CORS personalizadas.
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList(
//                "http://localhost:4200",
//                "http://127.0.0.1:4200",
//                "http://front-padelvivo1.s3-website-us-east-1.amazonaws.com"
//        ));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
//        configuration.setAllowCredentials(true);
//        configuration.setExposedHeaders(Arrays.asList("Authorization"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//}
