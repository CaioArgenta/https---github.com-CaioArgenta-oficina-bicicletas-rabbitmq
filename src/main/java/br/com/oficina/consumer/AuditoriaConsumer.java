package br.com.oficina.consumer;
import br.com.oficina.model.Evento;
import br.com.oficina.service.EfeitosService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Component
@ConditionalOnProperty(name="app.role",havingValue="auditoria")
public class AuditoriaConsumer {
 private final EfeitosService efeitos; private final ConsumerSupport support;
 public AuditoriaConsumer(EfeitosService efeitos,ConsumerSupport support){this.efeitos=efeitos;this.support=support;}
 @RabbitListener(queues="q.auditoria",containerFactory="manualAckFactory")
 public void receber(Evento e,Message m,Channel ch)throws Exception{support.executar(e,m,ch,()->efeitos.auditoria(e));}
}
