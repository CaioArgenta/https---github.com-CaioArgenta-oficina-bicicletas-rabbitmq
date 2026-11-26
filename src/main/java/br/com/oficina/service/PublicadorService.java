package br.com.oficina.service;
import br.com.oficina.config.RabbitConfig;
import br.com.oficina.model.Evento;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
@Service
public class PublicadorService {
 private final RabbitTemplate rabbit;
 public PublicadorService(RabbitTemplate rabbit){this.rabbit=rabbit;}
 public Evento publicar(String key,Evento e){
  rabbit.convertAndSend(RabbitConfig.EX,key,e,m->{m.getMessageProperties().setMessageId(e.eventId());m.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);return m;});
  return e;
 }
}
