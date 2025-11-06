/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ucb.estudo.controllers;

import org.springframework.web.bind.annotation.*;
import ucb.estudo.services.UsuarioService;
import ucb.estudo.entities.Usuario;
import ucb.estudo.repositories.GrupoUsuarioRepository;
import ucb.estudo.entities.GrupoUsuarios;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final GrupoUsuarioRepository grupoRepo;

    public UsuarioController(UsuarioService usuarioService, GrupoUsuarioRepository grupoRepo){
        this.usuarioService = usuarioService;
        this.grupoRepo = grupoRepo;
    }

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.findAll(); // you can implement findAll in service
    }

    @PostMapping
    public Usuario criar(@RequestBody Usuario u){
        // Expect front to provide id via DB function, or we can call a native query. Simpler: persist as-is.
        return usuarioService.createUser(u);
    }
}
