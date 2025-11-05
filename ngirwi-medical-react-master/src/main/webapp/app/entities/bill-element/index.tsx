import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BillElement from './bill-element';
import BillElementDetail from './bill-element-detail';
import BillElementUpdate from './bill-element-update';
import BillElementDeleteDialog from './bill-element-delete-dialog';

const BillElementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BillElement />} />
    <Route path="new" element={<BillElementUpdate />} />
    <Route path=":id">
      <Route index element={<BillElementDetail />} />
      <Route path="edit" element={<BillElementUpdate />} />
      <Route path="delete" element={<BillElementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BillElementRoutes;
