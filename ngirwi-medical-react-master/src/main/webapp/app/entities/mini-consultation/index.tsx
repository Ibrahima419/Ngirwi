import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MiniConsultationList from './mini-consultation-list';
import MiniConsultationDetail from './mini-consultation-detail';
import MiniConsultationUpdate from './mini-consultation-update';
import MiniConsultationDeleteDialog from './mini-consultation-delete-dialog';

const MiniConsultationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path="/surveillance-sheet/:surveillanceSheetId/mini-consultations">
      <Route index element={<MiniConsultationList />} />
      <Route path="new" element={<MiniConsultationUpdate />} />
      <Route path=":id">
        <Route index element={<MiniConsultationDetail />} />
        <Route path="edit" element={<MiniConsultationUpdate />} />
        <Route path="delete" element={<MiniConsultationDeleteDialog />} />
      </Route>
    </Route>
  </ErrorBoundaryRoutes>
);

export default MiniConsultationRoutes;
