import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Card, Spinner } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dossier-medical.reducer';
import Header from 'app/shared/layout/header/header';

export const DossierMedicalDetail = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const dossierMedicalEntity = useAppSelector(state => state.dossierMedical.entity);
  const loading = useAppSelector(state => state.dossierMedical.loading);
  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  if (loading) {
    return (
      <div style={{ paddingLeft: '16vw', paddingTop: '10%', display: 'flex', justifyContent: 'center' }}>
        <Spinner color="primary" />
      </div>
    );
  }

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
      <Header pageName="Dossier médical" />
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
            width: '33.38vw',
            borderRadius: '20px',
            backgroundColor: '#11485C',
            marginLeft: '25%',
            justifyContent: 'center',
            textAlign: 'center',
            color: 'white',
            boxShadow: '0px 10px 50px rgba(138, 161, 203, 0.23)',
          }}
        >
          <span>Dossier médical</span>
        </Card>

        <Card
          style={{
            marginLeft: '4%',
            height: '55vh',
            width: '70vw',
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
            }}
          >
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                paddingTop: '30%',
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
                {dossierMedicalEntity.dateCreated ? (
                  <TextFormat value={dossierMedicalEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
                ) : null}
              </span>
            </div>
          </Card>

          <Card
            style={{
              height: '90%',
              width: '40%',
              borderRadius: '15px',
              borderTopLeftRadius: '0px',
              marginRight: '8%',
              backgroundColor: '#F6FAFF',
              borderColor: '#F6FAFF',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center',
              fontSize: '20px',
              fontWeight: 'bold',
              fontFamily: 'Mulish',
            }}
          >
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Nom du patient:{' '}
              </span>
              <span>{dossierMedicalEntity.patient ? dossierMedicalEntity.patient.lastName : ' '}</span>
            </div>
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Prénom du patient:{' '}
              </span>
              <span>{dossierMedicalEntity.patient ? dossierMedicalEntity.patient.firstName : ' '}</span>
            </div>
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Motif Consultation:{' '}
              </span>
              <span>{dossierMedicalEntity.motifConsultation}</span>
            </div>
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Histoire de la maladie:{' '}
              </span>
              <span style={{}}>{dossierMedicalEntity.histoireMaladie}</span>
            </div>
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Antécédants personnels:{' '}
              </span>
              <span>{dossierMedicalEntity.antecedantsPersonnels}</span>
            </div>
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Antécédants chirurgicaux:{' '}
              </span>
              <span>{dossierMedicalEntity.antecedantsChirurgicaux}</span>
            </div>
            <div style={{ wordBreak: 'break-all' }}>
              <span
                style={{
                  color: '#A9B7CD',
                }}
              >
                Antécédants familiaux:{' '}
              </span>
              <span>{dossierMedicalEntity.antecedantsFamiliaux}</span>
            </div>
            {/* <div>
                <span
                  style={{
                    color:"#A9B7CD"
                  }}
                >Téléphone: </span>
                <span>
                  {patientEntity.phone}  
                </span>
              </div>
              <div>
                <span
                  style={{
                    color:"#A9B7CD"
                  }}
                >CNI: </span>
                <span>
                  {patientEntity.cni}  
                </span>
              </div>
              <div>
                <span
                  style={{
                    color:"#A9B7CD"
                  }}
                >Groupe sanguin: </span>
                <span>
                  {translateBloodType(patientEntity.bloodType)}  
                </span>
              </div>
              <div>
                <span
                  style={{
                    color:"#A9B7CD"
                  }}
                >Date de mise à jour: </span>
                <span>
                  {patientEntity.dateUpdated? <TextFormat value={patientEntity.dateUpdated} type="date" format={APP_DATE_FORMAT} /> : null}  
                </span>
              </div> */}
          </Card>

          <div
            style={{
              display: 'flex',
              flexDirection: 'column',
              gap: '5vh',
              marginTop: '3vh',
            }}
          >
            <Button
              tag={Link}
              to={`/dossier-medical/${dossierMedicalEntity?.id}/edit/${'voir'}`}
              style={{
                borderColor: '#0075FF',
                backgroundColor: '#0075FF',
                color: '#FFFFFF',
                width: '25vh',
                height: '8vh',
                borderRadius: '4px',
                fontFamily: 'Ubuntu',
                justifyContent: 'center',
                textAlign: 'center',
              }}
            >
              Voir détails
            </Button>
            <Button
              href={`/dossier-medical/${dossierMedicalEntity?.id}/edit`}
              style={{
                borderColor: '#0075FF',
                backgroundColor: '#0075FF',
                color: '#FFFFFF',
                width: '25vh',
                height: '8vh',
                borderRadius: '4px',
                fontFamily: 'Ubuntu',
                justifyContent: 'center',
                textAlign: 'center',
              }}
            >
              Editer dossier
            </Button>
            {/* <Button
              tag={Link}
              to={`/consultation/list/${patientEntity.id}`}
              style={{
                borderColor:"#0075FF",
                backgroundColor:"#0075FF",
                color:"#FFFFFF",
                width:"25vh",
                height:"8vh",
                borderRadius:"4px",
                fontFamily:"Ubuntu",
              }}
            >
              Voir consultations faites
            </Button> */}
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
    // <Row style={{marginLeft:"16vw"}}>
    //   <Col md="8">
    //     <h2 data-cy="dossierMedicalDetailsHeading">Dossier Medical</h2>
    //     <dl className="jh-entity-details">
    //       <dt>
    //         <span id="id">ID</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.id }</dd>
    //       <dt>
    //         <span id="motifConsultation">Motif Consultation</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.motifConsultation}</dd>
    //       <dt>
    //         <span id="histoireMaladie">Histoire Maladie</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.histoireMaladie}</dd>
    //       <dt>
    //         <span id="terrain">Terrain</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.terrain}</dd>
    //       <dt>
    //         <span id="antecedantsPersonnels">Antecedants Personnels</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.antecedantsPersonnels}</dd>
    //       <dt>
    //         <span id="antecedantsChirurgicaux">Antecedants Chirurgicaux</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.antecedantsChirurgicaux}</dd>
    //       <dt>
    //         <span id="antecedantsFamiliaux">Antecedants Familiaux</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.antecedantsFamiliaux}</dd>
    //       <dt>
    //         <span id="gynecoObstretrique">Gyneco Obstretrique</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.gynecoObstretrique}</dd>
    //       <dt>
    //         <span id="syndromique">Résumé Syndromique</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.syndromique}</dd>
    //       <dt>
    //         <span id="dad">Père</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.dad}</dd>
    //       <dt>
    //         <span id="mom">Mère</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.mom}</dd>
    //       <dt>
    //         <span id="siblings">Frères</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.siblings}</dd>
    //       <dt>
    //         <span id="descendants">Descendants</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.descendants}</dd>
    //       <dt>
    //         <span id="dateCreated">Date de création</span>
    //       </dt>
    //       <dd>
    //         {dossierMedicalEntity.dateCreated ? (
    //           <TextFormat value={dossierMedicalEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
    //         ) : null}
    //       </dd>
    //       <dt>
    //         <span id="dateUpdated">Date de mise à jour</span>
    //       </dt>
    //       <dd>
    //         {dossierMedicalEntity.dateUpdated ? (
    //           <TextFormat value={dossierMedicalEntity.dateUpdated} type="date" format={APP_DATE_FORMAT} />
    //         ) : null}
    //       </dd>
    //       <dt>
    //         <span id="author">Autheur</span>
    //       </dt>
    //       <dd>{dossierMedicalEntity.author}</dd>
    //       <dt>Patient</dt>
    //       <dd>{dossierMedicalEntity.patient ? dossierMedicalEntity.patient.id : ''}</dd>
    //     </dl>
    //     <Button tag={Link} to="/dossier-medical" replace color="info" data-cy="entityDetailsBackButton">
    //       <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
    //     </Button>
    //     &nbsp;
    //     <Button tag={Link} to={`/dossier-medical/${dossierMedicalEntity.id}/edit`} replace color="primary">
    //       <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
    //     </Button>
    //   </Col>
    // </Row>
  );
};

export default DossierMedicalDetail;
