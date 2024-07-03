import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { UserExtraType } from 'app/entities/enumerations/user-extra-type.model';

export interface IUserExtra {
  id: number;
  numNiu?: string | null;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  motDePasse?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  numCni?: string | null;
  sexe?: keyof typeof Gender | null;
  telephone?: string | null;
  addresse?: string | null;
  ville?: string | null;
  pays?: string | null;
  type?: keyof typeof UserExtraType | null;
  signature?: string | null;
  signatureContentType?: string | null;
  internalUser?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUserExtra = Omit<IUserExtra, 'id'> & { id: null };
