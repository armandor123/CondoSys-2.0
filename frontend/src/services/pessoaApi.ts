import {
    apiGet,
    apiPost
} from './api'


type PessoaRaw =
    Record<string, unknown>


export type TipoPessoa =
    | 'VISITANTE'
    | 'PROPRIETARIO'
    | 'PORTEIRO'
    | 'ADMINISTRADOR'
    | 'PRESTADOR_SERVICO'


export type PessoaResumo = {
    id: string
    nome: string
    documento: string
    telefone: string | null
    tipoPessoa: string | null
    ativo: boolean
}


export type PessoaRequest = {
    nome: string
    documento: string
    telefone: string | null
    tipoPessoa: TipoPessoa
}


export type PessoaResponse = {
    id: string
    nome: string
    documento: string
    telefone: string | null
    tipoPessoa: TipoPessoa
    ativo: boolean
    criadoEm: string
    atualizadoEm: string
}


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


export async function listarPessoas():
    Promise<PessoaResumo[]> {

    const dados =
        await apiGet<unknown>(
            '/pessoas'
        )


    console.log(
        'Resposta real de /pessoas:',
        dados
    )


    let lista: PessoaRaw[] = []


    if (Array.isArray(dados)) {

        lista =
            dados as PessoaRaw[]

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
                    content: PessoaRaw[]
                }
            ).content
    }


    return lista.map((pessoa) => ({

        id:
            texto(pessoa.id) ||
            texto(pessoa.pessoaId),

        nome:
            texto(pessoa.nome) ||
            texto(pessoa.nomeCompleto),

        documento:
            texto(pessoa.documento) ||
            texto(pessoa.cpf),

        telefone:
            texto(pessoa.telefone) ||
            null,

        tipoPessoa:
            texto(pessoa.tipoPessoa) ||
            texto(pessoa.tipo) ||
            null,

        ativo:
            typeof pessoa.ativo ===
            'boolean'
                ? pessoa.ativo
                : true
    }))
}


export async function cadastrarPessoa(
    dados: PessoaRequest
):
    Promise<PessoaResponse> {

    return apiPost<PessoaResponse>(
        '/pessoas',
        dados
    )
}