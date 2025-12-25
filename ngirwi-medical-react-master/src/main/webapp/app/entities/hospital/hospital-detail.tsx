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
    <div
      style={{
        paddingLeft: '16vw',
        paddingTop: '1%',
        fontFamily: 'Mulish',
        fontWeight: 900,
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Row>
        <Col md="8">
          <h2 data-cy="hospitalDetailsHeading" style={{ marginBottom: '12px', fontWeight: 800 }}>
            Hôpital
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="id">ID</span>
            </dt>
            <dd>{hospitalEntity.id}</dd>
            <dt>
              <span id="name">Nom</span>
            </dt>
            <dd>{hospitalEntity.name}</dd>
            <dt>
              <span id="adress">Adresse</span>
            </dt>
            <dd>{hospitalEntity.adress}</dd>
            <dt>
              <span id="phone">Téléphone</span>
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
                      <img src={`data:${hospitalEntity.logoContentType};base64,${hospitalEntity.logo}`} style={{ maxHeight: '60px' }} />
                    </a>
                  ) : null}
                  <div style={{ fontSize: '12px', color: '#6c757d' }}>
                    {hospitalEntity.logoContentType}, {byteSize(hospitalEntity.logo)}
                  </div>
                </div>
              ) : (
                <span className="text-muted">Aucun logo</span>
              )}
            </dd>
          </dl>
          <div style={{ marginTop: '12px', display: 'flex', gap: '8px' }}>
            <Button tag={Link} to="/hospital" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
            </Button>
            <Button tag={Link} to={`/hospital/${hospitalEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Éditer</span>
            </Button>
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default HospitalDetail;
