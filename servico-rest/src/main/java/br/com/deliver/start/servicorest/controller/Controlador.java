package br.com.deliver.start.servicorest.controller;


import br.com.deliver.start.servicorest.service.ServicoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controlador {

    @Autowired
    private ServicoUsuario servicoUser;


    public void programa() {
        servicoUser.criarContas();
        System.out.println("Contas Criadas");
        servicoUser.novaConta();
        servicoUser.contasCadastradas();
    }
}