import {
    CalendarDays,
    CircleAlert,
    ClipboardCheck,
    Info,
    Package,
    Users,
    Wrench
} from 'lucide-react'

import '../styles/dashboard.css'


export function DashboardPage() {

    const indicadores = [
        {
            titulo: 'Visitantes dentro do condomínio',
            valor: 12,
            icon: Users,
            classe: 'blue'
        },
        {
            titulo: 'Entregas hoje',
            valor: 48,
            icon: Package,
            classe: 'green'
        },
        {
            titulo: 'Encomendas aguardando retirada',
            valor: 5,
            icon: ClipboardCheck,
            classe: 'orange'
        },
        {
            titulo: 'Reservas do dia',
            valor: 3,
            icon: CalendarDays,
            classe: 'blue'
        },
        {
            titulo: 'Ocorrências abertas',
            valor: 2,
            icon: CircleAlert,
            classe: 'red'
        },
        {
            titulo: 'Manutenções em andamento',
            valor: 4,
            icon: Wrench,
            classe: 'yellow'
        }
    ]


    return (
        <main className="dashboard-content">

            {/* Título */}
            <div className="dashboard-title">

                <h1>
                    Dashboard
                </h1>

                <p>
                    Visão geral do condomínio
                </p>

            </div>


            {/* Indicadores */}
            <section className="dashboard-cards">

                {indicadores.map((indicador) => {

                    const Icon = indicador.icon

                    return (
                        <article
                            key={indicador.titulo}
                            className="dashboard-card"
                        >

                            <div
                                className={
                                    `card-icon ${indicador.classe}`
                                }
                            >
                                <Icon size={25} />
                            </div>


                            <div className="card-information">

                <span>
                  {indicador.titulo}
                </span>

                                <strong>
                                    {indicador.valor}
                                </strong>

                            </div>

                        </article>
                    )
                })}

            </section>


            {/* Painéis */}
            <section className="dashboard-panels">

                {/* Últimas entradas */}
                <article className="dashboard-panel entries-panel">

                    <div className="panel-header">

                        <h2>
                            Últimas entradas
                        </h2>

                        <button type="button">
                            Ver todas →
                        </button>

                    </div>


                    <div className="table-container">

                        <table>

                            <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Tipo</th>
                                <th>Unidade</th>
                                <th>Horário</th>
                            </tr>
                            </thead>

                            <tbody>

                            <tr>
                                <td>Carla Silva</td>
                                <td>Visitante</td>
                                <td>1101</td>
                                <td>14:32</td>
                            </tr>

                            <tr>
                                <td>Mariana Costa</td>
                                <td>Morador</td>
                                <td>0103</td>
                                <td>14:18</td>
                            </tr>

                            <tr>
                                <td>João Pereira</td>
                                <td>Prestador</td>
                                <td>A101</td>
                                <td>13:55</td>
                            </tr>

                            <tr>
                                <td>Luciana Souza</td>
                                <td>Visitante</td>
                                <td>11201</td>
                                <td>13:40</td>
                            </tr>

                            <tr>
                                <td>Roberto Alves</td>
                                <td>Funcionário</td>
                                <td>C201</td>
                                <td>13:20</td>
                            </tr>

                            </tbody>

                        </table>

                    </div>

                </article>


                {/* Próximas reservas */}
                <article className="dashboard-panel reservations-panel">

                    <div className="panel-header">

                        <h2>
                            Próximas reservas
                        </h2>

                        <button type="button">
                            Ver todas →
                        </button>

                    </div>


                    <div className="table-container">

                        <table>

                            <thead>
                            <tr>
                                <th>Data</th>
                                <th>Horário</th>
                                <th>Área</th>
                            </tr>
                            </thead>

                            <tbody>

                            <tr>
                                <td>28/04</td>
                                <td>10:00 - 12:00</td>
                                <td>Salão de Festas</td>
                            </tr>

                            <tr>
                                <td>28/04</td>
                                <td>14:00 - 16:00</td>
                                <td>Churrasqueira</td>
                            </tr>

                            <tr>
                                <td>29/04</td>
                                <td>09:00 - 11:00</td>
                                <td>Academia</td>
                            </tr>

                            <tr>
                                <td>29/04</td>
                                <td>18:00 - 20:00</td>
                                <td>Salão de Jogos</td>
                            </tr>

                            </tbody>

                        </table>

                    </div>

                </article>


                {/* Comunicados */}
                <article className="dashboard-panel notices-panel">

                    <div className="panel-header">

                        <h2>
                            Comunicados recentes
                        </h2>

                        <button type="button">
                            Ver todos →
                        </button>

                    </div>


                    <div className="notice-list">

                        <div className="notice-item">

                            <Info size={20} />

                            <div>
                                <strong>
                                    Manutenção no elevador
                                </strong>

                                <span>
                  Hoje, 10:32
                </span>
                            </div>

                        </div>


                        <div className="notice-item">

                            <Info size={20} />

                            <div>
                                <strong>
                                    Assembleia Geral Ordinária
                                </strong>

                                <span>
                  Ontem, 18:10
                </span>
                            </div>

                        </div>


                        <div className="notice-item">

                            <Info size={20} />

                            <div>
                                <strong>
                                    Nova política de visitantes
                                </strong>

                                <span>
                  22/04, 09:22
                </span>
                            </div>

                        </div>


                        <div className="notice-item">

                            <Info size={20} />

                            <div>
                                <strong>
                                    Limpeza da área comum
                                </strong>

                                <span>
                  20/04, 16:45
                </span>
                            </div>

                        </div>

                    </div>

                </article>

            </section>

        </main>
    )
}