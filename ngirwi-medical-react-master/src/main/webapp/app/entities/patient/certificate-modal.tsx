import React, { useState } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { PDFDownloadLink, Document, Page, Text, View, Image } from '@react-pdf/renderer';
import { convertDateTimeFromServerToDate, displayDefaultDateTime, convertDateTimeFromServerToHours } from 'app/shared/util/date-utils';

const CertificateModal = ({ isOpen, toggle, patient, hospital, account }) => {
  const [days, setDays] = useState('');

  const handleDaysChange = event => {
    setDays(event.target.value);
  };

  const generatePdf = () => {
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
              <Text style={{ fontSize: '15px', fontWeight: 'thin' }}>{hospital?.phone}</Text>
            </View>
            <View>
              <Image style={{ width: '60px', height: '60px' }} src="content/images/logo-medecin-240x300.png" />
            </View>
            <View style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center' }}>
              <Text style={{ fontSize: '20px', color: 'green', marginBottom: '9px', fontWeight: 'bold' }}>{hospital?.name}</Text>
              <Text style={{ fontSize: '15px', marginBottom: '9px', fontWeight: 'medium' }}>{hospital?.adress}</Text>
              {/* <Text style={{ fontSize: '15px', fontWeight: 'thin' }}>Email Clinique</Text> */}
              <Text style={{ fontSize: '15px', fontWeight: 'thin' }}></Text>
            </View>
          </View>
          <View
            style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-around', alignItems: 'center', marginTop: '15px' }}
          >
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
                  {patient?.lastName?.toUpperCase()}{' '}
                  {patient?.firstName
                    ? patient.firstName
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
              // border: '3px solid silver',
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
                  // borderRight: '3px solid silver',
                  textTransform: 'uppercase',
                  fontSize: '15px',
                  paddingTop: '3px',
                  paddingBottom: '5px',
                  textAlign: 'center',
                }}
              >
                Je, soussigné Dr {account.firstName + ' ' + account.lastName}, certifie avoir reçu en consultation Mme/Mlle/Mr{' '}
                {patient.lastName + ' ' + patient.firstName} né le {patient.birthday} et atteste de son état de santé nécessite un repos
                médical de {days} jours. Ce présent certificat lui à été délivré pour servir et valoir ce que de droit.
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

    const fileName = `certificate_${patient.firstName}_${patient.lastName}.pdf`;

    return (
      <PDFDownloadLink style={{ backgroundColor: 'transparent', textDecoration: 'none' }} document={doc} fileName={fileName}>
        Télécharger
      </PDFDownloadLink>
    );
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>Certificat Médical</ModalHeader>
      <ModalBody>
        <Form>
          <FormGroup>
            <Label for="days">Nombre de jours</Label>
            <Input type="number" name="days" id="days" value={days} onChange={handleDaysChange} placeholder="Nombre de jours" />
          </FormGroup>
        </Form>
      </ModalBody>
      <ModalFooter>
        {days && generatePdf()}
        <Button color="secondary" onClick={toggle}>
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default CertificateModal;
