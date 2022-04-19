import { IOrdine } from 'app/entities/ordine/ordine.model';
import { IStoricoOrdini } from 'app/entities/storico-ordini/storico-ordini.model';
import { ILocale } from 'app/entities/locale/locale.model';

export interface IPiatto {
  id?: number;
  codice?: string | null;
  descrizione?: string | null;
  url?: string | null;
  spicy?: boolean | null;
  vegan?: boolean | null;
  limiteOrdine?: boolean | null;
  ordine?: IOrdine | null;
  storicoOrdini?: IStoricoOrdini | null;
  locale?: ILocale | null;
}

export class Piatto implements IPiatto {
  constructor(
    public id?: number,
    public codice?: string | null,
    public descrizione?: string | null,
    public url?: string | null,
    public spicy?: boolean | null,
    public vegan?: boolean | null,
    public limiteOrdine?: boolean | null,
    public ordine?: IOrdine | null,
    public storicoOrdini?: IStoricoOrdini | null,
    public locale?: ILocale | null
  ) {
    this.spicy = this.spicy ?? false;
    this.vegan = this.vegan ?? false;
    this.limiteOrdine = this.limiteOrdine ?? false;
  }
}

export function getPiattoIdentifier(piatto: IPiatto): number | undefined {
  return piatto.id;
}
