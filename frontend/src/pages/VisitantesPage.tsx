import {
    useEffect,
    useMemo,
    useState
} from 'react'

import type {
    FormEvent
} from 'react'

import {
    CircleCheck,
    FileText,
    Pencil,
    Phone,
    Plus,
    RefreshCw,
    Search,
    UserRound,
    Users,
    X
} from 'lucide-react'

import {
    cadastrarPessoa
} from '../services/pessoaApi.ts'

import {
    atualizarObservacaoVisitante,
    cadastrarVisitante,
    listarVisitantes,
    type VisitanteResponse
} from '../services/visitanteApi.ts'

import '../styles/visitantes.css'


export function VisitantesPage() {

    const [
        visitantes,
        setVisitantes
    ] = useState<VisitanteResponse[]>(
        []
    )


    const [
        filtro,
        setFiltro
    ] = useState('')


    const [
        carregando,
        setCarregando
    ] = useState(true)


    const [
        salvando,
        setSalvando
    ] = useState(false)


    const [
        formularioAberto,
        setFormularioAberto
    ] = useState(false)


    const [
        erro,
        setErro
    ] = useState('')


    const [
        sucesso,
        setSucesso
    ] = useState('')


    /*
     * Formulário
     */
    const [
        nome,
        setNome
    ] = useState('')


    const [
        documento,
        setDocumento
    ] = useState('')


    const [
        telefone,
        setTelefone
    ] = useState('')


    const [
        observacao,
        setObservacao
    ] = useState('')


    async function carregarVisitantes() {

        setCarregando(true)
        setErro('')


        try {

            const dados =
                await listarVisitantes()


            setVisitantes(
                dados
            )

        } catch (error) {

            console.error(
                'Erro ao listar visitantes:',
                error
            )


            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível carregar os visitantes.'
            )

        } finally {

            setCarregando(false)
        }
    }


    useEffect(() => {

        void carregarVisitantes()

    }, [])


    const visitantesFiltrados =
        useMemo(() => {

            const termo =
                filtro
                    .trim()
                    .toLowerCase()


            const documentoPesquisa =
                filtro.replace(/\D/g, '')


            if (!termo) {

                return visitantes
            }


            return visitantes.filter(
                (visitante) => {

                    const nomeVisitante =
                        visitante.nome
                            .toLowerCase()


                    const documentoVisitante =
                        visitante.documento
                            .replace(/\D/g, '')


                    const telefoneVisitante =
                        visitante.telefone
                            ?.replace(/\D/g, '') ??
                        ''


                    return (
                        nomeVisitante.includes(
                            termo
                        ) ||

                        (
                            documentoPesquisa.length >
                            0 &&
                            documentoVisitante.includes(
                                documentoPesquisa
                            )
                        ) ||

                        (
                            documentoPesquisa.length >
                            0 &&
                            telefoneVisitante.includes(
                                documentoPesquisa
                            )
                        )
                    )
                }
            )

        }, [
            filtro,
            visitantes
        ])


    function limparFormulario() {

        setNome('')
        setDocumento('')
        setTelefone('')
        setObservacao('')
    }


    function abrirFormulario() {

        limparFormulario()

        setErro('')
        setSucesso('')

        setFormularioAberto(true)
    }


    function fecharFormulario() {

        if (salvando) {
            return
        }


        setFormularioAberto(false)

        limparFormulario()
    }


    async function cadastrar(
        event: FormEvent
    ) {

        event.preventDefault()


        const nomeLimpo =
            nome.trim()


        const documentoLimpo =
            documento.trim()


        if (!nomeLimpo) {

            setErro(
                'Informe o nome do visitante.'
            )

            return
        }


        if (!documentoLimpo) {

            setErro(
                'Informe o documento do visitante.'
            )

            return
        }


        setSalvando(true)

        setErro('')
        setSucesso('')


        try {

            /*
             * PASSO 1:
             *
             * Cria a Pessoa.
             */
            const pessoa =
                await cadastrarPessoa({

                    nome:
                    nomeLimpo,

                    documento:
                    documentoLimpo,

                    telefone:
                        telefone.trim() ||
                        null,

                    tipoPessoa:
                        'VISITANTE'
                })


            /*
             * PASSO 2:
             *
             * Usa o UUID da Pessoa
             * para criar o Visitante.
             */
            await cadastrarVisitante({

                pessoaId:
                pessoa.id,

                observacao:
                    observacao.trim() ||
                    null
            })


            setSucesso(
                'Visitante cadastrado com sucesso.'
            )


            fecharFormulario()


            await carregarVisitantes()

        } catch (error) {

            console.error(
                'Erro ao cadastrar visitante:',
                error
            )


            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível cadastrar o visitante.'
            )

        } finally {

            setSalvando(false)
        }
    }


    async function editarObservacao(
        visitante: VisitanteResponse
    ) {

        const novaObservacao =
            window.prompt(
                'Observação do visitante:',
                visitante.observacao ?? ''
            )


        if (
            novaObservacao === null
        ) {

            return
        }


        try {

            await atualizarObservacaoVisitante(
                visitante.pessoaId,
                novaObservacao.trim() ||
                null
            )


            setSucesso(
                'Observação atualizada.'
            )


            await carregarVisitantes()

        } catch (error) {

            setErro(
                error instanceof Error
                    ? error.message
                    : 'Erro ao atualizar observação.'
            )
        }
    }


    return (
        <main className="visitantes-page">

            {/* Cabeçalho */}
            <div className="visitantes-header">

                <div>

                    <h1>
                        Visitantes
                    </h1>

                    <p>
                        Cadastre e consulte os
                        visitantes do condomínio.
                    </p>

                </div>


                <button
                    type="button"
                    className="visitantes-new-button"
                    onClick={abrirFormulario}
                >

                    <Plus size={19} />

                    Novo visitante

                </button>

            </div>


            {/* Mensagens */}
            {sucesso && (

                <div className="visitantes-message success">

                    <CircleCheck size={18} />

                    {sucesso}

                </div>

            )}


            {erro && (

                <div className="visitantes-message error">

                    {erro}

                </div>

            )}


            {/* Resumo */}
            <section className="visitantes-summary">

                <div className="visitantes-summary-icon">

                    <Users size={25} />

                </div>


                <div>

          <span>
            Visitantes cadastrados
          </span>

                    <strong>
                        {visitantes.length}
                    </strong>

                </div>

            </section>


            {/* Pesquisa */}
            <section className="visitantes-toolbar">

                <div className="visitantes-search">

                    <Search size={19} />

                    <input
                        type="text"
                        value={filtro}
                        onChange={(event) =>
                            setFiltro(
                                event.target.value
                            )
                        }
                        placeholder={
                            'Pesquisar por nome, documento ou telefone...'
                        }
                    />

                </div>


                <button
                    type="button"
                    className="visitantes-refresh"
                    onClick={() =>
                        void carregarVisitantes()
                    }
                    disabled={carregando}
                >

                    <RefreshCw size={18} />

                    Atualizar

                </button>

            </section>


            {/* Conteúdo */}
            <section className="visitantes-card">

                <div className="visitantes-card-header">

                    <div>

                        <h2>
                            Relação de visitantes
                        </h2>

                        <span>
              {
                  visitantesFiltrados
                      .length
              }
                            {' '}
                            registro(s)
            </span>

                    </div>

                </div>


                {carregando ? (

                    <div className="visitantes-empty">

                        <RefreshCw size={26} />

                        <span>
              Carregando visitantes...
            </span>

                    </div>

                ) : visitantesFiltrados
                    .length === 0 ? (

                    <div className="visitantes-empty">

                        <UserRound size={30} />

                        <strong>
                            Nenhum visitante encontrado
                        </strong>

                        <span>
              Cadastre um novo visitante
              ou altere sua pesquisa.
            </span>

                    </div>

                ) : (

                    <div className="visitantes-table-wrapper">

                        <table className="visitantes-table">

                            <thead>

                            <tr>

                                <th>
                                    Visitante
                                </th>

                                <th>
                                    Documento
                                </th>

                                <th>
                                    Telefone
                                </th>

                                <th>
                                    Observação
                                </th>

                                <th>
                                    Ações
                                </th>

                            </tr>

                            </thead>


                            <tbody>

                            {visitantesFiltrados.map(
                                (visitante) => (

                                    <tr
                                        key={
                                            visitante.pessoaId
                                        }
                                    >

                                        <td>

                                            <div className="visitante-name">

                                                <div className="visitante-avatar">

                                                    <UserRound
                                                        size={18}
                                                    />

                                                </div>


                                                <strong>
                                                    {
                                                        visitante.nome
                                                    }
                                                </strong>

                                            </div>

                                        </td>


                                        <td>
                                            {
                                                visitante
                                                    .documento
                                            }
                                        </td>


                                        <td>

                      <span className="visitante-inline">

                        <Phone
                            size={15}
                        />

                          {
                              visitante
                                  .telefone ||
                              'Não informado'
                          }

                      </span>

                                        </td>


                                        <td>

                      <span className="visitante-observacao">

                        <FileText
                            size={15}
                        />

                          {
                              visitante
                                  .observacao ||
                              'Sem observação'
                          }

                      </span>

                                        </td>


                                        <td>

                                            <button
                                                type="button"
                                                className="visitante-edit-button"
                                                onClick={() =>
                                                    void editarObservacao(
                                                        visitante
                                                    )
                                                }
                                                title={
                                                    'Editar observação'
                                                }
                                            >

                                                <Pencil
                                                    size={16}
                                                />

                                            </button>

                                        </td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>

                    </div>

                )}

            </section>


            {/* ==================================
          MODAL NOVO VISITANTE
      =================================== */}

            {formularioAberto && (

                <div
                    className="visitante-modal-overlay"
                    onClick={fecharFormulario}
                >

                    <section
                        className="visitante-modal"
                        onClick={(event) =>
                            event.stopPropagation()
                        }
                    >

                        <header className="visitante-modal-header">

                            <div>

                                <h2>
                                    Novo visitante
                                </h2>

                                <p>
                                    Cadastre primeiro os
                                    dados pessoais.
                                </p>

                            </div>


                            <button
                                type="button"
                                onClick={fecharFormulario}
                                disabled={salvando}
                            >

                                <X size={21} />

                            </button>

                        </header>


                        <form
                            onSubmit={cadastrar}
                            className="visitante-form"
                        >

                            <div className="visitante-field full">

                                <label>
                                    Nome completo *
                                </label>

                                <input
                                    type="text"
                                    value={nome}
                                    onChange={(event) =>
                                        setNome(
                                            event.target.value
                                        )
                                    }
                                    maxLength={150}
                                    placeholder={
                                        'Nome do visitante'
                                    }
                                />

                            </div>


                            <div className="visitante-field">

                                <label>
                                    Documento *
                                </label>

                                <input
                                    type="text"
                                    value={documento}
                                    onChange={(event) =>
                                        setDocumento(
                                            event.target.value
                                        )
                                    }
                                    maxLength={30}
                                    placeholder={
                                        'CPF, RG ou outro documento'
                                    }
                                />

                            </div>


                            <div className="visitante-field">

                                <label>
                                    Telefone
                                </label>

                                <input
                                    type="text"
                                    value={telefone}
                                    onChange={(event) =>
                                        setTelefone(
                                            event.target.value
                                        )
                                    }
                                    maxLength={30}
                                    placeholder={
                                        '(11) 99999-9999'
                                    }
                                />

                            </div>


                            <div className="visitante-field full">

                                <label>
                                    Observação
                                </label>

                                <textarea
                                    value={observacao}
                                    onChange={(event) =>
                                        setObservacao(
                                            event.target.value
                                        )
                                    }
                                    rows={4}
                                    placeholder={
                                        'Informações adicionais sobre o visitante...'
                                    }
                                />

                            </div>


                            <div className="visitante-form-actions">

                                <button
                                    type="button"
                                    className="visitante-cancel-button"
                                    onClick={fecharFormulario}
                                    disabled={salvando}
                                >

                                    Cancelar

                                </button>


                                <button
                                    type="submit"
                                    className="visitante-save-button"
                                    disabled={salvando}
                                >

                                    <CircleCheck
                                        size={18}
                                    />

                                    {salvando
                                        ? 'Salvando...'
                                        : 'Cadastrar visitante'}

                                </button>

                            </div>

                        </form>

                    </section>

                </div>

            )}

        </main>
    )
}