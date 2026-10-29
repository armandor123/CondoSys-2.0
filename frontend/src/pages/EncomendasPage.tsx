import {
    useEffect,
    useMemo,
    useState
} from 'react'

import type {
    FormEvent
} from 'react'

import {
    Box,
    CheckCircle2,
    Clock3,
    PackageCheck,
    PackageOpen,
    Plus,
    RefreshCw,
    Search,
    Truck,
    UserRound,
    X
} from 'lucide-react'

import {
    cadastrarEncomenda,
    entregarEncomenda,
    listarEncomendas,
    marcarAguardandoRetirada,
    type EncomendaResponse
} from '../services/encomendaApi'

import {
    listarUnidades,
    type UnidadeResponse
} from '../services/unidadeApi'

import {
    listarPorteiros
} from '../services/porteiroApi'

import '../styles/encomendas.css'


export function EncomendasPage() {

    const [
        encomendas,
        setEncomendas
    ] = useState<EncomendaResponse[]>(
        []
    )


    const [
        unidades,
        setUnidades
    ] = useState<UnidadeResponse[]>(
        []
    )


    const [
        porteiroId,
        setPorteiroId
    ] = useState<string | null>(
        null
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
        unidadeId,
        setUnidadeId
    ] = useState('')


    const [
        remetente,
        setRemetente
    ] = useState('')


    const [
        destinatario,
        setDestinatario
    ] = useState('')


    const [
        codigoRastreio,
        setCodigoRastreio
    ] = useState('')


    const [
        descricao,
        setDescricao
    ] = useState('')


    async function carregarDados() {

        setCarregando(true)
        setErro('')


        try {

            const [
                encomendasResponse,
                unidadesResponse,
                porteirosResponse
            ] = await Promise.all([

                listarEncomendas(),

                listarUnidades(),

                listarPorteiros()
            ])


            setEncomendas(
                encomendasResponse
            )


            setUnidades(
                unidadesResponse.filter(
                    (unidade) =>
                        unidade.ativa
                )
            )


            /*
             * Enquanto o login ainda é
             * simulado, usamos o primeiro
             * porteiro ativo encontrado.
             */
            const porteiroAtivo =
                porteirosResponse.find(
                    (porteiro) =>
                        !porteiro.dataDemissao
                )


            setPorteiroId(
                porteiroAtivo
                    ?.usuarioId ??
                null
            )

        } catch (error) {

            console.error(
                'Erro ao carregar encomendas:',
                error
            )


            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível carregar as encomendas.'
            )

        } finally {

            setCarregando(false)
        }
    }


    useEffect(() => {

        void carregarDados()

    }, [])


    const encomendasFiltradas =
        useMemo(() => {

            const termo =
                filtro
                    .trim()
                    .toLowerCase()


            return encomendas.filter(
                (encomenda) => {

                    const correspondeStatus =
                        filtroStatus === 'TODAS' ||
                        encomenda.status ===
                        filtroStatus


                    if (!correspondeStatus) {

                        return false
                    }


                    if (!termo) {

                        return true
                    }


                    const dadosPesquisa = [

                        encomenda.destinatario,

                        encomenda.remetente ?? '',

                        encomenda.codigoRastreio ??
                        '',

                        encomenda.bloco,

                        encomenda.numero,

                        encomenda.porteiroNome

                    ]
                        .join(' ')
                        .toLowerCase()


                    return dadosPesquisa.includes(
                        termo
                    )
                }
            )

        }, [
            encomendas,
            filtro,
            filtroStatus
        ])


    const totalAguardando =
        encomendas.filter(
            (encomenda) =>
                encomenda.status ===
                'AGUARDANDO_RETIRADA'
        ).length


    const totalRecebidas =
        encomendas.filter(
            (encomenda) =>
                encomenda.status ===
                'RECEBIDA'
        ).length


    const totalEntregues =
        encomendas.filter(
            (encomenda) =>
                encomenda.status ===
                'ENTREGUE'
        ).length


    function formatarDataHora(
        dataHora: string | null
    ) {

        if (!dataHora) {

            return '-'
        }


        const data =
            new Date(dataHora)


        if (
            Number.isNaN(
                data.getTime()
            )
        ) {

            return dataHora
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


    function statusLabel(
        status: string
    ) {

        switch (status) {

            case 'RECEBIDA':
                return 'Recebida'

            case 'AGUARDANDO_RETIRADA':
                return 'Aguardando retirada'

            case 'ENTREGUE':
                return 'Entregue'

            default:
                return status
        }
    }


    function limparFormulario() {

        setUnidadeId('')
        setRemetente('')
        setDestinatario('')
        setCodigoRastreio('')
        setDescricao('')
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

        limparFormulario()
    }


    function selecionarUnidade(
        novoUnidadeId: string
    ) {

        setUnidadeId(
            novoUnidadeId
        )


        const unidade =
            unidades.find(
                (item) =>
                    item.id ===
                    novoUnidadeId
            )


        /*
         * Ajuda no preenchimento,
         * mas o campo continua editável.
         */
        if (
            unidade &&
            !destinatario.trim()
        ) {

            setDestinatario(
                unidade.proprietarioNome
            )
        }
    }


    async function cadastrar(
        event: FormEvent
    ) {

        event.preventDefault()


        if (!unidadeId) {

            setErro(
                'Selecione a unidade destinatária.'
            )

            return
        }


        if (!porteiroId) {

            setErro(
                'Nenhum porteiro ativo foi encontrado.'
            )

            return
        }


        if (!destinatario.trim()) {

            setErro(
                'Informe o destinatário.'
            )

            return
        }


        setSalvando(true)

        setErro('')
        setSucesso('')


        try {

            await cadastrarEncomenda({

                unidadeId,

                porteiroId,

                remetente:
                    remetente.trim() ||
                    null,

                destinatario:
                    destinatario.trim(),

                codigoRastreio:
                    codigoRastreio.trim() ||
                    null,

                descricao:
                    descricao.trim() ||
                    null
            })


            setModalAberto(false)

            limparFormulario()


            setSucesso(
                'Encomenda registrada com sucesso.'
            )


            await carregarDados()

        } catch (error) {

            console.error(
                'Erro ao cadastrar encomenda:',
                error
            )


            setErro(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível registrar a encomenda.'
            )

        } finally {

            setSalvando(false)
        }
    }


    async function marcarAguardando(
        encomenda:
        EncomendaResponse
    ) {

        setOperandoId(
            encomenda.id
        )

        setErro('')
        setSucesso('')


        try {

            await marcarAguardandoRetirada(
                encomenda.id
            )


            setSucesso(
                'Encomenda marcada como aguardando retirada.'
            )


            await carregarDados()

        } catch (error) {

            setErro(
                error instanceof Error
                    ? error.message
                    : 'Erro ao atualizar encomenda.'
            )

        } finally {

            setOperandoId(null)
        }
    }


    async function registrarEntrega(
        encomenda:
        EncomendaResponse
    ) {

        const retiradoPor =
            window.prompt(
                'Informe o nome de quem retirou a encomenda:'
            )


        if (
            !retiradoPor ||
            !retiradoPor.trim()
        ) {

            return
        }


        setOperandoId(
            encomenda.id
        )

        setErro('')
        setSucesso('')


        try {

            await entregarEncomenda(
                encomenda.id,
                retiradoPor.trim()
            )


            setSucesso(
                'Entrega registrada com sucesso.'
            )


            await carregarDados()

        } catch (error) {

            setErro(
                error instanceof Error
                    ? error.message
                    : 'Erro ao registrar entrega.'
            )

        } finally {

            setOperandoId(null)
        }
    }


    return (
        <main className="encomendas-page">

            {/* Cabeçalho */}
            <div className="encomendas-header">

                <div>

                    <h1>
                        Encomendas
                    </h1>

                    <p>
                        Controle de recebimento e
                        retirada de encomendas.
                    </p>

                </div>


                <button
                    type="button"
                    className="encomendas-new-button"
                    onClick={abrirModal}
                >

                    <Plus size={19} />

                    Nova encomenda

                </button>

            </div>


            {/* Mensagens */}
            {sucesso && (

                <div className="encomendas-message success">

                    <CheckCircle2 size={18} />

                    {sucesso}

                </div>

            )}


            {erro && (

                <div className="encomendas-message error">

                    {erro}

                </div>

            )}


            {/* Indicadores */}
            <section className="encomendas-kpis">

                <article>

                    <div className="encomenda-kpi-icon recebida">

                        <PackageOpen size={23} />

                    </div>

                    <div>

            <span>
              Recebidas
            </span>

                        <strong>
                            {totalRecebidas}
                        </strong>

                    </div>

                </article>


                <article>

                    <div className="encomenda-kpi-icon aguardando">

                        <Clock3 size={23} />

                    </div>

                    <div>

            <span>
              Aguardando retirada
            </span>

                        <strong>
                            {totalAguardando}
                        </strong>

                    </div>

                </article>


                <article>

                    <div className="encomenda-kpi-icon entregue">

                        <PackageCheck size={23} />

                    </div>

                    <div>

            <span>
              Entregues
            </span>

                        <strong>
                            {totalEntregues}
                        </strong>

                    </div>

                </article>

            </section>


            {/* Pesquisa */}
            <section className="encomendas-toolbar">

                <div className="encomendas-search">

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
                            'Pesquisar destinatário, unidade ou rastreio...'
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

                    <option value="RECEBIDA">
                        Recebidas
                    </option>

                    <option value="AGUARDANDO_RETIRADA">
                        Aguardando retirada
                    </option>

                    <option value="ENTREGUE">
                        Entregues
                    </option>

                </select>


                <button
                    type="button"
                    className="encomendas-refresh"
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
            <section className="encomendas-card">

                <div className="encomendas-card-header">

                    <div>

                        <h2>
                            Controle de encomendas
                        </h2>

                        <span>
              {
                  encomendasFiltradas
                      .length
              }
                            {' '}
                            registro(s)
            </span>

                    </div>

                </div>


                {carregando ? (

                    <div className="encomendas-empty">

                        <RefreshCw size={27} />

                        <span>
              Carregando encomendas...
            </span>

                    </div>

                ) : encomendasFiltradas
                    .length === 0 ? (

                    <div className="encomendas-empty">

                        <Box size={31} />

                        <strong>
                            Nenhuma encomenda encontrada
                        </strong>

                        <span>
              Registre uma nova encomenda
              ou altere os filtros.
            </span>

                    </div>

                ) : (

                    <div className="encomendas-table-wrapper">

                        <table className="encomendas-table">

                            <thead>

                            <tr>

                                <th>
                                    Destinatário
                                </th>

                                <th>
                                    Unidade
                                </th>

                                <th>
                                    Remetente
                                </th>

                                <th>
                                    Rastreio
                                </th>

                                <th>
                                    Recebido em
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

                            {encomendasFiltradas.map(
                                (encomenda) => (

                                    <tr
                                        key={
                                            encomenda.id
                                        }
                                    >

                                        <td>

                                            <div className="encomenda-person">

                                                <UserRound
                                                    size={17}
                                                />

                                                <strong>
                                                    {
                                                        encomenda
                                                            .destinatario
                                                    }
                                                </strong>

                                            </div>

                                        </td>


                                        <td>

                                            <strong className="encomenda-unidade">

                                                {encomenda.bloco}
                                                {encomenda.numero}

                                            </strong>

                                        </td>


                                        <td>
                                            {
                                                encomenda
                                                    .remetente ||
                                                'Não informado'
                                            }
                                        </td>


                                        <td>
                                            {
                                                encomenda
                                                    .codigoRastreio ||
                                                '-'
                                            }
                                        </td>


                                        <td>
                                            {
                                                formatarDataHora(
                                                    encomenda
                                                        .recebidoEm
                                                )
                                            }
                                        </td>


                                        <td>

                      <span
                          className={
                              `encomenda-status ${
                                  encomenda
                                      .status
                                      .toLowerCase()
                              }`
                          }
                      >

                        {
                            statusLabel(
                                encomenda.status
                            )
                        }

                      </span>

                                        </td>


                                        <td>

                                            <div className="encomenda-actions">

                                                {encomenda.status ===
                                                    'RECEBIDA' && (

                                                        <button
                                                            type="button"
                                                            className="encomenda-action waiting"
                                                            onClick={() =>
                                                                void marcarAguardando(
                                                                    encomenda
                                                                )
                                                            }
                                                            disabled={
                                                                operandoId ===
                                                                encomenda.id
                                                            }
                                                        >

                                                            <Clock3
                                                                size={16}
                                                            />

                                                            Aguardar retirada

                                                        </button>

                                                    )}


                                                {encomenda.status ===
                                                    'AGUARDANDO_RETIRADA' && (

                                                        <button
                                                            type="button"
                                                            className="encomenda-action deliver"
                                                            onClick={() =>
                                                                void registrarEntrega(
                                                                    encomenda
                                                                )
                                                            }
                                                            disabled={
                                                                operandoId ===
                                                                encomenda.id
                                                            }
                                                        >

                                                            <PackageCheck
                                                                size={16}
                                                            />

                                                            Entregar

                                                        </button>

                                                    )}


                                                {encomenda.status ===
                                                    'ENTREGUE' && (

                                                        <span className="encomenda-delivered">

                            <CheckCircle2
                                size={16}
                            />

                                                            {
                                                                encomenda
                                                                    .retiradoPor
                                                            }

                          </span>

                                                    )}

                                            </div>

                                        </td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>

                    </div>

                )}

            </section>


            {/* ==================================
          MODAL NOVA ENCOMENDA
      =================================== */}

            {modalAberto && (

                <div
                    className="encomenda-modal-overlay"
                    onClick={fecharModal}
                >

                    <section
                        className="encomenda-modal"
                        onClick={(event) =>
                            event.stopPropagation()
                        }
                    >

                        <header className="encomenda-modal-header">

                            <div>

                                <h2>
                                    Nova encomenda
                                </h2>

                                <p>
                                    Registre o recebimento
                                    de uma encomenda.
                                </p>

                            </div>


                            <button
                                type="button"
                                onClick={fecharModal}
                                disabled={salvando}
                            >

                                <X size={21} />

                            </button>

                        </header>


                        <form
                            className="encomenda-form"
                            onSubmit={cadastrar}
                        >

                            <div className="encomenda-field full">

                                <label>
                                    Unidade *
                                </label>

                                <select
                                    value={unidadeId}
                                    onChange={(event) =>
                                        selecionarUnidade(
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


                            <div className="encomenda-field">

                                <label>
                                    Destinatário *
                                </label>

                                <input
                                    type="text"
                                    value={destinatario}
                                    onChange={(event) =>
                                        setDestinatario(
                                            event.target.value
                                        )
                                    }
                                    maxLength={150}
                                    placeholder={
                                        'Nome do destinatário'
                                    }
                                />

                            </div>


                            <div className="encomenda-field">

                                <label>
                                    Remetente
                                </label>

                                <input
                                    type="text"
                                    value={remetente}
                                    onChange={(event) =>
                                        setRemetente(
                                            event.target.value
                                        )
                                    }
                                    maxLength={150}
                                    placeholder={
                                        'Amazon, Mercado Livre...'
                                    }
                                />

                            </div>


                            <div className="encomenda-field full">

                                <label>
                                    Código de rastreio
                                </label>

                                <input
                                    type="text"
                                    value={codigoRastreio}
                                    onChange={(event) =>
                                        setCodigoRastreio(
                                            event.target.value
                                        )
                                    }
                                    maxLength={100}
                                    placeholder={
                                        'Código de rastreamento'
                                    }
                                />

                            </div>


                            <div className="encomenda-field full">

                                <label>
                                    Descrição
                                </label>

                                <textarea
                                    rows={4}
                                    value={descricao}
                                    onChange={(event) =>
                                        setDescricao(
                                            event.target.value
                                        )
                                    }
                                    placeholder={
                                        'Caixa pequena, envelope, pacote...'
                                    }
                                />

                            </div>


                            {!porteiroId && (

                                <div className="encomenda-warning full">

                                    <Truck size={18} />

                                    Nenhum porteiro ativo foi
                                    encontrado. O cadastro ficará
                                    indisponível.

                                </div>

                            )}


                            <div className="encomenda-form-actions">

                                <button
                                    type="button"
                                    className="encomenda-cancel-button"
                                    onClick={fecharModal}
                                    disabled={salvando}
                                >

                                    Cancelar

                                </button>


                                <button
                                    type="submit"
                                    className="encomenda-save-button"
                                    disabled={
                                        salvando ||
                                        !porteiroId
                                    }
                                >

                                    <PackageOpen
                                        size={18}
                                    />

                                    {salvando
                                        ? 'Salvando...'
                                        : 'Registrar encomenda'}

                                </button>

                            </div>

                        </form>

                    </section>

                </div>

            )}

        </main>
    )
}