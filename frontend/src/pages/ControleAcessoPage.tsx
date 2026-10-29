import { useState } from 'react'

import {
    Ban,
    CircleCheck,
    CircleX,
    Clock3,
    HardHat,
    History,
    LogIn,
    LogOut,
    Search,
    ShieldCheck,
    TriangleAlert,
    UserPlus,
    UserRound,
    X
} from 'lucide-react'

import {
    listarPessoas
} from '../services/pessoaApi'

import {
    listarAutorizacoes,
    type AutorizacaoResumo
} from '../services/autorizacaoApi'

import {
    listarPorteiros
} from '../services/porteiroApi'

import {
    listarRegistrosPessoa,
    registrarAcesso,
    type RegistroAcessoResponse
} from '../services/registroAcessoApi'

import '../styles/controle-acesso.css'


type PessoaPortaria = {
    id: string

    nome: string

    cpf: string

    autorizacaoId:
        string | null

    unidadeId:
        string | null

    unidade: string

    morador: string

    validade: string

    tipoAutorizacao: string

    autorizado: boolean

    autorizacaoValida: boolean

    dentroCondominio: boolean

    horarioEntrada:
        string | null
}


export function ControleAcessoPage() {

    const [
        pesquisa,
        setPesquisa
    ] = useState('')


    const [
        pessoaSelecionada,
        setPessoaSelecionada
    ] = useState<PessoaPortaria | null>(
        null
    )


    const [
        mensagemPesquisa,
        setMensagemPesquisa
    ] = useState(
        'Pesquise uma pessoa para consultar o acesso.'
    )


    const [
        mensagemOperacao,
        setMensagemOperacao
    ] = useState('')


    const [
        carregando,
        setCarregando
    ] = useState(false)


    const [
        operando,
        setOperando
    ] = useState(false)


    const [
        porteiroId,
        setPorteiroId
    ] = useState<string | null>(
        null
    )


    /*
     * Histórico
     */
    const [
        historicoAberto,
        setHistoricoAberto
    ] = useState(false)


    const [
        historico,
        setHistorico
    ] = useState<RegistroAcessoResponse[]>(
        []
    )


    const [
        carregandoHistorico,
        setCarregandoHistorico
    ] = useState(false)


    const [
        erroHistorico,
        setErroHistorico
    ] = useState('')


    function formatarData(
        data: string | null
    ) {

        if (!data) {
            return null
        }


        const somenteData =
            data.substring(0, 10)


        const partes =
            somenteData.split('-')


        if (partes.length === 3) {

            const [
                ano,
                mes,
                dia
            ] = partes


            return `${dia}/${mes}/${ano}`
        }


        return data
    }


    function formatarDataHora(
        dataHora: string
    ) {

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


    function formatarHora(
        dataHora: string
    ) {

        const data =
            new Date(dataHora)


        if (
            Number.isNaN(
                data.getTime()
            )
        ) {

            return ''
        }


        return data.toLocaleTimeString(
            'pt-BR',
            {
                hour: '2-digit',
                minute: '2-digit'
            }
        )
    }


    function converterDataValidade(
        valor: string,
        fimDoDia: boolean
    ) {

        if (
            /^\d{4}-\d{2}-\d{2}$/
                .test(valor)
        ) {

            return new Date(
                `${valor}${
                    fimDoDia
                        ? 'T23:59:59.999'
                        : 'T00:00:00'
                }`
            ).getTime()
        }


        return new Date(
            valor
        ).getTime()
    }


    function montarValidade(
        autorizacao:
            AutorizacaoResumo | null
    ) {

        if (!autorizacao) {

            return (
                'Nenhuma autorização encontrada'
            )
        }


        const inicio =
            formatarData(
                autorizacao.inicioValidade
            )


        const fim =
            formatarData(
                autorizacao.fimValidade
            )


        if (inicio && fim) {

            return `${inicio} - ${fim}`
        }


        if (inicio) {

            return `A partir de ${inicio}`
        }


        if (fim) {

            return `Até ${fim}`
        }


        return 'Prazo indeterminado'
    }


    function montarUnidade(
        autorizacao:
            AutorizacaoResumo | null
    ) {

        if (!autorizacao) {

            return 'Não informada'
        }


        if (
            autorizacao.bloco &&
            autorizacao.numero
        ) {

            return (
                `${autorizacao.bloco}` +
                `${autorizacao.numero}`
            )
        }


        return (
            autorizacao.numero ||
            autorizacao.bloco ||
            'Não informada'
        )
    }


    function autorizacaoEstaValida(
        autorizacao:
        AutorizacaoResumo
    ) {

        if (
            autorizacao.status !== 'ATIVA'
        ) {

            return false
        }


        const agora =
            Date.now()


        if (
            autorizacao.inicioValidade
        ) {

            const inicio =
                converterDataValidade(
                    autorizacao.inicioValidade,
                    false
                )


            if (
                !Number.isNaN(inicio) &&
                agora < inicio
            ) {

                return false
            }
        }


        if (
            autorizacao.fimValidade
        ) {

            const fim =
                converterDataValidade(
                    autorizacao.fimValidade,
                    true
                )


            if (
                !Number.isNaN(fim) &&
                agora > fim
            ) {

                return false
            }
        }


        return true
    }


    function ordenarAutorizacoes(
        autorizacoes:
        AutorizacaoResumo[]
    ) {

        return [...autorizacoes]
            .sort((a, b) => {

                const dataA =
                    a.criadoEm
                        ? new Date(
                            a.criadoEm
                        ).getTime()
                        : 0


                const dataB =
                    b.criadoEm
                        ? new Date(
                            b.criadoEm
                        ).getTime()
                        : 0


                return dataB - dataA
            })
    }


    function descobrirSituacaoAtual(
        registros:
        RegistroAcessoResponse[]
    ) {

        const autorizados =
            registros
                .filter(
                    (registro) =>
                        registro.status ===
                        'AUTORIZADO'
                )
                .sort((a, b) => {

                    return (
                        new Date(
                            b.dataHora
                        ).getTime() -

                        new Date(
                            a.dataHora
                        ).getTime()
                    )
                })


        const ultimo =
            autorizados[0]


        if (!ultimo) {

            return {
                dentro: false,
                horarioEntrada: null
            }
        }


        if (
            ultimo.tipoMovimento ===
            'ENTRADA'
        ) {

            return {
                dentro: true,

                horarioEntrada:
                    formatarHora(
                        ultimo.dataHora
                    )
            }
        }


        return {
            dentro: false,
            horarioEntrada: null
        }
    }


    async function pesquisarPessoa() {

        const termo =
            pesquisa
                .trim()
                .toLowerCase()


        const documentoPesquisa =
            pesquisa.replace(/\D/g, '')


        if (!termo) {

            setPessoaSelecionada(null)

            setMensagemPesquisa(
                'Digite um nome ou CPF para pesquisar.'
            )

            return
        }


        setCarregando(true)
        setMensagemOperacao('')


        try {

            /*
             * Primeiro consulta somente
             * as pessoas.
             *
             * Isso evita que uma falha em
             * autorizações, porteiros ou
             * histórico impeça a pesquisa.
             */
            const pessoas =
                await listarPessoas()


            console.log(
                'Pessoas recebidas do backend:',
                pessoas
            )


            const pessoaEncontrada =
                pessoas.find((pessoa) => {

                    const nome =
                        pessoa.nome
                            .trim()
                            .toLowerCase()


                    const documento =
                        pessoa.documento
                            .replace(/\D/g, '')


                    const encontrouNome =
                        nome.includes(termo)


                    const encontrouDocumento =
                        documentoPesquisa.length > 0 &&
                        documento.includes(
                            documentoPesquisa
                        )


                    return (
                        encontrouNome ||
                        encontrouDocumento
                    )
                })


            console.log(
                'Pessoa encontrada:',
                pessoaEncontrada
            )


            if (!pessoaEncontrada) {

                setPessoaSelecionada(null)

                setMensagemPesquisa(
                    'Nenhuma pessoa encontrada com esse nome ou documento.'
                )

                return
            }


            /*
             * Variáveis auxiliares.
             *
             * Mesmo que as consultas abaixo
             * falhem, a pessoa continuará
             * aparecendo na tela.
             */
            let autorizacaoExibida:
                AutorizacaoResumo | null =
                null


            let autorizacaoValida:
                AutorizacaoResumo | undefined =
                undefined


            let dentroCondominio =
                false


            let horarioEntrada:
                string | null =
                null


            /*
             * AUTORIZAÇÕES
             */
            try {

                const autorizacoes =
                    await listarAutorizacoes()


                console.log(
                    'Autorizações recebidas:',
                    autorizacoes
                )


                const autorizacoesPessoa =
                    ordenarAutorizacoes(
                        autorizacoes.filter(
                            (autorizacao) =>
                                String(
                                    autorizacao.pessoaId
                                ) ===
                                String(
                                    pessoaEncontrada.id
                                )
                        )
                    )


                autorizacaoValida =
                    autorizacoesPessoa.find(
                        autorizacaoEstaValida
                    )


                autorizacaoExibida =
                    autorizacaoValida ??
                    autorizacoesPessoa[0] ??
                    null

            } catch (error) {

                console.error(
                    'Erro ao consultar autorizações:',
                    error
                )
            }


            /*
             * HISTÓRICO
             */
            try {

                const registros =
                    await listarRegistrosPessoa(
                        pessoaEncontrada.id
                    )


                console.log(
                    'Registros da pessoa:',
                    registros
                )


                const situacao =
                    descobrirSituacaoAtual(
                        registros
                    )


                dentroCondominio =
                    situacao.dentro


                horarioEntrada =
                    situacao.horarioEntrada

            } catch (error) {

                console.error(
                    'Erro ao consultar histórico:',
                    error
                )
            }


            /*
             * PORTEIRO
             */
            try {

                const porteiros =
                    await listarPorteiros()


                console.log(
                    'Porteiros recebidos:',
                    porteiros
                )


                const porteiroAtivo =
                    porteiros.find(
                        (porteiro) =>
                            !porteiro.dataDemissao
                    )


                if (porteiroAtivo) {

                    setPorteiroId(
                        porteiroAtivo.usuarioId
                    )

                } else {

                    console.warn(
                        'Nenhum porteiro ativo encontrado.'
                    )

                    setPorteiroId(null)
                }

            } catch (error) {

                console.error(
                    'Erro ao consultar porteiros:',
                    error
                )

                setPorteiroId(null)
            }


            /*
             * Monta o card da pessoa.
             */
            setPessoaSelecionada({

                id:
                pessoaEncontrada.id,

                nome:
                pessoaEncontrada.nome,

                cpf:
                    pessoaEncontrada.documento ||
                    'Documento não informado',

                autorizacaoId:
                    autorizacaoValida?.id ??
                    null,

                unidadeId:
                    autorizacaoExibida
                        ?.unidadeId ??
                    null,

                unidade:
                    autorizacaoExibida
                        ? montarUnidade(
                            autorizacaoExibida
                        )
                        : 'Não informada',

                morador:
                    autorizacaoExibida
                        ?.proprietarioNome ||
                    'Não informado',

                validade:
                    autorizacaoExibida
                        ? montarValidade(
                            autorizacaoExibida
                        )
                        : 'Nenhuma autorização encontrada',

                tipoAutorizacao:
                    autorizacaoExibida
                        ?.tipo ||
                    'Não informada',

                autorizado:
                    autorizacaoValida !==
                    undefined,

                autorizacaoValida:
                    autorizacaoValida !==
                    undefined,

                dentroCondominio,

                horarioEntrada
            })


            setMensagemPesquisa('')

        } catch (error) {

            console.error(
                'Erro ao consultar pessoas:',
                error
            )


            setPessoaSelecionada(null)


            setMensagemPesquisa(
                error instanceof Error
                    ? `Erro ao consultar pessoas: ${error.message}`
                    : 'Não foi possível consultar o backend.'
            )

        } finally {

            setCarregando(false)
        }
    }


    async function abrirHistorico() {

        if (!pessoaSelecionada) {
            return
        }


        setHistoricoAberto(true)

        setCarregandoHistorico(true)

        setErroHistorico('')


        try {

            const registros =
                await listarRegistrosPessoa(
                    pessoaSelecionada.id
                )


            const ordenados =
                [...registros]
                    .sort((a, b) => {

                        return (
                            new Date(
                                b.dataHora
                            ).getTime() -

                            new Date(
                                a.dataHora
                            ).getTime()
                        )
                    })


            setHistorico(
                ordenados
            )

        } catch (error) {

            console.error(
                'Erro ao carregar histórico:',
                error
            )


            setErroHistorico(
                error instanceof Error
                    ? error.message
                    : 'Não foi possível carregar o histórico.'
            )

        } finally {

            setCarregandoHistorico(false)
        }
    }


    function fecharHistorico() {

        setHistoricoAberto(false)

        setErroHistorico('')
    }


    async function registrarEntrada() {

        if (
            !pessoaSelecionada ||
            !porteiroId
        ) {
            return
        }


        if (
            !pessoaSelecionada.unidadeId
        ) {

            setMensagemOperacao(
                'Não foi possível identificar a unidade.'
            )

            return
        }


        if (
            !pessoaSelecionada
                .autorizacaoId
        ) {

            setMensagemOperacao(
                'Não existe autorização válida para esta entrada.'
            )

            return
        }


        setOperando(true)


        try {

            await registrarAcesso({

                pessoaId:
                pessoaSelecionada.id,

                unidadeId:
                pessoaSelecionada
                    .unidadeId,

                porteiroId,

                autorizacaoId:
                pessoaSelecionada
                    .autorizacaoId,

                tipoMovimento:
                    'ENTRADA',

                status:
                    'AUTORIZADO',

                liberacaoManual:
                    false,

                motivoLiberacao:
                    null,

                observacao:
                    'Entrada registrada pela portaria'
            })


            await pesquisarPessoa()


            setMensagemOperacao(
                'Entrada registrada com sucesso no banco de dados.'
            )

        } catch (error) {

            setMensagemOperacao(
                error instanceof Error
                    ? error.message
                    : 'Erro ao registrar entrada.'
            )

        } finally {

            setOperando(false)
        }
    }


    async function registrarSaida() {

        if (
            !pessoaSelecionada ||
            !porteiroId
        ) {
            return
        }


        if (
            !pessoaSelecionada.unidadeId
        ) {

            setMensagemOperacao(
                'Não foi possível identificar a unidade.'
            )

            return
        }


        setOperando(true)


        try {

            await registrarAcesso({

                pessoaId:
                pessoaSelecionada.id,

                unidadeId:
                pessoaSelecionada
                    .unidadeId,

                porteiroId,

                autorizacaoId:
                    null,

                tipoMovimento:
                    'SAIDA',

                status:
                    'AUTORIZADO',

                liberacaoManual:
                    false,

                motivoLiberacao:
                    null,

                observacao:
                    'Saída registrada pela portaria'
            })


            await pesquisarPessoa()


            setMensagemOperacao(
                'Saída registrada com sucesso no banco de dados.'
            )

        } catch (error) {

            setMensagemOperacao(
                error instanceof Error
                    ? error.message
                    : 'Erro ao registrar saída.'
            )

        } finally {

            setOperando(false)
        }
    }


    async function recusarEntrada() {

        if (
            !pessoaSelecionada ||
            !porteiroId
        ) {
            return
        }


        if (
            !pessoaSelecionada.unidadeId
        ) {

            setMensagemOperacao(
                'É necessário identificar a unidade antes de registrar a recusa.'
            )

            return
        }


        setOperando(true)


        try {

            await registrarAcesso({

                pessoaId:
                pessoaSelecionada.id,

                unidadeId:
                pessoaSelecionada
                    .unidadeId,

                porteiroId,

                autorizacaoId:
                    null,

                tipoMovimento:
                    'ENTRADA',

                status:
                    'NEGADO',

                liberacaoManual:
                    false,

                motivoLiberacao:
                    null,

                observacao:
                    'Entrada recusada pela portaria'
            })


            await pesquisarPessoa()


            setMensagemOperacao(
                'Entrada recusada e registrada no histórico.'
            )

        } catch (error) {

            setMensagemOperacao(
                error instanceof Error
                    ? error.message
                    : 'Erro ao registrar recusa.'
            )

        } finally {

            setOperando(false)
        }
    }


    async function liberarAcesso() {

        if (
            !pessoaSelecionada ||
            !porteiroId
        ) {
            return
        }


        if (
            !pessoaSelecionada.unidadeId
        ) {

            setMensagemOperacao(
                'É necessário identificar a unidade para realizar a liberação manual.'
            )

            return
        }


        const motivo =
            window.prompt(
                'Informe o motivo da liberação manual:'
            )


        if (
            !motivo ||
            !motivo.trim()
        ) {

            return
        }


        setOperando(true)


        try {

            await registrarAcesso({

                pessoaId:
                pessoaSelecionada.id,

                unidadeId:
                pessoaSelecionada
                    .unidadeId,

                porteiroId,

                autorizacaoId:
                    null,

                tipoMovimento:
                    'ENTRADA',

                status:
                    'AUTORIZADO',

                liberacaoManual:
                    true,

                motivoLiberacao:
                    motivo.trim(),

                observacao:
                    'Liberação manual registrada pela portaria'
            })


            await pesquisarPessoa()


            setMensagemOperacao(
                'Acesso liberado manualmente e registrado no banco.'
            )

        } catch (error) {

            setMensagemOperacao(
                error instanceof Error
                    ? error.message
                    : 'Erro ao liberar acesso.'
            )

        } finally {

            setOperando(false)
        }
    }


    function gerarIniciais(
        nome: string
    ) {

        return nome
            .split(' ')
            .filter(Boolean)
            .slice(0, 2)
            .map(
                (parte) =>
                    parte[0]
            )
            .join('')
            .toUpperCase()
    }


    return (
        <main className="portaria-page">

            <div className="portaria-title">

                <h1>
                    Controle de Acesso / Portaria
                </h1>

                <p>
                    Pesquise visitantes e gerencie
                    o acesso ao condomínio.
                </p>

            </div>


            <section className="portaria-search-area">

                <div className="portaria-search-box">

                    <Search size={20} />

                    <input
                        type="text"
                        value={pesquisa}
                        onChange={(event) =>
                            setPesquisa(
                                event.target.value
                            )
                        }
                        onKeyDown={(event) => {

                            if (
                                event.key === 'Enter' &&
                                !carregando
                            ) {

                                void pesquisarPessoa()
                            }
                        }}
                        placeholder={
                            'Pesquisar por nome, CPF ou documento...'
                        }
                    />

                </div>


                <button
                    type="button"
                    className="portaria-search-button"
                    onClick={() =>
                        void pesquisarPessoa()
                    }
                    disabled={
                        carregando ||
                        operando
                    }
                >

                    <Search size={18} />

                    {carregando
                        ? 'Consultando...'
                        : 'Pesquisar'}

                </button>


                <button
                    type="button"
                    className="portaria-register-button"
                >

                    <UserPlus size={19} />

                    Cadastrar visitante

                </button>

            </section>


            {!pessoaSelecionada && (

                <section className="portaria-search-feedback">

                    <Search size={30} />

                    <div>

                        <strong>
                            Consulta de acesso
                        </strong>

                        <span>
              {mensagemPesquisa}
            </span>

                    </div>

                </section>

            )}


            {pessoaSelecionada && (

                <>

                    <section className="portaria-main-grid">

                        <article className="portaria-person-card">

                            <div className="portaria-person-top">

                                <div className="portaria-avatar">

                                    {gerarIniciais(
                                        pessoaSelecionada.nome
                                    )}

                                </div>


                                <div className="portaria-person-data">

                                    <div className="portaria-name-line">

                                        <h2>
                                            {
                                                pessoaSelecionada
                                                    .nome
                                            }
                                        </h2>


                                        {pessoaSelecionada
                                            .autorizacaoValida ? (

                                            <span className="portaria-authorized-badge">

                        <CircleCheck
                            size={15}
                        />

                        Autorizado

                      </span>

                                        ) : (

                                            <span className="portaria-denied-badge">

                        <CircleX
                            size={15}
                        />

                        Sem autorização válida

                      </span>

                                        )}

                                    </div>


                                    <span>
                    Documento:
                                        {' '}
                                        {
                                            pessoaSelecionada
                                                .cpf
                                        }
                  </span>

                                </div>

                            </div>


                            <div className="portaria-person-details">

                                <div>

                                    <strong>
                                        Unidade
                                    </strong>

                                    <span>
                    {
                        pessoaSelecionada
                            .unidade
                    }
                  </span>

                                </div>


                                <div>

                                    <strong>
                                        Morador responsável
                                    </strong>

                                    <span>
                    {
                        pessoaSelecionada
                            .morador
                    }
                  </span>

                                </div>


                                <div>

                                    <strong>
                                        Validade
                                    </strong>

                                    <span>
                    {
                        pessoaSelecionada
                            .validade
                    }
                  </span>

                                </div>

                            </div>


                            <div className="portaria-current-status">

                                <div className="portaria-inside-status">

                                    {pessoaSelecionada
                                        .dentroCondominio ? (

                                        <CircleCheck
                                            size={18}
                                        />

                                    ) : (

                                        <UserRound
                                            size={18}
                                        />

                                    )}


                                    <span>

                    {pessoaSelecionada
                        .dentroCondominio
                        ? 'Dentro do condomínio'
                        : 'Fora do condomínio'}

                  </span>

                                </div>


                                {pessoaSelecionada
                                    .horarioEntrada && (

                                    <div className="portaria-entry-time">

                                        <Clock3 size={17} />

                                        Entrada:
                                        {' '}
                                        {
                                            pessoaSelecionada
                                                .horarioEntrada
                                        }

                                    </div>

                                )}

                            </div>


                            <div className="portaria-access-actions">

                                <button
                                    type="button"
                                    className="portaria-action manual"
                                    onClick={() =>
                                        void liberarAcesso()
                                    }
                                    disabled={
                                        operando ||
                                        pessoaSelecionada
                                            .dentroCondominio ||
                                        !pessoaSelecionada
                                            .unidadeId
                                    }
                                >

                                    <ShieldCheck size={20} />

                                    Liberar acesso

                                </button>


                                <button
                                    type="button"
                                    className="portaria-action entry"
                                    onClick={() =>
                                        void registrarEntrada()
                                    }
                                    disabled={
                                        operando ||
                                        pessoaSelecionada
                                            .dentroCondominio ||
                                        !pessoaSelecionada
                                            .autorizacaoValida
                                    }
                                >

                                    <LogIn size={20} />

                                    Registrar entrada

                                </button>


                                <button
                                    type="button"
                                    className="portaria-action deny"
                                    onClick={() =>
                                        void recusarEntrada()
                                    }
                                    disabled={
                                        operando ||
                                        pessoaSelecionada
                                            .dentroCondominio ||
                                        !pessoaSelecionada
                                            .unidadeId
                                    }
                                >

                                    <Ban size={20} />

                                    Recusar entrada

                                </button>


                                <button
                                    type="button"
                                    className="portaria-action exit"
                                    onClick={() =>
                                        void registrarSaida()
                                    }
                                    disabled={
                                        operando ||
                                        !pessoaSelecionada
                                            .dentroCondominio
                                    }
                                >

                                    <LogOut size={20} />

                                    Registrar saída

                                </button>

                            </div>

                        </article>


                        <article className="portaria-side-card">

                            <h2>
                                Status da pessoa
                            </h2>


                            <div className="portaria-status-list">

                                <div
                                    className={
                                        pessoaSelecionada
                                            .autorizacaoValida
                                            ? 'portaria-status-item success'
                                            : 'portaria-status-item danger'
                                    }
                                >

                                    {pessoaSelecionada
                                        .autorizacaoValida ? (

                                        <CircleCheck
                                            size={17}
                                        />

                                    ) : (

                                        <CircleX
                                            size={17}
                                        />

                                    )}


                                    <div>

                                        <strong>

                                            {pessoaSelecionada
                                                .autorizacaoValida
                                                ? 'Autorização válida'
                                                : 'Sem autorização válida'}

                                        </strong>

                                        <span>
                      {
                          pessoaSelecionada
                              .validade
                      }
                    </span>

                                    </div>

                                </div>


                                <div className="portaria-status-item blue">

                                    <ShieldCheck
                                        size={17}
                                    />

                                    <div>

                                        <strong>
                                            Tipo de autorização
                                        </strong>

                                        <span>
                      {
                          pessoaSelecionada
                              .tipoAutorizacao
                      }
                    </span>

                                    </div>

                                </div>


                                <div
                                    className={
                                        pessoaSelecionada
                                            .dentroCondominio
                                            ? 'portaria-status-item success'
                                            : 'portaria-status-item blue'
                                    }
                                >

                                    <UserRound size={17} />

                                    <div>

                                        <strong>

                                            {pessoaSelecionada
                                                .dentroCondominio
                                                ? 'Dentro do condomínio'
                                                : 'Fora do condomínio'}

                                        </strong>

                                        <span>

                      {pessoaSelecionada
                          .horarioEntrada
                          ? `Entrada às ${pessoaSelecionada.horarioEntrada}`
                          : 'Nenhuma entrada ativa'}

                    </span>

                                    </div>

                                </div>

                            </div>

                        </article>


                        <article className="portaria-side-card">

                            <h2>
                                Ações rápidas
                            </h2>


                            <button
                                type="button"
                                className="portaria-quick-action"
                            >

                                <HardHat size={20} />

                                <div>

                                    <strong>
                                        Prestadores autorizados
                                    </strong>

                                    <span>
                    Consultar lista
                  </span>

                                </div>

                            </button>


                            <button
                                type="button"
                                className="portaria-quick-action"
                                onClick={() =>
                                    void abrirHistorico()
                                }
                            >

                                <History size={20} />

                                <div>

                                    <strong>
                                        Histórico recente
                                    </strong>

                                    <span>
                    Entradas e saídas
                  </span>

                                </div>

                            </button>


                            <button
                                type="button"
                                className="portaria-quick-action warning"
                            >

                                <TriangleAlert size={20} />

                                <div>

                                    <strong>
                                        Alertas / Restrições
                                    </strong>

                                    <span>
                    Consultar ocorrências
                  </span>

                                </div>

                            </button>

                        </article>

                    </section>


                    {mensagemOperacao && (

                        <div className="portaria-operation-message">

                            <CircleCheck size={19} />

                            {mensagemOperacao}

                        </div>

                    )}

                </>

            )}


            {historicoAberto &&
                pessoaSelecionada && (

                    <div
                        className="historico-overlay"
                        onClick={fecharHistorico}
                    >

                        <section
                            className="historico-modal"
                            onClick={(event) =>
                                event.stopPropagation()
                            }
                        >

                            <header className="historico-header">

                                <div>

                                    <h2>
                                        Histórico de acesso
                                    </h2>

                                    <span>
                  {pessoaSelecionada.nome}
                                        {' • '}
                                        {pessoaSelecionada.unidade}
                </span>

                                </div>


                                <button
                                    type="button"
                                    className="historico-close"
                                    onClick={fecharHistorico}
                                >

                                    <X size={21} />

                                </button>

                            </header>


                            <div className="historico-content">

                                {carregandoHistorico && (

                                    <div className="historico-empty">

                                        <Clock3 size={26} />

                                        <span>
                    Carregando histórico...
                  </span>

                                    </div>

                                )}


                                {!carregandoHistorico &&
                                    erroHistorico && (

                                        <div className="historico-error">

                                            <TriangleAlert
                                                size={22}
                                            />

                                            {erroHistorico}

                                        </div>

                                    )}


                                {!carregandoHistorico &&
                                    !erroHistorico &&
                                    historico.length === 0 && (

                                        <div className="historico-empty">

                                            <History size={28} />

                                            <strong>
                                                Nenhum acesso registrado
                                            </strong>

                                            <span>
                    Essa pessoa ainda não possui
                    entradas ou saídas registradas.
                  </span>

                                        </div>

                                    )}


                                {!carregandoHistorico &&
                                    !erroHistorico &&
                                    historico.length > 0 && (

                                        <div className="historico-list">

                                            {historico.map(
                                                (registro) => (

                                                    <article
                                                        key={registro.id}
                                                        className="historico-item"
                                                    >

                                                        <div
                                                            className={
                                                                `historico-icon ` +
                                                                (
                                                                    registro.status ===
                                                                    'NEGADO'
                                                                        ? 'negado'
                                                                        : registro
                                                                            .tipoMovimento ===
                                                                        'ENTRADA'
                                                                            ? 'entrada'
                                                                            : 'saida'
                                                                )
                                                            }
                                                        >

                                                            {registro.status ===
                                                            'NEGADO' ? (

                                                                <Ban size={19} />

                                                            ) : registro
                                                                .tipoMovimento ===
                                                            'ENTRADA' ? (

                                                                <LogIn size={19} />

                                                            ) : (

                                                                <LogOut size={19} />

                                                            )}

                                                        </div>


                                                        <div className="historico-info">

                                                            <div className="historico-main-line">

                                                                <strong>

                                                                    {registro.status ===
                                                                    'NEGADO'
                                                                        ? 'Entrada recusada'
                                                                        : registro
                                                                            .tipoMovimento ===
                                                                        'ENTRADA'
                                                                            ? 'Entrada autorizada'
                                                                            : 'Saída registrada'}

                                                                </strong>


                                                                <span>
                            {formatarDataHora(
                                registro.dataHora
                            )}
                          </span>

                                                            </div>


                                                            <div className="historico-details">

                          <span>
                            Porteiro:
                              {' '}
                              {registro.porteiroNome}
                          </span>


                                                                <span>
                            Unidade:
                                                                    {' '}
                                                                    {registro.bloco}
                                                                    {registro.numero}
                          </span>


                                                                {registro
                                                                    .liberacaoManual && (

                                                                    <span className="historico-manual">
                              Liberação manual
                            </span>

                                                                )}

                                                            </div>


                                                            {registro
                                                                .motivoLiberacao && (

                                                                <p>
                                                                    Motivo:
                                                                    {' '}
                                                                    {
                                                                        registro
                                                                            .motivoLiberacao
                                                                    }
                                                                </p>

                                                            )}


                                                            {registro
                                                                .observacao && (

                                                                <p>
                                                                    Observação:
                                                                    {' '}
                                                                    {
                                                                        registro
                                                                            .observacao
                                                                    }
                                                                </p>

                                                            )}

                                                        </div>

                                                    </article>

                                                ))}

                                        </div>

                                    )}

                            </div>

                        </section>

                    </div>

                )}

        </main>
    )
}