import {
    Bell,
    ChevronDown,
    ClipboardCheck,
    Clock3,
    HardHat,
    Home,
    Menu,
    Package,
    Settings,
    ShieldCheck,
    UserCircle,
    UserCog,
    Users
} from 'lucide-react'

import {
    NavLink,
    Outlet
} from 'react-router'

import '../styles/app-layout.css'


export function AppLayout() {

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
            nome: 'Prestadores',
            caminho: '/prestadores',
            icon: HardHat
        },
        {
            nome: 'Controle de Acesso',
            caminho: '/controle-acesso',
            icon: ShieldCheck
        },
        {
            nome: 'Registro de Entrada e Saída',
            caminho: '/registros-acesso',
            icon: Clock3
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
        },
        {
            nome: 'Usuários e Permissões',
            caminho: '/usuarios',
            icon: UserCog
        },
        {
            nome: 'Configurações',
            caminho: '/configuracoes',
            icon: Settings
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

                        return (
                            <NavLink
                                key={item.caminho}
                                to={item.caminho}
                                className={({ isActive }) =>
                                    isActive
                                        ? 'app-sidebar-item active'
                                        : 'app-sidebar-item'
                                }
                            >

                                <Icon size={22} />

                                <span>
                  {item.nome}
                </span>

                            </NavLink>
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


                {/* Aqui entra Dashboard, Autorizações etc. */}
                <div className="app-page-content">

                    <Outlet />

                </div>

            </div>

        </div>
    )
}