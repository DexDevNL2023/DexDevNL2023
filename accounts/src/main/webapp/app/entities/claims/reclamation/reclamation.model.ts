import dayjs from 'dayjs/esm';
import { ISinistre } from 'app/entities/claims/sinistre/sinistre.model';
import { ReclamationStatus } from 'app/entities/enumerations/reclamation-status.model';

export interface IReclamation {
  id: number;
  numeroReclamation?: string | null;
  dateReclamation?: dayjs.Dayjs | null;
  description?: string | null;
  status?: keyof typeof ReclamationStatus | null;
  sinistre?: Pick<ISinistre, 'id'> | null;
}

export type NewReclamation = Omit<IReclamation, 'id'> & { id: null };
