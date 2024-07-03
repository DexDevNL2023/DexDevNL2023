import dayjs from 'dayjs/esm';
import { ISinistre } from 'app/entities/claims/sinistre/sinistre.model';

export interface IDocumentDeSinistre {
  id: number;
  nom?: string | null;
  type?: string | null;
  url?: string | null;
  dateTelechargement?: dayjs.Dayjs | null;
  sinistre?: Pick<ISinistre, 'id'> | null;
}

export type NewDocumentDeSinistre = Omit<IDocumentDeSinistre, 'id'> & { id: null };
