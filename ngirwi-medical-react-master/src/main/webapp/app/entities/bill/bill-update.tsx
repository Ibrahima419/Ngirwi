import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';

import {
  convertDateTimeFromServer,
  convertDateTimeFromServerToDate,
  convertDateTimeFromServerToHours,
  convertDateTimeToServer,
  displayDefaultDate,
  displayDefaultDateTime,
} from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntitiesBis as getPatients } from 'app/entities/patient/patient.reducer';
import { getEntity as getHospital } from '../hospital/hospital.reducer';
import { createEntity, getEntity, reset, updateEntity } from './bill.reducer';
import { getElemntByBillId } from '../bill-element/bill-element.reducer';

//pdf
import { Page, Text, View, Document, PDFDownloadLink, Font } from '@react-pdf/renderer';
import Header from 'app/shared/layout/header/header';
import { IoIosAddCircleOutline, IoIosArrowBack, IoIosAddCircle, IoIosRemoveCircle } from 'react-icons/io';
import { BiDownload } from 'react-icons/bi';
import { IBillElement } from 'app/shared/model/bill-element.model';
export const BillUpdate = () => {
  const dispatch = useAppDispatch();
  const assurance = [
    { label: '', id: '0' },
    { label: 'ALLIANCE INTERNATIONAL', id: '1' },
    { label: 'AMSA ASSURANCES', id: '2' },
    { label: 'ASKIA ASSURANCES', id: '3' },
    { label: 'ASSOCIATION ASSOFAL', id: '4' },
    { label: 'ASSUR CONSEILS MARSH', id: '5' },
    { label: 'ASSURANCES LA SECURITE SENEGALAISE', id: '6' },
    { label: 'AXA ASSURANCES', id: '7' },
    { label: 'AXA INTERNATIONAL', id: '8' },
    { label: 'CGA SANTE', id: '9' },
    { label: 'CIGNA', id: '10' },
    { label: 'CNART ASSURANCES', id: '11' },
    { label: 'COLINA', id: '12' },
    { label: 'EURO CENTER', id: '13' },
    { label: 'EUROP ASSISTANCE', id: '14' },
    { label: 'GMC', id: '15' },
    { label: 'GRAS SAVOYE', id: '16' },
    { label: 'NSIA', id: '17' },
    { label: 'PREVOYANCE ASSURANCE', id: '18' },
    { label: 'SAAR VIE', id: '19' },
    { label: 'SALAMA ASSURANCES', id: '20' },
    { label: 'SEGMA', id: '21' },
    { label: 'SONAM', id: '22' },
    { label: 'UASEN VIE', id: '23' },
  ];
  const ipm = [
    { label: '', id: '0' },
    { label: 'AFRIC MANAGEMENT', id: '1' },
    { label: 'ARMEMENT DE PECHE', id: '2' },
    { label: 'BANQUE ISLAMIQUE DE SENEGAL', id: '3' },
    { label: 'BCEAO', id: '4' },
    { label: 'BHS', id: '5' },
    { label: 'BICIS', id: '6' },
    { label: 'BOKK', id: '7' },
    { label: 'CAISSE SECU SOC', id: '8' },
    { label: 'CBAO', id: '9' },
    { label: 'CITIBANK', id: '10' },
    { label: 'CNCAS', id: '11' },
    { label: 'COTONIERE DU CAP VERT', id: '12' },
    { label: 'CNCAS', id: '13' },
    { label: 'CSE', id: '14' },
    { label: 'CSS', id: '15' },
    { label: 'ECOBANK', id: '16' },
    { label: 'EIFFAGE SENEGAL', id: '17' },
    { label: 'FILFILI', id: '18' },
    { label: 'GROUPE MIMRAN', id: '19' },
    { label: 'ICS', id: '20' },
    { label: 'INTER-ENTREPRISE DU PERSONNEL', id: '21' },
    { label: 'IPM NDIMBAL', id: '22' },
    { label: 'KEUR GU MAG', id: '23' },
    { label: 'LA POSTE', id: '24' },
    { label: 'LONASE', id: '25' },
    { label: 'MBARUM KOLUTE', id: '26' },
    { label: 'MERIDIEN KING FAHD PALACE', id: '27' },
    { label: 'MUTUELLE HOTELIERE DU CAP VERT', id: '28' },
    { label: 'NDIMBAL', id: '29' },
    { label: 'NESTLE SENEGAL', id: '30' },
    { label: 'NJABOOT', id: '31' },
    { label: 'OXFAM', id: '32' },
    { label: 'PREVOYANCE DU PERSONEL SOSETER', id: '33' },
    { label: 'PROFESSION LIBERALE', id: '34' },
    { label: 'SAGAM', id: '35' },
    { label: 'SANTE POUR TOUS', id: '36' },
    { label: 'SAR', id: '37' },
    { label: 'SDE', id: '38' },
    { label: 'SENELEC', id: '39' },
    { label: 'SENTENAC', id: '40' },
    { label: 'SERA', id: '41' },
    { label: 'SGBS', id: '42' },
    { label: 'SOBOA', id: '43' },
    { label: 'SODEFITEX', id: '44' },
    { label: 'SONATEL', id: '45' },
    { label: 'SOSETER', id: '46' },
    { label: 'SYPAOA', id: '47' },
    { label: 'TRANSIT', id: '48' },
    { label: 'TRANSPORT AERIEN', id: '49' },
  ];
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { idPatient } = useParams<'idPatient'>();
  const { idEdit } = useParams<'idEdit'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const hospital = useAppSelector(state => state.hospital.entity);
  const billEntity = useAppSelector(state => state.bill.entity);
  const updating = useAppSelector(state => state.bill.updating);
  const updateSuccess = useAppSelector(state => state.bill.updateSuccess);
  const [blockIPM, setBlockIPM] = useState('');
  const [blockAssurance, setBlockAssurance] = useState('');

  const account = useAppSelector(state => state.authentication.account);
  const [patientId, setPatientId] = useState(patients);
  const [billElements, setBillElements] = useState<IBillElement[]>([]);
  // Inside your component
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [description, setDescription] = useState('');

  let getPatient = e => {
    setPatientId(e.target.value);
    console.log('PatientId', e.target.value);
    setSelectedPatient(patients.find(p => p.id === Number(e.target.value))); // Store selected patient in state
    console.log(selectedPatient);
  };

  // const getIPM = e => {
  //   setBlockIPM(e.target.value);
  //   // return e.target.value;
  // };

  useEffect(() => {
    if (!isNew && billEntity) {
      // Check if the billEntity is fetched correctly before setting the value
      console.log('Fetched Bill Entity:', billEntity);
      setBlockAssurance(billEntity.insurance || ''); // Update the state after the data is fetched
      setBlockIPM(billEntity.ipm || ''); // Update the state after the data is fetched
    }
  }, [billEntity, isNew]); // Only run when billEntity changes

  useEffect(() => {
    console.log('blockAssurance:', blockAssurance); // Log to track changes
  }, [blockAssurance]);

  const getAssurance = e => {
    const value = e.target.value;
    console.log('Selected Assurance:', value); // Log the selected value
    setBlockAssurance(value); // Update state when selection changes
  };

  const getIPM = e => {
    const value = e.target.value;
    console.log('Selected IPM:', value); // Log the selected value
    setBlockIPM(value); // Update state when selection changes
  };

  const handleClose = () => {
    navigate('/bill' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(Number(id)));
    }

    getElemntByBillId(Number(id))
      .then(data => {
        setBillElements(data);
      })
      .catch(error => {
        console.error('Error fetching elements:', error);
      });

    dispatch(getPatients({ id: account.hospitalId !== null && account.hospitalId !== undefined ? account.hospitalId : 0 }));
    dispatch(getHospital(account.hospitalId));
  }, [isNew, id, dispatch, idPatient]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);
    // total = tab();

    const entity = {
      ...billEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
      billElements: billElements,
      insurance: blockAssurance,
      ipm: blockIPM,
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
          date: displayDefaultDate(),
          patient: idPatient,
          author: account.login,
        }
      : {
          ...billEntity,
          date: convertDateTimeFromServer(billEntity.date),
          patient: billEntity?.patient?.id,
        };

  //info ordonance
  const [formValues, setFormValues] = useState([{ service: '', amount: '', taux: '', quantity: '' }]);

  let handleChange = (i, e) => {
    let newFormValues = [...billElements]; // Create a copy of the state array
    newFormValues[i][e.target.name] = e.target.value; // Update the specific field in the copied array
    // console.log(newFormValues);
    setBillElements(newFormValues); // Update the state with the modified array
  };

  let addFormFields = () => {
    const newBillElement: IBillElement = {
      id: undefined,
      name: null,
      price: null,
      percentage: null,
      quantity: null,
      bill: null,
    };
    setBillElements([...billElements, newBillElement]);
  };

  if (billElements.length === 0) {
    addFormFields();
  }

  // let addFormFields = () => {
  //   setFormValues([...formValues, { service: '', amount: '', taux: '', quantity: '' }]);
  // };

  let removeFormFields = i => {
    let newFormValues = [...billElements];
    newFormValues.splice(i, 1);
    setBillElements(newFormValues);
  };

  let nombre = 1;
  let tarif = 0;
  const [total, setTotal] = useState(0);
  let remise = 0;
  let tab = () => {
    let calculatedTotal = 0;

    let newElement = [...billElements];

    for (let i = 0; i < newElement.length; i++) {
      if (newElement[i].percentage !== null) {
        remise = 1 - newElement[i].percentage / 100;
      } else {
        remise = 1;
      }
      if (newElement[i].price !== null) {
        tarif = newElement[i].price;
      } else {
        tarif = 0;
      }
      if (newElement[i].quantity === null) {
        nombre = 1;
      } else {
        nombre = newElement[i].quantity;
      }
      calculatedTotal += Math.round(tarif * remise * nombre);
    }
    return calculatedTotal;
  };

  // total = tab();
  // Recalculate total whenever billElements changes
  useEffect(() => {
    setTotal(tab());
  }, [billElements]);

  Font.register({
    family: 'Poppins',
    fonts: [
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Bold.woff', fontWeight: 'bold' },
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Medium.woff', fontWeight: 'medium' },
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Medium.woff', fontWeight: 'thin' },
    ],
  });
  const valuesHeight = 6;

  const doc = (
    <Document>
      <Page style={{ display: 'flex', flexDirection: 'column', fontFamily: 'Poppins' }}>
        <View
          style={{
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-around',
            alignItems: 'center',
            borderBottom: '1px solid #141414',
            paddingBottom: '10px',
            marginTop: '20px',
            marginLeft: '5vw',
            marginRight: '5vw',
            width: '90vw',
          }}
        >
          <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center' }}>
            <Text style={{ fontSize: '10px', marginBottom: '9px', fontWeight: 'bold' }}>{hospital?.name}</Text>
            <Text style={{ fontSize: '10px', marginBottom: '9px', fontWeight: 'medium' }}>{hospital?.adress}</Text>
            {/* <Text style={{ fontSize: '10px', fontWeight: 'thin' }}>Email Clinique</Text> */}
            <Text style={{ fontSize: '10px', fontWeight: 'thin' }}></Text>
            <Text style={{ fontSize: '10px', fontWeight: 'thin' }}>{hospital?.phone}</Text>
          </View>
        </View>
        <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center', marginTop: '10px' }}>
          <Text style={{ fontSize: '20px', fontWeight: 'extrabold', marginBottom: '5px' }}>Facture</Text>
          <Text style={{ fontSize: '12px', marginBottom: '9px', marginLeft: '70vw' }}>
            {' '}
            Dakar, Le {convertDateTimeFromServerToDate(displayDefaultDateTime())}
          </Text>
        </View>

        <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', marginTop: '15px', marginLeft: '5vw' }}>
          <Text style={{ fontSize: '12px', marginBottom: '7px' }}>
            Nom : {selectedPatient ? selectedPatient.lastName?.toUpperCase() : billEntity?.patient?.lastName?.toUpperCase()}{' '}
          </Text>
          <Text style={{ fontSize: '12px', marginBottom: '7px' }}>
            Prénom(s):{' '}
            {selectedPatient
              ? selectedPatient?.firstName
                  .split(' ')
                  .map(a => a.charAt(0).toUpperCase() + a.slice(1))
                  .join(' ')
              : billEntity?.patient?.firstName
                  .split(' ')
                  .map(a => a.charAt(0).toUpperCase() + a.slice(1))
                  .join(' ')}{' '}
          </Text>
        </View>
        <View
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginTop: '15px',
            border: '1px solid #141414',
            marginLeft: '5vw',
            marginRight: '5vw',
            width: '90vw',
          }}
        >
          <View style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
            <Text
              style={{
                width: '15vw',
                height: '6vh',
                borderRight: '1px solid #141414',
                textTransform: 'uppercase',
                fontSize: '12px',
                color: '#141414',
                paddingTop: '20px',
                textAlign: 'center',
              }}
            >
              Quantité
            </Text>
            <Text
              style={{
                width: '25vw',
                height: '6vh',
                borderRight: '1px solid #141414',
                textTransform: 'uppercase',
                fontSize: '12px',
                color: '#141414',
                padding: '10px',
                paddingTop: '20px',
                textAlign: 'center',
              }}
            >
              Intervention
            </Text>
            <Text
              style={{
                width: '20vw',
                height: '6vh',
                textTransform: 'uppercase',
                fontSize: '12px',
                color: '#141414',
                padding: '10px',
                paddingTop: '20px',
                textAlign: 'center',
              }}
            >
              Prix unitaire
            </Text>
            <Text
              style={{
                width: '10vw',
                height: '6vh',
                borderLeft: '1px solid #141414',
                textTransform: 'uppercase',
                fontSize: '12px',
                paddingRight: '5px',
                paddingTop: '20px',
                paddingLeft: '5px',
                color: '#141414',
                textAlign: 'center',
              }}
            >
              Remise
            </Text>
            <Text
              style={{
                width: '20vw',
                height: '6vh',
                borderLeft: '1px solid #141414',
                textTransform: 'uppercase',
                fontSize: '12px',
                color: '#141414',
                padding: '10px',
                paddingTop: '20px',
                textAlign: 'center',
              }}
            >
              Prix total
            </Text>
          </View>
          {billElements.map((element, i) =>
            true ? (
              <View key={`entity-${i}`} style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
                <Text
                  style={{
                    width: '15vw',
                    minHeight: `${valuesHeight}vh`,
                    borderRight: '1px solid #141414',
                    borderTop: '1px solid #141414',
                    fontSize: '14px',
                    padding: '10px',
                    textAlign: 'center',
                  }}
                >
                  {element?.quantity === null ? 1 : element?.quantity}
                </Text>
                <Text
                  style={{
                    width: '25vw',
                    minHeight: `${valuesHeight}vh`,
                    borderRight: '1px solid #141414',
                    borderTop: '1px solid #141414',
                    fontSize: '14px',
                    // padding: '10px',
                    textAlign: 'center',
                    overflow: 'hidden',
                    textTransform: 'capitalize',
                  }}
                >
                  {element.name}
                </Text>
                <Text
                  style={{
                    width: '20vw',
                    minHeight: `${valuesHeight}vh`,
                    borderTop: '1px solid #141414',
                    fontSize: '14px',
                    padding: '10px',
                    textAlign: 'center',
                  }}
                >
                  {element.price === null ? 0 : element.price} FCFA
                </Text>
                <Text
                  style={{
                    width: '10vw',
                    height: '6vh',
                    borderTop: '1px solid #141414',
                    borderLeft: '1px solid #141414',
                    fontSize: '14px',
                    padding: '10px',
                    textAlign: 'center',
                  }}
                >
                  {element.percentage === null ? 0 : element.percentage} %
                </Text>
                <Text
                  style={{
                    width: '20vw',
                    height: '6vh',
                    borderTop: '1px solid #141414',
                    borderLeft: '1px solid #141414',
                    fontSize: '14px',
                    padding: '10px',
                    textAlign: 'center',
                  }}
                >
                  {Math.round(
                    (element.price === null ? 0 : element.price) *
                      (1 - (element.percentage === null ? 0 : element.percentage / 100)) *
                      (element.quantity === null ? 1 : element.quantity)
                  )}
                  FCFA
                  {/* {Math.round(tarif*remise*nombre)} */}
                </Text>
              </View>
            ) : null
          )}
          <View style={{ borderTop: '1px solid #141414', width: '90vw', paddingLeft: '5px', paddingRight: '5px' }}>
            <Text style={{ width: '60vw' }}>Montant total : </Text>
            <Text style={{ position: 'absolute', right: '0' }}>{total + 'FCFA'} </Text>
          </View>
        </View>
        {/* Description Section */}
        <View style={{ marginLeft: '5vw', marginTop: '15px' }}>
          <Text style={{ fontSize: '12px', marginBottom: '7px' }}>Description: {description}</Text>
        </View>
        <View
          style={{
            borderTop: '2px solid #141414',
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
      <Header pageName={'Facture'} />
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
          <span>{isNew ? 'Enregistrement nouvelle ' : idEdit === 'voir' ? 'Consultation ' : 'Modification '} facture</span>
        </Card>
        <Card
          style={{
            minHeight: '70vh',
            marginRight: '3%',
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
              {isNew ? 'Nouvelle facture ' : 'Facture '}patient
            </span>
            <div style={{ display: 'flex', flexDirection: 'row', gap: '3vh' }}>
              <PDFDownloadLink
                style={{ backgroundColor: 'transparent', textDecoration: 'none' }}
                document={doc}
                // fileName={`facture_${account.login}_${JSON.stringify(
                //   convertDateTimeFromServerToDate(displayDefaultDateTime()) +
                //     'H:' +
                //     convertDateTimeFromServerToHours(displayDefaultDateTime())
                // )}`}
                fileName={`facture_${JSON.stringify(
                  convertDateTimeFromServerToDate(displayDefaultDateTime()) +
                    'H:' +
                    convertDateTimeFromServerToHours(displayDefaultDateTime())
                )}`}
              >
                <div
                  style={{
                    cursor: 'pointer',
                    fontWeight: '900',
                    color: '#B3C0D3',
                    textAlign: 'center',
                    display: 'flex',
                    flexDirection: 'row',
                    alignItems: 'center',
                    gap: '3px',
                  }}
                >
                  {React.createElement(BiDownload, { size: '23' })} <span>Télécharger</span>
                </div>
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
              backgroundPosition: '60% 120%',
            }}
            defaultValues={defaultValues()}
            onSubmit={saveEntity}
          >
            {!isNew ? <ValidatedField hidden name="id" required readOnly id="bill-id" label="ID" validate={{ required: true }} /> : null}
            <ValidatedField
              style={{ borderRadius: '25px', backgroundColor: '#A9B7CD', color: '#F6FAFF', borderColor: '#CBDCF7', width: '75vw' }}
              disabled
              label="Date"
              id="bill-date"
              name="date"
              data-cy="date"
              type="datetime-local"
              placeholder="YYYY-MM-DD HH:mm"
            >
              <i className="fa-light fa-lock" style={{ position: 'absolute', zIndex: '0', color: 'white', right: '0' }}>
                {' '}
              </i>
            </ValidatedField>
            <ValidatedField hidden label="Author" id="bill-author" name="author" data-cy="author" type="text" />
            <ValidatedField
              onChange={e => getPatient(e)}
              style={
                isNew
                  ? { borderRadius: '25px', borderColor: '#CBDCF7', width: '28vw' }
                  : { borderRadius: '25px', backgroundColor: '#A9B7CD', borderColor: '#CBDCF7', color: '#F6FAFF', width: '28vw' }
              }
              disabled={isNew ? false : true}
              id="bill-patient"
              name="patient"
              data-cy="patient"
              label="Patient"
              type="select"
            >
              <option value="" key="0" />
              {patients
                ? patients.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.lastName.toUpperCase()}{' '}
                      {otherEntity.firstName
                        .split(' ')
                        .map(a => a.charAt(0).toUpperCase() + a.slice(1))
                        .join(' ')}
                    </option>
                  ))
                : null}
            </ValidatedField>
            <ValidatedField
              value={blockAssurance} // Bind the value to the state
              style={
                isNew
                  ? {
                      borderRadius: '25px',
                      borderColor: '#CBDCF7',
                      width: '20vw',
                    }
                  : {
                      borderRadius: '25px',
                      borderColor: '#CBDCF7',
                      width: '20vw',
                      backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                      color: idEdit === 'voir' ? '#F6FAFF' : '',
                    }
              }
              disabled={
                isNew && blockIPM === '' ? false : isNew && blockIPM !== '' ? true : idEdit !== 'voir' && blockIPM === '' ? false : true
              }
              id="bill-asurance"
              name="insurance"
              data-cy="asurance"
              label="Assurance"
              type="select"
              onChange={getAssurance} // Update state when selection changes
            >
              {assurance.map(assur => (
                <option value={assur.label} key={assur.id}>
                  {assur.label}
                </option>
              ))}
            </ValidatedField>
            <ValidatedField
              value={blockIPM} // Bind the value to the state
              style={
                isNew
                  ? { borderRadius: '25px', borderColor: '#CBDCF7', width: '20vw' }
                  : {
                      borderRadius: '25px',
                      borderColor: '#CBDCF7',
                      width: '20vw',
                      backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                      color: idEdit === 'voir' ? '#F6FAFF' : '',
                    }
              }
              id="bill-ipm"
              name="ipm"
              data-cy="ipm"
              label="IPM"
              type="select"
              disabled={
                isNew && blockAssurance === ''
                  ? false
                  : isNew && blockAssurance !== ''
                  ? true
                  : idEdit !== 'voir' && blockAssurance === ''
                  ? false
                  : true
              }
              onChange={getIPM}
            >
              {ipm.map(a => (
                <option value={a.label} key={a.id}>
                  {a.label}
                </option>
              ))}
            </ValidatedField>
            {/* <Button
              onClick={() => resetBlock()}
              style={{
                width: "4vw",
                color: "red",
                backgroundColor: "transparent",
                borderColor: "transparent"
              }}
            >
              {React.createElement(AiFillCloseCircle, { size: "25" })}
            </Button> */}
            {billElements.map((element, index, arr) => (
              <div
                style={{
                  flex: '1 1 100%',
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '0.7vw',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
                key={index}
              >
                <ValidatedField
                  type="number"
                  label="Quantité"
                  name="quantity"
                  min="1"
                  value={element.quantity || ''}
                  onChange={e => handleChange(index, e)}
                  required
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '6vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '21vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                  type="text"
                  label="Intervention"
                  placeholder="Intervention..."
                  name="name"
                  value={element.name || ''}
                  onChange={e => handleChange(index, e)}
                  validate={{
                    required: { value: false, message: 'Ce champ est obligatoire.' },
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '20vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                  type="number"
                  label="Tarif unitaire(FCFA)"
                  placeholder="Tarif..."
                  name="price"
                  value={element.price || ''}
                  onChange={e => handleChange(index, e)}
                  validate={{
                    required: { value: false, message: 'Ce champ est obligatoire.' },
                    validate: v => isNumber(v) || 'Ce champ doit être un nombre.',
                  }}
                />
                <ValidatedField
                  disabled={idEdit === 'voir' ? true : false}
                  style={{
                    borderRadius: '25px',
                    borderColor: '#CBDCF7',
                    width: '20vw',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                    color: idEdit === 'voir' ? '#F6FAFF' : '',
                  }}
                  label="Taux de remboursement(en %)"
                  type="number"
                  name="percentage"
                  placeholder="Taux..."
                  value={element.percentage || ''}
                  onChange={e => handleChange(index, e)}
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
            <ValidatedField
              value={isNaN(total) ? 'Calcul en cours...' : total}
              style={
                isNew
                  ? { borderRadius: '25px', borderColor: '#CBDCF7', width: '36vw' }
                  : {
                      borderRadius: '25px',
                      borderColor: '#CBDCF7',
                      width: '36vw',
                      backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                      color: idEdit === 'voir' ? '#F6FAFF' : '',
                    }
              }
              disabled
              id="bill-total"
              name="total"
              data-cy="total"
              label="Montant Total(CFA)"
              placeholder="Montant..."
              type="number"
            />
            <ValidatedField
              style={
                isNew
                  ? { borderRadius: '25px', borderColor: '#CBDCF7', width: '36vw', height: '20vh' }
                  : {
                      borderRadius: '25px',
                      borderColor: '#CBDCF7',
                      width: '36vw',
                      height: '20vh',
                      backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                      color: idEdit === 'voir' ? '#F6FAFF' : '',
                    }
              }
              disabled={isNew || idEdit !== 'voir' ? false : idEdit === 'voir' ? false : true}
              id="bill-desc"
              name="desc"
              label="Description"
              placeholder="Description..."
              type="textarea"
              onChange={e => setDescription(e.target.value)}
            />

            <Button
              hidden={idEdit === 'voir' ? true : false}
              style={{ borderRadius: '25px', flex: '1 1 100%', marginTop: '2vh', backgroundColor: '#56B5C5', borderColor: '#56B5C5' }}
              id="save-entity"
              data-cy="entityCreateSaveButton"
              type="submit"
              disabled={updating}
            >
              Enregistrer
            </Button>
            <Button
              style={{
                borderRadius: '25px',
                flex: '1 1 100%',
                marginTop: '2vh',
                backgroundColor: '#EC4747',
                borderColor: '#EC4747',
                marginBottom: '2vh',
              }}
              id="cancel-save"
              data-cy="entityCreateCancelButton"
              onClick={() => (confirm('Êtes-vous sur de vouloir quitter?') === true ? window.history.back() : null)}
              replace
              color="danger"
            >
              <span className="d-none d-md-inline"> {idEdit === 'voir' ? 'Retour' : 'Annuler'} </span>
            </Button>
          </ValidatedForm>
        </Card>
      </div>
    </div>
  );
};

export default BillUpdate;
