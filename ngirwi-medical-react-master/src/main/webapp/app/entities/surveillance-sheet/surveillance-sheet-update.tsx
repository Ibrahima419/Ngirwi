import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams, useLocation } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { toast } from 'react-toastify';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { formatFcfa, mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHospitalisation } from 'app/shared/model/hospitalisation.model';
import { getEntities as getHospitalisations } from 'app/entities/hospitalisation/hospitalisation.reducer';
import { ISurveillanceSheet, MedicationEntry, ActEntry } from 'app/shared/model/surveillance-sheet.model';
import { getEntity, updateEntity, createEntity, reset, getByHospitalisation } from './surveillance-sheet.reducer';

export const SurveillanceSheetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const hospitalisations = useAppSelector(state => state.hospitalisation.entities);
  const surveillanceSheetEntity = useAppSelector(state => state.surveillanceSheet.entity);
  const loading = useAppSelector(state => state.surveillanceSheet.loading);
  const updating = useAppSelector(state => state.surveillanceSheet.updating);
  const updateSuccess = useAppSelector(state => state.surveillanceSheet.updateSuccess);

  const handleClose = () => {
    const hId = (surveillanceSheetEntity as any)?.hospitalisationId || (surveillanceSheetEntity as any)?.hospitalisation?.id;
    if (hId) navigate(`/hospitalisation/${hId}`);
    else navigate('/surveillance-sheet' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHospitalisations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      // Refresh hospitalisation to update totals if we're editing a sheet
      const hId = (surveillanceSheetEntity as any)?.hospitalisationId || (surveillanceSheetEntity as any)?.hospitalisation?.id;
      if (hId) {
        // Small delay to ensure backend has persisted
        setTimeout(() => {
          handleClose();
        }, 100);
      } else {
        handleClose();
      }
    }
  }, [updateSuccess]);

  const [medications, setMedications] = useState<MedicationEntry[]>([]);
  const [acts, setActs] = useState<ActEntry[]>([]);

  const addMedication = () => setMedications([...medications, { name: '', unitPrice: 0, quantity: 1 }]);
  const removeMedication = (i: number) => setMedications(medications.filter((_, idx) => idx !== i));
  const updateMedication = (i: number, patch: Partial<MedicationEntry>) => {
    const cp = medications.slice();
    cp[i] = { ...cp[i], ...patch };
    cp[i].total = Number(cp[i].unitPrice || 0) * Number(cp[i].quantity || 0);
    setMedications(cp);
  };

  const addAct = () => setActs([...acts, { name: '', unitPrice: 0, quantity: 1 }]);
  const removeAct = (i: number) => setActs(acts.filter((_, idx) => idx !== i));
  const updateAct = (i: number, patch: Partial<ActEntry>) => {
    const cp = acts.slice();
    cp[i] = { ...cp[i], ...patch };
    cp[i].total = Number(cp[i].unitPrice || 0) * Number(cp[i].quantity || 0);
    setActs(cp);
  };

  const medsTotal = medications.reduce((a, m) => a + Number(m.total || 0), 0);
  const actsTotal = acts.reduce((a, m) => a + Number(m.total || 0), 0);
  const sheetTotal = medsTotal + actsTotal;

  const saveEntity = values => {
    // Duplicate (hospitalisationId, sheetDate) local pre-check
    const hospId = Number(values.hospitalisation || (surveillanceSheetEntity as any)?.hospitalisationId);
    const targetDate = values.sheetDate;
    const currentId = (surveillanceSheetEntity as any)?.id;
    const duplicate = (existingSheets || []).some((s: any) => {
      const sHospId = Number((s as any).hospitalisationId || (s as any).hospitalisation?.id);
      const sDate = (s as any).sheetDate;
      const sId = (s as any).id;
      return sHospId === hospId && sDate === targetDate && (!currentId || sId !== currentId);
    });
    if (duplicate) {
      toast.error('Une fiche de surveillance existe déjà pour cette date.');
      return;
    }
    // Block save if any medication/act row invalid
    const medInvalid = (medications || []).some(
      m =>
        !m.name ||
        String(m.name).trim() === '' ||
        isNaN(Number(m.unitPrice)) ||
        Number(m.unitPrice) < 0 ||
        isNaN(Number(m.quantity)) ||
        Number(m.quantity) < 1
    );
    const actInvalid = (acts || []).some(
      a =>
        !a.name ||
        String(a.name).trim() === '' ||
        isNaN(Number(a.unitPrice)) ||
        Number(a.unitPrice) < 0 ||
        isNaN(Number(a.quantity)) ||
        Number(a.quantity) < 1
    );
    if (medInvalid || actInvalid) {
      alert('Veuillez corriger les erreurs dans les listes Médicaments/Actes avant de sauvegarder.');
      return;
    }
    const cleanMeds = (medications || [])
      .filter(m => {
        const nameOk = (m.name || '').toString().trim().length > 0;
        const priceOk = m.unitPrice !== undefined && m.unitPrice !== null && !isNaN(Number(m.unitPrice));
        const qtyOk = m.quantity !== undefined && m.quantity !== null && Number(m.quantity) > 0;
        return nameOk && priceOk && qtyOk;
      })
      .map(m => ({
        name: (m.name || '').toString().trim(),
        unitPrice: Number(m.unitPrice),
        quantity: Number(m.quantity),
      }));
    const cleanActs = (acts || [])
      .filter(a => {
        const nameOk = (a.name || '').toString().trim().length > 0;
        const priceOk = a.unitPrice !== undefined && a.unitPrice !== null && !isNaN(Number(a.unitPrice));
        const qtyOk = a.quantity !== undefined && a.quantity !== null && Number(a.quantity) > 0;
        return nameOk && priceOk && qtyOk;
      })
      .map(a => ({
        name: (a.name || '').toString().trim(),
        unitPrice: Number(a.unitPrice),
        quantity: Number(a.quantity),
      }));

    const payload: ISurveillanceSheet = {
      ...surveillanceSheetEntity,
      ...values,
      hospitalisationId: Number(values.hospitalisation || (surveillanceSheetEntity as any)?.hospitalisationId),
      medications: cleanMeds,
      acts: cleanActs,
    };

    if (isNew) {
      dispatch(createEntity(payload));
    } else {
      dispatch(updateEntity(payload));
    }
  };

  // Extract hospitalisationId from query string (when coming from hospitalisation detail)
  const qsHospId = (() => {
    const p = new URLSearchParams(location.search).get('hospitalisationId');
    return p ? Number(p) : undefined;
  })();

  // Load existing sheets for this hospitalisation (for duplicate date check)
  useEffect(() => {
    if (qsHospId) {
      dispatch(getByHospitalisation({ hospitalisationId: qsHospId }));
    }
  }, [qsHospId]);

  const existingSheets = useAppSelector(state => state.surveillanceSheet.entities) || [];

  const defaultValues = () =>
    isNew
      ? {
          hospitalisation: qsHospId, // auto-preselect when provided
        }
      : {
          ...surveillanceSheetEntity,
          hospitalisation: surveillanceSheetEntity?.hospitalisation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ngirwiFrontEndApp.surveillanceSheet.home.createOrEditLabel" data-cy="SurveillanceSheetCreateUpdateHeading">
            Créer ou éditer un Surveillance Sheet
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="surveillance-sheet-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Date de la fiche"
                id="surveillance-sheet-sheetDate"
                name="sheetDate"
                data-cy="sheetDate"
                type="date"
                validate={{ required: { value: true, message: 'Date requise' } }}
              />
              <ValidatedField
                label="Température (°C)"
                id="surveillance-sheet-temperature"
                name="temperature"
                data-cy="temperature"
                type="number"
                step="0.1"
                validate={{
                  min: { value: 30, message: 'Température minimale 30°C' },
                  max: { value: 45, message: 'Température maximale 45°C' },
                }}
              />
              <ValidatedField
                label="TA Systolique (mmHg)"
                id="systolicBP"
                name="systolicBP"
                data-cy="systolicBP"
                type="number"
                validate={{ min: { value: 0, message: 'Valeur minimale 0' }, max: { value: 300, message: 'Valeur maximale 300' } }}
              />
              <ValidatedField
                label="TA Diastolique (mmHg)"
                id="diastolicBP"
                name="diastolicBP"
                data-cy="diastolicBP"
                type="number"
                validate={{ min: { value: 0, message: 'Valeur minimale 0' }, max: { value: 200, message: 'Valeur maximale 200' } }}
              />
              <ValidatedField
                label="Pouls (bpm)"
                id="pulseRate"
                name="pulseRate"
                data-cy="pulseRate"
                type="number"
                validate={{ min: { value: 0, message: 'Valeur minimale 0' }, max: { value: 300, message: 'Valeur maximale 300' } }}
              />
              <ValidatedField
                label="Fréquence respiratoire (rpm)"
                id="respirationRate"
                name="respirationRate"
                data-cy="respirationRate"
                type="number"
                validate={{ min: { value: 0, message: 'Valeur minimale 0' }, max: { value: 120, message: 'Valeur maximale 120' } }}
              />
              <ValidatedField
                label="SaO2 (%)"
                id="spo2"
                name="spo2"
                data-cy="spo2"
                type="number"
                validate={{ min: { value: 0, message: 'Valeur minimale 0' }, max: { value: 100, message: 'Valeur maximale 100' } }}
              />
              <ValidatedField
                label="Notes soignantes"
                id="nursingNotes"
                name="nursingNotes"
                data-cy="nursingNotes"
                type="textarea"
                rows="3"
              />
              <ValidatedField
                label="Observations médicales"
                id="medicalObservations"
                name="medicalObservations"
                data-cy="medicalObservations"
                type="textarea"
                rows="3"
              />
              <ValidatedField
                id="surveillance-sheet-hospitalisation"
                name="hospitalisation"
                data-cy="hospitalisation"
                label="Hospitalisation"
                type="select"
              >
                <option value="" key="0" />
                {hospitalisations
                  ? hospitalisations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {/* Dynamic Medications */}
              <hr />
              <h5>Médicaments</h5>
              {medications.map((m, i) => (
                <div key={`med-${i}`} className="d-flex align-items-end gap-2 mb-2">
                  <ValidatedField
                    label="Nom"
                    name={`med-name-${i}`}
                    type="text"
                    value={m.name}
                    onChange={e => updateMedication(i, { name: (e.target as any).value })}
                  />
                  <ValidatedField
                    label="Prix unitaire"
                    name={`med-price-${i}`}
                    type="number"
                    value={m.unitPrice as any}
                    onChange={e => updateMedication(i, { unitPrice: Number((e.target as any).value) })}
                  />
                  <ValidatedField
                    label="Quantité"
                    name={`med-qty-${i}`}
                    type="number"
                    value={m.quantity as any}
                    onChange={e => updateMedication(i, { quantity: Number((e.target as any).value) })}
                  />
                  <div style={{ minWidth: 120 }}>
                    <strong>{formatFcfa(Number(m.total || 0))}</strong>
                  </div>
                  <Button color="danger" onClick={() => removeMedication(i)}>
                    Supprimer
                  </Button>
                  <div className="w-100" />
                  {(!m.name || String(m.name).trim() === '') && <small className="text-danger">Nom requis</small>}
                  {(m.unitPrice === undefined || m.unitPrice === null || isNaN(Number(m.unitPrice)) || Number(m.unitPrice) < 0) && (
                    <small className="text-danger">Prix ≥ 0</small>
                  )}
                  {(m.quantity === undefined || m.quantity === null || isNaN(Number(m.quantity)) || Number(m.quantity) < 1) && (
                    <small className="text-danger">Quantité ≥ 1</small>
                  )}
                </div>
              ))}
              <Button color="primary" onClick={addMedication} className="mb-3">
                + Ajouter un médicament
              </Button>
              {/* Dynamic Acts */}
              <h5>Actes réalisés</h5>
              {acts.map((a, i) => (
                <div key={`act-${i}`} className="d-flex align-items-end gap-2 mb-2">
                  <ValidatedField
                    label="Nom"
                    name={`act-name-${i}`}
                    type="text"
                    value={a.name}
                    onChange={e => updateAct(i, { name: (e.target as any).value })}
                  />
                  <ValidatedField
                    label="Prix unitaire"
                    name={`act-price-${i}`}
                    type="number"
                    value={a.unitPrice as any}
                    onChange={e => updateAct(i, { unitPrice: Number((e.target as any).value) })}
                  />
                  <ValidatedField
                    label="Quantité"
                    name={`act-qty-${i}`}
                    type="number"
                    value={a.quantity as any}
                    onChange={e => updateAct(i, { quantity: Number((e.target as any).value) })}
                  />
                  <div style={{ minWidth: 120 }}>
                    <strong>{formatFcfa(Number(a.total || 0))}</strong>
                  </div>
                  <Button color="danger" onClick={() => removeAct(i)}>
                    Supprimer
                  </Button>
                  <div className="w-100" />
                  {(!a.name || String(a.name).trim() === '') && <small className="text-danger">Nom requis</small>}
                  {(a.unitPrice === undefined || a.unitPrice === null || isNaN(Number(a.unitPrice)) || Number(a.unitPrice) < 0) && (
                    <small className="text-danger">Prix ≥ 0</small>
                  )}
                  {(a.quantity === undefined || a.quantity === null || isNaN(Number(a.quantity)) || Number(a.quantity) < 1) && (
                    <small className="text-danger">Quantité ≥ 1</small>
                  )}
                </div>
              ))}
              <Button color="primary" onClick={addAct} className="mb-3">
                + Ajouter un acte
              </Button>
              {/* Totals preview */}
              <div className="mt-3">
                <div>
                  <strong>Total médicaments:</strong> {formatFcfa(medsTotal)}
                </div>
                <div>
                  <strong>Total actes:</strong> {formatFcfa(actsTotal)}
                </div>
                <div>
                  <strong>Coût total:</strong> {formatFcfa(sheetTotal)}
                </div>
              </div>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/surveillance-sheet" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Retour</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Sauvegarder
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SurveillanceSheetUpdate;
