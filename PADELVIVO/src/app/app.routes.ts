import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ListaJugadoresComponent } from './components/admin/lista-jugadores/lista-jugadores.component';
import { ListaPartidosComponent } from './components/admin/lista-partidos/lista-partidos.component';
import { ListaActualizacionesComponent } from './components/admin/lista-actualizaciones/lista-actualizaciones.component';

import { PerfilusuarioComponent } from './components/user/perfilusuario/perfilusuario.component';
import { ListaTorneosComponent } from './components/admin/lista-torneos/lista-torneos.component';
import { CardspartidosComponent } from './components/user/cardspartidos/cardspartidos.component';
import { CardstorneosComponent } from './components/user/cardstorneos/cardstorneos.component';
import { RegisterComponent } from './components/register/register.component';
import { CardstemporadasComponent } from './components/user/cardstemporadas/cardstemporadas.component';
import { ListaTemporadasComponent } from './components/admin/lista-temporadas/lista-temporadas.component';
import { CrearTorneoComponent } from './components/formularios/crear-torneo/crear-torneo.component';
import { CrearTemporadaComponent } from './components/formularios/crear-temporada/crear-temporada.component';
import { CrearJugadorComponent } from './components/formularios/crear-jugador/crear-jugador.component';
import { CrearPartidoComponent } from './components/formularios/crear-partido/crear-partido.component';
import { AdminGuard } from './guards/admin.guard';
import { UserGuard } from './guards/user.guard';
import { UnauthorizedComponent } from './components/unauthorized/unauthorized.component';
import { CardsFavoritosComponent } from './components/user/cardsfavoritos/cardsfavoritos.component';
import { ListaUsersComponent } from './components/admin/lista-users/lista-users.component';
import { CrearUserComponent } from './components/formularios/crear-user/crear-user.component';


export const appRoutes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'partidos', component: CardspartidosComponent },
  { path: 'torneos', component: CardstorneosComponent },
  { path: 'temporadas', component: CardstemporadasComponent },

  {
    path: 'admin',
    canActivate: [AdminGuard],
    children: [
      { path: 'jugadores', component: ListaJugadoresComponent },
      { path: 'crear-jugador', component: CrearJugadorComponent },
      { path: 'partidos', component: ListaPartidosComponent },
      { path: 'crear-partido', component: CrearPartidoComponent },
      { path: 'torneos', component: ListaTorneosComponent },
      { path: 'crear-torneo', component: CrearTorneoComponent },
      { path: 'temporadas', component: ListaTemporadasComponent },
      { path: 'crear-temporada', component: CrearTemporadaComponent },
      { path: 'actualizaciones', component: ListaActualizacionesComponent },
      {path: 'users', component: ListaUsersComponent},
      { path: 'crear-user', component: CrearUserComponent },
    ]
  },
  {
    path: 'user',
    canActivate: [UserGuard],
    children: [
      { path: 'favoritos', component: CardsFavoritosComponent },
      { path: 'perfil', component: PerfilusuarioComponent }
    ]
  },

  { path: 'unauthorized', component: UnauthorizedComponent },

  { path: '', redirectTo: 'torneos', pathMatch: 'full' },
  { path: '**', redirectTo: 'torneos' }
];
