import {
    useEffect,
    useMemo,
    useState
} from 'react'

import type {
    FormEvent
} from 'react'

import {
    Ban,
    CalendarClock,
    CheckCircle2,
    Clock3,
    Plus,
    RefreshCw,
    Search,
    ShieldCheck,
    UserRound,
    X
} from 'lucide-react'

import {
    cadastrarAutorizacao,
    listarAutorizacoes,
    revogarAutorizacao,
    type AutorizacaoResumo,
    type TipoAutorizacao
} from '../services/autorizacaoApi'

import {
    listarVisitantes,
    type VisitanteResponse
} from '../services/visitanteApi'

import {
    listarUnidades,
    type UnidadeResponse
} from '../services/unidadeApi'

import '../styles/autorizacoes.css'


export function AutorizacoesPage() {

    const [
        autorizacoes,
        setAutorizacoes
    ] = useState<AutorizacaoResumo[]>(
        []
    )


    const [
        visitantes,
        setVisitantes
    ] = useState<VisitanteResponse[]>(
        []
    )


    const [
        unidades,
        setUnidades
    ] = useState<UnidadeResponse[]>(
        []
    )


    const [
        filtro,
        setFiltro
    ] = useState('')


    const [
        filtroStatus,
        setFiltroStatus
    ] = useState('TODAS')


    const [
        carregando,
        setCarregando
    ] = useState(true)


    const [
        salvando,
        setSalvando
    ] = useState(false)


    const [
        operandoId,
        setOperandoId
    ] = useState<string | null>(
        null
    )


    const [
        modalAberto,
        setModalAberto
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
        pessoaId,
        setPessoaId
    ] = useState('')


    const [
        unidadeId,
        setUnidadeId
    ] = useState('')


    const [
        tipo,
        setTipo
    ] =
        useState<TipoAutorizacao>(
            'UNICA_ENTRADA'
        )


    const [
        inicioValidade,
        setInicioValidade
    ] = useState('')


    const [
        fimValidade,
        setFimValidade
    ] = useState('')


    const [
        observacao,
        setObservacao
    ] = useState('')


    async function carregarDados() {

        setCarregando(true)
        setErro('')


        try {

            const [
                autorizacoesResponse,
                visitantesResponse,
                unidadesResponse
            ] = await Promise.all([

                listarAutorizacoes(),

                listarVisitantes(),

                listarUnidades()
            ])


            setAutorizacoes(
                autorizacoesResponse
            )


            setVisitantes(
                visitantesResponse
            )


            setUnidades(
                unidadesResponse.filter(
                    (unidade) =>
                        unidade.ativa
                )
            )

        } catch (error) {

            console.error(
                'Erro ao carregar autorizações:',
                error
            )


            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível carregar as autorizações.'
            )

        } finally {

            setCarregando(false)
        }
    }


    useEffect(() => {

        void carregarDados()

    }, [])


    const autorizacoesFiltradas =
        useMemo(() => {

            const termo =
                filtro
                    .trim()
                    .toLowerCase()


            return autorizacoes.filter(
                (autorizacao) => {

                    const statusCorreto =
                        filtroStatus === 'TODAS' ||
                        autorizacao.status ===
                        filtroStatus


                    if (!statusCorreto) {

                        return false
                    }


                    if (!termo) {

                        return true
                    }


                    const textoPesquisa = [

                        autorizacao.pessoaNome,

                        autorizacao.pessoaDocumento,

                        autorizacao.proprietarioNome,

                        autorizacao.bloco,

                        autorizacao.numero,

                        autorizacao.tipo,

                        autorizacao.status

                    ]
                        .join(' ')
                        .toLowerCase()


                    return textoPesquisa.includes(
                        termo
                    )
                }
            )

        }, [
            autorizacoes,
            filtro,
            filtroStatus
        ])


    const totalAtivas =
        autorizacoes.filter(
            (autorizacao) =>
                autorizacao.status ===
                'ATIVA'
        ).length


    const totalUtilizadas =
        autorizacoes.filter(
            (autorizacao) =>
                autorizacao.status ===
                'UTILIZADA'
        ).length


    const totalEncerradas =
        autorizacoes.filter(
            (autorizacao) =>
                autorizacao.status ===
                'REVOGADA' ||
                autorizacao.status ===
                'EXPIRADA'
        ).length


    function agoraParaInput() {

        const agora =
            new Date()


        const local =
            new Date(
                agora.getTime() -
                agora.getTimezoneOffset() *
                60000
            )


        return local
            .toISOString()
            .slice(0, 16)
    }


    function formatarData(
        valor: string | null
    ) {

        if (!valor) {

            return 'Sem data final'
        }


        const data =
            new Date(valor)


        if (
            Number.isNaN(
                data.getTime()
            )
        ) {

            return valor
        }


        return data.toLocaleString(
            'pt-BR',
            {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',

                hour: '2-digit',
                minute: '2-digit'
            }
        )
    }


    function labelTipo(
        valor: TipoAutorizacao
    ) {

        switch (valor) {

            case 'UNICA_ENTRADA':
                return 'Entrada única'

            case 'PRAZO_DETERMINADO':
                return 'Prazo determinado'

            case 'PRAZO_INDETERMINADO':
                return 'Prazo indeterminado'

            default:
                return valor
        }
    }


    function labelStatus(
        valor: string
    ) {

        switch (valor) {

            case 'ATIVA':
                return 'Ativa'

            case 'EXPIRADA':
                return 'Expirada'

            case 'REVOGADA':
                return 'Revogada'

            case 'UTILIZADA':
                return 'Utilizada'

            default:
                return valor
        }
    }


    function limparFormulario() {

        setPessoaId('')
        setUnidadeId('')

        setTipo(
            'UNICA_ENTRADA'
        )

        setInicioValidade(
            agoraParaInput()
        )

        setFimValidade('')
        setObservacao('')
    }


    function abrirModal() {

        limparFormulario()

        setErro('')
        setSucesso('')

        setModalAberto(true)
    }


    function fecharModal() {

        if (salvando) {

            return
        }


        setModalAberto(false)
    }


    function alterarTipo(
        novoTipo: TipoAutorizacao
    ) {

        setTipo(
            novoTipo
        )


        if (
            novoTipo !==
            'PRAZO_DETERMINADO'
        ) {

            setFimValidade('')
        }
    }


    async function cadastrar(
        event: FormEvent
    ) {

        event.preventDefault()


        setErro('')
        setSucesso('')


        if (!pessoaId) {

            setErro(
                'Selecione o visitante.'
            )

            return
        }


        if (!unidadeId) {

            setErro(
                'Selecione a unidade.'
            )

            return
        }


        const unidade =
            unidades.find(
                (item) =>
                    item.id === unidadeId
            )


        if (!unidade) {

            setErro(
                'Unidade não encontrada.'
            )

            return
        }


        if (!inicioValidade) {

            setErro(
                'Informe o início da validade.'
            )

            return
        }


        if (
            tipo ===
            'PRAZO_DETERMINADO' &&
            !fimValidade
        ) {

            setErro(
                'Informe o fim da validade.'
            )

            return
        }


        if (
            fimValidade &&
            new Date(fimValidade) <=
            new Date(inicioValidade)
        ) {

            setErro(
                'O fim da validade deve ser posterior ao início.'
            )

            return
        }


        setSalvando(true)


        try {

            await cadastrarAutorizacao({

                pessoaId,

                proprietarioId:
                unidade.proprietarioId,

                unidadeId,

                tipo,

                inicioValidade:
                    new Date(
                        inicioValidade
                    ).toISOString(),

                fimValidade:
                    tipo ===
                    'PRAZO_DETERMINADO'
                        ? new Date(
                            fimValidade
                        ).toISOString()
                        : null,

                observacao:
                    observacao.trim() ||
                    null
            })


            setModalAberto(false)


            setSucesso(
                'Autorização cadastrada com sucesso.'
            )


            await carregarDados()

        } catch (error) {

            console.error(
                'Erro ao cadastrar autorização:',
                error
            )


            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível cadastrar a autorização.'
            )

        } finally {

            setSalvando(false)
        }
    }


    async function revogar(
        autorizacao:
        AutorizacaoResumo
    ) {

        const confirmar =
            window.confirm(
                `Revogar autorização de ${autorizacao.pessoaNome}?`
            )


        if (!confirmar) {

            return
        }


        setOperandoId(
            autorizacao.id
        )

        setErro('')
        setSucesso('')


        try {

            await revogarAutorizacao(
                autorizacao.id
            )


            setSucesso(
                'Autorização revogada.'
            )


            await carregarDados()

        } catch (error) {

            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível revogar a autorização.'
            )

        } finally {

            setOperandoId(null)
        }
    }


    return (
        <main className="autorizacoes-page">

            {/* Cabeçalho */}
            <div className="autorizacoes-header">

                <div>

                    <h1>
                        Autorizações
                    </h1>

                    <p>
                        Gerencie as autorizações de
                        acesso ao condomínio.
                    </p>

                </div>


                <button
                    type="button"
                    className="autorizacoes-new-button"
                    onClick={abrirModal}
                >

                    <Plus size={19} />

                    Nova autorização

                </button>

            </div>


            {sucesso && (

                <div className="autorizacoes-message success">

                    <CheckCircle2 size={18} />

                    {sucesso}

                </div>

            )}


            {erro && (

                <div className="autorizacoes-message error">

                    {erro}

                </div>

            )}


            {/* Indicadores */}
            <section className="autorizacoes-kpis">

                <article>

                    <div className="autorizacao-kpi-icon ativa">

                        <ShieldCheck size={23} />

                    </div>

                    <div>

            <span>
              Ativas
            </span>

                        <strong>
                            {totalAtivas}
                        </strong>

                    </div>

                </article>


                <article>

                    <div className="autorizacao-kpi-icon utilizada">

                        <CheckCircle2 size={23} />

                    </div>

                    <div>

            <span>
              Utilizadas
            </span>

                        <strong>
                            {totalUtilizadas}
                        </strong>

                    </div>

                </article>


                <article>

                    <div className="autorizacao-kpi-icon encerrada">

                        <Ban size={23} />

                    </div>

                    <div>

            <span>
              Encerradas
            </span>

                        <strong>
                            {totalEncerradas}
                        </strong>

                    </div>

                </article>

            </section>


            {/* Filtros */}
            <section className="autorizacoes-toolbar">

                <div className="autorizacoes-search">

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
                            'Pesquisar visitante, documento ou unidade...'
                        }
                    />

                </div>


                <select
                    value={filtroStatus}
                    onChange={(event) =>
                        setFiltroStatus(
                            event.target.value
                        )
                    }
                >

                    <option value="TODAS">
                        Todos os status
                    </option>

                    <option value="ATIVA">
                        Ativas
                    </option>

                    <option value="UTILIZADA">
                        Utilizadas
                    </option>

                    <option value="EXPIRADA">
                        Expiradas
                    </option>

                    <option value="REVOGADA">
                        Revogadas
                    </option>

                </select>


                <button
                    type="button"
                    className="autorizacoes-refresh"
                    onClick={() =>
                        void carregarDados()
                    }
                    disabled={carregando}
                >

                    <RefreshCw size={18} />

                    Atualizar

                </button>

            </section>


            {/* Tabela */}
            <section className="autorizacoes-card">

                <div className="autorizacoes-card-header">

                    <div>

                        <h2>
                            Autorizações cadastradas
                        </h2>

                        <span>
              {
                  autorizacoesFiltradas
                      .length
              }
                            {' '}
                            registro(s)
            </span>

                    </div>

                </div>


                {carregando ? (

                    <div className="autorizacoes-empty">

                        <RefreshCw size={27} />

                        Carregando autorizações...

                    </div>

                ) : autorizacoesFiltradas
                    .length === 0 ? (

                    <div className="autorizacoes-empty">

                        <ShieldCheck size={31} />

                        <strong>
                            Nenhuma autorização encontrada
                        </strong>

                    </div>

                ) : (

                    <div className="autorizacoes-table-wrapper">

                        <table className="autorizacoes-table">

                            <thead>

                            <tr>

                                <th>
                                    Visitante
                                </th>

                                <th>
                                    Unidade
                                </th>

                                <th>
                                    Responsável
                                </th>

                                <th>
                                    Tipo
                                </th>

                                <th>
                                    Validade
                                </th>

                                <th>
                                    Status
                                </th>

                                <th>
                                    Ações
                                </th>

                            </tr>

                            </thead>


                            <tbody>

                            {autorizacoesFiltradas.map(
                                (autorizacao) => (

                                    <tr
                                        key={
                                            autorizacao.id
                                        }
                                    >

                                        <td>

                                            <div className="autorizacao-person">

                                                <UserRound
                                                    size={17}
                                                />

                                                <div>

                                                    <strong>
                                                        {
                                                            autorizacao
                                                                .pessoaNome
                                                        }
                                                    </strong>

                                                    <span>
                            {
                                autorizacao
                                    .pessoaDocumento
                            }
                          </span>

                                                </div>

                                            </div>

                                        </td>


                                        <td>

                                            <strong className="autorizacao-unidade">

                                                {autorizacao.bloco}
                                                {' '}
                                                {autorizacao.numero}

                                            </strong>

                                        </td>


                                        <td>
                                            {
                                                autorizacao
                                                    .proprietarioNome
                                            }
                                        </td>


                                        <td>
                                            {
                                                labelTipo(
                                                    autorizacao.tipo
                                                )
                                            }
                                        </td>


                                        <td>

                                            <div className="autorizacao-validade">

                        <span>

                          Início:
                            {' '}
                            {
                                formatarData(
                                    autorizacao
                                        .inicioValidade
                                )
                            }

                        </span>

                                                <span>

                          Fim:
                                                    {' '}
                                                    {
                                                        formatarData(
                                                            autorizacao
                                                                .fimValidade
                                                        )
                                                    }

                        </span>

                                            </div>

                                        </td>


                                        <td>

                      <span
                          className={
                              `autorizacao-status ${
                                  autorizacao
                                      .status
                                      .toLowerCase()
                              }`
                          }
                      >

                        {
                            labelStatus(
                                autorizacao.status
                            )
                        }

                      </span>

                                        </td>


                                        <td>

                                            {autorizacao.status ===
                                            'ATIVA' ? (

                                                <button
                                                    type="button"
                                                    className="autorizacao-revoke"
                                                    onClick={() =>
                                                        void revogar(
                                                            autorizacao
                                                        )
                                                    }
                                                    disabled={
                                                        operandoId ===
                                                        autorizacao.id
                                                    }
                                                >

                                                    <Ban size={15} />

                                                    Revogar

                                                </button>

                                            ) : (

                                                <span className="autorizacao-no-action">
                          -
                        </span>

                                            )}

                                        </td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>

                    </div>

                )}

            </section>


            {/* Modal */}
            {modalAberto && (

                <div
                    className="autorizacao-modal-overlay"
                    onClick={fecharModal}
                >

                    <section
                        className="autorizacao-modal"
                        onClick={(event) =>
                            event.stopPropagation()
                        }
                    >

                        <header className="autorizacao-modal-header">

                            <div>

                                <h2>
                                    Nova autorização
                                </h2>

                                <p>
                                    Autorize um visitante
                                    para uma unidade.
                                </p>

                            </div>


                            <button
                                type="button"
                                onClick={fecharModal}
                            >

                                <X size={21} />

                            </button>

                        </header>


                        <form
                            className="autorizacao-form"
                            onSubmit={cadastrar}
                        >

                            <div className="autorizacao-field full">

                                <label>
                                    Visitante *
                                </label>

                                <select
                                    value={pessoaId}
                                    onChange={(event) =>
                                        setPessoaId(
                                            event.target.value
                                        )
                                    }
                                >

                                    <option value="">
                                        Selecione um visitante
                                    </option>


                                    {visitantes.map(
                                        (visitante) => (

                                            <option
                                                key={
                                                    visitante.pessoaId
                                                }
                                                value={
                                                    visitante.pessoaId
                                                }
                                            >

                                                {visitante.nome}
                                                {' - '}
                                                {visitante.documento}

                                            </option>

                                        ))}

                                </select>

                            </div>


                            <div className="autorizacao-field full">

                                <label>
                                    Unidade *
                                </label>

                                <select
                                    value={unidadeId}
                                    onChange={(event) =>
                                        setUnidadeId(
                                            event.target.value
                                        )
                                    }
                                >

                                    <option value="">
                                        Selecione uma unidade
                                    </option>


                                    {unidades.map(
                                        (unidade) => (

                                            <option
                                                key={unidade.id}
                                                value={unidade.id}
                                            >

                                                {unidade.bloco}
                                                {' '}
                                                {unidade.numero}
                                                {' - '}
                                                {
                                                    unidade
                                                        .proprietarioNome
                                                }

                                            </option>

                                        ))}

                                </select>

                            </div>


                            <div className="autorizacao-field full">

                                <label>
                                    Tipo de autorização *
                                </label>

                                <select
                                    value={tipo}
                                    onChange={(event) =>
                                        alterarTipo(
                                            event.target
                                                .value as
                                                TipoAutorizacao
                                        )
                                    }
                                >

                                    <option value="UNICA_ENTRADA">
                                        Entrada única
                                    </option>

                                    <option value="PRAZO_DETERMINADO">
                                        Prazo determinado
                                    </option>

                                    <option value="PRAZO_INDETERMINADO">
                                        Prazo indeterminado
                                    </option>

                                </select>

                            </div>


                            <div className="autorizacao-field">

                                <label>
                                    Início da validade *
                                </label>

                                <div className="autorizacao-date-input">

                                    <Clock3 size={17} />

                                    <input
                                        type="datetime-local"
                                        value={
                                            inicioValidade
                                        }
                                        onChange={(event) =>
                                            setInicioValidade(
                                                event.target.value
                                            )
                                        }
                                    />

                                </div>

                            </div>


                            <div className="autorizacao-field">

                                <label>
                                    Fim da validade
                                </label>

                                <div className="autorizacao-date-input">

                                    <CalendarClock
                                        size={17}
                                    />

                                    <input
                                        type="datetime-local"
                                        value={
                                            fimValidade
                                        }
                                        onChange={(event) =>
                                            setFimValidade(
                                                event.target.value
                                            )
                                        }
                                        disabled={
                                            tipo !==
                                            'PRAZO_DETERMINADO'
                                        }
                                    />

                                </div>

                            </div>


                            <div className="autorizacao-field full">

                                <label>
                                    Observação
                                </label>

                                <textarea
                                    rows={4}
                                    value={observacao}
                                    onChange={(event) =>
                                        setObservacao(
                                            event.target.value
                                        )
                                    }
                                    placeholder={
                                        'Informações adicionais...'
                                    }
                                />

                            </div>


                            <div className="autorizacao-form-actions">

                                <button
                                    type="button"
                                    className="autorizacao-cancel-button"
                                    onClick={fecharModal}
                                    disabled={salvando}
                                >

                                    Cancelar

                                </button>


                                <button
                                    type="submit"
                                    className="autorizacao-save-button"
                                    disabled={salvando}
                                >

                                    <ShieldCheck
                                        size={18}
                                    />

                                    {salvando
                                        ? 'Salvando...'
                                        : 'Criar autorização'}

                                </button>

                            </div>

                        </form>

                    </section>

                </div>

            )}

        </main>
    )
}