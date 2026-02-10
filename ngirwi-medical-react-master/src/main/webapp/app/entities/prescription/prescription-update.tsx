import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { toast } from 'react-toastify';
import {
  convertDateTimeFromServerToDate,
  convertDateTimeFromServerToHours,
  convertDateTimeFromServerToMinute,
  convertDateTimeToServer,
  displayDefaultDate,
  displayDefaultDateTime,
} from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getConsultations } from 'app/entities/consultation/consultation.reducer';
import { getEntity, updateEntity, reset, createEntityBis } from './prescription.reducer';
import { getEntitiesBis as getPatients } from '../patient/patient.reducer';
import { getMedecineByPrescriptionId } from '../medecine/medecine.reducer';
import { IMedecine } from 'app/shared/model/medecine.model';
import { getMyHospital } from '../hospital/hospital.reducer';
import { Page, Text, Image, View, Document, PDFDownloadLink, Font } from '@react-pdf/renderer';
import Header from 'app/shared/layout/header/header';
import { IoIosAddCircle, IoIosAddCircleOutline, IoIosArrowBack, IoIosRemoveCircle } from 'react-icons/io';
import { BiDownload } from 'react-icons/bi';

export const PrescriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { idEdit } = useParams<'idEdit'>();
  const { idConsultation } = useParams<'idConsultation'>();
  const isNew = id === undefined;

  const consultations = useAppSelector(state => state.consultation.entities);
  const prescriptionEntity = useAppSelector(state => state.prescription.entity);
  const hospital = useAppSelector(state => state.hospital.entity);
  const patientList = useAppSelector(state => state.patient.entities);
  const loading = useAppSelector(state => state.prescription.loading);
  const updating = useAppSelector(state => state.prescription.updating);
  const updateSuccess = useAppSelector(state => state.prescription.updateSuccess);
  const errorMessage = useAppSelector(state => state.prescription.errorMessage);

  const account = useAppSelector(state => state.authentication.account);
  const [consul, setConsul] = useState(idConsultation);
  const [medecines, setMedecine] = useState<IMedecine[]>([]);

  const handleConsulChange = e => {
    setConsul(e.target.value);
  };

  const handleClose = () => {
    navigate('/prescription' + location.search);
  };
  function rtn() {
    window.history.back();
  }

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
      getMedecineByPrescriptionId(Number(id))
        .then(data => {
          setMedecine(data);
        })
        .catch(error => {
          if (DEVELOPMENT) {
            console.error('Error fetching medicine:', error);
          }
        });
    }

    dispatch(getMyHospital());
    dispatch(getPatients({}));
    dispatch(getConsultations({}));
  }, [isNew, id, dispatch, idConsultation]); // Specify dependencies here

  useEffect(() => {
    if (prescriptionEntity?.consultation?.id) {
      setConsul(prescriptionEntity.consultation.id.toString());
    }
  }, [prescriptionEntity]);

  const consultation = consultations.filter(consult => {
    return consult.id == idConsultation;
  });

  useEffect(() => {
    if (updateSuccess) {
      toast.success(isNew ? 'Ordonnance créée avec succès!' : 'Ordonnance mise à jour avec succès!');
      handleClose();
    }
  }, [updateSuccess]);

  // Display error message when creation/update fails
  useEffect(() => {
    if (errorMessage) {
      toast.error(errorMessage);
    }
  }, [errorMessage]);

  const saveEntity = values => {
    values.creationDate = convertDateTimeToServer(values.creationDate);

    const entity = {
      ...prescriptionEntity,
      ...values,
      consultation: consultations.find(it => it.id.toString() === values.consultation.toString()),
      medecines,
    };

    if (isNew) {
      dispatch(createEntityBis(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          creationDate: displayDefaultDate(),
          consultation: idConsultation,
          author: account.login,
        }
      : {
          ...prescriptionEntity,
          creationDate: convertDateTimeFromServerToMinute(prescriptionEntity.creationDate),
          consultation: prescriptionEntity?.consultation?.id,
        };

  const handleChange = (i, e) => {
    const newFormValues = [...medecines];
    newFormValues[i] = { ...newFormValues[i], [e.target.name]: e.target.value };
    setMedecine(newFormValues);
  };

  const addFormFields = () => {
    const newMedecine: IMedecine = {
      id: undefined, // Assuming id is auto-generated or managed elsewhere
      name: null,
      duration: null,
      frequency: null,
      ordonance: null,
    };
    setMedecine([...medecines, newMedecine]);
  };

  useEffect(() => {
    if (medecines.length === 0) {
      addFormFields();
    }
  }, [medecines.length]);

  const removeFormFields = i => {
    const newFormValues = [...medecines];
    newFormValues.splice(i, 1);
    setMedecine(newFormValues);
  };
  let p;
  const infosPatient = () => {
    const selectedConsultation = consultations.find(consult => consult.id.toString() === consul);
    if (selectedConsultation) {
      return (
        selectedConsultation.patient?.lastName?.toUpperCase() +
        ' ' +
        selectedConsultation.patient?.firstName
          ?.split(' ')
          .map(a => a.charAt(0).toUpperCase() + a.slice(1))
          .join(' ')
      );
    }
    return '';
  };

  p = infosPatient();

  const showID = () => {
    // Function to show patient ID
  };

  Font.register({
    family: 'Poppins',
    fonts: [
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Bold.woff', fontWeight: 'bold' },
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Medium.woff', fontWeight: 'medium' },
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Medium.woff', fontWeight: 'thin' },
    ],
  });
  const doc = (
    <Document>
      <Page style={{ display: 'flex', flexDirection: 'column', fontFamily: 'Poppins' }}>
        {/* Header Section */}
        <View
          style={{
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-around',
            alignItems: 'center',
            borderBottom: '1px solid green',
            paddingBottom: '10px',
            marginTop: '20px',
          }}
        >
          <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
            <Text style={{ fontSize: '18px', color: 'green', marginBottom: '9px', fontWeight: 'bold' }}>
              {account.lastName + ' ' + account.firstName}
            </Text>
            <Text style={{ fontSize: '15px', marginBottom: '9px', fontWeight: 'medium' }}>Médecin général</Text>
            <Text style={{ fontSize: '15px', fontWeight: 'thin' }}>{hospital?.phone}</Text>
          </View>
          <View>
            <Image
              style={{ width: '60px', height: '60px' }}
              src={
                hospital?.logo && hospital?.logoContentType
                  ? `data:${hospital.logoContentType};base64,${hospital.logo}`
                  : 'content/images/logo-medecin-240x300.png'
              }
            />
          </View>
          <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
            <Text style={{ fontSize: '20px', color: 'green', marginBottom: '9px', fontWeight: 'bold' }}>{hospital?.name}</Text>
            <Text style={{ fontSize: '15px', marginBottom: '9px', fontWeight: 'medium' }}>{hospital?.adress}</Text>
            <Text style={{ fontSize: '15px', fontWeight: 'thin' }}></Text>
          </View>
        </View>

        {/* Title and Patient Information Section */}
        <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', marginTop: '15px' }}>
          <Text style={{ fontSize: '35px', fontWeight: 'extrabold', marginBottom: '9px' }}>Ordonnance Médicale</Text>

          {/* Information Rows */}
          <View style={{ display: 'flex', flexDirection: 'row', alignItems: 'flex-start', justifyContent: 'center' }}>
            {/* Labels */}
            <View style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', marginRight: '10px', minWidth: '150px' }}>
              <Text style={{ fontSize: '18px', marginBottom: '9px' }}>Fait à Dakar Le:</Text>
              <Text style={{ fontSize: '18px', marginBottom: '9px' }}>Nom & Prénom(s):</Text>
            </View>
            {/* Values */}
            <View style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
              <Text style={{ fontSize: '18px', marginBottom: '9px' }}>
                {convertDateTimeFromServerToDate(displayDefaultDateTime()) +
                  ' à ' +
                  convertDateTimeFromServerToHours(displayDefaultDateTime())}
              </Text>
              <Text>
                {prescriptionEntity.consultation?.patient
                  ? `${prescriptionEntity.consultation.patient.lastName.toUpperCase()} ${
                      prescriptionEntity.consultation.patient.firstName
                        ? prescriptionEntity.consultation.patient.firstName
                            .split(' ')
                            .map(a => a.charAt(0).toUpperCase() + a.slice(1))
                            .join(' ')
                        : ''
                    }`
                  : infosPatient()}
              </Text>
            </View>
          </View>
        </View>

        {/* Watermark Image */}
        <Image
          src={
            hospital?.logo && hospital?.logoContentType
              ? `data:${hospital.logoContentType};base64,${hospital.logo}`
              : 'content/images/logo-medecin-240x300.png'
          }
          style={{ position: 'absolute', top: '335px', left: '15vw', zIndex: '1', width: '70vw', height: '40vh', opacity: 0.1 }}
        />

        {/* Medications Table */}
        <View
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginTop: '15px',
            border: '3px solid silver',
            marginLeft: '10vw',
            marginRight: '5vw',
            width: '80vw',
            zIndex: '0',
          }}
        >
          <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
            <Text
              style={{
                width: '40vw',
                borderRight: '3px solid silver',
                textTransform: 'uppercase',
                fontSize: '15px',
                color: 'green',
                paddingTop: '3px',
                paddingBottom: '5px',
                textAlign: 'center',
              }}
            >
              Médicament(s)
            </Text>
            <Text
              style={{
                width: '20vw',
                textTransform: 'uppercase',
                fontSize: '15px',
                color: 'green',
                paddingTop: '3px',
                paddingBottom: '5px',
                textAlign: 'center',
              }}
            >
              Durée
            </Text>
            <Text
              style={{
                width: '20vw',
                borderLeft: '3px solid silver',
                textTransform: 'uppercase',
                fontSize: '15px',
                color: 'green',
                paddingTop: '3px',
                paddingBottom: '5px',
                textAlign: 'center',
              }}
            >
              Fréquence
            </Text>
          </View>
          {medecines.map((element, i) => (
            <View key={`entity-${i}`} style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
              <Text
                style={{
                  width: '40vw',
                  minHeight: '4vh',
                  borderRight: '3px solid silver',
                  borderTop: '3px solid silver',
                  fontSize: '15px',
                  paddingTop: '5px',
                  paddingBottom: '3px',
                  textAlign: 'center',
                }}
              >
                {element.name}
              </Text>
              <Text
                style={{
                  width: '20vw',
                  minHeight: '4vh',
                  borderTop: '3px solid silver',
                  fontSize: '15px',
                  paddingTop: '5px',
                  paddingBottom: '3px',
                  textAlign: 'center',
                }}
              >
                {element.duration !== null && element.duration !== undefined ? element.duration : ''} J
              </Text>
              <Text
                style={{
                  width: '20vw',
                  minHeight: '4vh',
                  borderTop: '3px solid silver',
                  borderLeft: '3px solid silver',
                  fontSize: '15px',
                  paddingTop: '5px',
                  paddingBottom: '3px',
                  textAlign: 'center',
                }}
              >
                {element.frequency} /J
              </Text>
            </View>
          ))}
        </View>

        {/* Footer */}
        <View
          style={{
            borderTop: '2px solid green',
            position: 'absolute',
            top: '93vh',
            width: '100vw',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginBottom: '10px',
            paddingTop: '6px',
          }}
        >
          <Text style={{ fontSize: '14px' }}>Propulsé par l&apos;entreprise NGIRWI S.A.R.L</Text>
          <Text style={{ fontSize: '12px' }}>www.ngirwisarl.com</Text>
        </View>
      </Page>
    </Document>
  );

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
      <Header pageName={'Ordonnance'} />
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
            height: '6.28vh',
            width: '32vw',
            maxWidth: '50vw',
            borderRadius: '20px',
            backgroundColor: '#11485C',
            textAlign: 'center',
            color: 'white',
            marginBottom: '5vh',
            marginLeft: '25vw',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'center',
            alignItems: 'center',
          }}
        >
          <Button
            replace
            onClick={() => window.history.back()}
            style={{ color: '#53BFD1', backgroundColor: '#11485C', borderColor: '#11485C' }}
          >
            {React.createElement(IoIosArrowBack, { size: '20' })}
          </Button>
          <span>{isNew ? 'Enregistrement nouvelle ' : idEdit === 'voir' ? 'Consultation ' : 'Modification '} ordonnance</span>
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
          <div
            style={{
              marginTop: '1%',
              paddingBottom: '2vh',
              position: 'sticky',
              top: '0',
              display: 'flex',
              flexDirection: 'row',
              alignItems: 'center',
              backgroundColor: 'white',
              borderBottom: '1px solid #B3C0D3',
            }}
          >
            <span style={{ marginLeft: '3%', color: '#141414', fontSize: '19px', width: '65vw' }}>
              {isNew ? 'Nouvelle ordonnance ' : 'Ordonnance '}patient
            </span>
            <div style={{ display: 'flex', flexDirection: 'row', gap: '3vh' }}>
              <PDFDownloadLink
                style={{ textDecoration: 'none' }}
                document={doc}
                fileName={`ordonnance_${JSON.stringify(displayDefaultDateTime())}`}
              >
                <span
                  style={{
                    cursor: 'pointer',
                    fontWeight: '600',
                    color: '#ffffff',
                    textAlign: 'center',
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center',
                    gap: '8px',
                    backgroundColor: '#56B5C5',
                    padding: '10px 20px',
                    borderRadius: '25px',
                    boxShadow: '0 4px 15px rgba(86, 181, 197, 0.3)',
                    transition: 'all 0.3s ease',
                    fontSize: '14px',
                  }}
                >
                  {React.createElement(BiDownload, { size: '20' })} <span>Télécharger PDF</span>
                </span>
              </PDFDownloadLink>
            </div>
          </div>
          <ValidatedForm
            style={{
              width: '94%',
              marginLeft: '3%',
              height: '70%',
              display: 'flex',
              flexWrap: 'wrap',
              columnGap: '25px',
              marginTop: '1%',
              fontSize: '12px',
              fontWeight: '900',
              backgroundImage: 'url(content/images/NgirwiLogo.png)',
              backgroundRepeat: 'no-repeat',
              backgroundAttachment: 'fixed',
              backgroundPosition: '65% 90%',
              backgroundSize: '50% 50%',
            }}
            defaultValues={defaultValues()}
            onSubmit={saveEntity}
          >
            {!isNew ? <ValidatedField hidden name="id" required readOnly id="bill-id" label="ID" validate={{ required: true }} /> : null}
            <ValidatedField
              style={{ borderRadius: '25px', backgroundColor: '#A9B7CD', color: '#F6FAFF', borderColor: '#CBDCF7', width: '30vw' }}
              disabled
              label="Date de création"
              id="prescription-creationDate"
              name="creationDate"
              data-cy="creationDate"
              type={isNew ? 'datetime-local' : 'datetime'}
              placeholder="YYYY-MM-DD HH:mm"
            />

            {/* <ValidatedField
              style={{ borderRadius: "25px", backgroundColor: "#A9B7CD", color: "#F6FAFF", borderColor: "#CBDCF7" }}
              disabled label="Date" id="bill-date" name="date" data-cy="date" type="datetime-local" placeholder="YYYY-MM-DD HH:mm" /> */}
            <ValidatedField hidden label="Author" id="prescription-author" name="author" data-cy="author" type="text" />
            <ValidatedField
              onChange={b => handleConsulChange(b)}
              style={
                isNew
                  ? { borderRadius: '25px', borderColor: '#CBDCF7', width: '37vw' }
                  : { borderRadius: '25px', backgroundColor: '#A9B7CD', borderColor: '#CBDCF7', color: '#F6FAFF', width: '37vw' }
              }
              disabled={isNew ? false : true}
              id="prescription-consultation"
              name="consultation"
              data-cy="medecine"
              label="Consultation"
              type="select"
            >
              <option value="" key="0" />
              {consultations
                ? consultations.map(otherEntity =>
                    patientList.map(patient =>
                      patient.id === otherEntity?.patient?.id ? (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          Faite le {convertDateTimeFromServerToDate(otherEntity?.dateTime)} à {patient?.lastName?.toUpperCase()}{' '}
                          {patient?.firstName?.charAt(0).toUpperCase() + patient?.firstName?.slice(1)}
                        </option>
                      ) : null
                    )
                  )
                : null}
            </ValidatedField>
            {/* <ValidatedField style={isNew ? { borderRadius: "25px", borderColor: "#CBDCF7" } : { borderRadius: "25px", backgroundColor: "#A9B7CD", borderColor: "#CBDCF7", color: "#F6FAFF" }} disabled={isNew ? false : true} id="bill-patient" name="patient" data-cy="patient" label="Patient" type="select">
              <option value="" key="0" />
              {patients
                ? patients.map(otherEntity => (
                  <option value={otherEntity.id} key={otherEntity.id}>
                    {otherEntity.lastName + ' ' + otherEntity.firstName}
                  </option>
                ))
                : null}
            </ValidatedField> */}

            {medecines.map((element, index, arr) => (
              <div
                style={{
                  display: 'flex',
                  flex: '1 1 100%',
                  flexWrap: 'wrap',
                  gap: '0.7vw',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
                key={index}
              >
                <ValidatedField
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '28vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                  disabled={idEdit === 'voir' ? true : false}
                  type="text"
                  label="Médicament"
                  name="name"
                  value={element.name || ''}
                  onChange={e => handleChange(index, e)}
                />
                <ValidatedField
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '20vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                  disabled={idEdit === 'voir' ? true : false}
                  type="number"
                  label="Durée (en Jours)"
                  name="duration"
                  value={element.duration || ''}
                  onChange={e => handleChange(index, e)}
                />
                <ValidatedField
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '20vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                  disabled={idEdit === 'voir' ? true : false}
                  label="Féquence (par Jour)"
                  type="number"
                  name="frequency"
                  value={element.frequency || ''}
                  onChange={e => handleChange(index, e)}
                  validate={{
                    required: { value: false, message: 'Ce champ est obligatoire.' },
                    validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                  }}
                />
                {arr.length - 1 === index ? (
                  <span
                    hidden={idEdit === 'voir' ? true : false}
                    onClick={idEdit !== 'voir' ? () => addFormFields() : null}
                    style={{
                      backgroundColor: 'transparent',
                      borderColor: 'transparent',
                      color: '#11485C',
                      borderRadius: '50%',
                      fontWeight: '900',
                      width: '1vw',
                      cursor: 'pointer',
                    }}
                  >
                    {React.createElement(IoIosAddCircle, { size: '25' })}
                  </span>
                ) : (
                  <span style={{ width: '1vw', color: 'transparent' }}>{React.createElement(IoIosAddCircleOutline, { size: '25' })}</span>
                )}

                {index ? (
                  <span
                    hidden={idEdit === 'voir' ? true : false}
                    onClick={idEdit !== 'voir' ? () => removeFormFields(index) : null}
                    style={{
                      backgroundColor: 'transparent',
                      cursor: 'pointer',
                      borderColor: 'transparent',
                      color: '#EC4747',
                      fontWeight: '900',
                      width: '0.1vw',
                      display: 'inline',
                    }}
                  >
                    {React.createElement(IoIosRemoveCircle, { size: '25' })}
                  </span>
                ) : null}
              </div>
            ))}

            <Button
              style={{ borderRadius: '25px', flex: '1 1 100%', marginTop: '2vh' }}
              id="cancel-save"
              data-cy="entityCreateCancelButton"
              onClick={() => (confirm('Êtes-vous sur de vouloir quitter?') === true ? window.history.back() : null)}
              replace
              color="danger"
            >
              <span className="d-none d-md-inline"> Retour</span>
            </Button>
            <Button
              style={{ borderRadius: '25px', flex: '1 1 100%', marginTop: '2vh', marginBottom: '2vh' }}
              color="primary"
              id="save-entity"
              data-cy="entityCreateSaveButton"
              type="submit"
              disabled={updating}
            >
              Sauvegarder
            </Button>
          </ValidatedForm>
        </Card>
      </div>
    </div>
  );
};

export default PrescriptionUpdate;
