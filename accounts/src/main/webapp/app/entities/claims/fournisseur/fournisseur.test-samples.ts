import dayjs from 'dayjs/esm';

import { IFournisseur, NewFournisseur } from './fournisseur.model';

export const sampleWithRequiredData: IFournisseur = {
  id: 4689,
  nom: 'worth cute',
  adresse: 'underneath whoever',
  telephone: '1-782-985-5733 x98346',
  email: 'Odessa36@gmail.com',
  addresse: 'faithfully furthermore',
  ville: 'pro gear modulo',
  pays: 'to excel',
  servicesFournis: 'shirt drat',
  dateDebutContrat: dayjs('2024-07-03T00:11'),
};

export const sampleWithPartialData: IFournisseur = {
  id: 15444,
  nom: 'surprisingly',
  adresse: 'warrant oddball',
  telephone: '1-413-823-9569 x78810',
  email: 'Tremaine15@hotmail.com',
  addresse: 'fervently',
  ville: 'resell',
  pays: 'lamp',
  servicesFournis: 'unimportant',
  dateDebutContrat: dayjs('2024-07-02T13:42'),
  dateFinContrat: dayjs('2024-07-02T10:56'),
};

export const sampleWithFullData: IFournisseur = {
  id: 4475,
  nom: 'place unbearably masculine',
  adresse: 'between',
  telephone: '1-489-277-5923 x310',
  email: 'Kristopher.Gorczany@hotmail.com',
  addresse: 'tread as pronounce',
  ville: 'aha oh major',
  pays: 'wherever alongside',
  servicesFournis: 'times blot describe',
  dateDebutContrat: dayjs('2024-07-03T05:58'),
  dateFinContrat: dayjs('2024-07-02T23:40'),
};

export const sampleWithNewData: NewFournisseur = {
  nom: 'fooey',
  adresse: 'cucumber',
  telephone: '(221) 687-2476 x763',
  email: 'Reagan.Kerluke13@yahoo.com',
  addresse: 'hm',
  ville: 'yogurt',
  pays: 'patio plug',
  servicesFournis: 'sunbathe',
  dateDebutContrat: dayjs('2024-07-02T17:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
