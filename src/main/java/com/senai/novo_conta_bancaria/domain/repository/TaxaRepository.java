package com.senai.novo_conta_bancaria.domain.repository;

import com.senai.novo_conta_bancaria.domain.entity.Taxa;
import com.senai.novo_conta_bancaria.domain.enums.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TaxaRepository extends JpaRepository<Taxa, String> {
    Optional<Taxa> findByIdAndAtivoTrue(String id);
    Optional<Taxa> findByDescricaoAndAtivoTrue(String id);

    Set<Taxa> findAllByAtivoTrue();
    Set<Taxa> findAllByFormaPagamentoAndAtivoTrue(FormaPagamento formaPagamento);
}