import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'accountsApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-extra',
    data: { pageTitle: 'accountsApp.userExtra.home.title' },
    loadChildren: () => import('./user-extra/user-extra.routes'),
  },
  {
    path: 'medical-record',
    data: { pageTitle: 'accountsApp.medicalRecord.home.title' },
    loadChildren: () => import('./medical-record/medical-record.routes'),
  },
  {
    path: 'medical-document',
    data: { pageTitle: 'accountsApp.medicalDocument.home.title' },
    loadChildren: () => import('./medical-document/medical-document.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'accountsApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'assurance',
    data: { pageTitle: 'accountsApp.insurancesAssurance.home.title' },
    loadChildren: () => import('./insurances/assurance/assurance.routes'),
  },
  {
    path: 'garantie',
    data: { pageTitle: 'accountsApp.insurancesGarantie.home.title' },
    loadChildren: () => import('./insurances/garantie/garantie.routes'),
  },
  {
    path: 'rapport',
    data: { pageTitle: 'accountsApp.insurancesRapport.home.title' },
    loadChildren: () => import('./insurances/rapport/rapport.routes'),
  },
  {
    path: 'polices-assurances',
    data: { pageTitle: 'accountsApp.insurancesPolicesAssurances.home.title' },
    loadChildren: () => import('./insurances/polices-assurances/polices-assurances.routes'),
  },
  {
    path: 'souscription',
    data: { pageTitle: 'accountsApp.insurancesSouscription.home.title' },
    loadChildren: () => import('./insurances/souscription/souscription.routes'),
  },
  {
    path: 'paiement',
    data: { pageTitle: 'accountsApp.insurancesPaiement.home.title' },
    loadChildren: () => import('./insurances/paiement/paiement.routes'),
  },
  {
    path: 'sinistre',
    data: { pageTitle: 'accountsApp.claimsSinistre.home.title' },
    loadChildren: () => import('./claims/sinistre/sinistre.routes'),
  },
  {
    path: 'document-de-sinistre',
    data: { pageTitle: 'accountsApp.claimsDocumentDeSinistre.home.title' },
    loadChildren: () => import('./claims/document-de-sinistre/document-de-sinistre.routes'),
  },
  {
    path: 'reclamation',
    data: { pageTitle: 'accountsApp.claimsReclamation.home.title' },
    loadChildren: () => import('./claims/reclamation/reclamation.routes'),
  },
  {
    path: 'fournisseur',
    data: { pageTitle: 'accountsApp.claimsFournisseur.home.title' },
    loadChildren: () => import('./claims/fournisseur/fournisseur.routes'),
  },
  {
    path: 'prestation-soin',
    data: { pageTitle: 'accountsApp.claimsPrestationSoin.home.title' },
    loadChildren: () => import('./claims/prestation-soin/prestation-soin.routes'),
  },
  {
    path: 'financeur-soin',
    data: { pageTitle: 'accountsApp.claimsFinanceurSoin.home.title' },
    loadChildren: () => import('./claims/financeur-soin/financeur-soin.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
