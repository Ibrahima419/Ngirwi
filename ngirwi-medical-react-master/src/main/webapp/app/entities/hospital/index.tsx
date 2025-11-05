import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Hospital from './hospital';
import HospitalDetail from './hospital-detail';
import HospitalUpdate from './hospital-update';
import HospitalDeleteDialog from './hospital-delete-dialog';

const HospitalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Hospital />} />
    <Route path="new" element={<HospitalUpdate />} />
    <Route path=":id">
      <Route index element={<HospitalDetail />} />
      <Route path="edit" element={<HospitalUpdate />} />
      <Route path="delete" element={<HospitalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default HospitalRoutes;
