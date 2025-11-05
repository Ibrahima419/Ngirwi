import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SurveillanceSheet from './surveillance-sheet';
import SurveillanceSheetDetail from './surveillance-sheet-detail';
import SurveillanceSheetUpdate from './surveillance-sheet-update';
import SurveillanceSheetDeleteDialog from './surveillance-sheet-delete-dialog';
import SurveillanceSheetList from './surveillance-sheet-list';

const SurveillanceSheetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SurveillanceSheet />} />
    {/* Standalone list scoped to a hospitalisation */}
    <Route path="hospitalisation/:hospitalisationId" element={<SurveillanceSheetList />} />
    <Route path="new" element={<SurveillanceSheetUpdate />} />
    <Route path=":id">
      <Route index element={<SurveillanceSheetDetail />} />
      <Route path="edit" element={<SurveillanceSheetUpdate />} />
      <Route path="delete" element={<SurveillanceSheetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SurveillanceSheetRoutes;
