import dayjs from 'dayjs/esm';
import { IPolicesAssurances } from 'app/entities/insurances/polices-assurances/polices-assurances.model';
import { SubscriptionStatus } from 'app/entities/enumerations/subscription-status.model';
import { PaymentFrequency } from 'app/entities/enumerations/payment-frequency.model';

export interface ISouscription {
  id: number;
  numeroSouscription?: string | null;
  dateSouscription?: dayjs.Dayjs | null;
  status?: keyof typeof SubscriptionStatus | null;
  cumulSinistres?: number | null;
  frequencePaiement?: keyof typeof PaymentFrequency | null;
  utilisateurId?: number | null;
  policesAssurances?: Pick<IPolicesAssurances, 'id'> | null;
}

export type NewSouscription = Omit<ISouscription, 'id'> & { id: null };
