import {
    apiGet,
    apiPost,
    apiPut
} from './api'


export type StatusEncomenda =
    | 'RECEBIDA'
    | 'AGUARDANDO_RETIRADA'
    | 'ENTREGUE'


export type EncomendaRequest = {

    unidadeId: string

    porteiroId: string

    remetente: string | null

    destinatario: string

    codigoRastreio: string | null

    descricao: string | null
}


export type EncomendaResponse = {

    id: string

    unidadeId: string

    bloco: string

    numero: string

    porteiroId: string

    porteiroNome: string

    remetente: string | null

    destinatario: string

    codigoRastreio: string | null

    descricao: string | null

    recebidoEm: string

    retiradoEm: string | null

    status: StatusEncomenda

    retiradoPor: string | null
}


export async function listarEncomendas():
    Promise<EncomendaResponse[]> {

    return apiGet<
        EncomendaResponse[]
    >(
        '/encomendas'
    )
}


export async function cadastrarEncomenda(
    dados: EncomendaRequest
):
    Promise<EncomendaResponse> {

    return apiPost<
        EncomendaResponse
    >(
        '/encomendas',
        dados
    )
}


export async function marcarAguardandoRetirada(
    id: string
):
    Promise<EncomendaResponse> {

    return apiPut<
        EncomendaResponse
    >(
        `/encomendas/${id}/aguardando-retirada`
    )
}


export async function entregarEncomenda(
    id: string,
    retiradoPor: string
):
    Promise<EncomendaResponse> {

    return apiPut<
        EncomendaResponse
    >(
        `/encomendas/${id}/entregar`,
        {
            retiradoPor
        }
    )
}