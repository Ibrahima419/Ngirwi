import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bill.reducer';

export const BillDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billEntity = useAppSelector(state => state.bill.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billDetailsHeading">Bill</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{billEntity.id}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{billEntity.date ? <TextFormat value={billEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="author">Author</span>
          </dt>
          <dd>{billEntity.author}</dd>
          <dt>Patient</dt>
          <dd>{billEntity.patient ? billEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bill" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bill/${billEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillDetail;
