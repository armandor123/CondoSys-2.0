const API_URL =
    import.meta.env.VITE_API_URL


async function tratarResposta<T>(
    response: Response
): Promise<T> {

    if (!response.ok) {

        let mensagem =
            `Erro na API: ${response.status}`

        try {

            const erro = await response.json()

            if (
                erro &&
                typeof erro === 'object' &&
                'erro' in erro &&
                typeof erro.erro === 'string'
            ) {
                mensagem = erro.erro
            }

        } catch {
            // Mantém a mensagem padrão.
        }

        throw new Error(mensagem)
    }


    // DELETE ou respostas sem conteúdo.
    if (response.status === 204) {
        return undefined as T
    }


    return response.json()
}


export async function apiGet<T>(
    caminho: string
): Promise<T> {

    const response = await fetch(
        `${API_URL}${caminho}`,
        {
            method: 'GET',

            headers: {
                Accept: 'application/json'
            }
        }
    )


    return tratarResposta<T>(response)
}


export async function apiPost<T>(
    caminho: string,
    dados: unknown
): Promise<T> {

    const response = await fetch(
        `${API_URL}${caminho}`,
        {
            method: 'POST',

            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json'
            },

            body: JSON.stringify(dados)
        }
    )


    return tratarResposta<T>(response)
}


export async function apiPut<T>(
    caminho: string,
    dados?: unknown
): Promise<T> {

    const response = await fetch(
        `${API_URL}${caminho}`,
        {
            method: 'PUT',

            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json'
            },

            body:
                dados !== undefined
                    ? JSON.stringify(dados)
                    : undefined
        }
    )


    return tratarResposta<T>(response)
}


export async function apiDelete(
    caminho: string
): Promise<void> {

    const response = await fetch(
        `${API_URL}${caminho}`,
        {
            method: 'DELETE'
        }
    )


    await tratarResposta<void>(response)
}