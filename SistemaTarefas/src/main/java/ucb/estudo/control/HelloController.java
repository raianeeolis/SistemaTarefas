/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indica que esta classe é um Controller e seus métodos retornam dados diretamente
public class HelloController {

    /**
     * Endpoint simples que retorna uma mensagem de boas-vindas.
     * Pode ser acessado via GET em http://localhost:8080/hello
     * @return Uma String de saudação.
     */
    @GetMapping("/hello")
    public String hello() {
        return "Olá do Backend Spring Boot!";
    }

    /**
     * Outro endpoint que retorna um objeto JSON.
     * Pode ser acessado via GET em http://localhost:8080/saudacao
     * @return Um objeto Mensagem.
     */
    @GetMapping("/saudacao")
    public Mensagem saudacao() {
        return new Mensagem("Boas-vindas ao nosso sistema!");
    }
}

// Classe simples para representar uma mensagem (será serializada para JSON)
class Mensagem {
    private String texto;

    public Mensagem(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}