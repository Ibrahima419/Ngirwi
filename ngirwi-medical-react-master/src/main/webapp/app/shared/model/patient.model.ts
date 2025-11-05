import dayjs from 'dayjs';
import { IDossierMedical } from 'app/shared/model/dossier-medical.model';
import { IConsultation } from 'app/shared/model/consultation.model';
import { GENDER } from 'app/shared/model/enumerations/gender.model';
import { BLOODTYPE } from 'app/shared/model/enumerations/bloodtype.model';
import { MARITALSTATUS } from 'app/shared/model/enumerations/maritalstatus.model';

export interface IPatient {
  id?: number;
  firstName?: string;
  lastName?: string;
  birthday?: string;
  birthplace?: string | null;
  gender?: GENDER;
  adress?: string;
  phone?: string;
  cni?: string;
  job?: string | null;
  bloodType?: BLOODTYPE | null;
  maritialStatus?: MARITALSTATUS | null;
  dateCreated?: string | null;
  dateUpdated?: string | null;
  author?: string | null;
  dossierMedical?: IDossierMedical | null;
  consultations?: IConsultation[] | null;
  hospitalId?: number;
}

export const defaultValue: Readonly<IPatient> = {};
