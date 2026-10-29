import {
    apiGet,
    apiPost,
    apiPut
} from './api'


export type TipoAutorizacao =
    | 'UNICA_ENTRADA'
    | 'PRAZO_DETERMINADO'
    | 'PRAZO_INDETERMINADO'


export type StatusAutorizacao =
    | 'ATIVA'
    | 'EXPIRADA'
    | 'REVOGADA'
    | 'UTILIZADA'


export type AutorizacaoRequest = {
    pessoaId: string
    proprietarioId: string
    unidadeId: string
    tipo: TipoAutorizacao
    inicioValidade: string
    fimValidade: string | null
    observacao: string | null
}


export type AutorizacaoResumo = {
    id: string

    pessoaId: string
    pessoaNome: string
    pessoaDocumento: string

    proprietarioId: string
    proprietarioNome: string

    unidadeId: string
    bloco: string
    numero: string

    tipo: TipoAutorizacao
    status: StatusAutorizacao

    inicioValidade: string | null
    fimValidade: string | null

    observacao: string | null

    criadoEm: string | null
    revogadoEm: string | null
}


type AutorizacaoRaw =
    Record<string, unknown>


function texto(
    valor: unknown
): string {

    if (
        typeof valor === 'string' ||
        typeof valor === 'number'
    ) {

        return String(valor)
    }

    return ''
}


function textoOuNull(
    valor: unknown
): string | null {

    const resultado =
        texto(valor)

    return resultado || null
}


function converterAutorizacao(
    autorizacao: AutorizacaoRaw
): AutorizacaoResumo {

    return {

        id:
            texto(autorizacao.id),

        pessoaId:
            texto(autorizacao.pessoaId),

        pessoaNome:
            texto(autorizacao.pessoaNome),

        pessoaDocumento:
            texto(
                autorizacao.pessoaDocumento
            ),

        proprietarioId:
            texto(
                autorizacao.proprietarioId
            ),

        proprietarioNome:
            texto(
                autorizacao.proprietarioNome
            ),

        unidadeId:
            texto(autorizacao.unidadeId),

        bloco:
            texto(autorizacao.bloco),

        numero:
            texto(autorizacao.numero),

        tipo:
            texto(
                autorizacao.tipo
            ) as TipoAutorizacao,

        status:
            texto(
                autorizacao.status
            ) as StatusAutorizacao,

        inicioValidade:
            textoOuNull(
                autorizacao.inicioValidade
            ),

        fimValidade:
            textoOuNull(
                autorizacao.fimValidade
            ),

        observacao:
            textoOuNull(
                autorizacao.observacao
            ),

        criadoEm:
            textoOuNull(
                autorizacao.criadoEm
            ),

        revogadoEm:
            textoOuNull(
                autorizacao.revogadoEm
            )
    }
}


export async function listarAutorizacoes():
    Promise<AutorizacaoResumo[]> {

    const dados =
        await apiGet<unknown>(
            '/autorizacoes'
        )


    let lista: AutorizacaoRaw[] = []


    if (Array.isArray(dados)) {

        lista =
            dados as AutorizacaoRaw[]

    } else if (
        dados &&
        typeof dados === 'object' &&
        'content' in dados &&
        Array.isArray(
            (
                dados as {
                    content?: unknown
                }
            ).content
        )
    ) {

        lista =
            (
                dados as {
                    content: AutorizacaoRaw[]
                }
            ).content
    }


    return lista.map(
        converterAutorizacao
    )
}


export async function cadastrarAutorizacao(
    dados: AutorizacaoRequest
):
    Promise<AutorizacaoResumo> {

    const resposta =
        await apiPost<AutorizacaoRaw>(
            '/autorizacoes',
            dados
        )


    return converterAutorizacao(
        resposta
    )
}


export async function revogarAutorizacao(
    id: string
):
    Promise<AutorizacaoResumo> {

    const resposta =
        await apiPut<AutorizacaoRaw>(
            `/autorizacoes/${id}/revogar`
        )


    return converterAutorizacao(
        resposta
    )
}