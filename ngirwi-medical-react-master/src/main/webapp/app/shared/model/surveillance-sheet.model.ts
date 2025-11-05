import { IHospitalisation } from 'app/shared/model/hospitalisation.model';

export interface MedicationEntry {
  name: string;
  unitPrice: number | string;
  quantity: number;
  total?: number | string;
}

export interface ActEntry {
  name: string;
  unitPrice: number | string;
  quantity: number;
  total?: number | string;
}

export interface ISurveillanceSheet {
  id?: number;
  sheetDate?: string | null; // ISO LocalDate
  createdDate?: string | null; // timestamp when saved
  hospitalisationId?: number | null;
  patientId?: number | null;
  temperature?: number | string | null;
  systolicBP?: number | null;
  diastolicBP?: number | null;
  pulseRate?: number | null;
  respirationRate?: number | null;
  spo2?: number | null;
  medicalObservations?: string | null;
  nursingNotes?: string | null;
  medications?: MedicationEntry[] | null;
  acts?: ActEntry[] | null;
  authorName?: string | null;
  total?: number | string | null;
  hospitalisation?: IHospitalisation | null; // for legacy views
}

export const defaultValue: Readonly<ISurveillanceSheet> = {};
