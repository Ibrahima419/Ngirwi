import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';
import { IDossierMedical } from 'app/shared/model/dossier-medical.model';
import { ISurveillanceSheet } from 'app/shared/model/surveillance-sheet.model';
import { HospitalisationStatus } from 'app/shared/model/enumerations/hospitalisation-status.model';

export interface IHospitalisation {
  id?: number;
  entryDate?: string | null;
  releaseDate?: string | null;
  doctorName?: string | null;
  status?: HospitalisationStatus | null;
  // Details & billing (kept optional to match backend DTO flexibility)
  service?: string | null;
  admissionReason?: string | null;
  entryDiagnosis?: string | null;
  finalDiagnosis?: string | null;
  dailyRate?: number | null;
  comfortFees?: number | null;
  feeOverrun?: number | null;
  insuranceCoveragePercent?: number | null;
  totalAmount?: number | null;
  patient?: IPatient | null;
  dossierMedical?: IDossierMedical | null;
  dossierMedicalId?: number | null;
  patientId?: number | null;
  surveillanceSheets?: ISurveillanceSheet[] | null;
}

export const defaultValue: Readonly<IHospitalisation> = {};
