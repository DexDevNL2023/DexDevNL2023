import dayjs from 'dayjs/esm';
import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';

export interface IPolicesAssurances {
  id: number;
  numeroPolice?: string | null;
  dateEmission?: dayjs.Dayjs | null;
  dateExpiration?: dayjs.Dayjs | null;
  description?: string | null;
  montantAssure?: number | null;
  plafondAssure?: number | null;
  assurance?: Pick<IAssurance, 'id'> | null;
}

export type NewPolicesAssurances = Omit<IPolicesAssurances, 'id'> & { id: null };
