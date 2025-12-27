import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Card, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity as getPatient, getEntitiesBis } from 'app/entities/patient/patient.reducer';
import { HospitalisationStatus } from 'app/shared/model/enumerations/hospitalisation-status.model';
import { getEntity, updateEntity, createEntity, reset } from './hospitalisation.reducer';
import Header from 'app/shared/layout/header/header';
import { IoIosArrowBack } from 'react-icons/io';

export const HospitalisationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;
  const { idPatient } = useParams<'idPatient'>();
  const { idEdit } = useParams<'idEdit'>();

  const patient = useAppSelector(state => state.patient.entity);
  const patientList = useAppSelector(state => state.patient.entities);
  const hospitalisationEntity = useAppSelector(state => state.hospitalisation?.entity);
  const loading = useAppSelector(state => state.hospitalisation?.loading);
  const updating = useAppSelector(state => state.hospitalisation?.updating);
  const updateSuccess = useAppSelector(state => state.hospitalisation?.updateSuccess);
  const hospitalisationStatusValues = Object.keys(HospitalisationStatus);
  const account = useAppSelector(state => state.authentication.account);

  const [selectedPatientId, setSelectedPatientId] = useState<string>('');
  const [showClosureModal, setShowClosureModal] = useState(false);
  const [finalDiagnosis, setFinalDiagnosis] = useState('');
  const [pendingValues, setPendingValues] = useState<any>(null);

  
  // Controlled form states to guarantee submission captures typed values
  const [admissionReason, setAdmissionReason] = useState<string>('');
  const [entryDiagnosis, setEntryDiagnosis] = useState<string>('');
  const [service, setService] = useState<string>('');
  const [dailyRateInput, setDailyRateInput] = useState<string>('');
  const [comfortFeesInput, setComfortFeesInput] = useState<string>('');
  const [feeOverrunInput, setFeeOverrunInput] = useState<string>('');
  const [insuranceCoveragePercentInput, setInsuranceCoveragePercentInput] = useState<string>('');

  const handleClose = () => {
    navigate('/hospitalisation' + location.search);
  };

