import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './consultation.reducer';

export const ConsultationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const consultationEntity = useAppSelector(state => state.consultation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="consultationDetailsHeading">Consultation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{consultationEntity.id}</dd>
          <dt>
            <span id="dateTime">Date Time</span>
          </dt>
          <dd>
            {consultationEntity.dateTime ? <TextFormat value={consultationEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="temperature">Temperature</span>
          </dt>
          <dd>{consultationEntity.temperature}</dd>
          <dt>
            <span id="weight">Weight</span>
          </dt>
          <dd>{consultationEntity.weight}</dd>
          <dt>
            <span id="tension">Tension</span>
          </dt>
          <dd>{consultationEntity.tension}</dd>
          <dt>
            <span id="glycemie">Glycemie</span>
          </dt>
          <dd>{consultationEntity.glycemie}</dd>
          <dt>
            <span id="comment">Comment</span>
          </dt>
          <dd>{consultationEntity.comment}</dd>
          <dt>
            <span id="hypothesis">Hypothesis</span>
          </dt>
          <dd>{consultationEntity.hypothesis}</dd>
          <dt>
            <span id="exams">Exams</span>
          </dt>
          <dd>{consultationEntity.exams}</dd>
          <dt>
            <span id="treatment">Treatment</span>
          </dt>
          <dd>{consultationEntity.treatment}</dd>
          <dt>
            <span id="author">Author</span>
          </dt>
          <dd>{consultationEntity.author}</dd>
          <dt>
            <span id="og">Og</span>
          </dt>
          <dd>{consultationEntity.og}</dd>
          <dt>
            <span id="vd">Vd</span>
          </dt>
          <dd>{consultationEntity.vd}</dd>
          <dt>
            <span id="eseptum">Eseptum</span>
          </dt>
          <dd>{consultationEntity.eseptum}</dd>
          <dt>
            <span id="ao">Ao</span>
          </dt>
          <dd>{consultationEntity.ao}</dd>
          <dt>
            <span id="vGDiastole">V G Diastole</span>
          </dt>
          <dd>{consultationEntity.vGDiastole}</dd>
          <dt>
            <span id="ouvertureAo">Ouverture Ao</span>
          </dt>
          <dd>{consultationEntity.ouvertureAo}</dd>
          <dt>
            <span id="vGSystole">V G Systole</span>
          </dt>
          <dd>{consultationEntity.vGSystole}</dd>
          <dt>
            <span id="vp">Vp</span>
          </dt>
          <dd>{consultationEntity.vp}</dd>
          <dt>
            <span id="oGAo">O G Ao</span>
          </dt>
          <dd>{consultationEntity.oGAo}</dd>
          <dt>
            <span id="fRTeicholtz">F R Teicholtz</span>
          </dt>
          <dd>{consultationEntity.fRTeicholtz}</dd>
          <dt>
            <span id="eVp">E Vp</span>
          </dt>
          <dd>{consultationEntity.eVp}</dd>
          <dt>
            <span id="septumVg">Septum Vg</span>
          </dt>
          <dd>{consultationEntity.septumVg}</dd>
          <dt>
            <span id="fETeicholz">F E Teicholz</span>
          </dt>
          <dd>{consultationEntity.fETeicholz}</dd>
          <dt>
            <span id="tapse">Tapse</span>
          </dt>
          <dd>{consultationEntity.tapse}</dd>
          <dt>
            <span id="paroiPost">Paroi Post</span>
          </dt>
          <dd>{consultationEntity.paroiPost}</dd>
          <dt>
            <span id="surfaceOg">Surface Og</span>
          </dt>
          <dd>{consultationEntity.surfaceOg}</dd>
          <dt>
            <span id="surfaceOd">Surface Od</span>
          </dt>
          <dd>{consultationEntity.surfaceOd}</dd>
          <dt>
            <span id="mesureVd">Mesure Vd</span>
          </dt>
          <dd>{consultationEntity.mesureVd}</dd>
          <dt>
            <span id="fe">Fe</span>
          </dt>
          <dd>{consultationEntity.fe}</dd>
          <dt>
            <span id="feA2C">Fe A 2 C</span>
          </dt>
          <dd>{consultationEntity.feA2C}</dd>
          <dt>
            <span id="feBiplan">Fe Biplan</span>
          </dt>
          <dd>{consultationEntity.feBiplan}</dd>
          <dt>
            <span id="e">E</span>
          </dt>
          <dd>{consultationEntity.e}</dd>
          <dt>
            <span id="a">A</span>
          </dt>
          <dd>{consultationEntity.a}</dd>
          <dt>
            <span id="eA">E A</span>
          </dt>
          <dd>{consultationEntity.eA}</dd>
          <dt>
            <span id="td">Td</span>
          </dt>
          <dd>{consultationEntity.td}</dd>
          <dt>
            <span id="triv">Triv</span>
          </dt>
          <dd>{consultationEntity.triv}</dd>
          <dt>
            <span id="dureeAmIm">Duree Am Im</span>
          </dt>
          <dd>{consultationEntity.dureeAmIm}</dd>
          <dt>
            <span id="surfaceRegurgitee">Surface Regurgitee</span>
          </dt>
          <dd>{consultationEntity.surfaceRegurgitee}</dd>
          <dt>
            <span id="pba">Pba</span>
          </dt>
          <dd>{consultationEntity.pba}</dd>
          <dt>
            <span id="qr">Qr</span>
          </dt>
          <dd>{consultationEntity.qr}</dd>
          <dt>
            <span id="vr">Vr</span>
          </dt>
          <dd>{consultationEntity.vr}</dd>
          <dt>
            <span id="sor">Sor</span>
          </dt>
          <dd>{consultationEntity.sor}</dd>
          <dt>
            <span id="fr">Fr</span>
          </dt>
          <dd>{consultationEntity.fr}</dd>
          <dt>
            <span id="vmaxAp">Vmax Ap</span>
          </dt>
          <dd>{consultationEntity.vmaxAp}</dd>
          <dt>
            <span id="itv">Itv</span>
          </dt>
          <dd>{consultationEntity.itv}</dd>
          <dt>
            <span id="gradMax">Grad Max</span>
          </dt>
          <dd>{consultationEntity.gradMax}</dd>
          <dt>
            <span id="gradMoy">Grad Moy</span>
          </dt>
          <dd>{consultationEntity.gradMoy}</dd>
          <dt>
            <span id="dc">Dc</span>
          </dt>
          <dd>{consultationEntity.dc}</dd>
          <dt>
            <span id="iAoextension">I Aoextension</span>
          </dt>
          <dd>{consultationEntity.iAoextension}</dd>
          <dt>
            <span id="venaContracta">Vena Contracta</span>
          </dt>
          <dd>{consultationEntity.venaContracta}</dd>
          <dt>
            <span id="pht">Pht</span>
          </dt>
          <dd>{consultationEntity.pht}</dd>
          <dt>
            <span id="iTExtension">I T Extension</span>
          </dt>
          <dd>{consultationEntity.iTExtension}</dd>
          <dt>
            <span id="gradMaxB">Grad Max B</span>
          </dt>
          <dd>{consultationEntity.gradMaxB}</dd>
          <dt>
            <span id="paps">Paps</span>
          </dt>
          <dd>{consultationEntity.paps}</dd>
          <dt>
            <span id="ip">Ip</span>
          </dt>
          <dd>{consultationEntity.ip}</dd>
          <dt>
            <span id="vmax">Vmax</span>
          </dt>
          <dd>{consultationEntity.vmax}</dd>
          <dt>
            <span id="gradMaxC">Grad Max C</span>
          </dt>
          <dd>{consultationEntity.gradMaxC}</dd>
          <dt>
            <span id="gradMoyB">Grad Moy B</span>
          </dt>
          <dd>{consultationEntity.gradMoyB}</dd>
          <dt>
            <span id="s">S</span>
          </dt>
          <dd>{consultationEntity.s}</dd>
          <dt>
            <span id="d">D</span>
          </dt>
          <dd>{consultationEntity.d}</dd>
          <dt>
            <span id="sD">S D</span>
          </dt>
          <dd>{consultationEntity.sD}</dd>
          <dt>
            <span id="aA">A A</span>
          </dt>
          <dd>{consultationEntity.aA}</dd>
          <dt>
            <span id="dureeAp">Duree Ap</span>
          </dt>
          <dd>{consultationEntity.dureeAp}</dd>
          <dt>
            <span id="eAA">E AA</span>
          </dt>
          <dd>{consultationEntity.eAA}</dd>
          <dt>
            <span id="aAA">A AA</span>
          </dt>
          <dd>{consultationEntity.aAA}</dd>
          <dt>
            <span id="eAAa">E A Aa</span>
          </dt>
          <dd>{consultationEntity.eAAa}</dd>
          <dt>
            <span id="eEa">E Ea</span>
          </dt>
          <dd>{consultationEntity.eEa}</dd>
          <dt>
            <span id="z">Z</span>
          </dt>
          <dd>{consultationEntity.z}</dd>
          <dt>Patient</dt>
          <dd>{consultationEntity.patient ? consultationEntity.patient.lastName : ''}</dd>
        </dl>
        <Button tag={Link} to="/consultation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/consultation/${consultationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConsultationDetail;
