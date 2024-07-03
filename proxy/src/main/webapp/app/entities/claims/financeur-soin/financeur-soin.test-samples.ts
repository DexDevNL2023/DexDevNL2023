import { IFinanceurSoin, NewFinanceurSoin } from './financeur-soin.model';

export const sampleWithRequiredData: IFinanceurSoin = {
  id: 26952,
  nom: 'excluding toward till',
  type: 'ORGANISME_PUBLIC',
  adresse: 'politely including cruelly',
  telephone: '354-317-8155 x503',
  email: 'Rico_Kreiger34@hotmail.com',
  servicesFournis: 'especially badly',
};

export const sampleWithPartialData: IFinanceurSoin = {
  id: 18370,
  nom: 'transliterate phooey',
  type: 'ORGANISME_PUBLIC',
  adresse: 'nicely',
  telephone: '248-852-0528 x662',
  email: 'Aglae_Hoeger@yahoo.com',
  servicesFournis: 'ick garner ack',
};

export const sampleWithFullData: IFinanceurSoin = {
  id: 11497,
  nom: 'label',
  type: 'ASSUREUR',
  adresse: 'mercerise',
  telephone: '307-945-0069 x2944',
  email: 'Leif.Waelchi@gmail.com',
  servicesFournis: 'considering',
};

export const sampleWithNewData: NewFinanceurSoin = {
  nom: 'brr till fairly',
  type: 'ASSUREUR',
  adresse: 'provided',
  telephone: '825.809.5909 x08317',
  email: 'Anais39@gmail.com',
  servicesFournis: 'considering',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
