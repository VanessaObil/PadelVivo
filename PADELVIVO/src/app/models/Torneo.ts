import { Temporada } from "./Temporada";

export interface Torneo {
  name: string;
  location: string;
  country: string;
  level: string;
  status: string;
  startDate: string;   // en formato 'YYYY-MM-DD'
  endDate: string;
  temporada: Temporada;
}
