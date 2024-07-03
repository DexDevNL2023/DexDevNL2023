import { IPrestationSoin, NewPrestationSoin } from './prestation-soin.model';

export const sampleWithRequiredData: IPrestationSoin = {
  id: 10181,
  nom: 'glamorous bashfully',
  type: 'PHYSIOTHERAPIE',
  montant: 9304.37,
};

export const sampleWithPartialData: IPrestationSoin = {
  id: 31399,
  nom: 'oof bike heartfelt',
  description: 'smoothly unless',
  type: 'PHYSIOTHERAPIE',
  montant: 22302.2,
};

export const sampleWithFullData: IPrestationSoin = {
  id: 5089,
  nom: 'mmm curly',
  description: 'sturdy',
  type: 'AMBULANCE',
  montant: 21287.01,
};

export const sampleWithNewData: NewPrestationSoin = {
  nom: 'militarize',
  type: 'SOINS_OCULAIRES',
  montant: 22905.2,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
