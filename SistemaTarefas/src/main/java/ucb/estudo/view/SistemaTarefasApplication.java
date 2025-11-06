/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 * É o ponto de entrada para rodar o backend.
 */
@SpringBootApplication // Combina @Configuration, @EnableAutoConfiguration e @ComponentScan
public class SistemaTarefasApplication {

    public static void main(String[] args) {
        // Esta linha inicia toda a aplicação Spring.
        // Ela configura o contexto, carrega os beans e inicializa o servidor web (Tomcat).
        SpringApplication.run(SistemaTarefasApplication.class, args);
    }
}
