import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'f7741190-177b-4c1e-8400-a10fb1dcd9b5',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b18ba2f4-4db4-425f-962e-ab189b6ae5fe',
};

export const sampleWithFullData: IAuthority = {
  name: '720ba624-3718-43cd-9371-74503d467e76',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
