import { IOrdine } from 'app/entities/ordine/ordine.model';
import { IStoricoOrdini } from 'app/entities/storico-ordini/storico-ordini.model';
import { IPiatto } from 'app/entities/piatto/piatto.model';

export interface ILocale {
  id?: number;
  nome?: string | null;
  ordines?: IOrdine[] | null;
  storicoOrdinis?: IStoricoOrdini[] | null;
  piattos?: IPiatto[] | null;
}

export class Locale implements ILocale {
  constructor(
    public id?: number,
    public nome?: string | null,
    public ordines?: IOrdine[] | null,
    public storicoOrdinis?: IStoricoOrdini[] | null,
    public piattos?: IPiatto[] | null
  ) {}
}

export function getLocaleIdentifier(locale: ILocale): number | undefined {
  return locale.id;
}
