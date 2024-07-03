import dayjs from 'dayjs/esm';
import { IUserExtra } from 'app/entities/accounts/user-extra/user-extra.model';

export interface IMedicalRecord {
  id: number;
  numMedicalRecord?: string | null;
  details?: string | null;
  dateUpdated?: dayjs.Dayjs | null;
  utilisateur?: Pick<IUserExtra, 'id'> | null;
}

export type NewMedicalRecord = Omit<IMedicalRecord, 'id'> & { id: null };
