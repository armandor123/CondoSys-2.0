import {
    apiGet,
    apiPost,
    apiPut
} from './api'


export type VisitanteResponse = {
    pessoaId: string
    nome: string
    documento: string
    telefone: string | null
    observacao: string | null
}


export type VisitanteRequest = {
    pessoaId: string
    observacao: string | null
}


export type VisitanteObservacaoRequest = {
    observacao: string | null
}


export async function listarVisitantes():
    Promise<VisitanteResponse[]> {

    return apiGet<
        VisitanteResponse[]
    >(
        '/visitantes'
    )
}


export async function cadastrarVisitante(
    dados: VisitanteRequest
):
    Promise<VisitanteResponse> {

    return apiPost<
        VisitanteResponse
    >(
        '/visitantes',
        dados
    )
}


export async function atualizarObservacaoVisitante(
    pessoaId: string,
    observacao: string | null
):
    Promise<VisitanteResponse> {

    return apiPut<
        VisitanteResponse
    >(
        `/visitantes/${pessoaId}/observacao`,
        {
            observacao
        }
    )
}