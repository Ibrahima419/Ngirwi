import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DossierMedical from './dossier-medical';
import DossierMedicalDetail from './dossier-medical-detail';
import DossierMedicalUpdate from './dossier-medical-update';
import DossierMedicalDeleteDialog from './dossier-medical-delete-dialog';

const DossierMedicalRoutes = () => (
  <ErrorBoundaryRoutes>
    {/* <Route index element={<DossierMedical />} /> */}
    <Route path="new" element={<DossierMedicalUpdate />} />
    <Route path="new/:idPatient" element={<DossierMedicalUpdate />} />
    <Route path=":id">
      <Route index element={<DossierMedicalDetail />} />
      <Route path="edit" element={<DossierMedicalUpdate />} />
      <Route path=":idPatient" element={<DossierMedicalUpdate />} />
      <Route path="edit/:idEdit/:idPatient" element={<DossierMedicalUpdate />} />
      <Route path="delete" element={<DossierMedicalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DossierMedicalRoutes;
