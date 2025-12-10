package com.senai.novo_conta_bancaria.infrastructure.mqtt;

import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPublisher;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.AutenticacaoPayloadDTO;
import com.senai.novo_conta_bancaria.application.dto.dispositivo_iot.ValidacaoPayloadDTO;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisherService {

    // Publicações para PagamentoAppService

    @MqttPublisher("banco/solicitarBiometria")
    public String solicitarBiometria() {
        return "Insira sua biometria.";
    }

    @MqttPublisher("banco/autenticacao")
    public AutenticacaoPayloadDTO solicitarAutenticacao(String codigoAutenticacao, String idCliente, String biometria) {
        return AutenticacaoPayloadDTO.toDto(codigoAutenticacao, idCliente, biometria);
    }

    // Publicações para DispositivoIoTService

    @MqttPublisher("banco/validacao")
    public ValidacaoPayloadDTO confirmarValidacao(String codigoAutenticacao, boolean biometriaValidada) {
        return ValidacaoPayloadDTO.toDto(codigoAutenticacao, biometriaValidada);
    }
}