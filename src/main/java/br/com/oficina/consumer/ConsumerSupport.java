package br.com.oficina.consumer;
import br.com.oficina.model.Evento;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;
@Component
public class ConsumerSupport {
 public void executar(Evento e,Message m,Channel ch,Runnable acao)throws Exception{
  long tag=m.getMessageProperties().getDeliveryTag();
  try{acao.run();ch.basicAck(tag,false);System.out.println("ACK "+e.eventId()+" "+e.tipo());}
  catch(Exception ex){ch.basicNack(tag,false,true);System.err.println("NACK "+e.eventId()+" "+ex.getMessage());throw ex;}
 }
}
