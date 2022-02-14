package br.com.deliver.start.servicorest.service;

import br.com.deliver.start.servicorest.repository.RepositorioUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoUsuarioSistema implements UserDetailsService {
    private final RepositorioUsuario repositorioUsuario;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(repositorioUsuario.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario do sistema não encontrado"));
    }
}
