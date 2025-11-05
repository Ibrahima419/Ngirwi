import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Bill from './bill';
import BillDetail from './bill-detail';
import BillUpdate from './bill-update';
import BillDeleteDialog from './bill-delete-dialog';

const BillRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Bill />} />
    <Route path="new" element={<BillUpdate />} />
    <Route path="new/:idPatient" element={<BillUpdate />} />
    <Route path=":id">
      <Route index element={<BillDetail />} />
      <Route path="edit" element={<BillUpdate />} />
      <Route path="edit/:idEdit" element={<BillUpdate />} />
      <Route path="delete" element={<BillDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BillRoutes;
