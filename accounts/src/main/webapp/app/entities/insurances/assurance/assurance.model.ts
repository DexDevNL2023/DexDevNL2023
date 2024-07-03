import dayjs from 'dayjs/esm';
import { IGarantie } from 'app/entities/insurances/garantie/garantie.model';
import { InsuranceType } from 'app/entities/enumerations/insurance-type.model';

export interface IAssurance {
  id: number;
  nom?: string | null;
  description?: string | null;
  conditions?: string | null;
  type?: keyof typeof InsuranceType | null;
  prixAssurance?: number | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  garanties?: Pick<IGarantie, 'id'>[] | null;
}

export type NewAssurance = Omit<IAssurance, 'id'> & { id: null };
