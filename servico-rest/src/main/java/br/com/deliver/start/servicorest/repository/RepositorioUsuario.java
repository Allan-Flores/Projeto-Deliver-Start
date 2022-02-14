package br.com.deliver.start.servicorest.repository;

import br.com.deliver.start.servicorest.entity.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends JpaRepository<UsuarioSistema, Integer>{
    UsuarioSistema findByUsername(String userName);
}

