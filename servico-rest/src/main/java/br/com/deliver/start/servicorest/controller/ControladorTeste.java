package br.com.deliver.start.servicorest.controller;

import br.com.deliver.start.servicorest.service.ServicoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControladorTeste {

    @Autowired
    private ServicoUsuario servicoUsuario;

    @RequestMapping("/")
    public String home(Model model){

        servicoUsuario.criarContas();
        System.out.println("contas criadas");

        System.out.println("Chamou o deleta");
        servicoUsuario.deleta();

        model.addAttribute("mensagem", "Hello Word");
        System.out.println("Olá Mundo!");

        return "home";
    }
}
