package br.com.deliver.start.servicorest.repository;

import br.com.deliver.start.servicorest.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repositorio extends JpaRepository<Conta, Integer>{

}

