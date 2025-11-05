import React from 'react';
import { Navigate, Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Patient from './patient';
import DossierMedical from './dossier-medical';
import Consultation from './consultation';
import Prescription from './prescription';
import Medecine from './medecine';
import Bill from './bill';
import BillElement from './bill-element';
import Hospitalisation from './hospitalisation';
import Hospital from './hospital';
import SurveillanceSheet from './surveillance-sheet';
import MiniConsultation from './mini-consultation';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="patient/*" element={<Patient />} />
        <Route path="dossier-medical/*" element={<DossierMedical />} />
        <Route path="consultation/*" element={<Consultation />} />
        <Route path="prescription/*" element={<Prescription />} />
        <Route path="medecine/*" element={<Medecine />} />
        <Route path="bill/*" element={<Bill />} />
        <Route path="hospitalisation/*" element={<Hospitalisation />} />
        <Route path="bill-element/*" element={<BillElement />} />

        {/* custom routes */}
        <Route path="hospital/*" element={<Hospital />} />
        <Route path="surveillance-sheet/*" element={<SurveillanceSheet />} />
        <Route path="mini-consultation/*" element={<MiniConsultation />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
        <Route path="*" element={<Navigate to="/" />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
