import {
    Bell,
    ChevronDown,
    ClipboardCheck,
    Home,
    Menu,
    Package,
    ShieldCheck,
    UserCircle,
    Users
} from 'lucide-react'

import {
    Outlet,
    useLocation
} from 'react-router'

import '../styles/app-layout.css'


export function AppLayout() {

    const location = useLocation()


    const menuItems = [
        {
            nome: 'Dashboard',
            caminho: '/dashboard',
            icon: Home
        },
        {
            nome: 'Visitantes',
            caminho: '/visitantes',
            icon: Users
        },
        {
            nome: 'Controle de Acesso',
            caminho: '/controle-acesso',
            icon: ShieldCheck
        },
        {
            nome: 'Autorizações',
            caminho: '/autorizacoes',
            icon: ClipboardCheck
        },
        {
            nome: 'Encomendas',
            caminho: '/encomendas',
            icon: Package
        }
    ]


    return (
        <div className="app-layout">

            {/* Menu lateral */}
            <aside className="app-sidebar">

                <div className="app-sidebar-logo">
                    CondoSys
                </div>


                <nav className="app-sidebar-menu">

                    {menuItems.map((item) => {

                        const Icon = item.icon

                        const ativo =
                            location.pathname ===
                            item.caminho

                        return (
                            <a
                                key={item.caminho}
                                href={item.caminho}
                                className={
                                    ativo
                                        ? 'app-sidebar-item active'
                                        : 'app-sidebar-item'
                                }
                            >

                                <Icon size={22} />

                                <span>
                                    {item.nome}
                                </span>

                            </a>
                        )
                    })}

                </nav>

            </aside>


            {/* Parte direita */}
            <div className="app-main">

                {/* Barra superior */}
                <header className="app-header">

                    <button
                        type="button"
                        className="app-menu-button"
                        aria-label="Abrir menu"
                    >
                        <Menu size={28} />
                    </button>


                    <div className="app-header-actions">

                        <button
                            type="button"
                            className="app-notification-button"
                            aria-label="Notificações"
                        >
                            <Bell size={24} />

                            <span className="app-notification-dot" />
                        </button>


                        <div className="app-user-area">

                            <UserCircle size={38} />

                            <div className="app-user-info">

                                <strong>
                                    Administrador
                                </strong>

                                <span>
                                    Administrador
                                </span>

                            </div>

                            <ChevronDown size={18} />

                        </div>

                    </div>

                </header>


                {/* Conteúdo da página atual */}
                <div className="app-page-content">

                    <Outlet />

                </div>

            </div>

        </div>
    )
}