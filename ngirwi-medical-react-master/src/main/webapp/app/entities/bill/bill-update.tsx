/**
 * Composant de gestion des factures médicales
 * 
 * Architecture: Ce composant gère trois modes d'affichage distincts :
 * - Création (isNew=true) : Formulaire vierge avec calcul du total en temps réel
 * - Édition (idEdit=undefined) : Modification avec total "Calculé à la sauvegarde"
 * - Consultation (idEdit='voir') : Lecture seule avec tous les champs désactivés
 * 
 * Règle métier importante: Assurance et IPM sont mutuellement exclusifs.
 * Un patient ne peut avoir qu'un seul type de couverture à la fois.
 */
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card, FormGroup, Label } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { toast } from 'react-toastify';

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
import { getMyHospital } from '../hospital/hospital.reducer';
import { createEntity, getEntity, reset, updateEntity } from './bill.reducer';
import { getElemntByBillId } from '../bill-element/bill-element.reducer';
import { Page, Text, View, Document, PDFDownloadLink, Font, Image } from '@react-pdf/renderer';
import Header from 'app/shared/layout/header/header';
import { IoIosAddCircleOutline, IoIosArrowBack, IoIosAddCircle, IoIosRemoveCircle } from 'react-icons/io';
import { BiDownload } from 'react-icons/bi';
import { IBillElement } from 'app/shared/model/bill-element.model';

