import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Card } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { translateGender, translateMaritalStatus, translateBloodType } from 'app/shared/util/translation-utils';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity as getHospital } from '../hospital/hospital.reducer';
import { getEntity } from './patient.reducer';
import { getPatient, reset as resetDossier } from '../dossier-medical/dossier-medical.reducer';
import { getPatient as getHospitalisationPatient, reset as resetHospitalisation } from '../hospitalisation/hospitalisation.reducer';
import Header from 'app/shared/layout/header/header';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Page, Text, Image, View, Document, Font } from '@react-pdf/renderer';
import { convertDateTimeFromServerToDate, displayDefaultDateTime, convertDateTimeFromServerToHours } from 'app/shared/util/date-utils';
import CertificateModal from './certificate-modal';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const PatientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();
  const [hide, setHide] = useState(true);
  const [hidehos, setHidehos] = useState(true);
  const [modal, setModal] = useState(false);
  const [days, setDays] = useState(1);
  const isDoctor = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.DOCTOR]));

  let showhos = () => {
    if (hidehos === true) {
      setHidehos(false);
    }
    if (hidehos === false) {
      setHidehos(true);
    }
  };
  let show = () => {
    if (hide === true) {
      setHide(false);
    }
    if (hide === false) {
      setHide(true);
    }
  };
  function changeColor(e) {
    e.target.style.color = '#0075FF';
    e.target.style.backgroundColor = '#FFFFFF';
  }
  function setColor(e) {
    e.target.style.backgroundColor = '#0075FF';
    e.target.style.color = '#FFFFFF';
  }

  // Select state BEFORE effects to avoid using variables before declaration
  const patientEntity = useAppSelector(state => state.patient.entity);
  const hospitalEntity = useAppSelector(state => state.hospital.entity);
  const dossierMedicalEntity = useAppSelector(state => state.dossierMedical.entity);
  const hospitalisationEntity = useAppSelector(state => state.hospitalisation?.entity);
  const account = useAppSelector(state => state.authentication.account);

  useEffect(() => {
    dispatch(resetHospitalisation());
    dispatch(resetDossier());
    if (id) {
      dispatch(getEntity(id));
      dispatch(getPatient(id));
      dispatch(getHospitalisationPatient(id));
    }
    if (account?.hospitalId !== undefined && account?.hospitalId !== null) {
      dispatch(getHospital(account.hospitalId));
    }
  }, [dispatch, id, account?.hospitalId]);

  // selectors moved above to avoid "used before declaration" issues

  const toggleModal = () => {
    setModal(!modal);
  };

  const verifDossierExist = () => {
    let dossierExist = false;
    if (dossierMedicalEntity && Object.keys(dossierMedicalEntity).length > 0) {
      dossierExist = true;
    }
    return dossierExist;
  };

  const verifHospitalisationExist = () => {
    let hospitalisationExist = false;
    if (hospitalisationEntity && Object.keys(hospitalisationEntity).length > 0) {
      hospitalisationExist = true;
    }
    return hospitalisationExist;
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
          <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center' }}>
            <Text style={{ fontSize: '18px', color: 'green', marginBottom: '9px', fontWeight: 'bold' }}>
              {account.lastName + ' ' + account.firstName}
            </Text>
            <Text style={{ fontSize: '15px', marginBottom: '9px', fontWeight: 'medium' }}>Médecin général</Text>
            <Text style={{ fontSize: '15px', fontWeight: 'thin' }}>{hospitalEntity?.phone}</Text>
          </View>
          <View>
            <Image style={{ width: '60px', height: '60px' }} src="content/images/logo-medecin-240x300.png" />
          </View>
          <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center' }}>
            <Text style={{ fontSize: '20px', color: 'green', marginBottom: '9px', fontWeight: 'bold' }}>{hospitalEntity?.name}</Text>
            <Text style={{ fontSize: '15px', marginBottom: '9px', fontWeight: 'medium' }}>{hospitalEntity?.adress}</Text>
            {/* <Text style={{ fontSize: '15px', fontWeight: 'thin' }}>Email Clinique</Text> */}
            <Text style={{ fontSize: '15px', fontWeight: 'thin' }}></Text>
          </View>
        </View>
        <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center', marginTop: '15px' }}>
          <Text style={{ fontSize: '35px', fontWeight: 'extrabold', marginBottom: '9px' }}>Certificat Médical</Text>

          <View style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
            <View style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', marginRight: '10px' }}>
              <Text style={{ fontSize: '18px', marginBottom: '9px' }}>Fait à: Dakar Le:</Text>
              <Text style={{ fontSize: '18px', marginBottom: '7px' }}>Nom & Prénom(s):</Text>
            </View>
            <View style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
              <Text style={{ fontSize: '18px', marginBottom: '9px' }}>
                {convertDateTimeFromServerToDate(displayDefaultDateTime()) +
                  ' à ' +
                  convertDateTimeFromServerToHours(displayDefaultDateTime())}
              </Text>
              <Text>
                {patientEntity?.lastName?.toUpperCase()}{' '}
                {patientEntity?.firstName
                  ? patientEntity.firstName
                      .split(' ')
                      .map(a => a.charAt(0).toUpperCase() + a.slice(1))
                      .join(' ')
                  : ''}
              </Text>
            </View>
          </View>
        </View>
        <Image
          src="content/images/logo-medecin-240x300.png"
          style={{ position: 'absolute', top: '335', left: '15vw', zIndex: '1', width: '70vw', height: '40vh', opacity: 0.1 }}
        />
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
                paddingTop: '3px',
                paddingBottom: '5px',
                textAlign: 'center',
              }}
            >
              Je, soussigné Dr `{account.firstName + ' ' + account.lastName}`, certifie avoir reçu en consultation Mme/Mlle/Mr `
              {patientEntity.lastName + ' ' + patientEntity.firstName}` né le `{patientEntity.birthday}` et atteste de son état de santé
              nécessite un repos médical de `{days}`. Ce présent certificat lui à été délivré pour servir et valoir ce que de droit.
            </Text>
            {/* <Text
              style={{
                width: '20vw',
                textTransform: 'uppercase',
                fontSize: '15px',
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
                paddingTop: '3px',
                paddingBottom: '5px',
                textAlign: 'center',
              }}
            >
              Fréquence
            </Text> */}
          </View>
        </View>
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
      <Header pageName="Gestion patients" />
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
            height: '8.28vh',
            width: '33.38vw',
            borderRadius: '20px',
            backgroundColor: '#11485C',
            marginLeft: '25%',
            textAlign: 'center',
            justifyContent: 'center',
            color: 'white',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
          }}
        >
          <span style={{}}>Details Patient {patientEntity.lastName + ' ' + patientEntity.firstName}</span>
        </Card>

        <Card
          style={{
            marginLeft: '4%',
            height: '55vh',
            minWidth: '65vw',
            marginRight: '5%',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
            borderRadius: '15px',
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'flex-start',
            justifyContent: 'center',
            paddingTop: '5%',
          }}
        >
          <Card
            style={{
              height: '55%',
              width: '22%',
              backgroundColor: '#72C9D8',
              borderRadius: '15px',
              borderColor: '#72C9D8',
              borderTopRightRadius: '0px',
              borderBottomRightRadius: '0px',
              justifyContent: 'center',
            }}
          >
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <span>Enregistré le:</span>
              <span
                style={{
                  color: '#006CEB',
                  fontFamily: 'Mulish',
                  fontSize: '15px',
                }}
              >
                {patientEntity.dateCreated ? <TextFormat value={patientEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
              </span>
            </div>
          </Card>

          <Card
            style={{
              height: '90%',
              width: '40%',
              borderRadius: '15px',
              borderTopLeftRadius: '0px',
              marginRight: '3%',
              backgroundColor: '#F6FAFF',
              borderColor: '#F6FAFF',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center',
              fontSize: '15px',
              fontWeight: 'bold',
              fontFamily: 'Mulish',
            }}
          >
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Nom:{' '}
              </span>
              <span style={{ textTransform: 'uppercase' }}>{patientEntity.lastName}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Prénom:{' '}
              </span>
              <span style={{ textTransform: 'capitalize' }}>{patientEntity.firstName}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Date de naissance:{' '}
              </span>
              <span>{patientEntity.birthday}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Sexe:{' '}
              </span>
              <span>{translateGender(patientEntity.gender)}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Situation matrimoniale:{' '}
              </span>
              <span>{translateMaritalStatus(patientEntity.maritialStatus)}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Adresse:{' '}
              </span>
              <span>{patientEntity.adress}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Profession:{' '}
              </span>
              <span>{patientEntity.job}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Téléphone:{' '}
              </span>
              <span>{patientEntity.phone}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                CNI:{' '}
              </span>
              <span>{patientEntity.cni}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Groupe sanguin:{' '}
              </span>
              <span>{translateBloodType(patientEntity.bloodType)}</span>
            </div>
            <div>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Date de mise à jour:{' '}
              </span>
              <span>
                {patientEntity.dateUpdated ? <TextFormat value={patientEntity.dateUpdated} type="date" format={APP_DATE_FORMAT} /> : null}
              </span>
            </div>
          </Card>
          <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                gap: '5vh',
                marginTop: '3vh',
              }}
            >
              {/* Display patient details here */}
              <Button
                style={{
                  borderColor: '#0075FF',
                  backgroundColor: '#0075FF',
                  color: '#FFFFFF',
                  width: '25vh',
                  height: '9vh',
                  borderRadius: '4px',
                  fontFamily: 'Ubuntu',
                  textAlign: 'center',
                  justifyContent: 'center',
                  wordBreak: 'break-word',
                }}
                onClick={toggleModal}
              >
                Certificat
              </Button>

              <CertificateModal isOpen={modal} toggle={toggleModal} patient={patientEntity} hospital={hospitalEntity} account={account} />

              <Button
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                tag={Link}
                disabled={!isDoctor}
                // to={
                //   Object.keys(dossierMedicalEntity).length > 0
                //     ? `/dossier-medical/${dossierMedicalEntity?.id}/edit/${'voir'}`
                //     : `/dossier-medical/new/${patientEntity?.id}`
                // }
                to={
                  verifDossierExist()
                    ? `/dossier-medical/${dossierMedicalEntity?.id}/${patientEntity?.id}`
                    : `/dossier-medical/new/${patientEntity?.id}`
                }
                style={{
                  borderColor: '#0075FF',
                  backgroundColor: '#0075FF',
                  color: '#FFFFFF',
                  width: '25vh',
                  height: '9vh',
                  borderRadius: '4px',
                  fontFamily: 'Ubuntu',
                  padding: '10%',
                }}
              >
                Dossier médical
              </Button>
            </div>
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                gap: '5vh',
                marginTop: '3vh',
              }}
            >
              <Button
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                hidden={!hide}
                disabled={!isDoctor}
                href={`/consultation/new/${patientEntity.id}/`}
                style={{
                  borderColor: '#0075FF',
                  backgroundColor: '#0075FF',
                  color: '#FFFFFF',
                  width: '25vh',
                  height: '9vh',
                  borderRadius: '4px',
                  fontFamily: 'Ubuntu',
                  textAlign: 'center',
                  justifyContent: 'center',
                  wordBreak: 'break-word',
                }}
              >
                Nouvelle consultation
              </Button>
              <Button
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                hidden={hide}
                disabled={!isDoctor}
                tag={Link}
                to={`/consultation/list/${patientEntity.id}?page=1&sort=id,asc`}
                style={{
                  borderColor: '#0075FF',
                  backgroundColor: '#0075FF',
                  color: '#FFFFFF',
                  width: '25vh',
                  height: '9vh',
                  borderRadius: '4px',
                  fontFamily: 'Ubuntu',
                  justifyContent: 'center',
                  wordBreak: 'break-word',
                }}
              >
                <span>Voir consultations faites</span>
              </Button>
              <Button
                hidden={!hidehos}
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                disabled={!isDoctor}
                tag={Link}
                to={`/hospitalisation/new/${patientEntity.id}`}
                style={{
                  borderColor: '#0075FF',
                  backgroundColor: '#0075FF',
                  color: '#FFFFFF',
                  width: '25vh',
                  height: '9vh',
                  borderRadius: '4px',
                  fontFamily: 'Ubuntu',
                  wordBreak: 'break-word',
                }}
              >
                Démarrer hospitalisation
              </Button>
              <Button
                hidden={hidehos}
                onMouseOver={changeColor}
                onMouseLeave={setColor}
                disabled={!isDoctor}
                tag={Link}
                to={`/hospitalisation/patient/${patientEntity.id}`}
                style={{
                  borderColor: '#0075FF',
                  backgroundColor: '#0075FF',
                  color: '#FFFFFF',
                  width: '25vh',
                  height: '9vh',
                  borderRadius: '4px',
                  fontFamily: 'Ubuntu',
                  wordBreak: 'break-word',
                }}
              >
                Voir hospitalisations faites
              </Button>
            </div>
            <div
              style={{
                backgroundColor: '',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                // gap: '10vh',
                // marginTop: '15vh',
              }}
            >
              <FontAwesomeIcon
                onClick={() => show()}
                style={{ marginLeft: '15px', color: '#0075FF', height: '5vh', cursor: 'pointer' }}
                icon="sort"
              />
              <FontAwesomeIcon
                onClick={() => showhos()}
                style={{ marginLeft: '15px', color: '#0075FF', height: '5vh', cursor: 'pointer', marginTop: '10vh' }}
                icon="sort"
              />
            </div>
          </div>
        </Card>

        <Button
          id="cancel-save"
          data-cy="entityCreateCancelButton"
          onClick={() => window.history.back()}
          replace
          color="info"
          style={{
            marginLeft: '7%',
            width: '66vw',
            borderRadius: '25px',
            color: 'white',
            backgroundColor: '#EC4747',
            borderColor: '#EC4747',
          }}
        >
          <span className="d-none d-md-inline">Retour</span>
        </Button>
      </div>
    </div>
  );
};

export default PatientDetail;