useEffect(() => {
  if (isNew) {
    dispatch(reset());
  } else {
    dispatch(getEntity(id));
  }

  // ✅ Appel simple sans hospitalId
  dispatch(
    getEntitiesBis({
      filter: 'dossiermedical-is-null',
      page: 0,
      size: 9999,
      sort: 'id,asc',
    })
  );

  // Si un idPatient est présent dans l’URL, on pré-sélectionne ce patient
  if (idPatient) {
    setSelectedPatientId(idPatient);
    dispatch(getPatient(idPatient));
  }
}, []);


  // Update selected patient when editing existing hospitalisation
  useEffect(() => {
    if (!isNew && hospitalisationEntity?.patient?.id) {
      setSelectedPatientId(hospitalisationEntity.patient.id.toString());
    }
  }, [hospitalisationEntity]);

  useEffect(() => {
    if (updateSuccess) {
      // Get patient info for success message
      const selectedPatient = patientList?.find(p => p.id?.toString() === selectedPatientId);
      const patientName = selectedPatient ? `${selectedPatient.lastName} ${selectedPatient.firstName}` : '';

      const message = isNew ? `Hospitalisation enregistrée pour ${patientName}.` : `Hospitalisation mise à jour avec succès.`;

      toast.success(message);
      handleClose();
    }
  }, [updateSuccess]);

  // Sync local controlled fields from entity when editing existing
  useEffect(() => {
    if (!isNew && hospitalisationEntity) {
      setAdmissionReason(hospitalisationEntity.admissionReason || '');
      setEntryDiagnosis(hospitalisationEntity.entryDiagnosis || '');
      setService(hospitalisationEntity.service || '');
      setDailyRateInput(
        hospitalisationEntity.dailyRate !== undefined && hospitalisationEntity.dailyRate !== null
          ? String(hospitalisationEntity.dailyRate)
          : ''
      );
      setComfortFeesInput(
        hospitalisationEntity.comfortFees !== undefined && hospitalisationEntity.comfortFees !== null
          ? String(hospitalisationEntity.comfortFees)
          : ''
      );
      setFeeOverrunInput(
        hospitalisationEntity.feeOverrun !== undefined && hospitalisationEntity.feeOverrun !== null
          ? String(hospitalisationEntity.feeOverrun)
          : ''
      );
      setInsuranceCoveragePercentInput(
        hospitalisationEntity.insuranceCoveragePercent !== undefined && hospitalisationEntity.insuranceCoveragePercent !== null
          ? String(hospitalisationEntity.insuranceCoveragePercent)
          : ''
      );
    }
  }, [hospitalisationEntity]);

  const saveEntity = async values => {
    // Validate patient selection
    if (!selectedPatientId) {
      toast.error('Veuillez sélectionner un patient.');
      return;
    }

    // Check if status is being changed to DONE and hospitalisation is not already closed
    const isClosing = values.status === 'DONE' && hospitalisationEntity?.status !== 'DONE';

    if (isClosing && !isNew) {
      // Show closure modal to get final diagnosis
      setPendingValues(values);
      setFinalDiagnosis(hospitalisationEntity?.finalDiagnosis || '');
      setShowClosureModal(true);
      return; // Don't save yet, wait for modal confirmation
    }

    // Proceed with normal save
    await performSave(values);
  };

  const toNumber = (val: any): number | null => {
    if (val === undefined || val === null || val === '') return null;
    try {
      const s = String(val).replace(/\s+/g, '').replace(',', '.');
      const n = Number(s);
      return isNaN(n) ? null : n;
    } catch {
      return null;
    }
  };

  const performSave = async (values, closureDiagnosis = null) => {
    // no-op diagnostics removed for production

    values.entryDate = convertDateTimeToServer(values.entryDate);
    // no-op diagnostics removed for production

    // Auto-set discharge date when status is changed to DONE
    if (values.status === 'DONE' && !hospitalisationEntity?.releaseDate) {
      values.releaseDate = convertDateTimeToServer(displayDefaultDateTime());
    } else if (values.releaseDate) {
      values.releaseDate = convertDateTimeToServer(values.releaseDate);
    }

    // Add final diagnosis if provided from closure modal
    if (closureDiagnosis !== null) {
      values.finalDiagnosis = closureDiagnosis;
    }

    // Normalize numeric fields to plain numbers (avoid locale commas like 0,03)
    // Use controlled inputs for deterministic capture
    values.dailyRate = toNumber(dailyRateInput);
    values.comfortFees = toNumber(comfortFeesInput);
    values.feeOverrun = toNumber(feeOverrunInput);
    values.insuranceCoveragePercent = toNumber(insuranceCoveragePercentInput);
    values.admissionReason = admissionReason || null;
    values.entryDiagnosis = entryDiagnosis || null;
    values.service = service || null;

    // no-op diagnostics removed for production

    // Find the selected patient from the list
    const selectedPatient = patientList?.find(p => p.id?.toString() === selectedPatientId);

    const entity = {
      ...hospitalisationEntity,
      ...values,
      patientId: Number(selectedPatientId), // Backend expects patientId (Long), not patient object
      // ensure STARTED on create, preserve status on update
      status: isNew ? 'STARTED' : values.status,
    };

    // no-op diagnostics removed for production

    try {
      if (isNew) {
        //
        const action: any = await dispatch(createEntity(entity));
        //

        if (action?.meta?.requestStatus === 'fulfilled') {
          //
          navigate('/hospitalisation');
        } else {
          console.error('[DEBUG] Create FAILED:', action);
          const msg =
            action?.error?.message ||
            action?.payload?.response?.data?.title ||
            action?.payload?.response?.data?.message ||
            action?.payload?.message;
          toast.error(msg || "Échec de la création de l'hospitalisation");
          return;
        }
      } else {
        //
        const action: any = await dispatch(updateEntity(entity));
        //

        if (action?.meta?.requestStatus === 'fulfilled') {
          //
          navigate('/hospitalisation');
        } else {
          console.error('[DEBUG] Update FAILED:', action);
          const msg =
            action?.error?.message ||
            action?.payload?.response?.data?.title ||
            action?.payload?.response?.data?.message ||
            action?.payload?.message;
          toast.error(msg || "Échec de la mise à jour de l'hospitalisation");
          return;
        }
      }
    } catch (error: any) {
      console.error('[DEBUG] Exception caught in performSave:', error);
      const msg = error?.response?.data?.title || error?.response?.data?.message || error?.message;
      toast.error(msg || "Erreur lors de l'enregistrement de l'hospitalisation");
      console.error('[Hospitalisation] save error:', error);
    }
  };

  const handleClosureConfirm = async () => {
    if (!finalDiagnosis || finalDiagnosis.trim() === '') {
      toast.error("Veuillez saisir le diagnostic final avant de fermer l'hospitalisation.");
      return;
    }

    setShowClosureModal(false);
    await performSave(pendingValues, finalDiagnosis);
    setPendingValues(null);
    setFinalDiagnosis('');
  };

  const handleClosureCancel = () => {
    setShowClosureModal(false);
    setPendingValues(null);
    setFinalDiagnosis('');
  };

  const defaultValues = () =>
    isNew
      ? {
          entryDate: displayDefaultDateTime(),
          status: 'STARTED',
          doctorName: account.firstName + ' ' + account.lastName,
          // initialize fields to empty strings so RHF registers them and user input is captured
          dailyRate: '',
          comfortFees: '',
          feeOverrun: '',
          insuranceCoveragePercent: '',
          admissionReason: '',
          entryDiagnosis: '',
          service: '',
          // Business rule: finalDiagnosis and releaseDate MUST be null on creation
          releaseDate: null,
          finalDiagnosis: null,
        }
      : {
          ...hospitalisationEntity,
          entryDate: hospitalisationEntity?.entryDate ? convertDateTimeFromServer(hospitalisationEntity.entryDate) : null,
          releaseDate: hospitalisationEntity?.releaseDate ? convertDateTimeFromServer(hospitalisationEntity.releaseDate) : null,
        };

  return (
    <>
      <div
        style={{
          paddingLeft: '16vw',
          paddingTop: '1%',
          fontFamily: 'Mulish',
          fontWeight: '900',
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <Header pageName="Gestion hospitalisation" />

        <div
          style={{
            display: 'flex',
            flexDirection: 'column',
            marginTop: '12vh',
          }}
        >
          {/* Page Title with Back Button */}
          <div
            style={{
              display: 'flex',
              flexDirection: 'row',
              alignItems: 'center',
              marginBottom: '20px',
              marginLeft: '24px',
            }}
          >
            <Button
              tag={Link}
              to="/hospitalisation"
              style={{
                borderRadius: '50%',
                width: '50px',
                height: '50px',
                marginRight: '15px',
                color: '#53BFD1',
                backgroundColor: '#11485C',
                borderColor: '#11485C',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              {React.createElement(IoIosArrowBack, { size: '20' })}
            </Button>
            <span style={{ fontSize: '20px', fontWeight: '700', color: '#141414' }}>
              {isNew
                ? 'Enregistrement nouvelle hospitalisation'
                : idEdit === 'voir'
                ? 'Détails hospitalisation'
                : 'Modifier hospitalisation'}
            </span>
          </div>

          <Card
            style={{
              width: '83vw',
              backgroundColor: 'white',
              borderRadius: '15px',
              boxShadow: '0px 2px 12px 4px rgba(138, 161, 203, 0.23)',
              marginLeft: '24px',
              padding: '30px',
            }}
          >
            <div style={{ marginBottom: '20px' }}>
              <h4 style={{ color: '#141414', fontSize: '20px', marginBottom: '20px' }}>Initialisation des paramètres du patient</h4>
            </div>

            {loading ? (
              <p>Loading...</p>
            ) : (
              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                {/* Patient Selection - REQUIRED */}
                <Row className="mb-3">
                  <Col md="12">
                    <ValidatedField
                      label="Patient *"
                      id="hospitalisation-patient"
                      name="patient"
                      data-cy="patient"
                      type="select"
                      value={selectedPatientId}
                      onChange={e => setSelectedPatientId(e.target.value)}
                      validate={{
                        required: { value: true, message: 'Veuillez sélectionner un patient.' },
                      }}
                      style={{ borderRadius: '8px' }}
                    >
                      <option value="" key="0">
                        -- Sélectionner un patient --
                      </option>
                      {patientList
                        ? patientList.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.lastName?.toUpperCase()} {otherEntity.firstName} — P{otherEntity.id}
                            </option>
                          ))
                        : null}
                    </ValidatedField>
                  </Col>
                </Row>

                {/* Dates Section */}
                <Row className="mb-3">
                  <Col md={isNew ? '12' : '6'}>
                    <ValidatedField
                      label="Date d'admission au service"
                      id="hospitalisation-entryDate"
                      name="entryDate"
                      data-cy="entryDate"
                      type="datetime-local"
                      placeholder="YYYY-MM-DD HH:mm"
                      validate={{ required: { value: true, message: 'Ce champ est obligatoire.' } }}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                  {/* Discharge date only shown when editing (not during creation) */}
                  {!isNew && (
                    <Col md="6">
                      <ValidatedField
                        label="Date de sortie"
                        id="hospitalisation-releaseDate"
                        name="releaseDate"
                        data-cy="releaseDate"
                        type="datetime-local"
                        placeholder="YYYY-MM-DD HH:mm"
                        disabled
                        style={{ borderRadius: '8px', backgroundColor: '#f0f0f0' }}
                      />
                      <small className="text-muted">La date de sortie est définie automatiquement lors de la fermeture.</small>
                    </Col>
                  )}
                </Row>

                {/* Doctor and Status Section */}
                <Row className="mb-3">
                  <Col md="6">
                    <ValidatedField
                      label="Nom du médecin"
                      id="hospitalisation-doctorName"
                      name="doctorName"
                      data-cy="doctorName"
                      type="text"
                      validate={{ required: { value: true, message: 'Ce champ est obligatoire.' } }}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                  <Col md="6">
                    <ValidatedField
                      label="Statut de l'hospitalisation"
                      id="hospitalisation-status"
                      name="status"
                      data-cy="status"
                      type="select"
                      validate={{ required: { value: true, message: 'Ce champ est obligatoire.' } }}
                      style={{ borderRadius: '8px' }}
                    >
                      <option value="STARTED">Débute</option>
                      <option value="ONGOING">En cours</option>
                      <option value="DONE">Terminée</option>
                    </ValidatedField>
                  </Col>
                </Row>

                {/* Admission Reason Section */}
                <Row className="mb-3">
                  <Col md="12">
                    <ValidatedField
                      label="Raison d'admission"
                      id="hospitalisation-admissionReason"
                      name="admissionReason"
                      data-cy="admissionReason"
                      type="textarea"
                      rows="2"
                      value={admissionReason}
                      onChange={e => setAdmissionReason(e.target.value)}
                      validate={{ maxLength: { value: 512, message: '512 caractères maximum' } }}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                </Row>

                {/* Diagnosis Section */}
                <Row className="mb-3">
                  <Col md={hospitalisationEntity?.status === 'DONE' ? '6' : '12'}>
                    <ValidatedField
                      label="Diagnostic d'entrée"
                      id="hospitalisation-entryDiagnosis"
                      name="entryDiagnosis"
                      data-cy="entryDiagnosis"
                      type="textarea"
                      rows="2"
                      validate={{
                        required: { value: true, message: 'Ce champ est obligatoire.' },
                        maxLength: { value: 512, message: '512 caractères maximum' },
                      }}
                      value={entryDiagnosis}
                      onChange={e => setEntryDiagnosis(e.target.value)}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                  {/* Final diagnosis only shown for closed hospitalisations (read-only) */}
                  {!isNew && hospitalisationEntity?.status === 'DONE' && hospitalisationEntity?.finalDiagnosis && (
                    <Col md="6">
                      <ValidatedField
                        label="Diagnostic final"
                        id="hospitalisation-finalDiagnosis"
                        name="finalDiagnosis"
                        data-cy="finalDiagnosis"
                        type="textarea"
                        rows="2"
                        disabled
                        style={{ borderRadius: '8px', backgroundColor: '#f0f0f0' }}
                      />
                      <small className="text-muted">Diagnostic saisi lors de la fermeture</small>
                    </Col>
                  )}
                </Row>

                {/* Service Section */}
                <Row className="mb-3">
                  <Col md="12">
                    <ValidatedField
                      label="Service"
                      id="hospitalisation-service"
                      name="service"
                      data-cy="service"
                      type="text"
                      validate={{
                        required: { value: true, message: 'Ce champ est obligatoire.' },
                        maxLength: { value: 128, message: '128 caractères maximum' },
                      }}
                      value={service}
                      onChange={e => setService(e.target.value)}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                </Row>

                {/* Financial Section */}
                <Row className="mb-3">
                  <Col md="3">
                    <ValidatedField
                      label="Tarif journalier (FCFA)"
                      id="hospitalisation-dailyRate"
                      name="dailyRate"
                      data-cy="dailyRate"
                      type="number"
                      step="1"
                      min="0"
                      value={dailyRateInput}
                      onChange={e => setDailyRateInput(e.target.value)}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                  <Col md="3">
                    <ValidatedField
                      label="Frais de confort (FCFA)"
                      id="hospitalisation-comfortFees"
                      name="comfortFees"
                      data-cy="comfortFees"
                      type="number"
                      step="1"
                      min="0"
                      value={comfortFeesInput}
                      onChange={e => setComfortFeesInput(e.target.value)}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                  <Col md="3">
                    <ValidatedField
                      label="Dépassements (FCFA)"
                      id="hospitalisation-feeOverrun"
                      name="feeOverrun"
                      data-cy="feeOverrun"
                      type="number"
                      step="1"
                      min="0"
                      value={feeOverrunInput}
                      onChange={e => setFeeOverrunInput(e.target.value)}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                  <Col md="3">
                    <ValidatedField
                      label="Couverture assurance (%)"
                      id="hospitalisation-insuranceCoveragePercent"
                      name="insuranceCoveragePercent"
                      data-cy="insuranceCoveragePercent"
                      type="number"
                      step="0.01"
                      min="0"
                      max="100"
                      value={insuranceCoveragePercentInput}
                      onChange={e => setInsuranceCoveragePercentInput(e.target.value)}
                      style={{ borderRadius: '8px' }}
                    />
                  </Col>
                </Row>

                {/* Action Buttons */}
                <div style={{ marginTop: '30px', display: 'flex', gap: '15px', justifyContent: 'flex-start' }}>
                  <Button
                    id="save-entity"
                    data-cy="entityCreateSaveButton"
                    type="submit"
                    disabled={updating}
                    style={{
                      borderRadius: '25px',
                      color: 'white',
                      backgroundColor: '#53BFD1',
                      borderColor: '#53BFD1',
                      padding: '10px 30px',
                      fontWeight: '600',
                    }}
                  >
                    <FontAwesomeIcon icon="save" />
                    &nbsp; {isNew ? 'Créer' : 'Mettre à jour'}
                  </Button>

                  <Button
                    tag={Link}
                    to="/hospitalisation"
                    id="cancel-save"
                    data-cy="entityCreateCancelButton"
                    style={{
                      borderRadius: '25px',
                      color: 'white',
                      backgroundColor: '#EC4747',
                      borderColor: '#EC4747',
                      padding: '10px 30px',
                      fontWeight: '600',
                    }}
                  >
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp; Annuler
                  </Button>
                </div>
              </ValidatedForm>
            )}
          </Card>
        </div>
      </div>

      {/* Closure Modal - Final Diagnosis */}
      <Modal isOpen={showClosureModal} toggle={handleClosureCancel} size="lg">
        <ModalHeader toggle={handleClosureCancel}>
          <FontAwesomeIcon icon="check-circle" style={{ color: '#28a745', marginRight: '10px' }} />
          Fermeture de l'hospitalisation
        </ModalHeader>
        <ModalBody>
          <div className="mb-3">
            <p style={{ fontSize: '16px', marginBottom: '20px' }}>
              Vous êtes sur le point de fermer cette hospitalisation. Veuillez saisir le <strong>diagnostic final</strong> du patient avant
              de confirmer.
            </p>
            <p className="text-muted" style={{ fontSize: '14px', marginBottom: '20px' }}>
              <FontAwesomeIcon icon="info-circle" /> La date de sortie sera automatiquement définie à la date actuelle.
            </p>
          </div>

          <div className="form-group">
            <label htmlFor="closure-final-diagnosis" style={{ fontWeight: '600', marginBottom: '8px', display: 'block' }}>
              Diagnostic final *
            </label>
            <textarea
              id="closure-final-diagnosis"
              className="form-control"
              rows={5}
              value={finalDiagnosis}
              onChange={e => setFinalDiagnosis(e.target.value)}
              placeholder="Saisissez le diagnostic final du patient..."
              style={{
                borderRadius: '8px',
                fontSize: '14px',
                resize: 'vertical',
              }}
            />
            {finalDiagnosis.trim() === '' && <small className="text-danger">Ce champ est obligatoire pour fermer l'hospitalisation.</small>}
          </div>
        </ModalBody>
        <ModalFooter>
          <Button
            color="secondary"
            onClick={handleClosureCancel}
            style={{
              borderRadius: '25px',
              padding: '8px 25px',
            }}
          >
            <FontAwesomeIcon icon="times" />
            &nbsp; Annuler
          </Button>
          <Button
            color="success"
            onClick={handleClosureConfirm}
            disabled={!finalDiagnosis || finalDiagnosis.trim() === ''}
            style={{
              borderRadius: '25px',
              padding: '8px 25px',
            }}
          >
            <FontAwesomeIcon icon="check" />
            &nbsp; Confirmer la fermeture
          </Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default HospitalisationUpdate;
