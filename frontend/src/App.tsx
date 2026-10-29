import {
    Navigate,
    Route,
    Routes
} from 'react-router'

import {
    AppLayout
} from './components/AppLayout'

import {
    DashboardPage
} from './pages/DashboardPage'

import {
    LoginPage
} from './pages/LoginPage'

import {
    ControleAcessoPage
} from './pages/ControleAcessoPage'

import {
    VisitantesPage
} from './pages/VisitantesPage'

import {
    EncomendasPage
} from './pages/EncomendasPage'

import {
    AutorizacoesPage
} from './pages/AutorizacoesPage'


function App() {

    return (
        <Routes>

            <Route
                path="/"
                element={
                    <LoginPage />
                }
            />


            <Route
                element={
                    <AppLayout />
                }
            >

                <Route
                    path="/dashboard"
                    element={
                        <DashboardPage />
                    }
                />


                <Route
                    path="/visitantes"
                    element={
                        <VisitantesPage />
                    }
                />


                <Route
                    path="/controle-acesso"
                    element={
                        <ControleAcessoPage />
                    }
                />


                <Route
                    path="/encomendas"
                    element={
                        <EncomendasPage />
                    }
                />


                <Route
                    path="/autorizacoes"
                    element={
                        <AutorizacoesPage />
                    }
                />

            </Route>


            <Route
                path="*"
                element={
                    <Navigate
                        to="/"
                        replace
                    />
                }
            />

        </Routes>
    )
}


export default App