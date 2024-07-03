import dayjs from 'dayjs/esm';

import { IFournisseur, NewFournisseur } from './fournisseur.model';

export const sampleWithRequiredData: IFournisseur = {
  id: 26086,
  nom: 'fire huzzah leafy',
  adresse: 'foolishly',
  telephone: '(395) 789-2779 x86688',
  email: 'Mavis89@yahoo.com',
  addresse: 'dynamics',
  ville: 'rescue annotate',
  pays: 'flatter defog',
  servicesFournis: 'oh astride quizzical',
  dateDebutContrat: dayjs('2024-07-02T15:10'),
};

export const sampleWithPartialData: IFournisseur = {
  id: 13764,
  nom: 'ribbon',
  adresse: 'normal',
  telephone: '503-639-6705 x8483',
  email: 'Eleazar.Funk@gmail.com',
  addresse: 'harness remarkable dory',
  ville: 'solidarity',
  pays: 'worthwhile',
  servicesFournis: 'despair impart',
  dateDebutContrat: dayjs('2024-07-02T13:51'),
  dateFinContrat: dayjs('2024-07-03T05:31'),
};

export const sampleWithFullData: IFournisseur = {
  id: 16320,
  nom: 'ouch',
  adresse: 'blackmail',
  telephone: '(671) 359-5454 x8545',
  email: 'Edgardo72@yahoo.com',
  addresse: 'phooey entitle',
  ville: 'after sans resolution',
  pays: 'er paraphrase',
  servicesFournis: 'ew',
  dateDebutContrat: dayjs('2024-07-02T12:11'),
  dateFinContrat: dayjs('2024-07-02T15:48'),
};

export const sampleWithNewData: NewFournisseur = {
  nom: 'who who ape',
  adresse: 'fill',
  telephone: '436-818-7436 x67424',
  email: 'Theodore_Grady@hotmail.com',
  addresse: 'fearful lazily',
  ville: 'idle',
  pays: 'gee however',
  servicesFournis: 'faucet oddly',
  dateDebutContrat: dayjs('2024-07-02T13:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
