export interface IMiniConsultation {
  id?: number;
  summary?: string | null;
  diagnosis?: string | null;
  price?: number | string | null;
  surveillanceSheetId?: number;
  createdDate?: string | null;
  authorName?: string | null;
}

export const defaultValue: Readonly<IMiniConsultation> = {};
