import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bill-element.reducer';

export const BillElementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const billElementEntity = useAppSelector(state => state.billElement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="billElementDetailsHeading">Bill Element</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{billElementEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{billElementEntity.name}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{billElementEntity.price}</dd>
          <dt>
            <span id="percentage">Percentage</span>
          </dt>
          <dd>{billElementEntity.percentage}</dd>
          <dt>
            <span id="quantity">Quantity</span>
          </dt>
          <dd>{billElementEntity.quantity}</dd>
          <dt>Bill</dt>
          <dd>{billElementEntity.bill ? billElementEntity.bill.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bill-element" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bill-element/${billElementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BillElementDetail;
