import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Hospitalisation from './hospitalisation';
import HospitalisationDetail from './hospitalisation-detail';
import HospitalisationUpdate from './hospitalisation-update';
import HospitalisationDeleteDialog from './hospitalisation-delete-dialog';

const HospitalisationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Hospitalisation />} />
    {/* Create routes must come BEFORE dynamic segments to avoid matching "new" as an id */}
    <Route path="new" element={<HospitalisationUpdate />} />
    <Route path="new/:idPatient" element={<HospitalisationUpdate />} />
    {/* Detail route FIRST to prevent collisions with other dynamic segments */}
    <Route path=":id">
      <Route index element={<HospitalisationDetail />} />
      <Route path=":idPatient" element={<HospitalisationUpdate />} />
      <Route path="edit" element={<HospitalisationUpdate />} />
      <Route path="delete" element={<HospitalisationDeleteDialog />} />
    </Route>
    {/* Patient-scoped list under a distinct namespace to avoid conflicting with ":id" */}
    <Route path="patient/:idPatient" element={<Hospitalisation />} />
  </ErrorBoundaryRoutes>
);

export default HospitalisationRoutes;
