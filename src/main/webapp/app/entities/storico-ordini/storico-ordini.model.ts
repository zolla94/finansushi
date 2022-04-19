import { IPiatto } from 'app/entities/piatto/piatto.model';
import { IUtente } from 'app/entities/utente/utente.model';
import { ILocale } from 'app/entities/locale/locale.model';

export interface IStoricoOrdini {
  id?: number;
  note?: string | null;
  piatto?: IPiatto | null;
  utente?: IUtente | null;
  locale?: ILocale | null;
}

export class StoricoOrdini implements IStoricoOrdini {
  constructor(
    public id?: number,
    public note?: string | null,
    public piatto?: IPiatto | null,
    public utente?: IUtente | null,
    public locale?: ILocale | null
  ) {}
}

export function getStoricoOrdiniIdentifier(storicoOrdini: IStoricoOrdini): number | undefined {
  return storicoOrdini.id;
}
