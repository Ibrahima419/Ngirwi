import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Prescription from './prescription';
import PrescriptionDetail from './prescription-detail';
import PrescriptionUpdate from './prescription-update';
import PrescriptionDeleteDialog from './prescription-delete-dialog';

const PrescriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Prescription />} />
    <Route path="new" element={<PrescriptionUpdate />} />
    <Route path="new/:idConsultation" element={<PrescriptionUpdate />} />
    <Route path=":id">
      <Route index element={<PrescriptionDetail />} />
      <Route path="edit" element={<PrescriptionUpdate />} />
      <Route path="edit/:idEdit" element={<PrescriptionUpdate />} />
      <Route path="delete" element={<PrescriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrescriptionRoutes;
