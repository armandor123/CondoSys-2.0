import { apiGet } from './api'


export type UnidadeResponse = {
    id: string

    proprietarioId: string

    proprietarioNome: string

    bloco: string

    numero: string

    andar: number

    ativa: boolean
}


export async function listarUnidades():
    Promise<UnidadeResponse[]> {

    return apiGet<
        UnidadeResponse[]
    >(
        '/unidades'
    )
}