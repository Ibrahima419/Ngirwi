import dayjs from 'dayjs';
import { IConsultation } from 'app/shared/model/consultation.model';
import { IMedecine } from 'app/shared/model/medecine.model';

export interface IPrescription {
  id?: number;
  creationDate?: string | null;
  author?: string | null;
  consultation?: IConsultation | null;
  medecines?: IMedecine[] | null;
}

export const defaultValue: Readonly<IPrescription> = {};