export const BillUpdate = () => {
  const dispatch = useAppDispatch();
  
  // Référentiels des organismes payeurs au Sénégal
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

  // Extraction des paramètres de route pour déterminer le mode d'affichage
  const { id } = useParams<'id'>();
  const { idPatient } = useParams<'idPatient'>();
  const { idEdit } = useParams<'idEdit'>();
  const isNew = id === undefined;

  // État Redux
  const patients = useAppSelector(state => state.patient.entities);
  const hospital = useAppSelector(state => state.hospital.entity);
  const billEntity = useAppSelector(state => state.bill.entity);
  const updating = useAppSelector(state => state.bill.updating);
  const updateSuccess = useAppSelector(state => state.bill.updateSuccess);
  
  // Source de vérité unique pour Assurance/IPM (PDF, UI, et sauvegarde)
  const [blockAssurance, setBlockAssurance] = useState('');
  const [blockIPM, setBlockIPM] = useState('');
  
  // Exclusivité mutuelle dérivée — toujours cohérente, pas de sync manuelle
  const insuranceDisabled = idEdit === 'voir' || blockIPM !== '';
  const ipmDisabled = idEdit === 'voir' || blockAssurance !== '';

  const account = useAppSelector(state => state.authentication.account);
  const [patientId, setPatientId] = useState(patients);
  const [billElements, setBillElements] = useState<IBillElement[]>([]);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [description, setDescription] = useState('');

  const getPatient = e => {
    setPatientId(e.target.value);
    setSelectedPatient(patients.find(p => p.id === Number(e.target.value)));
  };

  /**
   * Gestion de l'exclusivité mutuelle Assurance/IPM
   * Règle métier: Un patient ne peut avoir qu'un seul type de couverture à la fois.
   * Quand on sélectionne un champ, l'autre est vidé ET désactivé (via dérivation).
   * Quand on revient à "-- Aucune --", l'autre redevient actif.
   */
  const handleAssuranceChange = (value: string) => {
    setBlockAssurance(value);
    if (value !== '') {
      setBlockIPM(''); // Vider IPM quand Assurance est sélectionnée
    }
  };

  const handleIPMChange = (value: string) => {
    setBlockIPM(value);
    if (value !== '') {
      setBlockAssurance(''); // Vider Assurance quand IPM est sélectionné
    }
  };

  /**
   * Synchronisation des états locaux avec l'entité chargée
   * blockAssurance/blockIPM sont la source de vérité unique ;
   * l'état disabled est dérivé automatiquement.
   */
  useEffect(() => {
    if (!isNew && billEntity) {
      setBlockAssurance(billEntity.insurance || '');
      setBlockIPM(billEntity.ipm || '');
      setDescription(billEntity.desc || '');
    } else if (isNew) {
      setBlockAssurance('');
      setBlockIPM('');
      setDescription('');
    }
  }, [billEntity, isNew]);

  const handleClose = () => {
    navigate('/bill' + location.search);
  };

  /**
   * Chargement initial des données
   * - En création: réinitialise le state et prépare un élément vide
   * - En édition: charge la facture et ses éléments depuis l'API
   */
  useEffect(() => {
    if (isNew) {
      dispatch(reset());
      setBillElements([{
        id: undefined,
        name: null,
        price: null,
        percentage: null,
        quantity: null,
        bill: null,
      }]);
    } else {
      dispatch(getEntity(Number(id)));
      getElemntByBillId(Number(id))
        .then(data => {
          if (data && data.length > 0) {
            setBillElements(data);
          } else {
            setBillElements([{
              id: undefined,
              name: null,
              price: null,
              percentage: null,
              quantity: null,
              bill: null,
            }]);
          }
        })
        .catch(error => {
          console.error('Error fetching elements:', error);
          setBillElements([{
            id: undefined,
            name: null,
            price: null,
            percentage: null,
            quantity: null,
            bill: null,
          }]);
        });
    }

    dispatch(getPatients({}));
    dispatch(getMyHospital());
  }, [isNew, id, dispatch, idPatient]);

  // Redirection après sauvegarde réussie avec notification
  useEffect(() => {
    if (updateSuccess) {
      toast.success(isNew ? 'Facturation créée avec succès!' : 'Facturation mise à jour avec succès!');
      handleClose();
    }
  }, [updateSuccess]);

  /**
   * Soumission du formulaire
   */
  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);
    
    // Filtrage des éléments vides avant sauvegarde
    const validBillElements = billElements.filter(
      el => el.name && el.name.trim() !== '' && el.price !== null && el.price !== undefined
    );

    const calculatedTotal = calculateTotal();

    // Assurance/IPM: source de vérité = blockAssurance/blockIPM (état local)
    // L'exclusivité mutuelle est garantie par les handlers (handleAssuranceChange/handleIPMChange)
    const entity = {
      ...billEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
      billElements: validBillElements,
      insurance: blockAssurance,
      ipm: blockIPM,
      total: calculatedTotal,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  /**
   * Valeurs par défaut du formulaire
   */
  const defaultValues = () => {
    if (isNew) {
      return {
        date: displayDefaultDate(),
        patient: idPatient,
        author: account.login,
        desc: '',
      };
    }
    // insurance/ipm sont gérés via blockAssurance/blockIPM (état local), pas via RHF
    const { insurance: _ins, ipm: _ipm, ...billEntityWithoutInsIpm } = billEntity || {};
    return {
      ...billEntityWithoutInsIpm,
      date: convertDateTimeFromServer(billEntity?.date),
      patient: billEntity?.patient?.id,
      desc: billEntity?.desc || '',
    };
  };

  /**
   * Gestion des modifications d'un élément de facture
   * 
   * Utilise une mise à jour immutable pour garantir la détection des changements par React.
   * Inclut des validations métier: remise entre 0-100%, prix >= 0, quantité >= 1
   */
  const handleChange = (i: number, e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    
    setBillElements(prevElements => {
      return prevElements.map((element, index) => {
        if (index === i) {
          let parsedValue: string | number | null = value;
          if (name === 'price' || name === 'quantity' || name === 'percentage') {
            parsedValue = value === '' ? null : Number(value);
            // Contraintes métier sur les valeurs numériques
            if (name === 'percentage' && parsedValue !== null) {
              parsedValue = Math.max(0, Math.min(100, parsedValue));
            }
            if (name === 'price' && parsedValue !== null && parsedValue < 0) {
              parsedValue = 0;
            }
            if (name === 'quantity' && parsedValue !== null && parsedValue < 1) {
              parsedValue = 1;
            }
          }
          return { ...element, [name]: parsedValue };
        }
        return element;
      });
    });
  };

  // Ajout d'une nouvelle ligne d'intervention
  const addFormFields = () => {
    const newBillElement: IBillElement = {
      id: undefined,
      name: '',
      price: null,
      percentage: null,
      quantity: 1,
      bill: null,
    };
    setBillElements(prev => [...prev, newBillElement]);
  };

  // Suppression d'une ligne d'intervention
  const removeFormFields = (i: number) => {
    setBillElements(prev => prev.filter((_, index) => index !== i));
  };

  const [total, setTotal] = useState(0);
  
  /**
   * Calcul du montant total de la facture
   * 
   * Formule: Σ(prix_unitaire × quantité × (1 - remise/100))
   * Le résultat est arrondi à l'entier pour éviter les centimes
   */
  const calculateTotal = () => {
    let calculatedTotal = 0;

    for (const element of billElements) {
      const price = typeof element.price === 'number' ? element.price : parseFloat(element.price) || 0;
      const quantity = typeof element.quantity === 'number' ? element.quantity : parseFloat(element.quantity) || 1;
      const percentage = typeof element.percentage === 'number' ? element.percentage : parseFloat(element.percentage) || 0;
      
      const discount = 1 - (percentage / 100);
      calculatedTotal += Math.round(price * discount * quantity);
    }
    
    return calculatedTotal;
  };

  /**
   * Mise à jour du total affiché selon le mode
   * - Création: calcul en temps réel pour feedback immédiat
   * - Édition: affiche "Calculé à la sauvegarde" pour éviter la confusion
   * - Consultation: affiche le total sauvegardé
   */
  useEffect(() => {
    if (isNew) {
      const calculatedTotal = calculateTotal();
      setTotal(calculatedTotal);
    } else if (billEntity?.total) {
      setTotal(Number(billEntity.total));
    }
  }, [billElements, isNew, billEntity]);

  // Configuration de la police pour le PDF
  Font.register({
    family: 'Poppins',
    fonts: [
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Bold.woff', fontWeight: 'bold' },
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Medium.woff', fontWeight: 'medium' },
      { src: 'https://fonts.cdnfonts.com/s/16009/Poppins-Medium.woff', fontWeight: 'thin' },
    ],
  });
  
  const valuesHeight = 6;

  // Définition du document PDF de la facture
  const doc = (
    <Document>
      <Page style={{ display: 'flex', flexDirection: 'column', fontFamily: 'Poppins' }}>
        <View
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            marginTop: '20px',
            paddingBottom: '10px',
          }}
        >
          <Image
            style={{ width: '70px', height: '70px' }}
            src={
              hospital?.logo && hospital?.logoContentType
                ? `data:${hospital.logoContentType};base64,${hospital.logo}`
                : 'content/images/logo-medecin-240x300.png'
            }
          />
        </View>
        <View style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '10px' }}>
          <Text style={{ fontSize: '24px', fontWeight: 'extrabold', marginBottom: '10px' }}>Facture</Text>
          <Text style={{ fontSize: '12px', marginBottom: '9px', alignSelf: 'flex-end', marginRight: '5vw' }}>
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
        
        {/* Filigrane pour l'authenticité du document */}
        <Image
          src="content/images/Ngirwi_Transparent.png"
          style={{ position: 'absolute', top: '35%', left: '25%', zIndex: '-1', width: '50%', height: 'auto', opacity: 0.08 }}
        />
        
        {/* Tableau des interventions */}
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
                </Text>
              </View>
            ) : null
          )}
          <View style={{ borderTop: '1px solid #141414', width: '90vw', paddingLeft: '5px', paddingRight: '5px' }}>
            <Text style={{ width: '60vw' }}>Montant total : </Text>
            <Text style={{ position: 'absolute', right: '0' }}>{total + 'FCFA'} </Text>
          </View>
        </View>
        
        <View style={{ marginLeft: '5vw', marginTop: '15px' }}>
          <Text style={{ fontSize: '12px', marginBottom: '7px' }}>Description: {description}</Text>
        </View>
        
        {/* Pied de page */}
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
                style={{ textDecoration: 'none' }}
                document={doc}
                fileName={`facture_${JSON.stringify(
                  convertDateTimeFromServerToDate(displayDefaultDateTime()) +
                    'H:' +
                    convertDateTimeFromServerToHours(displayDefaultDateTime())
                )}`}
              >
                <div
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
            />
            <ValidatedField hidden label="Author" id="bill-author" name="author" data-cy="author" type="text" />
            
            {/* Ligne Patient + Assurance + IPM - Disposition horizontale pour une meilleure ergonomie */}
            <div style={{ display: 'flex', flex: '1 1 100%', gap: '25px', flexWrap: 'wrap', alignItems: 'flex-end' }}>
              <ValidatedField
                onChange={e => getPatient(e)}
                style={
                  isNew
                    ? { borderRadius: '25px', borderColor: '#CBDCF7', width: '24vw' }
                    : { borderRadius: '25px', backgroundColor: '#A9B7CD', borderColor: '#CBDCF7', color: '#F6FAFF', width: '24vw' }
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
              
              {/* Sélecteur Assurance - Mutuellement exclusif avec IPM */}
              <FormGroup style={{ width: '18vw' }}>
                <Label id="insuranceLabel" for="bill-assurance">
                  {insuranceDisabled ? "Assurance (IPM sélectionné)" : "Assurance"}
                </Label>
                <select
                  id="bill-assurance"
                  data-cy="assurance"
                  className="form-control"
                  value={blockAssurance}
                  disabled={insuranceDisabled}
                  onChange={e => handleAssuranceChange(e.target.value)}
                  style={{
                    borderRadius: '25px',
                    borderColor: insuranceDisabled ? '#ffc107' : '#CBDCF7',
                    backgroundColor: insuranceDisabled ? '#A9B7CD' : 'white',
                    color: insuranceDisabled ? '#F6FAFF' : 'inherit',
                  }}
                >
                  <option value="">-- Aucune --</option>
                  {assurance.map(assur => (
                    <option value={assur.label} key={assur.id}>
                      {assur.label}
                    </option>
                  ))}
                </select>
              </FormGroup>
              
              {/* Sélecteur IPM - Mutuellement exclusif avec Assurance */}
              <FormGroup style={{ width: '18vw' }}>
                <Label id="ipmLabel" for="bill-ipm">
                  {ipmDisabled ? "IPM (Assurance sélectionnée)" : "IPM"}
                </Label>
                <select
                  id="bill-ipm"
                  data-cy="ipm"
                  className="form-control"
                  value={blockIPM}
                  disabled={ipmDisabled}
                  onChange={e => handleIPMChange(e.target.value)}
                  style={{
                    borderRadius: '25px',
                    borderColor: ipmDisabled ? '#ffc107' : '#CBDCF7',
                    backgroundColor: ipmDisabled ? '#A9B7CD' : 'white',
                    color: ipmDisabled ? '#F6FAFF' : 'inherit',
                  }}
                >
                  <option value="">-- Aucune --</option>
                  {ipm.map(a => (
                    <option value={a.label} key={a.id}>
                      {a.label}
                    </option>
                  ))}
                </select>
              </FormGroup>
            </div>
            
            {/* Liste dynamique des éléments de facturation */}
            {billElements.map((element, index, arr) => (
              <div
                style={{
                  flex: '1 1 100%',
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '15px',
                  alignItems: 'flex-end',
                  justifyContent: 'center',
                  marginBottom: '15px',
                  padding: '10px',
                  backgroundColor: '#f8fafc',
                  borderRadius: '10px',
                }}
                key={`bill-element-${index}`}
              >
                <ValidatedField
                  label="Quantité"
                  name="quantity"
                  type="number"
                  min="1"
                  value={element.quantity !== null && element.quantity !== undefined ? element.quantity : ''}
                  onChange={e => handleChange(index, e)}
                  disabled={idEdit === 'voir'}
                  style={{
                    borderRadius: '12px',
                    borderColor: '#CBDCF7',
                    width: '100px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : 'white',
                    color: idEdit === 'voir' ? '#F6FAFF' : '#333',
                  }}
                  validate={{
                    min: { value: 1, message: 'Minimum 1' },
                  }}
                />
                <ValidatedField
                  label="Intervention"
                  name="name"
                  type="text"
                  placeholder="Intervention..."
                  value={element.name || ''}
                  onChange={e => handleChange(index, e)}
                  disabled={idEdit === 'voir'}
                  style={{
                    borderRadius: '12px',
                    borderColor: '#CBDCF7',
                    minWidth: '200px',
                    flex: '1',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : 'white',
                    color: idEdit === 'voir' ? '#F6FAFF' : '#333',
                  }}
                />
                <ValidatedField
                  label="Prix unitaire (FCFA)"
                  name="price"
                  type="number"
                  placeholder="Prix unitaire..."
                  min="0"
                  value={element.price !== null && element.price !== undefined ? element.price : ''}
                  onChange={e => handleChange(index, e)}
                  disabled={idEdit === 'voir'}
                  style={{
                    borderRadius: '12px',
                    borderColor: '#CBDCF7',
                    width: '150px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : 'white',
                    color: idEdit === 'voir' ? '#F6FAFF' : '#333',
                  }}
                  validate={{
                    min: { value: 0, message: 'Minimum 0' },
                  }}
                />
                <ValidatedField
                  label="Remise (%)"
                  name="percentage"
                  type="number"
                  placeholder="0"
                  min="0"
                  max="100"
                  value={element.percentage !== null && element.percentage !== undefined ? element.percentage : ''}
                  onChange={e => handleChange(index, e)}
                  disabled={idEdit === 'voir'}
                  style={{
                    borderRadius: '12px',
                    borderColor: '#CBDCF7',
                    width: '100px',
                    backgroundColor: idEdit === 'voir' ? '#A9B7CD' : 'white',
                    color: idEdit === 'voir' ? '#F6FAFF' : '#333',
                  }}
                  validate={{
                    min: { value: 0, message: 'Minimum 0%' },
                    max: { value: 100, message: 'Maximum 100%' },
                  }}
                />
                {/* Boutons d'ajout/suppression - Affichés uniquement en mode édition */}
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', paddingBottom: '5px' }}>
                  {arr.length - 1 === index && idEdit !== 'voir' && (
                    <span
                      onClick={() => addFormFields()}
                      style={{
                        backgroundColor: '#11485C',
                        color: 'white',
                        borderRadius: '50%',
                        width: '35px',
                        height: '35px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        cursor: 'pointer',
                      }}
                    >
                      {React.createElement(IoIosAddCircle, { size: '30' })}
                    </span>
                  )}
                  {index > 0 && idEdit !== 'voir' && (
                    <span
                      onClick={() => removeFormFields(index)}
                      style={{
                        backgroundColor: '#EC4747',
                        color: 'white',
                        borderRadius: '50%',
                        width: '35px',
                        height: '35px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        cursor: 'pointer',
                      }}
                    >
                      {React.createElement(IoIosRemoveCircle, { size: '30' })}
                    </span>
                  )}
                </div>
              </div>
            ))}
            
            {/* Affichage du total - Comportement différent selon le mode */}
            <div style={{ width: '36vw' }}>
              <label className="form-label" htmlFor="bill-total-display">Montant Total (CFA)</label>
              <div
                id="bill-total-display"
                style={{
                  borderRadius: '25px',
                  border: '1px solid #CBDCF7',
                  padding: '0.375rem 0.75rem',
                  backgroundColor: '#A9B7CD',
                  color: '#F6FAFF',
                  height: '38px',
                  display: 'flex',
                  alignItems: 'center',
                }}
              >
                {isNew 
                  ? total + ' FCFA' 
                  : idEdit === 'voir' 
                    ? (billEntity?.total || 0) + ' FCFA'
                    : 'Calculé à la sauvegarde'}
              </div>
            </div>
            {/* Description */}
            <ValidatedField
              label="Description"
              id="bill-desc"
              name="desc"
              data-cy="desc"
              type="textarea"
              placeholder="Description..."
              disabled={idEdit === 'voir'}
              onChange={e => setDescription(e.target.value)}
              style={{
                borderRadius: '25px',
                borderColor: '#CBDCF7',
                height: '20vh',
                backgroundColor: idEdit === 'voir' ? '#A9B7CD' : '',
                color: idEdit === 'voir' ? '#F6FAFF' : '',
                width: '36vw',
              }}
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
