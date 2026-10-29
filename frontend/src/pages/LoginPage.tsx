import { useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from 'react-router'
import {
    Eye,
    EyeOff,
    LockKeyhole,
    UserRound,
    CircleAlert
} from 'lucide-react'

import '../styles/login.css'

export function LoginPage() {

    const [usuario, setUsuario] = useState('')
    const [senha, setSenha] = useState('')
    const [mostrarSenha, setMostrarSenha] = useState(false)
    const [erro, setErro] = useState('')
    const navigate = useNavigate()

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()

        setErro('')

        // Login temporário somente para demonstração do MVP.
        if (
            usuario === 'admin@condosys.local' &&
            senha === '123456'
        ) {

            navigate(
                '/dashboard',
                { replace: true }
            )

            return
        }

        setErro('Usuário ou senha incorretos.')
    }


    return (
        <main className="login-page">

            {/* Área visual do CondoSys */}
            <section className="login-image">

                <div className="login-brand">
                    <span className="login-step">1.</span>
                    <span>Login</span>
                </div>

                <div className="login-presentation">
                    <h1>CondoSys</h1>

                    <p>
                        Mais segurança, organização e
                        tranquilidade para o seu condomínio.
                    </p>
                </div>

            </section>


            {/* Área do formulário */}
            <section className="login-content">

                <div className="login-form-container">

                    <h2>Bem-vindo ao CondoSys</h2>

                    <p className="login-subtitle">
                        Faça login para acessar o sistema.
                    </p>


                    <form onSubmit={handleSubmit}>

                        {/* Usuário */}
                        <div className="form-group">

                            <label htmlFor="usuario">
                                E-mail ou usuário
                            </label>

                            <div className="input-with-icon">

                                <UserRound
                                    size={21}
                                    className="input-icon"
                                />

                                <input
                                    id="usuario"
                                    type="text"
                                    value={usuario}
                                    onChange={(event) =>
                                        setUsuario(event.target.value)
                                    }
                                    placeholder="Digite seu e-mail ou usuário"
                                    autoComplete="username"
                                />

                            </div>

                        </div>


                        {/* Senha */}
                        <div className="form-group">

                            <label htmlFor="senha">
                                Senha
                            </label>

                            <div className="input-with-icon password-field">

                                <LockKeyhole
                                    size={21}
                                    className="input-icon"
                                />

                                <input
                                    id="senha"
                                    type={mostrarSenha ? 'text' : 'password'}
                                    value={senha}
                                    onChange={(event) =>
                                        setSenha(event.target.value)
                                    }
                                    placeholder="Digite sua senha"
                                    autoComplete="current-password"
                                />

                                <button
                                    type="button"
                                    className="password-toggle"
                                    aria-label={
                                        mostrarSenha
                                            ? 'Ocultar senha'
                                            : 'Mostrar senha'
                                    }
                                    onClick={() =>
                                        setMostrarSenha(!mostrarSenha)
                                    }
                                >
                                    {mostrarSenha ? (
                                        <EyeOff size={22} />
                                    ) : (
                                        <Eye size={22} />
                                    )}
                                </button>

                            </div>

                        </div>


                        {/* Opções */}
                        <div className="login-options">

                            <label className="show-password">

                                <input
                                    type="checkbox"
                                    checked={mostrarSenha}
                                    onChange={() =>
                                        setMostrarSenha(!mostrarSenha)
                                    }
                                />

                                Mostrar senha

                            </label>


                            <button
                                type="button"
                                className="forgot-password"
                            >
                                Esqueci minha senha
                            </button>

                        </div>


                        {/* Botão principal */}
                        <button
                            type="submit"
                            className="login-button"
                        >
                            Entrar
                        </button>


                        {/* Mensagem de erro */}
                        {erro && (
                            <div className="login-error">

                                <CircleAlert size={22} />

                                <span>{erro}</span>

                            </div>
                        )}

                    </form>

                </div>

            </section>

        </main>
    )
}