import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hospital.reducer';

export const HospitalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hospitalEntity = useAppSelector(state => state.hospital.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hospitalDetailsHeading">Hospital</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{hospitalEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{hospitalEntity.name}</dd>
          <dt>
            <span id="adress">Adress</span>
          </dt>
          <dd>{hospitalEntity.adress}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{hospitalEntity.phone}</dd>
          <dt>
            <span id="logo">Logo</span>
          </dt>
          <dd>
            {hospitalEntity.logo ? (
              <div>
                {hospitalEntity.logoContentType ? (
                  <a onClick={openFile(hospitalEntity.logoContentType, hospitalEntity.logo)}>
                    <img src={`data:${hospitalEntity.logoContentType};base64,${hospitalEntity.logo}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {hospitalEntity.logoContentType}, {byteSize(hospitalEntity.logo)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hospital" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hospital/${hospitalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default HospitalDetail;
