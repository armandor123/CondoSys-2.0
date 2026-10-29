import {
    apiGet,
    apiPost
} from './api'


export type TipoMovimentoAcesso =
    'ENTRADA' |
    'SAIDA'


export type StatusAcesso =
    'AUTORIZADO' |
    'NEGADO'


export type RegistroAcessoRequest = {

    pessoaId: string

    unidadeId: string

    porteiroId: string

    autorizacaoId:
        string | null

    tipoMovimento:
        TipoMovimentoAcesso

    status:
        StatusAcesso

    liberacaoManual:
        boolean

    motivoLiberacao:
        string | null

    observacao:
        string | null
}


export type RegistroAcessoResponse = {

    id: string

    pessoaId: string

    pessoaNome: string

    pessoaDocumento: string

    unidadeId: string

    bloco: string

    numero: string

    porteiroId: string

    porteiroNome: string

    autorizacaoId:
        string | null

    tipoMovimento:
        TipoMovimentoAcesso

    status:
        StatusAcesso

    liberacaoManual:
        boolean

    motivoLiberacao:
        string | null

    observacao:
        string | null

    dataHora: string
}


export async function registrarAcesso(
    dados: RegistroAcessoRequest
):
    Promise<RegistroAcessoResponse> {

    return apiPost<
        RegistroAcessoResponse
    >(
        '/registros-acesso',
        dados
    )
}


export async function listarRegistrosPessoa(
    pessoaId: string
):
    Promise<RegistroAcessoResponse[]> {

    return apiGet<
        RegistroAcessoResponse[]
    >(
        `/registros-acesso/pessoa/${encodeURIComponent(pessoaId)}`
    )
}