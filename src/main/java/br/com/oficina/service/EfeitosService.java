package br.com.oficina.service;
import br.com.oficina.model.Evento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.UUID;
@Service
public class EfeitosService {
 private final JdbcTemplate db; private final ObjectMapper json;
 public EfeitosService(JdbcTemplate db,ObjectMapper json){this.db=db;this.json=json;}
 public void garantia(Evento e){int d=e.prazoGarantiaDias()==null?90:e.prazoGarantiaDias();db.update("INSERT INTO garantias(ordem_id,evento_id,cliente_nome,inicio,termino,prazo_dias) VALUES(?,?,?,?,?,?) ON CONFLICT(ordem_id) DO NOTHING",e.ordemId(),e.eventId(),e.clienteNome(),Timestamp.from(e.ocorridoEm()),Timestamp.from(e.ocorridoEm().plusSeconds(d*86400L)),d);}
 public void atendimento(Evento e){String msg=switch(e.tipo()){case "ORDEM_SERVICO_CONCLUIDA"->"Sua ordem de serviço foi concluída.";case "ORDEM_SERVICO_CANCELADA"->"Sua ordem de serviço foi cancelada.";case "PAGAMENTO_ESTORNADO"->"O pagamento foi estornado.";default->"Atualização: "+e.tipo();};db.update("INSERT INTO avisos_atendimento(evento_id,ordem_id,cliente_nome,tipo,mensagem,criado_em) VALUES(?,?,?,?,?,?) ON CONFLICT(evento_id) DO NOTHING",e.eventId(),e.ordemId(),e.clienteNome(),e.tipo(),msg,Timestamp.from(e.ocorridoEm()));}
 public void caixa(Evento e){db.update("INSERT INTO lancamentos_caixa(evento_id,ordem_id,tipo,valor,ocorrido_em) VALUES(?,?,?,?,?) ON CONFLICT(evento_id) DO NOTHING",e.eventId(),e.ordemId(),e.tipo(),e.valor(),Timestamp.from(e.ocorridoEm()));}
 public void auditoria(Evento e){try{db.update("INSERT INTO auditoria_eventos(evento_id,tipo,ordem_id,payload,ocorrido_em) VALUES(?,?,?,?,?) ON CONFLICT(evento_id) DO NOTHING",e.eventId(),e.tipo(),e.ordemId(),json.writeValueAsString(e),Timestamp.from(e.ocorridoEm()));}catch(Exception ex){throw new IllegalStateException(ex);}}
 public void posvenda(Evento e){String inst=System.getenv().getOrDefault("INSTANCE_ID",UUID.randomUUID().toString());var data=e.ocorridoEm().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(7);db.update("INSERT INTO tarefas_pos_venda(evento_id,ordem_id,cliente_nome,data_contato,instancia) VALUES(?,?,?,?,?) ON CONFLICT(evento_id) DO NOTHING",e.eventId(),e.ordemId(),e.clienteNome(),data,inst);}
}
