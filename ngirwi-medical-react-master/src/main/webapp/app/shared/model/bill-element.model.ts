import { IBill } from 'app/shared/model/bill.model';

export interface IBillElement {
  id?: number;
  name?: string | null;
  price?: number | null;
  percentage?: number | null;
  quantity?: number | null;
  bill?: IBill | null;
}

export const defaultValue: Readonly<IBillElement> = {};
