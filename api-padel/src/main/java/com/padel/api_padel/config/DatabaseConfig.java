package com.padel.api_padel.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuración de las bases de datos MariaDB y MongoDB.
 *
 * Esta clase proporciona los beans necesarios para establecer conexiones tanto con MariaDB
 * como con MongoDB usando Spring y sus herramientas asociadas.
 */
@Configuration // Indica que esta clase contiene definiciones de beans para el contexto de Spring
public class DatabaseConfig {

    // Valores inyectados desde el archivo de propiedades (application.properties o application.yml)

    @Value("${spring.datasource.url}")
    private String mariaDbUrl;

    @Value("${spring.datasource.username}")
    private String mariaDbUser;

    @Value("${spring.datasource.password}")
    private String mariaDbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String mariaDbDriverClassName;

    @Value("${spring.data.mongodb.uri}")
    private String mongoDbUrl;

    @Value("${spring.data.mongodb.database}")
    private String mongoDbName;

    /**
     * Configura el DataSource para MariaDB usando HikariCP como pool de conexiones.
     *
     * @return una instancia de DataSource conectada a MariaDB.
     */
    @Bean
    public DataSource mariaDbDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(mariaDbUrl);
        dataSource.setUsername(mariaDbUser);
        dataSource.setPassword(mariaDbPassword);
        dataSource.setDriverClassName(mariaDbDriverClassName);
        return dataSource;
    }

    /**
     * Bean para acceder a la base de datos relacional mediante JDBC.
     *
     * @param mariaDbDataSource fuente de datos configurada para MariaDB.
     * @return una instancia de JdbcTemplate.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource mariaDbDataSource) {
        return new JdbcTemplate(mariaDbDataSource);
    }

    /**
     * Crea un cliente MongoDB usando la URI configurada.
     *
     * @return una instancia de MongoClient.
     */
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoDbUrl);
    }

    /**
     * Obtiene una instancia de base de datos MongoDB específica.
     *
     * @param mongoClient cliente Mongo configurado previamente.
     * @return una instancia de MongoDatabase conectada a la base especificada.
     */
    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(mongoDbName);
    }

    /**
     * Bean de MongoTemplate que facilita las operaciones con MongoDB desde Spring.
     *
     * @param mongoClient cliente Mongo configurado previamente.
     * @return una instancia de MongoTemplate conectada a la base de datos configurada.
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, mongoDbName);
    }
}
