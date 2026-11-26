package br.com.oficina.model;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
public record Evento(String eventId, String tipo, Instant ocorridoEm, String ordemId,
 String clienteNome, Integer prazoGarantiaDias, BigDecimal valor, String referencia) {
 public Evento {
  if(eventId==null || eventId.isBlank()) eventId=UUID.randomUUID().toString();
  if(ocorridoEm==null) ocorridoEm=Instant.now();
 }
}
