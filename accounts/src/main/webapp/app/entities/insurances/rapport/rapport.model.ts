import dayjs from 'dayjs/esm';
import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { RapportType } from 'app/entities/enumerations/rapport-type.model';

export interface IRapport {
  id: number;
  titre?: string | null;
  description?: string | null;
  type?: keyof typeof RapportType | null;
  dateGeneration?: dayjs.Dayjs | null;
  url?: string | null;
  assurance?: Pick<IAssurance, 'id'> | null;
}

export type NewRapport = Omit<IRapport, 'id'> & { id: null };
