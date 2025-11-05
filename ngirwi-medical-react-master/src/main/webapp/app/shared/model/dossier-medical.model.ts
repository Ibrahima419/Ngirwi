import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';

export interface IDossierMedical {
  id?: number;
  motifConsultation?: string;
  histoireMaladie?: string;
  terrain?: string;
  antecedantsPersonnels?: string;
  antecedantsChirurgicaux?: string;
  antecedantsFamiliaux?: string;
  gynecoObstretrique?: string | null;
  syndromique?: string;
  dad?: string | null;
  mom?: string | null;
  siblings?: string | null;
  descendants?: string | null;
  dateCreated?: string | null;
  dateUpdated?: string | null;
  author?: string | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IDossierMedical> = {};
