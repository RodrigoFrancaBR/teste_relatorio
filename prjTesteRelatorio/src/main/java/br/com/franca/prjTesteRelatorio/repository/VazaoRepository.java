package br.com.franca.prjTesteRelatorio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.franca.prjTesteRelatorio.model.Vazao;

@Repository
public interface VazaoRepository extends JpaRepository<Vazao, Long> {

}
