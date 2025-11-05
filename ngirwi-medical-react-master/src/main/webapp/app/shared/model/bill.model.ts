import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';
import { IBillElement } from 'app/shared/model/bill-element.model';

export interface IBill {
  id?: number;
  date?: string | null;
  author?: string | null;
  insurance?: string | null;
  desc?: string | null;
  ipm?: string | null;
  total?: number | null;
  patient?: IPatient | null;
  billElements?: IBillElement[] | null;
}

export const defaultValue: Readonly<IBill> = {};
