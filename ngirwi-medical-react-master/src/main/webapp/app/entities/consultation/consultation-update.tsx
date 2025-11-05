import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Label, Card } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FiLock, FiLogOut } from 'react-icons/fi';

import {
  convertDateTimeFromServer,
  convertDateTimeFromServerToDate,
  convertDateTimeFromServerToHours,
  convertDateTimeToServer,
  displayDefaultDate,
  displayDefaultDateTime,
} from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntitiesBis as getPatients } from 'app/entities/patient/patient.reducer';
import { IPrescription } from 'app/shared/model/prescription.model';
import { getEntities as getPrescriptions } from 'app/entities/prescription/prescription.reducer';
import { IConsultation } from 'app/shared/model/consultation.model';
import { getEntity, updateEntity, createEntity, reset } from './consultation.reducer';

import { getPatient as getDossierPatient } from '../dossier-medical/dossier-medical.reducer';

import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import { examsList } from 'app/shared/util/exams-list';
import Header from 'app/shared/layout/header/header';
import dayjs from 'dayjs';
import { IoIosArrowBack } from 'react-icons/io';

export const ConsultationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { idPatient } = useParams<'idPatient'>();
  const { idEdit } = useParams<'idEdit'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const prescriptions = useAppSelector(state => state.prescription.entities);
  const dossierMedicalEntity = useAppSelector(state => state.dossierMedical.entity);
  const consultationEntity = useAppSelector(state => state.consultation.entity);
  const loading = useAppSelector(state => state.consultation.loading);
  const updating = useAppSelector(state => state.consultation.updating);
  const updateSuccess = useAppSelector(state => state.consultation.updateSuccess);
  const [dossier, setDossier] = useState();
  const [doppler, setDoppler] = useState(true);

  const account = useAppSelector(state => state.authentication.account);
  // const [exams, setExams] = useState([{"label" : '', "value" : ''}])
  const [exams, setExams] = useState(Object);

  const [selectedExams, setSelectedExams] = useState([]);

  const handleExamsChange = selectedOptions => {
    setSelectedExams(selectedOptions ? selectedOptions.map(option => option.value) : []);
  };

  const [iPatient, setIPatient] = useState(idPatient);

  const getPatientId = e => {
    setIPatient(e.target.value.toString());
  };

  const handleClose = () => {
    navigate('/consultation' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      dispatch(getDossierPatient(idPatient));
    } else {
      dispatch(getEntity(id));
      dispatch(getDossierPatient(idPatient ? idPatient : consultationEntity?.patient?.id));
    }

    dispatch(getPatients({ id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0 }));
    dispatch(getPrescriptions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateTime = convertDateTimeToServer(values.dateTime);

    const entity = {
      ...consultationEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
    };

    console.log(entity);
    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          hours: dayjs().format('HH:mm:ss'),
          date: dayjs().format('DD/MM/YYYY'),
          dateTime: displayDefaultDate(),
          author: account.login,
          exams: selectedExams.join(','),
          // exams: JSON.stringify(selectedExams),
          patient: idPatient,
        }
      : {
          ...consultationEntity,
          dateTime: convertDateTimeFromServer(consultationEntity.dateTime),
          hours: convertDateTimeFromServerToHours(consultationEntity.dateTime),
          date: convertDateTimeFromServerToDate(consultationEntity.dateTime),
          patient: consultationEntity?.patient?.id,
          author: account.login,
          exams: selectedExams.join(','),
        };

  const animatedComponents = makeAnimated();

  const showDoppler = () => {
    setDoppler(!doppler);
  };

  useEffect(() => {
    if (consultationEntity.exams) {
      // Split the string "TDM,ECG" into an array: ['TDM', 'ECG']
      setSelectedExams(consultationEntity.exams.split(','));
    }
  }, [consultationEntity.exams]);

  return (
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
      <Header pageName="Gestion consultations" />

      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          marginTop: '7.5vh',
        }}
      >
        <div
          style={{
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
            marginRight: '10%',
            gap: '6vw',
          }}
        >
          <Card
            style={{
              height: '6.28vh',
              minWidth: '32vw',
              borderRadius: '20px',
              backgroundColor: '#11485C',
              textAlign: 'center',
              color: 'white',
              marginBottom: '5vh',
              boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'flex-start',
              alignItems: 'center',
              gap: isNew ? '2vw' : '4vw',
              paddingLeft: isNew ? '1vw' : '2vw',
              marginLeft: idPatient == undefined && isNew === true ? '25vw' : '',
              marginTop: '10vh',
              paddingRight: '3vw',
            }}
          >
            <Button onClick={() => window.history.back()} style={{ color: '#53BFD1', backgroundColor: '#11485C', borderColor: '#11485C' }}>
              {React.createElement(IoIosArrowBack, { size: '20' })}
            </Button>
            {isNew ? (
              <span>Enregistrement nouvelle consultation</span>
            ) : (
              <span>{idEdit === 'voir' ? 'Consultation patient ' : 'Mise à jour consultation patient '}</span>
            )}
          </Card>
          {idPatient == undefined && isNew === true ? null : (
            <Card
              style={{
                minHeight: '30vh',
                width: '40vw',
                boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
                borderRadius: '15px',
                borderColor: '#0075FF',
                backgroundColor: '#F6FAFF',
              }}
            >
              <Card
                style={{
                  backgroundColor: '#0075FF',
                  borderColor: '#0075FF',
                  color: '#F6FAFF',
                  fontSize: '15px',
                  borderRadius: '13px',
                  minHeight: '9vh',
                  display: 'flex',
                  flexDirection: 'row',
                  gap: '50%',
                  alignItems: 'center',
                  justifyContent: 'center',
                  paddingLeft: '2%',
                }}
              >
                <span style={{ fontSize: '18px', fontWeight: '900' }}>Dossier médical</span>
                <Link
                  // to={`/dossier-medical/${dossierMedicalEntity?.id}/edit/${'voir'}`}
                  to={`/dossier-medical/${dossierMedicalEntity?.id}/${idPatient}`}
                  style={{
                    fontSize: '13px',
                    color: '#F6FAFF',
                    textDecoration: 'none',
                    border: '1px solid #72C9D8',
                    backgroundColor: '#0075F5',
                    padding: '10px',
                    borderRadius: '25px',
                    boxShadow: '2px 5px 11px rgba(0, 0, 0, 0.25)',
                  }}
                >
                  Tout voir{' '}
                </Link>
              </Card>
              <div
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'flex-start',
                  fontSize: '15px',
                  color: '#A9B7CD',
                  fontWeight: '700',
                  marginTop: '15px',
                  marginLeft: '5%',
                  gap: '1vh',
                }}
              >
                {dossierMedicalEntity ? (
                  <>
                    <div>Motif: {dossierMedicalEntity?.motifConsultation}</div>
                    <div>Histoire de la maladie:{dossierMedicalEntity?.histoireMaladie}</div>
                    <div>Antécédants chirigicaux:{dossierMedicalEntity?.antecedantsChirurgicaux}</div>
                    <div>Antécédants familiaux:{dossierMedicalEntity?.antecedantsFamiliaux}</div>
                    <div>Gynéco-Obstrétique:{dossierMedicalEntity?.gynecoObstretrique}</div>
                  </>
                ) : (
                  <></>
                )}
              </div>
            </Card>
          )}
        </div>

        <Card
          style={{
            minHeight: '80vh',
            width: '80vw',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            borderRadius: '15px',
            marginBottom: '30px',
            fontFamily: 'Jost',
          }}
        >
          {isNew ? (
            <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>Remplir informations patient</span>
          ) : (
            <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>
              {' '}
              {idEdit === 'voir' ? 'Consultation patient' : 'Modifications consultation patient'}{' '}
            </span>
          )}
          <span
            style={{
              marginTop: '2%',
              color: '#141414',
              fontSize: '25px',
              marginBottom: '3%',
              textAlign: 'center',
              fontWeight: '900',
            }}
          >
            {patients
              ? patients.map(otherEntity =>
                  consultationEntity?.patient?.id === otherEntity.id || idPatient == otherEntity.id ? (
                    <div>
                      <span key={otherEntity.id}>{'Patient: ' + otherEntity.lastName.toUpperCase() + ' ' + otherEntity.firstName}</span>{' '}
                      <br />
                      <span style={{ fontWeight: '400' }}>{'Matricule: #' + otherEntity.id}</span>
                    </div>
                  ) : null
                )
              : null}
          </span>

          <ValidatedForm
            defaultValues={defaultValues()}
            onSubmit={saveEntity}
            style={{
              width: '94%',
              height: '80%',
              marginLeft: '3%',
              display: 'grid',
              columnGap: '30px',
              marginTop: '1%',
              gridTemplateColumns: 'repeat(2,1fr)',
              fontSize: '18px',
              fontWeight: '900',
              backgroundImage: 'url(content/images/NgirwiLogo.png)',
              backgroundRepeat: 'no-repeat',
              backgroundAttachment: 'fixed',
              backgroundPosition: '65% 90%',
            }}
          >
            <ValidatedField
              hidden
              disabled
              label="Date et heure"
              id="consultation-dateTime"
              name="dateTime"
              data-cy="dateTime"
              type="datetime-local"
              placeholder="YYYY-MM-DD HH:mm"
            />
            <ValidatedField
              disabled
              label="Date"
              id="consultation-date"
              name="date"
              data-cy="date"
              type="datetime"
              placeholder="YYYY-MM-DD HH:mm"
              style={{
                borderRadius: '25px',
                borderColor: '#CBDCF7',
                backgroundColor: '#A9B7CD',
                color: '#F6FAFF',
              }}
            />
            <ValidatedField
              disabled
              label="Heure"
              id="consultation-hours"
              name="hours"
              data-cy="hours"
              type="datetime"
              placeholder="YYYY-MM-DD HH:mm"
              style={{
                borderRadius: '25px',
                backgroundColor: '#A9B7CD',
                color: '#F6FAFF',
                borderColor: '#CBDCF7',
              }}
            >
              <span
                style={{
                  color: '#F6FAFF',
                  textAlign: 'end',
                }}
              >
                {React.createElement(FiLock, { size: '20' })}
              </span>
            </ValidatedField>
            <ValidatedField
              label="Patient"
              name="patient"
              type="select"
              // eslint-disable-next-line eqeqeq
              hidden={isNew && idPatient == undefined ? false : true}
              // eslint-disable-next-line eqeqeq
              disabled={isNew && idPatient == undefined ? false : true}
              onChange={b => getPatientId(b)}
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
              }}
            >
              {patients.map((otherEntity, i) => (
                <option value={otherEntity.id} key={`entity-${i}`}>
                  {otherEntity.lastName.toUpperCase() + ' ' + otherEntity.firstName}
                </option>
              ))}
            </ValidatedField>
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Examen Général"
              id="consultation-examGeneral"
              name="examGeneral"
              data-cy="examGeneral"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Temperature(°C)"
              id="consultation-temperature"
              name="temperature"
              data-cy="temperature"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
                validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Poids(KG)"
              id="consultation-weight"
              name="weight"
              data-cy="weight"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
                validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Tension(mmHg)"
              id="consultation-tension"
              name="tension"
              data-cy="tension"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Glycemie(g/L)"
              id="consultation-glycemie"
              name="glycemie"
              data-cy="glycemie"
              type="text"
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Fréquence Cardiaque(bpm)"
              id="consultation-frequenceCardiaque"
              name="frequenceCardiaque"
              data-cy="frequenceCardiaque"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
                validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Fréquence Respiratoire(cpm)"
              id="consultation-frequenceRespiratoire"
              name="frequenceRespiratoire"
              data-cy="frequenceRespiratoire"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
                validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Hypothèse diagnostique"
              id="consultation-hypothesis"
              name="hypothesis"
              data-cy="hypothesis"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            {/* <div
              style={{
                borderRadius: "25px",
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: "#CBDCF7",
                color: idEdit === 'voir' ? '#F6FAFF' : 'black'
              }}>
              <Label>Examens complémentaires</Label>
              <Select className='' name="exams"
                options={examsList} components={animatedComponents} isMulti onChange={(e) => setExams(e)} />
            </div> */}
            {/* a revoir */}
            {idEdit === 'voir' ? (
              <ValidatedField
                disabled={idEdit === 'voir'}
                label="exams"
                id="exams"
                name="exams"
                data-cy="exams"
                type="textarea"
                value={consultationEntity.exams || ''} // Use the string value or fallback to an empty string
                style={{
                  borderRadius: '25px',
                  backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                  borderColor: '#CBDCF7',
                  color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                }}
              />
            ) : (
              <div>
                <label>Examens Paracliniques</label>
                <Select
                  name="exams"
                  isMulti // Enable multi-select
                  options={examsList} // List of available options
                  value={examsList.filter(option => selectedExams.includes(option.value))} // Preselect matching options
                  onChange={handleExamsChange} // Handle selection changes
                  styles={{
                    control: base => ({
                      ...base,
                      borderRadius: '25px',
                      backgroundColor: '#F7FAFF',
                      borderColor: '#CBDCF7',
                    }),
                  }}
                />
              </div>
            )}
            {/* <ValidatedField
              label="Examens paracliniques"
              name="exams"
              type="select"
              isMulti
              options={examsList}
              value={selectedExams}
              onChange={handleExamsChange}
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
              }}
            >
              {examsList.map(exam => (
                <option key={exam.value} value={exam.value}>
                  {exam.label}
                </option>
              ))}
            </ValidatedField> */}
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Résultats Examens Paracliniques"
              id="consultation-resultatsParaclinique"
              name="resultatsParaclinique"
              data-cy="resultatsParaclinique"
              type="textarea"
              // validate={{
              //   required: { value: true, message: 'Ce champ est obligatoire.' },
              // }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Traitement"
              id="consultation-treatment"
              name="treatment"
              data-cy="treatment"
              type="textarea"
              // validate={{
              //   required: { value: true, message: 'Ce champ est obligatoire.' },
              // }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
              // style={{
              //   gridColumn: isNew ? '1/3' : '',
              //   width: isNew ? '' : '150%',
              //   marginBottom: '20px',
              //   borderRadius: '10px',
              //   color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              //   backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
              //   borderColor: '#CBDCF7',
              // }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Commentaires Libres"
              id="consultation-commentaireLibre"
              name="commentaireLibre"
              data-cy="commentaireLibre"
              type="textarea"
              // validate={{
              //   required: { value: true, message: 'Ce champ est obligatoire.' },
              // }}
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
              }}
            />
            {/* {idEdit === 'voir' ? <br /> : null} */}
            <Button
              onClick={() => {
                showDoppler();
              }}
              id="showDoppler"
              data-cy="showDoppler"
              replace
              color="info"
              style={{
                gridColumn: '1/3',
                borderRadius: '25px',
                // color: 'white',
                // backgroundColor: '#EC4747',
                // borderColor: '#EC4747',
                textAlign: 'center',
                fontSize: idEdit === 'voir' ? '20px' : '',
                marginBottom: '2vh',
              }}
            >
              <span className="d-none d-md-inline">Echo Doppler</span>
            </Button>
            <br />
            <div
              hidden={doppler}
              style={{
                gridColumn: '1/3',
              }}
            >
              {' '}
              {/* Echo Doppler header */}
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>Echo Doppler</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="og"
                  label="OG (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vd"
                  label="VD (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="eseptum"
                  label="E-septum (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="ao"
                  label="Ao (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vGDiastole"
                  label="VG Diastole (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="z"
                  label="VCI (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="ouvertureAo"
                  label="Ouverture Ao (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vGSystole"
                  label="VG Systole (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vp"
                  label="VP (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="oGAo"
                  label="OG/Ao (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="fRTeicholtz"
                  label="FR/Teicholtz (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="eVp"
                  label="E/VP (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="septumVg"
                  label="Septum VG (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="fETeicholz"
                  label="FE Teicholz (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="tapse"
                  label="TAPSE (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="paroiPost"
                  label="PAROI POST (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              {/* &nbsp; */}
              {/* MODE 2D header */}
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>MODE 2D</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="surfaceOg"
                  label="Surface OG (cm²)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="surfaceOd"
                  label="Surface OD (cm²)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="mesureVd"
                  label="Mesure VD (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="fe"
                  label="FE (Simpson Monoplan) (%)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="feA2C"
                  label="FE (A2C) (%)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="feBiplan"
                  label="FE (Simpson Biplan) (%)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              {/* MODE DOPPLER header */}
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>MODE DOPPLER</span>
              <br />
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>VALVE MITRALE</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="e"
                  label="E (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="a"
                  label="A (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="eA"
                  label="E/A"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="td"
                  label="TD (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="triv"
                  label="TRIV (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="dureeAmIm"
                  label="Duree AM IM"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="surfaceRegurgitee"
                  label="Surface regurgitée (cm²)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="pba"
                  label="PBA"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="qr"
                  label="QR (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vr"
                  label="VR (ml/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="sor"
                  label="SOR (mm²)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="fr"
                  label="FR (%)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>VALVE AORTIQUE</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vmaxAp"
                  label="VMAX Ao (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="itv"
                  label="ITV (cm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="gradMax"
                  label="Grad Max (mmHg)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="gradMoy"
                  label="Grad Moy (mmHg)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="dc"
                  label="DC"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="iAoextension"
                  label="IAO extension du jet"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="venaContracta"
                  label="Vena Contracta (mm)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="pht"
                  label="PHT (%)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>VALVE TRICUSPIDE</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="iTExtension"
                  label="IT Extension du jet"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="gradMaxB"
                  label="Grad Max (mmHg)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="paps"
                  label="PAPS (mmHg)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <br />
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>VALVE PULMONAIRE</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="ip"
                  label="IP"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="vmax"
                  label="VMax (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="gradMaxC"
                  label="Grad Max (mmHg)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="gradMoyB"
                  label="Grad Moy (mmHg)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>VEINES PULMONAIRES</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="s"
                  label="S (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="d"
                  label="D (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="sD"
                  label="S/D"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="aA"
                  label="A (ms)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="dureeAp"
                  label="Duree AP"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
              <br />
              <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', marginLeft: '3%' }}>DOPPLER TISSULAIRE</span>
              <br />
              <div style={{ display: 'flex', gap: '10px' }}>
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="eAA"
                  label="Ea (m/s)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="aAA"
                  label="Aa (ms)"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="eAAa"
                  label="Ea/Aa"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  name="eEa"
                  label="E/Ea"
                  type="text"
                  style={{
                    borderRadius: '25px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                    borderColor: '#CBDCF7',
                    color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                  }}
                />
              </div>
            </div>
            <Button
              hidden={idEdit === 'voir' ? true : false}
              id="save-entity"
              data-cy="entityCreateSaveButton"
              type="submit"
              disabled={updating}
              style={{
                gridColumn: '1/3',
                borderRadius: '25px',
                color: 'white',
                backgroundColor: '#56B5C5',
                borderColor: '#56B5C5',
              }}
            >
              Enregistrer
            </Button>
            &nbsp;
            <Button
              onClick={() => {
                confirm('Êtes-vous sur de vouloir quitter?') === true ? window.history.back() : null;
              }}
              id="cancel-save"
              data-cy="entityCreateCancelButton"
              replace
              color="info"
              style={{
                gridColumn: '1/3',
                borderRadius: '25px',
                color: 'white',
                backgroundColor: '#EC4747',
                borderColor: '#EC4747',
                textAlign: 'center',
                fontSize: idEdit === 'voir' ? '20px' : '',
                marginBottom: '2vh',
              }}
            >
              <span className="d-none d-md-inline">{idEdit === 'voir' ? 'Retour' : 'Annuler'}</span>
            </Button>
            <ValidatedField hidden label="Author" id="consultation-author" name="author" data-cy="author" type="text" />
            <ValidatedField
              hidden
              label="Date Created"
              id="patient-dateCreated"
              name="dateCreated"
              data-cy="dateCreated"
              type="datetime-local"
              placeholder="YYYY-MM-DD HH:mm"
            />
            <ValidatedField
              hidden
              label="Date Updated"
              id="patient-dateUpdated"
              name="dateUpdated"
              data-cy="dateUpdated"
              type="datetime-local"
              placeholder="YYYY-MM-DD HH:mm"
            />
            <ValidatedField hidden label="Author" id="patient-author" name="author" data-cy="author" type="text" />
          </ValidatedForm>
        </Card>
      </div>
    </div>

    //           {/* <ValidatedField

    //           /> */}
    //           <ValidatedField

    //           />
    //           < />

    //           <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/consultation" replace color="info">
    //             <FontAwesomeIcon icon="arrow-left" />
    //             &nbsp;
    //             <span className="d-none d-md-inline">Retour</span>
    //           </Button>
    //           &nbsp;
    //           <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
    //             <FontAwesomeIcon icon="save" />
    //             &nbsp; Sauvegarder
    //           </Button>
    //         </ValidatedForm>
    //       )}
    //     </Col>
    //   </Row>
    // </div>
  );
};

export default ConsultationUpdate;
