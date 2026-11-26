package br.com.oficina.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {
 public static final String EX="oficina.events";
 @Bean TopicExchange exchange(){return new TopicExchange(EX,true,false);}
 @Bean Queue garantia(){return QueueBuilder.durable("q.garantia").build();}
 @Bean Queue atendimento(){return QueueBuilder.durable("q.atendimento").build();}
 @Bean Queue caixa(){return QueueBuilder.durable("q.caixa").build();}
 @Bean Queue auditoria(){return QueueBuilder.durable("q.auditoria").build();}
 @Bean Queue posvenda(){return QueueBuilder.durable("q.pos-venda").build();}
 @Bean Binding bg(Queue garantia,TopicExchange exchange){return BindingBuilder.bind(garantia).to(exchange).with("os.concluida");}
 @Bean Binding ba1(Queue atendimento,TopicExchange exchange){return BindingBuilder.bind(atendimento).to(exchange).with("os.concluida");}
 @Bean Binding ba2(Queue atendimento,TopicExchange exchange){return BindingBuilder.bind(atendimento).to(exchange).with("os.cancelada");}
 @Bean Binding ba3(Queue atendimento,TopicExchange exchange){return BindingBuilder.bind(atendimento).to(exchange).with("financeiro.estornado");}
 @Bean Binding bc(Queue caixa,TopicExchange exchange){return BindingBuilder.bind(caixa).to(exchange).with("financeiro.*");}
 @Bean Binding bau(Queue auditoria,TopicExchange exchange){return BindingBuilder.bind(auditoria).to(exchange).with("#");}
 @Bean Binding bp(Queue posvenda,TopicExchange exchange){return BindingBuilder.bind(posvenda).to(exchange).with("os.concluida");}
 @Bean Jackson2JsonMessageConverter converter(){return new Jackson2JsonMessageConverter();}
 @Bean SimpleRabbitListenerContainerFactory manualAckFactory(ConnectionFactory cf,Jackson2JsonMessageConverter cv){
  var f=new SimpleRabbitListenerContainerFactory(); f.setConnectionFactory(cf); f.setMessageConverter(cv);
  f.setAcknowledgeMode(AcknowledgeMode.MANUAL); f.setDefaultRequeueRejected(true); return f;
 }
}
