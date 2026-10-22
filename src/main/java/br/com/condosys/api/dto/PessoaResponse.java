package  br.com.condosys.api.dto;

import br.com.condosys.api.domain.enums.TipoPessoa;

import java.time.OffsetDateTime;
import java.util.UUID;

// DADOS DE PESSOAS DDEVOLVIDO PELA API.
public record PessoaResponse(
        UUID id,
        String nome,
        String documento,
        String telefone,
        TipoPessoa tipoPessoa,
        boolean ativo,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm

){}