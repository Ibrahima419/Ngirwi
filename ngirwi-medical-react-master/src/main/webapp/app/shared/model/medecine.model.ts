import { IPrescription } from 'app/shared/model/prescription.model';

export interface IMedecine {
  id?: number;
  name?: string | null;
  duration?: number | null;
  frequency?: number | null;
  ordonance?: IPrescription | null;
}

export const defaultValue: Readonly<IMedecine> = {};
