import { IPrestationSoin, NewPrestationSoin } from './prestation-soin.model';

export const sampleWithRequiredData: IPrestationSoin = {
  id: 15335,
  nom: 'stained consequently',
  type: 'SOINS_A_DOMICILE',
  montant: 27991.1,
};

export const sampleWithPartialData: IPrestationSoin = {
  id: 8884,
  nom: 'surpass troop indeed',
  description: 'extremely territory addle',
  type: 'PHYSIOTHERAPIE',
  montant: 16198.69,
};

export const sampleWithFullData: IPrestationSoin = {
  id: 30784,
  nom: 'optimise usurp',
  description: 'aha',
  type: 'SOINS_PARAMEDICAUX',
  montant: 28150.67,
};

export const sampleWithNewData: NewPrestationSoin = {
  nom: 'beside',
  type: 'CHIRURGIE',
  montant: 2368.21,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
