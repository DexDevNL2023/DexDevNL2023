import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 26119,
  login: 'pXV',
};

export const sampleWithPartialData: IUser = {
  id: 3364,
  login: 'C5oY',
};

export const sampleWithFullData: IUser = {
  id: 28354,
  login: '{e*m|@Uwr4',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
