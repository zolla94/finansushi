import { IPiatto } from 'app/entities/piatto/piatto.model';
import { ILocale } from 'app/entities/locale/locale.model';
import { IUtente } from 'app/entities/utente/utente.model';

export interface IOrdine {
  id?: number;
  piatto?: IPiatto | null;
  locale?: ILocale | null;
  utente?: IUtente | null;
}

export class Ordine implements IOrdine {
  constructor(public id?: number, public piatto?: IPiatto | null, public locale?: ILocale | null, public utente?: IUtente | null) {}
}

export function getOrdineIdentifier(ordine: IOrdine): number | undefined {
  return ordine.id;
}
