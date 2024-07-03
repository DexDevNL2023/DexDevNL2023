import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 20134,
  login: 'T2wa@jwRWOJ\\Y3-k\\FMC\\N2wh\\9MfDU7S\\WxtC9',
};

export const sampleWithPartialData: IUser = {
  id: 32077,
  login: '?XB-+@UIuO\\Ru\\qS',
};

export const sampleWithFullData: IUser = {
  id: 9001,
  login: 'z',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
