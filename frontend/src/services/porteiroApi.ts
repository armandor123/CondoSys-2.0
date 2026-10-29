import { apiGet } from './api'


type PorteiroRaw =
    Record<string, unknown>


export type PorteiroResumo = {
    usuarioId: string
    nome: string
    email: string
    turno: string
    dataDemissao: string | null
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


function textoOuNull(
    valor: unknown
): string | null {

    const resultado =
        texto(valor)

    return resultado || null
}


export async function listarPorteiros():
    Promise<PorteiroResumo[]> {

    const dados =
        await apiGet<PorteiroRaw[]>(
            '/porteiros'
        )


    return dados.map((porteiro) => ({

        usuarioId:
            texto(porteiro.usuarioId),

        nome:
            texto(porteiro.nome),

        email:
            texto(porteiro.email),

        turno:
            texto(porteiro.turno),

        dataDemissao:
            textoOuNull(
                porteiro.dataDemissao
            )
    }))
}