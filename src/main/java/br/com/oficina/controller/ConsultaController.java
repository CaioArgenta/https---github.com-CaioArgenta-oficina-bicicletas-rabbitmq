package br.com.oficina.controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
 private final JdbcTemplate db; public ConsultaController(JdbcTemplate db){this.db=db;}
 @GetMapping("/{tipo}") public List<Map<String,Object>> consultar(@PathVariable String tipo){
  Map<String,String> tabelas=Map.of("garantias","garantias","atendimento","avisos_atendimento","caixa","lancamentos_caixa","auditoria","auditoria_eventos","pos-venda","tarefas_pos_venda");
  String t=tabelas.get(tipo);if(t==null)throw new IllegalArgumentException("Consulta inválida");
  return db.queryForList("SELECT * FROM "+t+" ORDER BY id DESC LIMIT 200");
 }
}
