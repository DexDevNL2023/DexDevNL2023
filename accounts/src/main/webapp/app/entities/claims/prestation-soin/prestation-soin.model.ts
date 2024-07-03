import { IFinanceurSoin } from 'app/entities/claims/financeur-soin/financeur-soin.model';
import { PrestationType } from 'app/entities/enumerations/prestation-type.model';

export interface IPrestationSoin {
  id: number;
  nom?: string | null;
  description?: string | null;
  type?: keyof typeof PrestationType | null;
  montant?: number | null;
  financeurSoin?: Pick<IFinanceurSoin, 'id'> | null;
  financeurSoins?: Pick<IFinanceurSoin, 'id'>[] | null;
}

export type NewPrestationSoin = Omit<IPrestationSoin, 'id'> & { id: null };
