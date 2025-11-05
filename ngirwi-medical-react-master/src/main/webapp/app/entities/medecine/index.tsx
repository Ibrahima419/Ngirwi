import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Medecine from './medecine';
import MedecineDetail from './medecine-detail';
import MedecineUpdate from './medecine-update';
import MedecineDeleteDialog from './medecine-delete-dialog';

const MedecineRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Medecine />} />
    <Route path="new" element={<MedecineUpdate />} />
    <Route path=":id">
      <Route index element={<MedecineDetail />} />
      <Route path="edit" element={<MedecineUpdate />} />
      <Route path="delete" element={<MedecineDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedecineRoutes;
