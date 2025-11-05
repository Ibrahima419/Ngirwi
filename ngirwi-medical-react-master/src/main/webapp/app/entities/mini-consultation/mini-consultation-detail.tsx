import React, { useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { Button, Row, Col, Card } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './mini-consultation.reducer';
import { TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { formatFcfa } from 'app/shared/util/entity-utils';

const MiniConsultationDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id, surveillanceSheetId } = useParams<'id' | 'surveillanceSheetId'>();
  const entity = useAppSelector(state => state.miniConsultation.entity);

  useEffect(() => {
    if (id) dispatch(getEntity(id));
  }, [id]);

  return (
    <Row>
      <Col md="8">
        <h2>Mini-consultation #{id}</h2>
        <Card className="p-3 mb-3">
          <div className="mb-2">
            <strong>Résumé</strong>
            <div>{entity.summary}</div>
          </div>
          <div className="mb-2">
            <strong>Diagnostic</strong>
            <div>{entity.diagnosis || '—'}</div>
          </div>
          <div className="mb-2">
            <strong>Prix</strong>
            <div>{formatFcfa(Number(entity.price || 0))}</div>
          </div>
          <div className="mb-2">
            <strong>Auteur</strong>
            <div>{entity.authorName || '—'}</div>
          </div>
          <div className="mb-2">
            <strong>Créée</strong>
            <div>{entity.createdDate ? <TextFormat value={entity.createdDate} type="date" format={APP_DATE_FORMAT} /> : '—'}</div>
          </div>
        </Card>
        <div className="d-flex gap-2">
          <Button tag={Link} to={`/surveillance-sheets/${surveillanceSheetId}/mini-consultations`} color="secondary">
            Retour
          </Button>
          <Button tag={Link} to={`/surveillance-sheets/${surveillanceSheetId}/mini-consultations/${id}/edit`} color="primary">
            Modifier
          </Button>
        </div>
      </Col>
    </Row>
  );
};

export default MiniConsultationDetail;
