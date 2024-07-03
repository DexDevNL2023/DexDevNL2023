import dayjs from 'dayjs/esm';

import { ISouscription, NewSouscription } from './souscription.model';

export const sampleWithRequiredData: ISouscription = {
  id: 28605,
  numeroSouscription: 'systematize failing',
  dateSouscription: dayjs('2024-07-02T19:52'),
  status: 'ACTIVE',
  cumulSinistres: 26373.78,
  frequencePaiement: 'TRIMESTRIEL',
  utilisateurId: 29331,
};

export const sampleWithPartialData: ISouscription = {
  id: 19781,
  numeroSouscription: 'cruelly wherever',
  dateSouscription: dayjs('2024-07-03T05:58'),
  status: 'ON_RISK',
  cumulSinistres: 20846.6,
  frequencePaiement: 'TRIMESTRIEL',
  utilisateurId: 27674,
};

export const sampleWithFullData: ISouscription = {
  id: 25357,
  numeroSouscription: 'demonise',
  dateSouscription: dayjs('2024-07-02T19:49'),
  status: 'WATTING',
  cumulSinistres: 6139.33,
  frequencePaiement: 'TRIMESTRIEL',
  utilisateurId: 8929,
};

export const sampleWithNewData: NewSouscription = {
  numeroSouscription: 'likewise',
  dateSouscription: dayjs('2024-07-03T03:21'),
  status: 'ON_RISK',
  cumulSinistres: 8104.42,
  frequencePaiement: 'TRIMESTRIEL',
  utilisateurId: 28642,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
