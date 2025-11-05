import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medecine.reducer';

export const MedecineDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const medecineEntity = useAppSelector(state => state.medecine.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medecineDetailsHeading">Medecine</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{medecineEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{medecineEntity.name}</dd>
          <dt>
            <span id="duration">Duration</span>
          </dt>
          <dd>{medecineEntity.duration}</dd>
          <dt>
            <span id="frequency">Frequency</span>
          </dt>
          <dd>{medecineEntity.frequency}</dd>
          <dt>Ordonance</dt>
          <dd>{medecineEntity.ordonance ? medecineEntity.ordonance.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/medecine" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medecine/${medecineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedecineDetail;
