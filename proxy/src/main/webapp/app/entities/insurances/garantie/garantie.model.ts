import dayjs from 'dayjs/esm';
import { IAssurance } from 'app/entities/insurances/assurance/assurance.model';
import { GarantieStatus } from 'app/entities/enumerations/garantie-status.model';

export interface IGarantie {
  id: number;
  description?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  status?: keyof typeof GarantieStatus | null;
  assurances?: Pick<IAssurance, 'id'>[] | null;
}

export type NewGarantie = Omit<IGarantie, 'id'> & { id: null };
