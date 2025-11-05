import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col, Card, CardBody, CardHeader, Table } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { formatFcfa } from 'app/shared/util/entity-utils';

import { getEntity } from './surveillance-sheet.reducer';

export const SurveillanceSheetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const surveillanceSheetEntity = useAppSelector(state => state.surveillanceSheet.entity);
  const hid = (surveillanceSheetEntity as any)?.hospitalisation?.id || (surveillanceSheetEntity as any)?.hospitalisationId;
  return (
    <div className="text-dark" style={{ paddingLeft: '16vw', paddingTop: '1%', fontFamily: 'Mulish' }}>
      <div className="d-flex align-items-center justify-content-between mb-3">
        <h2 className="mb-0" data-cy="surveillanceSheetDetailsHeading">
          Fiche de surveillance #{surveillanceSheetEntity?.id}
        </h2>
        <div className="d-flex gap-2">
          <Button tag={Link} to="/surveillance-sheet" replace color="info" data-cy="entityDetailsBackButton">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
          </Button>
          <Button tag={Link} to={`/surveillance-sheet/${surveillanceSheetEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
          </Button>
          {hid ? (
            <Button tag={Link} to={`/hospitalisation/${hid}`} color="secondary" outline>
              Aller à l’hospitalisation #{hid}
            </Button>
          ) : null}
        </div>
      </div>

      <Row className="g-3">
        <Col md="7">
          <Card className="shadow-sm">
            <CardHeader className="fw-bold">Paramètres vitaux</CardHeader>
            <CardBody>
              <Row className="gy-2">
                <Col md="6">
                  <div className="small text-muted">Date de fiche</div>
                  <div className="fw-semibold">{surveillanceSheetEntity?.sheetDate || '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Température (°C)</div>
                  <div className="fw-semibold">{surveillanceSheetEntity?.temperature ?? '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">TA Systolique (mmHg)</div>
                  <div className="fw-semibold">{(surveillanceSheetEntity as any)?.systolicBP ?? '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">TA Diastolique (mmHg)</div>
                  <div className="fw-semibold">{(surveillanceSheetEntity as any)?.diastolicBP ?? '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Pouls (bpm)</div>
                  <div className="fw-semibold">{surveillanceSheetEntity?.pulseRate ?? '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">Fréquence respiratoire (rpm)</div>
                  <div className="fw-semibold">{(surveillanceSheetEntity as any)?.respirationRate ?? '—'}</div>
                </Col>
                <Col md="6">
                  <div className="small text-muted">SaO2 (%)</div>
                  <div className="fw-semibold">{surveillanceSheetEntity?.spo2 ?? '—'}</div>
                </Col>
              </Row>
            </CardBody>
          </Card>
          <Card className="shadow-sm mt-3">
            <CardHeader className="fw-bold">Notes</CardHeader>
            <CardBody>
              <div className="mb-3">
                <div className="small text-muted">Notes soignantes</div>
                <div className="fw-semibold" style={{ whiteSpace: 'pre-wrap' }}>
                  {(surveillanceSheetEntity as any)?.nursingNotes || '—'}
                </div>
              </div>
              <div>
                <div className="small text-muted">Observations médicales</div>
                <div className="fw-semibold" style={{ whiteSpace: 'pre-wrap' }}>
                  {(surveillanceSheetEntity as any)?.medicalObservations || '—'}
                </div>
              </div>
            </CardBody>
          </Card>
        </Col>
        <Col md="5">
          <Card className="shadow-sm">
            <CardHeader className="fw-bold">Raccourcis</CardHeader>
            <CardBody className="d-flex flex-column gap-2">
              {hid ? (
                <Button tag={Link} to={`/hospitalisation/${hid}`} color="secondary" outline>
                  Voir l’hospitalisation #{hid}
                </Button>
              ) : (
                <div className="text-muted">Hospitalisation non renseignée</div>
              )}
              <Button tag={Link} to={`/surveillance-sheet/${surveillanceSheetEntity.id}/mini-consultations`} color="secondary">
                Mini-consultations
              </Button>
            </CardBody>
          </Card>
        </Col>
      </Row>

      {/* Médicaments */}
      <Row className="g-3 mt-1">
        <Col md="12">
          <Card className="shadow-sm">
            <CardHeader className="fw-bold">Médicaments administrés</CardHeader>
            <CardBody className="table-responsive">
              {Array.isArray((surveillanceSheetEntity as any)?.medications) && (surveillanceSheetEntity as any).medications.length > 0 ? (
                <Table size="sm" responsive className="mb-0">
                  <thead>
                    <tr>
                      <th>Nom</th>
                      <th className="text-end">Prix unitaire</th>
                      <th className="text-end">Quantité</th>
                      <th className="text-end">Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {(surveillanceSheetEntity as any).medications.map((m: any, idx: number) => (
                      <tr key={`med-${idx}`}>
                        <td>{m.name || '—'}</td>
                        <td className="text-end">{formatFcfa(Number(m.unitPrice || 0))}</td>
                        <td className="text-end">{Number(m.quantity || 0)}</td>
                        <td className="text-end">{formatFcfa(Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)))}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                <div className="text-muted">Aucun médicament enregistré.</div>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>

      {/* Actes */}
      <Row className="g-3 mt-1">
        <Col md="12">
          <Card className="shadow-sm">
            <CardHeader className="fw-bold">Actes réalisés</CardHeader>
            <CardBody className="table-responsive">
              {Array.isArray((surveillanceSheetEntity as any)?.acts) && (surveillanceSheetEntity as any).acts.length > 0 ? (
                <Table size="sm" responsive className="mb-0">
                  <thead>
                    <tr>
                      <th>Nom</th>
                      <th className="text-end">Prix unitaire</th>
                      <th className="text-end">Quantité</th>
                      <th className="text-end">Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {(surveillanceSheetEntity as any).acts.map((a: any, idx: number) => (
                      <tr key={`act-${idx}`}>
                        <td>{a.name || '—'}</td>
                        <td className="text-end">{formatFcfa(Number(a.unitPrice || 0))}</td>
                        <td className="text-end">{Number(a.quantity || 0)}</td>
                        <td className="text-end">{formatFcfa(Number(a.total || Number(a.unitPrice || 0) * Number(a.quantity || 0)))}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : (
                <div className="text-muted">Aucun acte enregistré.</div>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default SurveillanceSheetDetail;
