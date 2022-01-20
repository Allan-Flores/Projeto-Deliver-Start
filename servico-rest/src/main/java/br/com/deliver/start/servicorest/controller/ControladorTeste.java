package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.service.ServicoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


//@RestController
public class ControladorTeste {

//    @Autowired
//    private ServicoUsuario servicoUsuario;

    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("mensagem", "Hello Word");

        System.out.println("entrou no metodo");

        return "home";
    }
}

