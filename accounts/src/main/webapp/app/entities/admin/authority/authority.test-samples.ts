import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '45080f2d-944e-4993-953a-f673b3df98f2',
};

export const sampleWithPartialData: IAuthority = {
  name: 'af04aec1-cf87-4ea9-904c-987e7e4d82d0',
};

export const sampleWithFullData: IAuthority = {
  name: '616dd87b-f63a-47f7-9fb3-f8118b200170',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
