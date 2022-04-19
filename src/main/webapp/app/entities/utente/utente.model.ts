import { IOrdine } from 'app/entities/ordine/ordine.model';
import { IStoricoOrdini } from 'app/entities/storico-ordini/storico-ordini.model';

export interface IUtente {
  id?: number;
  ordines?: IOrdine[] | null;
  storicoOrdinis?: IStoricoOrdini[] | null;
}

export class Utente implements IUtente {
  constructor(public id?: number, public ordines?: IOrdine[] | null, public storicoOrdinis?: IStoricoOrdini[] | null) {}
}

export function getUtenteIdentifier(utente: IUtente): number | undefined {
  return utente.id;
}
