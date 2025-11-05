import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Consultation from './consultation';
import ConsultationDetail from './consultation-detail';
import ConsultationUpdate from './consultation-update';
import ConsultationDeleteDialog from './consultation-delete-dialog';
import ConsultationPatient from './consultation-list';

const ConsultationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Consultation />} />
    <Route path='list/:idPatient' element={<ConsultationPatient/>}/>
    <Route path="new" element={<ConsultationUpdate />} />
    <Route path="new/:idPatient" element={<ConsultationUpdate />} />
    <Route path=":id">
      <Route index element={<ConsultationDetail />} />
      <Route path="edit" element={<ConsultationUpdate />} />
      <Route path="edit/:idEdit" element={<ConsultationUpdate/>}/>
      <Route path="delete" element={<ConsultationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConsultationRoutes;
