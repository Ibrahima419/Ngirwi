import { HospitalisationStatus } from './enumerations/hospitalisation-status.model';
import { IPatient } from './patient.model';

export interface IHospitalisationForm {
  id?: number;
  entryDate?: string | null;
  releaseDate?: string | null;
  doctorName?: string | null;
  status?: HospitalisationStatus | null;
  patient?: IPatient | null;
  ta?: string | null;
  temperature?: string | null;
  pulseRate?: string | null;
  respiratoryFrequency?: string | null;
  recolorationTime?: string | null;
  glasgow?: string | null;
  gravityClass?: string | null;
  horaryDiuresis?: string | null;
  spo2?: string | null;
  treatment?: string | null;
  healthEvolution?: string | null;
  sheetDate?: string | null;
}

export const defaultValue: Readonly<IHospitalisationForm> = {};
