package br.com.condosys.api.domain.model;

/*
 * Importa o enum criado por nós.
 * Ele define quais tipos de pessoa existem no CondoSys.
 */
import br.com.condosys.api.domain.enums.TipoPessoa;

/*
 * Imports do JPA/Hibernate.
 *
 * Essas anotações permitem mapear a classe Java
 * para a tabela do PostgreSQL.
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/*
 * OffsetDateTime será usado para armazenar
 * data e hora com informação de fuso horário.
 */
import java.time.OffsetDateTime;

/*
 * UUID será usado como identificador único.
 */
import java.util.UUID;


/*
 * @Entity informa ao JPA/Hibernate que esta classe
 * representa uma entidade persistida no banco.
 */
@Entity

/*
 * Informa que esta entidade corresponde
 * à tabela "pessoas" do PostgreSQL.
 */
@Table(name = "pessoas")
public class Pessoa {


    /*
     * Identificador único da pessoa.
     *
     * @Id:
     * define a chave primária da entidade.
     *
     * @GeneratedValue:
     * faz o Hibernate gerar automaticamente
     * um UUID para uma nova pessoa.
     *
     * Banco:
     * id UUID PRIMARY KEY
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    /*
     * Nome da pessoa.
     *
     * nullable = false:
     * equivale ao NOT NULL do banco.
     *
     * length = 150:
     * equivale ao VARCHAR(150).
     */
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;


    /*
     * Documento da pessoa.
     *
     * unique = true:
     * impede documentos duplicados.
     *
     * Banco:
     * documento VARCHAR(30) NOT NULL UNIQUE
     */
    @Column(name = "documento", nullable = false, unique = true, length = 30)
    private String documento;


    /*
     * Telefone da pessoa.
     *
     * Como não possui nullable = false,
     * o telefone pode ser nulo.
     */
    @Column(name = "telefone", length = 30)
    private String telefone;


    /*
     * Tipo da pessoa dentro do CondoSys.
     *
     * @Enumerated(EnumType.STRING)
     * faz o Hibernate armazenar o nome do enum
     * como texto no PostgreSQL.
     *
     * Exemplos:
     * USUARIO
     * VISITANTE
     * PRESTADOR_SERVICO
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false, length = 30)
    private TipoPessoa tipoPessoa;


    /*
     * Indica se o cadastro está ativo.
     *
     * true:
     * pessoa ativa.
     *
     * false:
     * cadastro desativado.
     *
     * Isso permite fazer exclusão lógica,
     * sem apagar fisicamente a pessoa do banco.
     */
    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;


    /*
     * Data e hora em que a pessoa
     * foi cadastrada no sistema.
     *
     * PostgreSQL:
     * TIMESTAMPTZ
     */
    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;


    /*
     * Data e hora da última alteração
     * realizada no cadastro.
     */
    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    // =========================================================
// CONSTRUTORES
// =========================================================


    /*
     * Construtor vazio exigido pelo JPA/Hibernate.
     *
     * O Hibernate precisa conseguir criar uma Pessoa
     * internamente antes de preencher os atributos
     * com os valores vindos do banco de dados.
     *
     * Usamos "protected" porque:
     *
     * - o Hibernate consegue utilizá-lo;
     * - outras partes da aplicação não devem criar
     *   uma Pessoa vazia sem necessidade.
     *
     * IMPORTANTE:
     * quando existe apenas uma classe sem construtores,
     * o Java cria um construtor vazio automaticamente.
     *
     * Porém, como vamos criar outro construtor abaixo,
     * precisamos declarar este explicitamente.
     */
    protected Pessoa() {
    }


    /*
     * Construtor utilizado pela nossa aplicação
     * para criar uma nova Pessoa.
     *
     * Recebemos somente os dados necessários
     * para criar o objeto.
     *
     * Não recebemos:
     *
     * id
     * → será criado automaticamente pelo Hibernate.
     *
     * ativo
     * → já começa como true.
     *
     * criadoEm e atualizadoEm
     * → serão preenchidos automaticamente pelo
     *   @PrePersist.
     */
    public Pessoa(
            String nome,
            String documento,
            String telefone,
            TipoPessoa tipoPessoa
    ) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.tipoPessoa = tipoPessoa;
    }


    // =========================================================
    // CICLO DE VIDA DA ENTIDADE
    // =========================================================


    /*
     * @PrePersist executa este método automaticamente
     * antes do primeiro INSERT da pessoa no banco.
     */
    @PrePersist
    private void prePersist() {

        /*
         * Guardamos o momento atual em uma variável
         * para que criadoEm e atualizadoEm recebam
         * exatamente o mesmo horário.
         */
        OffsetDateTime agora = OffsetDateTime.now();

        /*
         * Caso criadoEm ainda esteja vazio,
         * registramos a data da criação.
         */
        if (criadoEm == null) {
            criadoEm = agora;
        }

        /*
         * Na criação, a última atualização
         * também corresponde ao momento atual.
         */
        atualizadoEm = agora;
    }


    /*
     * @PreUpdate executa este método automaticamente
     * antes de um UPDATE realizado pelo Hibernate.
     */
    @PreUpdate
    private void preUpdate() {

        /*
         * Atualiza somente o momento da última alteração.
         *
         * criadoEm não é modificado.
         */
        atualizadoEm = OffsetDateTime.now();
    }



    // =========================================================
    // GETTERS
    // =========================================================


    /*
     * Retorna o UUID da pessoa.
     *
     * Não criamos setId(), pois o ID
     * é controlado pelo Hibernate.
     */
    public UUID getId() {
        return id;
    }


    /*
     * Retorna o nome da pessoa.
     */
    public String getNome() {
        return nome;
    }


    /*
     * Retorna o documento da pessoa.
     */
    public String getDocumento() {
        return documento;
    }


    /*
     * Retorna o telefone da pessoa.
     *
     * Pode retornar null.
     */
    public String getTelefone() {
        return telefone;
    }


    /*
     * Retorna o tipo da pessoa.
     */
    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }


    /*
     * Para boolean utilizamos normalmente "is"
     * em vez de "get".
     *
     * true  -> ativa
     * false -> desativada
     */
    public boolean isAtivo() {
        return ativo;
    }


    /*
     * Retorna quando o cadastro foi criado.
     */
    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }


    /*
     * Retorna quando ocorreu a última atualização.
     */
    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }



    // =========================================================
    // SETTERS
    // =========================================================


    /*
     * Altera o nome da pessoa.
     *
     * "this.nome" representa o atributo da classe.
     * "nome" representa o valor recebido pelo método.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /*
     * Altera o documento da pessoa.
     *
     * Futuramente poderemos adicionar
     * validações específicas para CPF/CNPJ.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }


    /*
     * Altera o telefone.
     *
     * Como telefone é opcional,
     * ele também pode receber null.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    /*
     * Altera o tipo da pessoa.
     *
     * Como usamos TipoPessoa, não podemos
     * passar qualquer texto.
     *
     * Exemplo correto:
     *
     * pessoa.setTipoPessoa(TipoPessoa.VISITANTE);
     */
    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }



    // =========================================================
    // COMPORTAMENTOS DA ENTIDADE
    // =========================================================


    /*
     * Ativa o cadastro da pessoa.
     *
     * Preferimos pessoa.ativar()
     * em vez de pessoa.setAtivo(true)
     * porque expressa melhor a intenção.
     */
    public void ativar() {
        this.ativo = true;
    }


    /*
     * Desativa logicamente a pessoa.
     *
     * O registro continua existindo no banco,
     * mas passa a ficar inativo.
     */
    public void desativar() {
        this.ativo = false;
    }

}