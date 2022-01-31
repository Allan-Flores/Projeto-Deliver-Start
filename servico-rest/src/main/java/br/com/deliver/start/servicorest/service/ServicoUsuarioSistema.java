package br.com.deliver.start.servicorest.service;

import br.com.deliver.start.servicorest.repository.RepositorioUsuarioSistema;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoUsuarioSistema implements UserDetailsService {
    private final RepositorioUsuarioSistema repositorioUsuarioSistema;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(repositorioUsuarioSistema.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario do sistema não encontrado"));
    }
}
