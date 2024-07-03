import dayjs from 'dayjs/esm';

import { IUserExtra, NewUserExtra } from './user-extra.model';

export const sampleWithRequiredData: IUserExtra = {
  id: 16077,
  numNiu: 'gladly crisp',
  nom: 'tremendously postfix',
  prenom: 'failing not bother',
  email: 'k~@,}whOW.KUh{Tl',
  motDePasse: 'owlishly distill',
  dateNaissance: dayjs('2024-07-03T05:14'),
  numCni: 'odd',
  sexe: 'OTHER',
  telephone: '407.465.2565',
  addresse: 'oeuvre',
  ville: 'early huzzah dangerous',
  pays: 'likewise',
  type: 'AGENT',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
};

export const sampleWithPartialData: IUserExtra = {
  id: 154,
  numNiu: 'skipper',
  nom: 'consequently',
  prenom: 'out anxiously if',
  email: 'Z&0C.)@sp_$.sY}*+',
  motDePasse: 'pfft more rearm',
  dateNaissance: dayjs('2024-07-02T23:12'),
  numCni: 'juggernaut anticipate',
  sexe: 'FEMALE',
  telephone: '(305) 839-9989 x4732',
  addresse: 'mmm impose likewise',
  ville: 'following',
  pays: 'supposing opposite vacillate',
  type: 'FOURNISSEUR',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
};

export const sampleWithFullData: IUserExtra = {
  id: 15203,
  numNiu: 'within inflammation',
  nom: 'steep ouch golden',
  prenom: 'ha gadzooks',
  email: 'bp@GQ8!L_.dwpnd',
  motDePasse: 'amidst what',
  dateNaissance: dayjs('2024-07-02T18:12'),
  numCni: 'beside',
  sexe: 'MALE',
  telephone: '929-664-2985 x2001',
  addresse: 'after kindheartedly',
  ville: 'whose',
  pays: 'other rotten',
  type: 'AGENT',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
};

export const sampleWithNewData: NewUserExtra = {
  numNiu: 'how bronco',
  nom: 'whether',
  prenom: 'ambitious',
  email: '*}S@K{;xW.fX0b',
  motDePasse: 'ownership after',
  dateNaissance: dayjs('2024-07-02T10:44'),
  numCni: 'what meanwhile',
  sexe: 'OTHER',
  telephone: '1-786-463-4071',
  addresse: 'snarling',
  ville: 'vacantly mid seriously',
  pays: 'which pro',
  type: 'FOURNISSEUR',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
