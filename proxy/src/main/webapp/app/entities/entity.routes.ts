import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'proxyApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-extra',
    data: { pageTitle: 'proxyApp.accountsUserExtra.home.title' },
    loadChildren: () => import('./accounts/user-extra/user-extra.routes'),
  },
  {
    path: 'medical-record',
    data: { pageTitle: 'proxyApp.accountsMedicalRecord.home.title' },
    loadChildren: () => import('./accounts/medical-record/medical-record.routes'),
  },
  {
    path: 'medical-document',
    data: { pageTitle: 'proxyApp.accountsMedicalDocument.home.title' },
    loadChildren: () => import('./accounts/medical-document/medical-document.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'proxyApp.accountsNotification.home.title' },
    loadChildren: () => import('./accounts/notification/notification.routes'),
  },
  {
    path: 'assurance',
    data: { pageTitle: 'proxyApp.insurancesAssurance.home.title' },
    loadChildren: () => import('./insurances/assurance/assurance.routes'),
  },
  {
    path: 'garantie',
    data: { pageTitle: 'proxyApp.insurancesGarantie.home.title' },
    loadChildren: () => import('./insurances/garantie/garantie.routes'),
  },
  {
    path: 'rapport',
    data: { pageTitle: 'proxyApp.insurancesRapport.home.title' },
    loadChildren: () => import('./insurances/rapport/rapport.routes'),
  },
  {
    path: 'polices-assurances',
    data: { pageTitle: 'proxyApp.insurancesPolicesAssurances.home.title' },
    loadChildren: () => import('./insurances/polices-assurances/polices-assurances.routes'),
  },
  {
    path: 'souscription',
    data: { pageTitle: 'proxyApp.insurancesSouscription.home.title' },
    loadChildren: () => import('./insurances/souscription/souscription.routes'),
  },
  {
    path: 'paiement',
    data: { pageTitle: 'proxyApp.insurancesPaiement.home.title' },
    loadChildren: () => import('./insurances/paiement/paiement.routes'),
  },
  {
    path: 'sinistre',
    data: { pageTitle: 'proxyApp.claimsSinistre.home.title' },
    loadChildren: () => import('./claims/sinistre/sinistre.routes'),
  },
  {
    path: 'document-de-sinistre',
    data: { pageTitle: 'proxyApp.claimsDocumentDeSinistre.home.title' },
    loadChildren: () => import('./claims/document-de-sinistre/document-de-sinistre.routes'),
  },
  {
    path: 'reclamation',
    data: { pageTitle: 'proxyApp.claimsReclamation.home.title' },
    loadChildren: () => import('./claims/reclamation/reclamation.routes'),
  },
  {
    path: 'fournisseur',
    data: { pageTitle: 'proxyApp.claimsFournisseur.home.title' },
    loadChildren: () => import('./claims/fournisseur/fournisseur.routes'),
  },
  {
    path: 'prestation-soin',
    data: { pageTitle: 'proxyApp.claimsPrestationSoin.home.title' },
    loadChildren: () => import('./claims/prestation-soin/prestation-soin.routes'),
  },
  {
    path: 'financeur-soin',
    data: { pageTitle: 'proxyApp.claimsFinanceurSoin.home.title' },
    loadChildren: () => import('./claims/financeur-soin/financeur-soin.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
