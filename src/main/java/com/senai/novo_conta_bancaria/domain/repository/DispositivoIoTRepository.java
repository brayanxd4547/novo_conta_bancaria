package com.senai.novo_conta_bancaria.domain.repository;

import com.senai.novo_conta_bancaria.domain.entity.Conta;
import com.senai.novo_conta_bancaria.domain.entity.DispositivoIoT;
import com.senai.novo_conta_bancaria.domain.entity.Gerente;
import com.senai.novo_conta_bancaria.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispositivoIoTRepository extends JpaRepository<DispositivoIoT, String> {
    Optional<DispositivoIoT> findByCodigoSerialAndAtivoTrue(String codigoSerial);

    List<DispositivoIoT> findAllByAtivoTrue();
}