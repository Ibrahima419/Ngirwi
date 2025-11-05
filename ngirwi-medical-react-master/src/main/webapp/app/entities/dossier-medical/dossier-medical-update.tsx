import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Card } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDate, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntitiesBis as getPatients } from 'app/entities/patient/patient.reducer';
import { IDossierMedical } from 'app/shared/model/dossier-medical.model';
import { getEntity, updateEntity, createEntity, reset } from './dossier-medical.reducer';
import Header from 'app/shared/layout/header/header';
import { IoIosArrowBack } from 'react-icons/io';
import { FiLock } from 'react-icons/fi';
import Patient from '../patient/patient';

export const DossierMedicalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { idPatient } = useParams<'idPatient'>();
  const { idEdit } = useParams<'idEdit'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const dossierMedicalEntity = useAppSelector(state => state.dossierMedical.entity);
  const loading = useAppSelector(state => state.dossierMedical.loading);
  const updating = useAppSelector(state => state.dossierMedical.updating);
  const updateSuccess = useAppSelector(state => state.dossierMedical.updateSuccess);

  const account = useAppSelector(state => state.authentication.account);

  const handleClose = () => {
    navigate('/dossier-medical' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({ id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0 }));
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
      ...dossierMedicalEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
      window.history.back();
      // navigate(`/patient/${idPatient}`);
    } else {
      dispatch(updateEntity(entity));
      window.history.back();
      // navigate(`/patient/${idPatient}`);
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateCreated: displayDefaultDate(),
          dateUpdated: displayDefaultDate(),
          author: account.login,
          patient: idPatient,
        }
      : {
          ...dossierMedicalEntity,
          // dateCreated: convertDateTimeFromServer(dossierMedicalEntity.dateCreated),
          dateUpdated: displayDefaultDate(),
          patient: dossierMedicalEntity?.patient?.id,
          author: account.login,
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
      <Header pageName="Gestion patients" />

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
              minWidth: '30vw',
              borderRadius: '20px',
              backgroundColor: '#11485C',
              textAlign: 'center',
              color: 'white',
              marginTop: '5vh',
              marginBottom: '5vh',
              boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'flex-start',
              alignItems: 'center',
              gap: '2vw',
              paddingLeft: isNew ? '1vw' : '2vw',
              marginLeft: '25vw',
            }}
          >
            <Button onClick={() => window.history.back()} style={{ color: '#53BFD1', backgroundColor: '#11485C', borderColor: '#11485C' }}>
              {React.createElement(IoIosArrowBack, { size: '20' })}
            </Button>
            {isNew ? (
              <span>Enregistrement nouveau dossier médical</span>
            ) : (
              <span>{idEdit === 'voir' ? 'Consultation dossier médical ' : 'Mise à jour dossier médical '}</span>
            )}
          </Card>
        </div>

        <Card
          style={{
            minHeight: '110vh',
            width: '80vw',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            borderRadius: '15px',
            marginBottom: '30px',
            fontFamily: 'Jost',
            fontStyle: 'normal',
            fontWeight: '500',
            fontSize: '15px',
          }}
        >
          <div style={{ marginTop: '1%', display: 'flex', justifyContent: 'flex-end' }}>
            {idEdit === 'voir' ? (
              <Button
                className="btn btn-primary btn-sm"
                tag={Link}
                to={`/dossier-medical/${dossierMedicalEntity?.id}/edit`}
                style={{
                  backgroundColor: '#53BFD1',
                  borderColor: '#53BFD1',
                  color: '#FFFFFF',
                  fontSize: '19px',
                  marginRight: '3%',
                  width: '13%',
                  borderRadius: '25px',
                  boxShadow: '0px 1px 3px #141414',
                }}
              >
                <FontAwesomeIcon icon={'pencil'} /> Modifier
              </Button>
            ) : null}
          </div>

          {/* {isNew?(<span style={{marginTop:"1%", color:"#141414",fontSize:"19px", marginLeft:"3%"}}>Remplir informations patient</span>):(
              <span style={{marginTop:"1%", color:"#141414",fontSize:"19px", fontFamily:"jost", marginLeft:"3%"}}> {idEdit==="voir"?"Dossier médical patient":"Modifications dossier médical patient"} </span>
             )}  */}
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
                  dossierMedicalEntity?.patient?.id === otherEntity.id || idPatient == otherEntity.id ? (
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
              display: 'flex',
              columnGap: '30px',
              marginTop: '1%',
              fontSize: '18px',
              fontWeight: '900',
              flexWrap: 'wrap',
              backgroundImage: 'url(content/images/NgirwiLogo.png)',
              backgroundRepeat: 'no-repeat',
              backgroundAttachment: 'fixed',
              backgroundPosition: '65% 100%',
              backgroundSize: '40% 40%',
            }}
          >
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Motif de la consultation"
              id="dossier-medical-motifConsultation"
              name="motifConsultation"
              data-cy="motifConsultation"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '35vw',
                height: '20vh',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Histoire de la maladie"
              id="dossier-medical-histoireMaladie"
              name="histoireMaladie"
              data-cy="histoireMaladie"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '35vw',
                height: '20vh',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Antécédents personnels"
              id="dossier-medical-antecedantsPersonnels"
              name="antecedantsPersonnels"
              data-cy="antecedantsPersonnels"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
                height: '20vh',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Antécédents chirurgicaux"
              id="dossier-medical-antecedantsChirurgicaux"
              name="antecedantsChirurgicaux"
              data-cy="antecedantsChirurgicaux"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '22vw',
                height: '20vh',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Antécédents familiaux"
              id="dossier-medical-antecedantsFamiliaux"
              name="antecedantsFamiliaux"
              data-cy="antecedantsFamiliaux"
              type="textarea"
              validate={{
                required: { value: true, message: 'Ce champ est obligatoire.' },
              }}
              style={{
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '24vw',
                height: '20vh',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Gynéco"
              id="dossier-medical-gyneco"
              name="gynecoObstretrique"
              data-cy="gynecoObstretrique"
              type="textarea"
              // validate={{
              //   required: { value: true, message: 'Ce champ est obligatoire.' },
              // }}
              style={{
                rowGap: '5vh',
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '35vw',
                height: '10vh',
              }}
            />
            <ValidatedField
              disabled={idEdit === 'voir' ? true : false}
              label="Obstétrique"
              id="dossier-medical-syndromique"
              name="syndromique"
              data-cy="syndromique"
              type="textarea"
              // validate={{
              //   required: { value: true, message: 'Ce champ est obligatoire.' },
              // }}
              style={{
                rowGap: '5vh',
                marginBottom: '20px',
                borderRadius: '25px',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                borderColor: '#CBDCF7',
                width: '35vw',
                height: '10vh',
              }}
            />
            <ValidatedField
              hidden
              defaultValue={'aucun'}
              disabled={idEdit === 'voir' ? true : false}
              label="Obstétrique"
              id="dossier-medical-terrain"
              name="terrain"
              data-cy="terrain"
              type="textarea"
            />
            <ValidatedField
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                width: '15vw',
              }}
              label="Père"
              id="dossier-medical-dad"
              name="dad"
              data-cy="dad"
              type="text"
            />
            <ValidatedField
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                width: '15vw',
              }}
              label="Mère"
              id="dossier-medical-mom"
              name="mom"
              data-cy="mom"
              type="text"
            />
            <ValidatedField
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                width: '15vw',
              }}
              label="Fratrie"
              id="dossier-medical-siblings"
              name="siblings"
              data-cy="siblings"
              type="text"
            />
            <ValidatedField
              style={{
                borderRadius: '25px',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '#F7FAFF',
                color: idEdit === 'voir' ? '#F6FAFF' : 'black',
                width: '15vw',
              }}
              label="Descendants"
              id="dossier-medical-descendants"
              name="descendants"
              data-cy="descendants"
              type="text"
            />
            <Button
              hidden={idEdit === 'voir' ? true : false}
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
              onClick={() => {
                confirm('Êtes-vous sur de vouloir quitter?') === true ? window.history.back() : null;
              }}
              id="cancel-save"
              data-cy="entityCreateCancelButton"
              replace
              color="info"
              style={{
                borderRadius: '25px',
                color: 'white',
                backgroundColor: '#EC4747',
                borderColor: '#EC4747',
                textAlign: 'center',
                fontSize: idEdit === 'voir' ? '20px' : '',
                flex: '1 1 100%',
                marginBottom: '20px',
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
    // <div style={{marginLeft:"16vw"}}>
    //   <Row className="justify-content-center">
    //     <Col md="8">
    //       <h2 id="ngirwiFrontEndApp.dossierMedical.home.createOrEditLabel" data-cy="DossierMedicalCreateUpdateHeading">
    //         Créer ou éditer un Dossier Medical
    //       </h2>
    //     </Col>
    //   </Row>
    //   <Row className="justify-content-center">
    //     <Col md="8">
    //       {loading ? (
    //         <p>Loading...</p>
    //       ) : (
    //         <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
    //           {!isNew ? (
    //             <ValidatedField name="id" required readOnly id="dossier-medical-id" label="ID" validate={{ required: true }} />
    //           ) : null}
    //           <ValidatedField
    //             label="Motif Consultation"
    // id="dossier-medical-motifConsultation"
    // name="motifConsultation"
    // data-cy="motifConsultation"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    //           <ValidatedField
    // label="Histoire Maladie"
    // id="dossier-medical-histoireMaladie"
    // name="histoireMaladie"
    // data-cy="histoireMaladie"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    //           <ValidatedField
    //             label="Terrain"
    //             id="dossier-medical-terrain"
    //             name="terrain"
    //             data-cy="terrain"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    //           <ValidatedField
    //             label="Antecedants Personnels"
    //             id="dossier-medical-antecedantsPersonnels"
    //             name="antecedantsPersonnels"
    //             data-cy="antecedantsPersonnels"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    //           <ValidatedField
    //             label="Antecedants Chirurgicaux"
    // id="dossier-medical-antecedantsChirurgicaux"
    // name="antecedantsChirurgicaux"
    // data-cy="antecedantsChirurgicaux"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    //           <ValidatedField
    //             label="Antecedants Familiaux"
    // id="dossier-medical-antecedantsFamiliaux"
    // name="antecedantsFamiliaux"
    // data-cy="antecedantsFamiliaux"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    //           <ValidatedField
    //             label="Gyneco Obstretrique"
    // id="dossier-medical-gynecoObstretrique"
    // name="gynecoObstretrique"
    // data-cy="gynecoObstretrique"
    //             type="text"
    //           />
    //           <ValidatedField
    //             label="Résumé Syndromique"
    //             id="dossier-medical-syndromique"
    //             name="syndromique"
    //             data-cy="syndromique"
    //             type="text"
    //             validate={{
    //               required: { value: true, message: 'Ce champ est obligatoire.' },
    //             }}
    //           />
    // <ValidatedField label="Père" id="dossier-medical-dad" name="dad" data-cy="dad" type="text" />
    // <ValidatedField label="Mère" id="dossier-medical-mom" name="mom" data-cy="mom" type="text" />
    // <ValidatedField label="Frères" id="dossier-medical-siblings" name="siblings" data-cy="siblings" type="text" />
    // <ValidatedField label="Descendants" id="dossier-medical-descendants" name="descendants" data-cy="descendants" type="text" />
    //           <ValidatedField
    //             hidden
    //             label="Date Created"
    //             id="dossier-medical-dateCreated"
    //             name="dateCreated"
    //             data-cy="dateCreated"
    //             type="datetime-local"
    //             placeholder="YYYY-MM-DD HH:mm"
    //           />
    //           <ValidatedField
    //             hidden
    //             label="Date Updated"
    //             id="dossier-medical-dateUpdated"
    //             name="dateUpdated"
    //             data-cy="dateUpdated"
    //             type="datetime-local"
    //             placeholder="YYYY-MM-DD HH:mm"
    //           />
    //           <ValidatedField hidden label="Author" id="dossier-medical-author" name="author" data-cy="author" type="text" />
    //           <ValidatedField hidden id="dossier-medical-patient" name="patient" data-cy="patient" label="Patient" type="select">
    //             <option value="" key="0" />
    //             {patients
    //               ? patients.map(otherEntity => (
    //                 <option value={otherEntity.id} key={otherEntity.id}>
    //                   {otherEntity.id}
    //                 </option>
    //               ))
    //               : null}
    //           </ValidatedField>
    //           <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dossier-medical" replace color="info">
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

export default DossierMedicalUpdate;
