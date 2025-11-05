export interface IHospital {
  id?: number;
  name?: string | null;
  adress?: string | null;
  phone?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
}

export const defaultValue: Readonly<IHospital> = {};
