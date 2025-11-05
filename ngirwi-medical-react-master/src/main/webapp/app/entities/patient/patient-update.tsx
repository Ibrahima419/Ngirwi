import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';

import { convertDateTimeToServer, displayDefaultDate } from 'app/shared/util/date-utils';
import { translateGender, translateMaritalStatus, translateBloodType } from 'app/shared/util/translation-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { GENDER } from 'app/shared/model/enumerations/gender.model';
import { BLOODTYPE } from 'app/shared/model/enumerations/bloodtype.model';
import { MARITALSTATUS } from 'app/shared/model/enumerations/maritalstatus.model';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';
import { IoIosArrowBack } from 'react-icons/io';
import Header from 'app/shared/layout/header/header';
import Hospital from '../hospital/hospital';

export const PatientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const account = useAppSelector(state => state.authentication.account);
  const patientEntity = useAppSelector(state => state.patient.entity);
  const updating = useAppSelector(state => state.patient.updating);
  const updateSuccess = useAppSelector(state => state.patient.updateSuccess);
  const gENDERValues = Object.keys(GENDER);
  const bLOODTYPEValues = Object.keys(BLOODTYPE);
  const mARITALSTATUSValues = Object.keys(MARITALSTATUS);

  const handleClose = () => {
    navigate('/patient' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateCreated = convertDateTimeToServer(values.dateCreated);
    values.dateUpdated = convertDateTimeToServer(values.dateUpdated);

    const entity = {
      ...patientEntity,
      ...values,
    };
    if (isNew) {
      dispatch(createEntity({ entity, id: account.hospitalId }));
    } else {
      dispatch(updateEntity({ entity, id: account.hospitalId }));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateCreated: displayDefaultDate(),
          dateUpdated: displayDefaultDate(),
          author: account.login,
        }
      : {
          // gender: 'MALE',
          // bloodType: 'A_PLUS',
          // maritialStatus: 'MARRIED',
          ...patientEntity,
          // dateCreated: convertDateTimeFromServer(patientEntity.dateCreated),
          dateUpdated: displayDefaultDate(),
          // author: account.login,
        };

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
      <Header pageName={'Gestion Patients'} />
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '5vh',
          marginTop: '9.5vh',
        }}
      >
        <Card
          style={{
            minHeight: '6.28vh',
            minWidth: '32vw',
            borderRadius: '20px',
            backgroundColor: '#11485C',
            textAlign: 'center',
            color: 'white',
            marginBottom: '5vh',
            marginLeft: '25vw',
            marginRight: '25vw',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'center',
            alignItems: 'center',
            paddingLeft: '1vw',
          }}
        >
          <Button
            replace
            onClick={() => window.history.back()}
            style={{ color: '#53BFD1', backgroundColor: '#11485C', borderColor: '#11485C' }}
          >
            {React.createElement(IoIosArrowBack, { size: '20' })}
          </Button>
          {isNew ? (
            <span>Enregistrement nouveau patient</span>
          ) : (
            <span>Mise à jour patient {patientEntity.lastName + ' ' + patientEntity.firstName}</span>
          )}
        </Card>
        <Card
          style={{
            minHeight: '70vh',
            marginRight: '5%',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            borderRadius: '15px',
            marginBottom: '2vh',
          }}
        >
          {isNew ? (
            <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', fontFamily: 'jost', marginLeft: '3%' }}>
              Remplir informations patient
            </span>
          ) : (
            <span style={{ marginTop: '1%', color: '#141414', fontSize: '19px', fontFamily: 'jost', marginLeft: '3%' }}>
              Modifications informations patient
            </span>
          )}
          <ValidatedForm
            defaultValues={defaultValues()}
            onSubmit={saveEntity}
            style={{
              width: '94%',
              marginLeft: '3%',
              minHeight: '80vh',
              display: 'flex',
              flexWrap: 'wrap',
              columnGap: '25px',
              marginTop: '1%',
              fontSize: '12px',
              fontWeight: '900',
              backgroundImage: 'url(content/images/NgirwiLogo.png)',
              backgroundRepeat: 'no-repeat',
              backgroundAttachment: 'fixed',
              backgroundPosition: '58% 110%',
            }}
          >
            {!isNew ? (
              <ValidatedField
                name="id"
                required
                readOnly
                id="patient-id"
                label="ID"
                validate={{ required: true }}
                placeholder="saisissez votre nom"
                style={{
                  borderRadius: '25px',
                  backgroundColor: '#F7FAFF',
                  borderColor: '#CBDCF7',
                  width: '22vw',
                }}
              />
            ) : null}
            <ValidatedField
              label="Nom"
              id="patient-lastName"
              name="lastName"
              data-cy="lastName"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              placeholder="Nom patient"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Prénom"
              id="patient-firstName"
              name="firstName"
              data-cy="firstName"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              placeholder="Prénom patient"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Date de naissance"
              id="patient-birthday"
              name="birthday"
              data-cy="birthday"
              type="date"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Lieu de naissance"
              id="patient-birthplace"
              name="birthplace"
              data-cy="birthplace"
              type="text"
              placeholder="Lieu de naissance patient"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Genre"
              id="patient-gender"
              name="gender"
              data-cy="gender"
              type="select"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            >
              {gENDERValues.map(gENDER => (
                <option value={gENDER} key={gENDER}>
                  {translateGender(gENDER)}
                </option>
              ))}
            </ValidatedField>
            <ValidatedField
              label="Adresse"
              id="patient-adress"
              name="adress"
              data-cy="adress"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              placeholder="Adresse patient"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Téléphone"
              id="patient-phone"
              name="phone"
              data-cy="phone"
              type="text"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              placeholder="Téléphone patient"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Pièce d'identité(verso)"
              id="patient-cni"
              name="cni"
              data-cy="cni"
              type="text"
              // validate={{
              //   required: { value: true, message: 'Ce champ est obligatoire.' },
              // }}
              placeholder="Numéro carte d'identité patient"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Profession"
              id="patient-job"
              name="job"
              data-cy="job"
              type="text"
              placeholder="saisissez votre profession"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            />
            <ValidatedField
              label="Groupe Sanguin"
              id="patient-bloodType"
              name="bloodType"
              data-cy="bloodType"
              type="select"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            >
              {bLOODTYPEValues.map(bLOODTYPE => (
                <option value={bLOODTYPE} key={bLOODTYPE}>
                  {translateBloodType(bLOODTYPE)}
                </option>
              ))}
            </ValidatedField>
            <ValidatedField
              label="Statut matrimonial"
              id="patient-maritialStatus"
              name="maritialStatus"
              data-cy="maritialStatus"
              type="select"
              style={{
                borderRadius: '25px',
                backgroundColor: '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
              }}
            >
              {mARITALSTATUSValues.map(mARITALSTATUS => (
                <option value={mARITALSTATUS} key={mARITALSTATUS}>
                  {translateMaritalStatus(mARITALSTATUS)}
                </option>
              ))}
            </ValidatedField>
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
            <Button
              id="save-entity"
              data-cy="entityCreateSaveButton"
              type="submit"
              disabled={updating}
              style={{
                borderRadius: '25px',
                color: 'white',
                backgroundColor: '#56B5C5',
                borderColor: '#56B5C5',
                flex: '1 1 100%',
              }}
            >
              Enregistrer
            </Button>
            &nbsp;
            <Button
              onClick={() => (confirm('Êtes-vous sur de vouloir quitter?') === true ? window.history.back() : null)}
              id="cancel-save"
              data-cy="entityCreateCancelButton"
              color="info"
              style={{
                borderRadius: '25px',
                color: 'white',
                backgroundColor: '#EC4747',
                borderColor: '#EC4747',
                marginBottom: '2vh',
                flex: '1 1 100%',
              }}
            >
              <span className="d-none d-md-inline">Annuler</span>
            </Button>
          </ValidatedForm>
        </Card>
      </div>
      {/* <Row className="justify-content-center">
        <Col md="8">
          {isNew ? (
            <h2 id="ngirwiFrontEndApp.patient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
              Créer un Patient
            </h2>
          ) : (
            <h2 id="ngirwiFrontEndApp.patient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
              Editer Patient {patientEntity.lastName + ' ' + patientEntity.firstName}<br/>
              cni : {patientEntity.cni}
            </h2>
          )}
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="patient-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Prénom"
                id="patient-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Nom"
                id="patient-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Date de naissance"
                id="patient-birthday"
                name="birthday"
                data-cy="birthday"
                type="date"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField label="Lieu de naissance" id="patient-birthplace" name="birthplace" data-cy="birthplace" type="text" />
              <ValidatedField label="Genre" id="patient-gender" name="gender" data-cy="gender" type="select">
                {gENDERValues.map(gENDER => (
                  <option value={gENDER} key={gENDER}>
                    {translateGender(gENDER)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Adresse"
                id="patient-adress"
                name="adress"
                data-cy="adress"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Téléphone"
                id="patient-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField
                label="Cni"
                id="patient-cni"
                name="cni"
                data-cy="cni"
                type="text"
                validate={{
                  required: { value: true, message: 'Ce champ est obligatoire.' },
                }}
              />
              <ValidatedField label="Profession" id="patient-job" name="job" data-cy="job" type="text" />
              <ValidatedField label="Groupe Sanguin" id="patient-bloodType" name="bloodType" data-cy="bloodType" type="select">
                {bLOODTYPEValues.map(bLOODTYPE => (
                  <option value={bLOODTYPE} key={bLOODTYPE}>
                    {translateBloodType(bLOODTYPE)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Status Matrimonial"
                id="patient-maritialStatus"
                name="maritialStatus"
                data-cy="maritialStatus"
                type="select"
              >
                {mARITALSTATUSValues.map(mARITALSTATUS => (
                  <option value={mARITALSTATUS} key={mARITALSTATUS}>
                    {translateMaritalStatus(mARITALSTATUS)}
                  </option>
                ))}
              </ValidatedField>
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient" replace color="info">
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
      </Row> */}
    </div>
  );
};

export default PatientUpdate;
