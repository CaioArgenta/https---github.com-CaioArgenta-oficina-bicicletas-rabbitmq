package br.com.oficina.controller;
import br.com.oficina.model.Evento;
import br.com.oficina.service.PublicadorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.Instant;
@RestController
@RequestMapping("/api/eventos")
public class EventosController {
 private final PublicadorService pub;
 public EventosController(PublicadorService pub){this.pub=pub;}
 public record OrdemReq(String eventId,String ordemId,String clienteNome,Integer prazoGarantiaDias,Instant ocorridoEm){}
 public record FinanceiroReq(String eventId,String ordemId,String clienteNome,BigDecimal valor,String referencia,Instant ocorridoEm){}
 private void validar(String o,String c){if(o==null||o.isBlank()||c==null||c.isBlank())throw new IllegalArgumentException("ordemId e clienteNome são obrigatórios");}
 @PostMapping("/os-concluida") @ResponseStatus(HttpStatus.ACCEPTED)
 public Evento concluir(@RequestBody OrdemReq r){validar(r.ordemId(),r.clienteNome());int d=r.prazoGarantiaDias()==null?90:r.prazoGarantiaDias();if(d<0)throw new IllegalArgumentException("prazo inválido");return pub.publicar("os.concluida",new Evento(r.eventId(),"ORDEM_SERVICO_CONCLUIDA",r.ocorridoEm(),r.ordemId(),r.clienteNome(),d,null,null));}
 @PostMapping("/os-cancelada") @ResponseStatus(HttpStatus.ACCEPTED)
 public Evento cancelar(@RequestBody OrdemReq r){validar(r.ordemId(),r.clienteNome());return pub.publicar("os.cancelada",new Evento(r.eventId(),"ORDEM_SERVICO_CANCELADA",r.ocorridoEm(),r.ordemId(),r.clienteNome(),null,null,null));}
 private void validarFin(FinanceiroReq r){validar(r.ordemId(),r.clienteNome());if(r.valor()==null||r.valor().signum()<=0)throw new IllegalArgumentException("valor deve ser maior que zero");}
 @PostMapping("/pagamento-recebido") @ResponseStatus(HttpStatus.ACCEPTED)
 public Evento receber(@RequestBody FinanceiroReq r){validarFin(r);return pub.publicar("financeiro.recebido",new Evento(r.eventId(),"PAGAMENTO_RECEBIDO",r.ocorridoEm(),r.ordemId(),r.clienteNome(),null,r.valor(),r.referencia()));}
 @PostMapping("/pagamento-estornado") @ResponseStatus(HttpStatus.ACCEPTED)
 public Evento estornar(@RequestBody FinanceiroReq r){validarFin(r);return pub.publicar("financeiro.estornado",new Evento(r.eventId(),"PAGAMENTO_ESTORNADO",r.ocorridoEm(),r.ordemId(),r.clienteNome(),null,r.valor(),r.referencia()));}
}
