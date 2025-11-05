import { IConsultation } from './consultation.model';
import { IMedecine } from './medecine.model';
import { IMedecineForm } from './medecineForm.model';

export interface IPrescriptionForm {
  id?: number;
  creationDate?: string | null;
  author?: string | null;
  form?: IMedecineForm[] | null;
  consultation?: IConsultation | null;
  // medecines?: IMedecine[] | null;
}

export const defaultValue: Readonly<IPrescriptionForm> = {};
