package br.com.deliver.start.servicorest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;


//@RestController Controller
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

