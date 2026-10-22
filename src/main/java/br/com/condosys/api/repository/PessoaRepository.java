package br.com.condosys.api.repository;

// iMPORTA NOSSA ENTIDADE PESSSOA, O REPOSITORY SERA RESPONSAVEL POR REALIZAR OPERAÇÃES DE BANCO RELAACIONADAS A TABELA PESSOA
import br.com.condosys.api.domain.model.Pessoa;

// JpaRepository é uma interface fornecida pelo Spring DAA JPA. ela ja possui varias operaçoes porntas para trabalhar com o banco de dados
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//UUID É O TIPO DA CHAVE PRIMAIRA UTILIZADA PELA ENTIDADE PESSOA.
import java.util.Optional;
import java.util.UUID;


//PessoaREPOSITORY NÃO PRECISA SER UMA CLASSE, CRIAMOS UMA INTERFACE QUE HERDA  DE JPAREPOSITORY
// O SPRING CRIA AUTOMATICAMENTE A IMPLEMENTAÇÃO DESSA INTERFACE QUANDO A APLICAÇÃO INICIA
public interface PessoaRepository
    extends JpaRepository<Pessoa, UUID>{

    /*Busca uma pessoa pelo documento
    *O  Spring Data JPA interpreta o proprio nome
    * do metodo e cria a consulta automatica
    *
    * FindByDdocumento, pode ser entendico como:
    * "Buscar pessoa onde documento seja igual ao valor recebid".
    * SQl EQUIVALENTE, APROXIMADAMENTE
    * SELECT * FROM PESSOAS
    * WHERE documentos =?
     */
    Optional<Pessoa> findByDocumento(String documento);

    //verfica se existe pessoa com documento informado
    boolean existsByDocumento(String documento);


    // Verifica se outro cadastro já utiliza o documento.
    boolean existsByDocumentoAndIdNot(String documento, UUID id);

    // Lista somente pessoas ativas.
    List<Pessoa> findAllByAtivoTrue();

    }

