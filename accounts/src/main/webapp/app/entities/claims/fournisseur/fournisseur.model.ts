import dayjs from 'dayjs/esm';

export interface IFournisseur {
  id: number;
  nom?: string | null;
  adresse?: string | null;
  telephone?: string | null;
  email?: string | null;
  addresse?: string | null;
  ville?: string | null;
  pays?: string | null;
  servicesFournis?: string | null;
  dateDebutContrat?: dayjs.Dayjs | null;
  dateFinContrat?: dayjs.Dayjs | null;
}

export type NewFournisseur = Omit<IFournisseur, 'id'> & { id: null };
