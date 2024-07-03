import dayjs from 'dayjs/esm';

import { IPaiement, NewPaiement } from './paiement.model';

export const sampleWithRequiredData: IPaiement = {
  id: 5212,
  datePaiement: dayjs('2024-07-03T06:22'),
  montant: 3684.4,
  type: 'REMBOURSEMENT',
  status: 'COMPLETED',
};

export const sampleWithPartialData: IPaiement = {
  id: 29241,
  datePaiement: dayjs('2024-07-02T18:08'),
  montant: 13779.21,
  type: 'PRIME',
  status: 'FAILED',
};

export const sampleWithFullData: IPaiement = {
  id: 12817,
  datePaiement: dayjs('2024-07-02T18:46'),
  montant: 31109.43,
  type: 'PRIME',
  status: 'COMPLETED',
};

export const sampleWithNewData: NewPaiement = {
  datePaiement: dayjs('2024-07-02T17:35'),
  montant: 20478.24,
  type: 'REMBOURSEMENT',
  status: 'COMPLETED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
