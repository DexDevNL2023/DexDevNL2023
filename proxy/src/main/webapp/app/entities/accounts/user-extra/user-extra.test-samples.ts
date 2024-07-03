import dayjs from 'dayjs/esm';

import { IUserExtra, NewUserExtra } from './user-extra.model';

export const sampleWithRequiredData: IUserExtra = {
  id: 29365,
  numNiu: 'meanwhile',
  nom: 'whopping',
  prenom: 'deceivingly',
  email: '9d%@^.&C',
  motDePasse: 'reminisce fellow',
  dateNaissance: dayjs('2024-07-03T00:52'),
  numCni: 'vainly softly',
  sexe: 'OTHER',
  telephone: '479.741.6598 x370',
  addresse: 'ew duh',
  ville: 'chrome glass',
  pays: 'frail bah yum',
  type: 'AGENT',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
};

export const sampleWithPartialData: IUserExtra = {
  id: 10541,
  numNiu: 'minus',
  nom: 'monthly on',
  prenom: 'supervisor memo savage',
  email: 'S@6w.^^9I}k',
  motDePasse: 'but',
  dateNaissance: dayjs('2024-07-03T00:42'),
  numCni: 'thresh immediately heart-throb',
  sexe: 'MALE',
  telephone: '(262) 797-7930 x59945',
  addresse: 'bracket hinder ah',
  ville: 'hideous brr astride',
  pays: 'on organizing',
  type: 'FOURNISSEUR',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
};

export const sampleWithFullData: IUserExtra = {
  id: 11397,
  numNiu: 'clatter whereas',
  nom: 'solemnly fiercely',
  prenom: 'bah',
  email: '\\Y\\>bh@rkeC_.B&8m',
  motDePasse: 'nibble',
  dateNaissance: dayjs('2024-07-03T01:54'),
  numCni: 'speculate yowza',
  sexe: 'OTHER',
  telephone: '544-739-6884 x636',
  addresse: 'of',
  ville: 'ugh',
  pays: 'doorbell harmony below',
  type: 'AGENT',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
};

export const sampleWithNewData: NewUserExtra = {
  numNiu: 'boo scratchy correctly',
  nom: 'impressive dramatise',
  prenom: 'cylindrical among',
  email: 'x;l;@[.4Xr',
  motDePasse: 'rationalize meh',
  dateNaissance: dayjs('2024-07-02T15:15'),
  numCni: 'frank whether which',
  sexe: 'OTHER',
  telephone: '(821) 518-3178 x4490',
  addresse: 'daughter',
  ville: 'er unexpectedly belch',
  pays: 'wan pheromone',
  type: 'FOURNISSEUR',
  signature: '../fake-data/blob/hipster.png',
  signatureContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
