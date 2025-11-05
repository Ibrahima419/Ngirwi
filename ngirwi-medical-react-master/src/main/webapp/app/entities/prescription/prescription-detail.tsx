import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './prescription.reducer';
import { IMedecine } from 'app/shared/model/medecine.model';
import { getMedecineByPrescriptionId } from '../medecine/medecine.reducer';

export const PrescriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();
  const [medecines, setMedecine] = useState<IMedecine[]>([]);

  // useEffect(() => {
  //   dispatch(getEntity(id));
  // }, []);

  useEffect(() => {
    dispatch(getEntity(id));
    getMedecineByPrescriptionId(Number(id))
      .then(data => {
        setMedecine(data);
      })
      .catch(error => {
        console.error('Error fetching medicine:', error);
      });
  }, [id]);

  const prescriptionEntity = useAppSelector(state => state.prescription.entity);
  return (
    <Row style={{ marginLeft: '16vw' }}>
      <Col md="8">
        <h2 data-cy="prescriptionDetailsHeading">Consultation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{prescriptionEntity.id}</dd>
          <dt>
            <span id="creationDate">Date de création</span>
          </dt>
          <dd>
            {prescriptionEntity.creationDate ? (
              <TextFormat value={prescriptionEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="author">Auteur</span>
          </dt>
          <dd>{prescriptionEntity.author}</dd>
          <dt>Consultation</dt>
          <dd>{prescriptionEntity.consultation ? prescriptionEntity.consultation.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/prescription" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/prescription/${prescriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrescriptionDetail;
