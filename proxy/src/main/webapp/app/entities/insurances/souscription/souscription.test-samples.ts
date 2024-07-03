import dayjs from 'dayjs/esm';

import { ISouscription, NewSouscription } from './souscription.model';

export const sampleWithRequiredData: ISouscription = {
  id: 23455,
  numeroSouscription: 'oof er',
  dateSouscription: dayjs('2024-07-02T23:24'),
  status: 'WATTING',
  cumulSinistres: 7408.95,
  frequencePaiement: 'MENSUEL',
  utilisateurId: 16258,
};

export const sampleWithPartialData: ISouscription = {
  id: 12360,
  numeroSouscription: 'badly till',
  dateSouscription: dayjs('2024-07-03T06:55'),
  status: 'RESILIE',
  cumulSinistres: 20605.78,
  frequencePaiement: 'SEMESTRIEL',
  utilisateurId: 23566,
};

export const sampleWithFullData: ISouscription = {
  id: 14234,
  numeroSouscription: 'unlike cloudy',
  dateSouscription: dayjs('2024-07-03T02:11'),
  status: 'ACTIVE',
  cumulSinistres: 7425.85,
  frequencePaiement: 'TRIMESTRIEL',
  utilisateurId: 15849,
};

export const sampleWithNewData: NewSouscription = {
  numeroSouscription: 'opposite lustrous',
  dateSouscription: dayjs('2024-07-02T19:51'),
  status: 'WATTING',
  cumulSinistres: 23980.48,
  frequencePaiement: 'SEMESTRIEL',
  utilisateurId: 12211,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
