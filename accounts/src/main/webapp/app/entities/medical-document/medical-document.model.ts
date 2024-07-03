import { IMedicalRecord } from 'app/entities/medical-record/medical-record.model';

export interface IMedicalDocument {
  id: number;
  nom?: string | null;
  type?: string | null;
  url?: string | null;
  dossierMedical?: Pick<IMedicalRecord, 'id'> | null;
}

export type NewMedicalDocument = Omit<IMedicalDocument, 'id'> & { id: null };
