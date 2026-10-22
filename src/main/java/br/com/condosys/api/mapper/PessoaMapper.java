package br.com.condosys.api.mapper;

import br.com.condosys.api.domain.model.Pessoa;
import br.com.condosys.api.dto.PessoaRequest;
import br.com.condosys.api.dto.PessoaResponse;
import org.springframework.stereotype.Component;


// Converte DTOs e entidade Pessoa.
@Component
public class PessoaMapper {


    // Converte dados recebidos pela API em uma entidade.
    public Pessoa toEntity(PessoaRequest request) {

        return new Pessoa(
                request.nome(),
                request.documento(),
                request.telefone(),
                request.tipoPessoa()
        );
    }


    // Converte a entidade para o DTO devolvido pela API.
    public PessoaResponse toResponse(Pessoa pessoa) {

        return new PessoaResponse(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getDocumento(),
                pessoa.getTelefone(),
                pessoa.getTipoPessoa(),
                pessoa.isAtivo(),
                pessoa.getCriadoEm(),
                pessoa.getAtualizadoEm()
        );
    }
}